package org.ospic.platform.organization.medicalservices.rowmap;

import org.ospic.platform.organization.medicalservices.data.MedicalServicePayload;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This file was created by eli on 26/03/2021 for org.ospic.platform.accounting.statistics.rowmap
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
public class MedicalServiceRowMap implements RowMapper<MedicalServicePayload> {
    public String schema() {
        return " select s.*, st.name as serviceTypeName, st.id as serviceTypeId  from m_services s " +
                "join m_service_types st on s.service_type_id = st.id ";
    };
    @Override
    public MedicalServicePayload mapRow(ResultSet rs, int rowNum) throws SQLException {
        return MedicalServicePayload.fromResultSet(rs);
    }
}
