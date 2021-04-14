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
public class UserStatistics {
    private Long totalUsers;
    private Long totalStaffs;

    public UserStatistics(Long totalUsers, Long totalStaffs) {
        this.totalUsers = totalUsers;
        this.totalStaffs = totalStaffs;
    }

    public static class  UserStatisticsRowMapper implements RowMapper< UserStatistics>{

        String queryString = "SELECT COUNT(*) as users, COUNT(IF(isStaff,1,NULL))'staff' FROM users; ";

        public String schema(){
            return  queryString;
        }
        @Override
        public UserStatistics mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserStatistics us = new  UserStatistics();
            us.setTotalStaffs(rs.getLong("staff"));
            us.setTotalUsers(rs.getLong("users"));

            return us;
        }
    }
}
