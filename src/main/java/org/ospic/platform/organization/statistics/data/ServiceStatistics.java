package org.ospic.platform.organization.statistics.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This file was created by eli on 08/01/2021 for org.ospic.platform.organization.statistics.data
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
public class ServiceStatistics {
    private Long total;
    private Long totalAssigned;
    private Long totalUnAssigned;
    private Long totalActive;
    private Long totalInActive;
    private Long totalOpd;
    private Long totalIpd;

    public ServiceStatistics(
            Long total, Long totalAssigned, Long totalUnAssigned,
            Long totalActive, Long totalInActive, Long totalOpd, Long totalIpd) {
        this.total = total;
        this.totalAssigned = totalAssigned;
        this.totalUnAssigned = totalUnAssigned;
        this.totalActive = totalActive;
        this.totalInActive = totalInActive;
        this.totalOpd = totalOpd;
        this.totalIpd = totalIpd;
    }

    public static class ServiceStatisticsRowMapper implements RowMapper<ServiceStatistics>{
        String queryString = "  SELECT COUNT(*) as total, " +
                "  COUNT(IF(is_active,1,NULL))'totalActive', " +
                "  COUNT(IF(is_active = 0,1,NULL))'totalInActive', " +
                "  COUNT(IF(staff_id,1,NULL))'totalAssigned', " +
                "  SUM(case WHEN staff_id IS NULL then 1 else 0 end)'totalUnAssigned', "+
                "  COUNT(IF(is_admitted,1,NULL))'totalIpd', " +
                "  COUNT(IF(is_admitted = 0,1,NULL))'totalOpd'  " +
                "  FROM m_consultations; ";
        public String schema(){
            return queryString;
        };

        @Override
        public ServiceStatistics mapRow(ResultSet rs, int rowNum) throws SQLException {
            ServiceStatistics se = new ServiceStatistics();
            se.setTotal(rs.getLong("total"));
            se.setTotalActive(rs.getLong("totalActive"));
            se.setTotalInActive(rs.getLong("totalInActive"));
            se.setTotalAssigned(rs.getLong("totalAssigned"));
            se.setTotalUnAssigned(rs.getLong("totalUnAssigned"));
            se.setTotalOpd(rs.getLong("totalOpd"));
            se.setTotalIpd(rs.getLong("totalIpd"));
            return se;
        }
    }
}
