package org.ospic.platform.organization.statistics.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.ospic.platform.util.DateUtil;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private LocalDate date;
    private Long total;
    private Long active;
    private Long inactive;
    private Long admitted;
    private Long unadmitted;

    public ServiceTrendStatistics(LocalDate date, Long total, Long active, Long inactive, Long admitted, Long unadmitted) {
        this.date = date;
        this.total = total;
        this.active = active;
        this.inactive = inactive;
        this.admitted = admitted;
        this.unadmitted = unadmitted;
    }

    public static class ServiceTrendStatisticsRowMapper implements RowMapper<ServiceTrendStatistics>{

        @Override
        public ServiceTrendStatistics mapRow(ResultSet rs, int rowNum) throws SQLException {
            ServiceTrendStatistics sd = new ServiceTrendStatistics();
            final LocalDate localDate= new DateUtil().convertToLocalDateViaSqlDate(rs.getDate("date"));
            sd.setDate(localDate);
            sd.setTotal(rs.getLong("total"));
            sd.setActive(rs.getLong("active"));
            sd.setInactive(rs.getLong("active"));
            sd.setAdmitted(rs.getLong("admitted"));
            sd.setUnadmitted(rs.getLong("unadmitted"));
            return sd;
        }
    }
}
