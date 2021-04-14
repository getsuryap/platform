package org.ospic.platform.organization.statistics.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.ospic.platform.util.DateUtil;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

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
public class ServiceTrendStatistics {
    private String date;
    private Long total;
    private Long active;
    private Long inactive;
    private Long admitted;
    private Long unAdmitted;

    public ServiceTrendStatistics(String date, Long total, Long active, Long inactive, Long admitted, Long unAdmitted) {
        this.date = date;
        this.total = total;
        this.active = active;
        this.inactive = inactive;
        this.admitted = admitted;
        this.unAdmitted = unAdmitted;
    }

    public static class ServiceTrendStatisticsRowMapper implements RowMapper<ServiceTrendStatistics>{
        String queryString = " "+
                "  SELECT date(fromdate) as date, count(*) as total," +
                "  COUNT(IF(is_active, 1, NULL))'active', " +
                "  COUNT(IF(is_active = 0, 1, NULL))'inactive'," +
                "  COUNT(IF(is_admitted, 1, NULL))'admitted'," +
                "  COUNT(IF(is_admitted = 0, 1, NULL))'unadmitted'" +
                "  FROM m_consultations group by date(fromdate)";
        public String schema(){
            return  queryString;
        }

        @Override
        public ServiceTrendStatistics mapRow(ResultSet rs, int rowNum) throws SQLException {
            ServiceTrendStatistics sd = new ServiceTrendStatistics();
            final String date = new DateUtil().convertToLocalDateViaSqlDate(rs.getDate("date")).format(DateTimeFormatter.ofPattern("yyy-MM-dd"));;
            sd.setDate(date);
            sd.setTotal(rs.getLong("total"));
            sd.setActive(rs.getLong("active"));
            sd.setInactive(rs.getLong("active"));
            sd.setAdmitted(rs.getLong("admitted"));
            sd.setUnAdmitted(rs.getLong("unadmitted"));
            return sd;
        }
    }
}
