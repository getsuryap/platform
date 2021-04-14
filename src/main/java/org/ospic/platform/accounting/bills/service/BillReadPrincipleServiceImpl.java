package org.ospic.platform.accounting.bills.service;

import org.ospic.platform.accounting.bills.data.BillPayload;
import org.ospic.platform.accounting.bills.repository.BillsJpaRepository;
import org.ospic.platform.accounting.bills.service.mapper.BillsRowMapper;
import org.ospic.platform.accounting.transactions.data.TransactionResponse;
import org.ospic.platform.accounting.transactions.data.TransactionRowMap;
import org.ospic.platform.accounting.transactions.service.mapper.TransactionDataRowMapper;
import org.ospic.platform.patient.consultation.repository.ConsultationResourceJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * This file was created by eli on 18/02/2021 for org.ospic.platform.accounting.bills.service
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
public class BillReadPrincipleServiceImpl implements BillReadPrincipleService {
    @Autowired
    BillsJpaRepository repository;
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    ConsultationResourceJpaRepository consultationResourceJpaRepository;

    @Autowired
    public BillReadPrincipleServiceImpl(
            BillsJpaRepository repository, final DataSource dataSource) {
        this.repository = repository;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Collection<BillPayload>  readAllBills() {
        BillsRowMapper rm = new BillsRowMapper();
        final String sql = rm.schema() + "  order by b.id DESC ";
        List<BillPayload> bills = this.jdbcTemplate.query(sql, rm, new Object[]{});
        return bills;
    }

    @Override
    public Collection<BillPayload>  readUnpaidBillsBills() {
        BillsRowMapper rm = new BillsRowMapper();
        final String sql = rm.schema() + " where b.is_paid = 0 order by b.id DESC ";
        List<BillPayload> bills = this.jdbcTemplate.query(sql, rm, new Object[]{});
        return bills;
    }

    @Override
    public Collection<BillPayload>  readBillsByPatientId(Long patientId) {
        BillsRowMapper rm = new BillsRowMapper();
        final String sql = rm.schema() + " where p.id = ? order by b.id DESC ";
        List<BillPayload> bills = this.jdbcTemplate.query(sql, rm, new Object[]{patientId});
        return bills;
    }

    @Override
    public ResponseEntity<?> readBillById(Long id) {

        BillsRowMapper rm = new BillsRowMapper();
        final String sql = rm.schema() + " where b.id = ? order by b.id DESC ";
        List<BillPayload> bill = this.jdbcTemplate.query(sql, rm, new Object[]{id});
        BillPayload billPayload = bill.get(0);


        final TransactionDataRowMapper trm = new TransactionDataRowMapper();
        final String sq = "select " + trm.schema() + " where co.id = ?  order by tr.id DESC ";
        List<TransactionRowMap> transactions = this.jdbcTemplate.query(sq, trm, billPayload.getConsultationId());
        TransactionResponse tresponse = new TransactionResponse().transactionResponse(transactions);

        this.updateBillAmount(billPayload.getConsultationId(), tresponse.getTotalAmount());
        billPayload.setTotalAmount(tresponse.getTotalAmount());
        billPayload.setTransactionResponse(tresponse);
        return ResponseEntity.ok().body(billPayload);
    }

    private void updateBillAmount(Long consultationId, BigDecimal amount) {
        this.consultationResourceJpaRepository.findById(consultationId).map(c -> {
            c.getBill().setTotalAmount(amount);
            this.consultationResourceJpaRepository.save(c);
            return null;
        });
    }
}
