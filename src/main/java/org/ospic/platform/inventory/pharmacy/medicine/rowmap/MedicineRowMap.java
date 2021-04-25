package org.ospic.platform.inventory.pharmacy.medicine.rowmap;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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
public class MedicineRowMap implements RowMapper<Map<String, Object>> {
    public String schema() {
        return " md.*, " +
                " expire_date < curdate() as isExpired, " +
                " expire_date < date_add(current_date(), interval 20 day) as isExpiring, " +
                " DATE_FORMAT(md.expire_date, \"%W %M %e %Y \") as expireOn, " +
                " ifnull(datediff(md.expire_date, curdate()),0) as daysToExpire FROM m_medicines md; ";
    };
    @Override
    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
        final Map<String, Object> map = new HashMap<>();
        map.put("id", rs.getLong("id"));
        map.put("name", rs.getString("name"));
        map.put("genericName", rs.getString("generic_name"));
        map.put("company", rs.getString("company"));
        map.put("compositions", rs.getString("compositions"));
        map.put("unit", rs.getString("units"));
        map.put("quantity", rs.getString("quantity"));
        map.put("effects", rs.getString("effects"));
        map.put("buyingPrice", rs.getBigDecimal("buying_price"));
        map.put("sellingPrice", rs.getBigDecimal("selling_price"));
        map.put("categoryId", rs.getLong("category_id"));
        map.put("groupId", rs.getLong("group_id"));
        map.put("storeBox", rs.getString("store_box"));
        map.put("isExpired", rs.getBoolean("isExpired"));
        map.put("isExpiring", rs.getBoolean("isExpiring"));
        map.put("expireOn", rs.getString("expireOn"));
        map.put("daysToExpire", rs.getLong("daysToExpire"));
        map.put("expireDate",rs.getString("expire_date"));

        return map;
    }
}
