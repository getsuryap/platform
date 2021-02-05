package org.ospic.platform.accounting.transactions.service;

import org.apache.poi.hpsf.Decimal;
import org.ospic.platform.accounting.transactions.data.TransactionResponse;
import org.ospic.platform.accounting.transactions.data.TransactionRowMap;
import org.ospic.platform.accounting.transactions.domain.Transactions;
import org.ospic.platform.accounting.transactions.repository.TransactionJpaRepository;
import org.ospic.platform.inventory.admission.data.AdmissionResponseData;
import org.ospic.platform.inventory.admission.service.AdmissionsReadServiceImpl;
import org.ospic.platform.organization.departments.repository.DepartmentJpaRepository;
import org.ospic.platform.organization.medicalservices.repository.MedicalServiceJpaRepository;
import org.ospic.platform.patient.consultation.repository.ConsultationResourceJpaRepository;
import org.ospic.platform.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * This file was created by eli on 03/02/2021 for org.ospic.platform.accounting.transactions.service
 * --
 * --
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
@Repository
public class TransactionReadPrincipleServiceImpl implements TransactionReadPrincipleService {
    @Autowired
    TransactionJpaRepository repository;
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public TransactionReadPrincipleServiceImpl(
            TransactionJpaRepository repository, final DataSource dataSource) {
        this.repository = repository;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public ResponseEntity<?> readTransactions() {
        final TransactionDataRowMapper rm = new TransactionDataRowMapper();
        final String sql = "select " + rm.schema() + "  order by tr.id DESC ";
        List <TransactionRowMap> transactions =  this.jdbcTemplate.query(sql, rm, new Object[]{});
        return ResponseEntity.ok().body(transactions);
    }

    @Override
    public ResponseEntity<?> readReversedTransactions() {
        return null;
    }

    @Override
    public ResponseEntity<?> readTransactionById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<?> readTransactionByDate(LocalDateTime dateTime) {
        return null;
    }

    @Override
    public ResponseEntity<?> readTransactionsByConsultationId(Long id) {
        final TransactionDataRowMapper rm = new TransactionDataRowMapper();
        final String sql = "select " + rm.schema() + " where co.id = ?  order by tr.id DESC ";
        List <TransactionRowMap> transactions =  this.jdbcTemplate.query(sql, rm, id);
        return ResponseEntity.ok().body(new TransactionResponse().transactionResponse(transactions));
    }

    @Override
    public ResponseEntity<?> readTransactionsByConsultationIdAndReversed(Long id) {
        final TransactionDataRowMapper rm = new TransactionDataRowMapper();
        final String sql = "select " + rm.schema() + " where co.id = ? and tr.is_reversed = true order by tr.id DESC ";
        List <TransactionRowMap> transactions =  this.jdbcTemplate.query(sql, rm, new Object[]{id});
        return ResponseEntity.ok().body(new TransactionResponse().transactionResponse(transactions));
    }

    @Override
    public ResponseEntity<?> readTransactionsByConsultationIdAndNotReversed(Long id) {
        final TransactionDataRowMapper rm = new TransactionDataRowMapper();
        final String sql = "select " + rm.schema() + " where co.id = ? and tr.is_reversed = false  order by tr.id DESC ";
        List <TransactionRowMap> transactions =  this.jdbcTemplate.query(sql, rm, new Object[]{id});
        return ResponseEntity.ok().body(new TransactionResponse().transactionResponse(transactions));
    }

    @Override
    public ResponseEntity<?> readTransactionByMedicalServiceId(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<?> readTransactionByMedicalServiceIdAndDate(Long id, LocalDateTime date) {
        return null;
    }

    @Override
    public ResponseEntity<?> readTransactionsByDepartmentId(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<?> readTransactionByDepartmentIdAndDate(Long id, LocalDateTime date) {
        return null;
    }

    private static final class TransactionDataRowMapper implements RowMapper<TransactionRowMap> {

        public String schema() {
            return " tr.id as id, tr.amount as amount, " +
                    " tr.currency_code as currencyCode, tr.is_reversed as isReversed, " +
                    " DATE_FORMAT(tr.transaction_date, \"%W %M %e %Y %r\") AS  transactionDate, " +
                    " co.id as consultationId, " +
                    " d.id as departmentId, d.name as departmentName, " +
                    " s.id as medicalServiceId, s.name as medicalServiceName " +
                    " FROM m_transactions tr " +
                    " JOIN m_consultations co on co.id = tr.consultation_id " +
                    " JOIN m_department d on d.id = tr.department_id " +
                    " JOIN m_services s on s.id = tr.medical_service_id " +
                    " ";
        }

        @Override
        public TransactionRowMap mapRow(ResultSet rs, int rowNum) throws SQLException {
            final Long id = rs.getLong("id");
            final String currencyCode = rs.getString("currencyCode");
            final BigDecimal amount = rs.getBigDecimal("amount");
            final Boolean isReversed = rs.getBoolean("isReversed");
            final String transactionDate = rs.getString("transactionDate");
            final Long consultationId = rs.getLong("consultationId");
            final Long departmentId = rs.getLong("departmentId");
            final String departmentName = rs.getString("departmentName");
            final Long medicalServiceId = rs.getLong("medicalServiceId");
            final String medicalServiceName = rs.getString("medicalServiceName");

            return new TransactionRowMap(id, amount, currencyCode, isReversed, transactionDate, consultationId, departmentId, departmentName, medicalServiceId, medicalServiceName);
        }
    }
}
