package org.ospic.platform.organization.calendar.rowmap;

import org.ospic.platform.organization.calendar.data.EventColor;
import org.ospic.platform.security.services.UserDetailsImpl;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * This file was created by eli on 04/05/2021 for org.ospic.platform.organization.calendar.rowmap
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
public class CalendarRowMapper implements RowMapper<Map<String, Object>> {
    UserDetailsImpl ud = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    private String username = ud.getUsername();
    public String schema(){
        String schema =" c.*, date_format(c.start, \"%W %M %Y %r\") as startDate, " +
                "date_format(c.end, \"%W %M %Y %r\") as endDate from m_calendar c;";

        return " c.*, c.start as startDate, c.end as endDate from m_calendar c;";
    }
    @Override
    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
        final Map<String, Object> map = new HashMap<>();
        map.put("id", rs.getLong("id"));
        map.put("name", rs.getString("name"));
        map.put("start", rs.getString("startDate"));
        map.put("end", rs.getString("endDate"));
        map.put("color", EventColor.randomColors().color);
        map.put("timed", rs.getBoolean("timed"));
        map.put("createdBy", rs.getString("created_by"));
        map.put("createdDate", rs.getString("created_date"));
        map.put("lastModifiedDate", rs.getString("last_modified_date"));
        map.put("lastModifiedBy", rs.getString("last_modified_by"));
        map.put("ownedByMe",rs.getString("created_by").equals(username) );
        map.put("description",rs.getString("description"));
        return map;
    }
}
