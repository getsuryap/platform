package org.ospic.platform.accounting.bills.service.mapper;

import org.ospic.platform.accounting.bills.data.BillPayload;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

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
public final class BillsRowMapper implements RowMapper<BillPayload> {

    public String schema() {
        return " select b.*, DATE_FORMAT(b.created_date, \"%W %M %e %Y %r\") AS  createdDate," +
                "DATE_FORMAT(b.last_modified_date, \"%W %M %e %Y %r\") AS  lastModifiedDate, c.id as consultationId,  " +
                " c.is_active as isActive, p.id as patientId, p.name as patientName,  " +
                " p.phone as phoneNumber, p.address as address, " +
                " p.email_address as emailAddress  " +
                " FROM m_bills b  " +
                " inner join m_consultations c on c.id = b.consultation_id  " +
                " inner join m_patients p on p.id = c.patient_id ";
    }

    @Override
    public BillPayload mapRow(ResultSet rs, int rowNum) throws SQLException {
        return BillPayload.fromResultSet(rs);
    }
}
