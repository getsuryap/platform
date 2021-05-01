package org.ospic.platform.patient.consultation.mappers;

import org.ospic.platform.patient.consultation.data.ConsultationPayload;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This file was created by eli on 25/12/2020 for org.ospic.platform.inventory.admission.wrappers
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
public class ConsultationResourceMapper implements RowMapper<ConsultationPayload> {
    public String schema() {
        return  " s.id as id, DATE_FORMAT(s.fromdate, \"%W %M %e %Y \") as fromDate, DATE_FORMAT(s.todate, \"%W %M %e %Y \") as toDate,  s.is_active as isActive, " +
                " s.patient_id as patientId, p.name as patientName, " +
                " s.staff_id as staffId, st.fullName as staffName,  " +
                " s.is_admitted as isAdmitted FROM m_consultations s " +
                " left join m_patients p on p.id = s.patient_id  " +
                " left join m_staff st on st.user_id = s.staff_id ";
    }

    @Override
    public ConsultationPayload mapRow(ResultSet rs, int i) throws SQLException {
        final Long id = rs.getLong("id");
        final String fromDate = rs.getString("fromDate");
        final String toDate = rs.getString("toDate");
        final Boolean isActive = rs.getBoolean("isActive");
        final Boolean  isAdmitted = rs.getBoolean("isAdmitted");
        final Long patientId = rs.getLong("patientId");
        final String patientName = rs.getString("patientName");
        final Long staffId = rs.getLong("staffId");
        final String staffName = rs.getString("staffName");
        return ConsultationPayload.instance(id, fromDate,toDate, isActive,isAdmitted, patientId, patientName, staffId, staffName);
    }
}
