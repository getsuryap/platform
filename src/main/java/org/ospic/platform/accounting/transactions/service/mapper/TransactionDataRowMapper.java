package org.ospic.platform.accounting.transactions.service.mapper;

import org.ospic.platform.accounting.transactions.data.TransactionRowMap;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This file was created by eli on 19/02/2021 for org.ospic.platform.accounting.transactions.service.mapper
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

public final class TransactionDataRowMapper implements RowMapper<TransactionRowMap> {

    public String schema() {
        return " tr.id as id, tr.amount as amount, " +
                " tr.currency_code as currencyCode, tr.is_reversed as isReversed, " +
                " DATE_FORMAT(tr.transaction_date, \"%W %M %e %Y %r\") AS  transactionDate, " +
                " co.id as consultationId, " +
                " d.id as departmentId, d.name as departmentName, " +
                " s.id as medicalServiceId, s.name as medicalServiceName, " +
                " md.name as medicineName, md.id as medicineId " +
                " FROM m_transactions tr " +
                " JOIN m_bills bl on bl.id = tr.bill_id " +
                " JOIN m_consultations co on co.id = bl.consultation_id " +
                " JOIN m_department d on d.id = tr.department_id " +
                " LEFT JOIN m_services s on s.id = tr.medical_service_id " +
                " LEFT JOIN m_medicines md on md.id = tr.medicine_id ";
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
        final String medicineName = rs.getString("medicineName");
        final Long medicineId = rs.getLong("medicineId");

        return new TransactionRowMap(id, amount, currencyCode, isReversed, transactionDate, consultationId, departmentId, departmentName, medicalServiceId, medicalServiceName, medicineId, medicineName);
    }
}
