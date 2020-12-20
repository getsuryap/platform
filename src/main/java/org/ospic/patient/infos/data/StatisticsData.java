package org.ospic.patient.infos.data;

import lombok.Data;
import lombok.NoArgsConstructor;


import org.springframework.jdbc.core.RowMapper;
import javax.swing.tree.TreePath;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This file was created by eli on 20/12/2020 for org.ospic.patient.infos.data
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
public class StatisticsData {
    private  Long total;
    private  Long totalMale;
    private  Long totalFemale;
    private  Long totalUnspecified;
    private  Long totalIpd;
    private  Long totalOpd;
    private  Long totalAssigned;
    private  Long totalUnassigned;


    public StatisticsData(Long total, Long totalMale, Long totalFemale, Long totalUnspecified, Long totalIpd, Long totalOpd, Long totalAssigned, Long totalUnassigned) {
        this.total = total;
        this.totalMale = totalMale;
        this.totalFemale = totalFemale;
        this.totalUnspecified = totalUnspecified;
        this.totalIpd = totalIpd;
        this.totalOpd = totalOpd;
        this.totalAssigned = totalAssigned;
        this.totalUnassigned = totalUnassigned;
    }

    public static class StatisticsDataRowMapper implements RowMapper<StatisticsData>{

        @Override
        public StatisticsData mapRow(ResultSet rs, int i) throws SQLException {
            StatisticsData st = new StatisticsData();
            st.setTotal(rs.getLong("total"));
            st.setTotalAssigned(rs.getLong("assigned"));
            st.setTotalUnassigned(rs.getLong("unassigned"));
            st.setTotalIpd(rs.getLong("ipd"));
            st.setTotalOpd(rs.getLong("opd"));
            st.setTotalFemale(rs.getLong("female"));
            st.setTotalMale(rs.getLong("male"));
            st.setTotalUnspecified(rs.getLong("unspecified"));
            return st;
        }
    }
}
