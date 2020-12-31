package org.ospic.patient.resource.mappers;

import org.ospic.patient.resource.data.ServicePayload;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * This file was created by eli on 25/12/2020 for org.ospic.inventory.admission.wrappers
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
public class ServiceResourceMapper implements RowMapper<ServicePayload> {
    public String schema() {
        return  " s.id as id, s.fromdate as fromDate, s.todate as toDate, s.is_active as isActive, " +
                " s.patient_id as patientId, p.name as patientName, " +
                " s.staff_id as staffId, st.fullName as staffName  " +
                " FROM m_service s " +
                " left join m_patients p on p.id = s.patient_id  " +
                " left join m_staff st on st.user_id = s.staff_id ";
    }

    @Override
    public ServicePayload mapRow(ResultSet rs, int i) throws SQLException {
        final Long id = rs.getLong("id");
        final Date fromDate = rs.getDate("fromDate");
        final Date toDate = rs.getDate("toDate");
        final Boolean isActive = rs.getBoolean("isActive");
        final Long patientId = rs.getLong("patientId");
        final String patientName = rs.getString("patientName");
        final Long staffId = rs.getLong("staffId");
        final String staffName = rs.getString("staffName");
        final LocalDate fromDateLocal = LocalDate.parse(new SimpleDateFormat("yyy-MM-dd").format(fromDate));
        final LocalDate toDateLocal = LocalDate.parse(new SimpleDateFormat("yyy-MM-dd").format(toDate));
        return ServicePayload.instance(id, fromDateLocal, toDateLocal, isActive, patientId, patientName, staffId, staffName);
    }
}
