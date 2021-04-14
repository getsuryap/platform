package org.ospic.platform.organization.statistics.data;

import lombok.Data;
import lombok.NoArgsConstructor;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This file was created by eli on 20/12/2020 for org.ospic.platform.patient.details.data
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
@Data
@NoArgsConstructor
public class PatientStatistics {
    private  Long total;
    private  Long totalMale;
    private  Long totalFemale;
    private  Long totalUnspecified;


    public PatientStatistics(Long total, Long totalMale, Long totalFemale, Long totalUnspecified, Long totalIpd, Long totalOpd, Long totalAssigned, Long totalUnassigned) {
        this.total = total;
        this.totalMale = totalMale;
        this.totalFemale = totalFemale;
        this.totalUnspecified = totalUnspecified;

    }

    public static class StatisticsDataRowMapper implements RowMapper<PatientStatistics>{
        String queryString = " SELECT " +
                " COUNT(*) as total,  " +
                " COUNT(IF(gender = 'male' ,1, NULL))'male', " +
                " COUNT(IF(gender = 'female' ,1, NULL))'female', " +
                " SUM(case when gender like 'male' then 1 else 0 end) 'males', " +
                " COUNT(IF(gender = 'unspecified' ,1, NULL))'unspecified' " +
                " FROM m_patients; ";

        public String schema(){
            return queryString;
        };

        @Override
        public PatientStatistics mapRow(ResultSet rs, int i) throws SQLException {
            PatientStatistics st = new PatientStatistics();
            st.setTotal(rs.getLong("total"));
            st.setTotalFemale(rs.getLong("female"));
            st.setTotalMale(rs.getLong("male"));
            st.setTotalUnspecified(rs.getLong("unspecified"));
            return st;
        }
    }
}
