package org.ospic.platform.inventory.pharmacy.categories.data;

import org.ospic.platform.inventory.pharmacy.categories.domains.MedicineCategory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This file was created by eli on 26/01/2021 for org.ospic.platform.inventory.pharmacy.categories.data
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
public class MedicineCategoryRowMapper implements RowMapper<MedicineCategoryRequest> {

    public String schema() {
        return "select mc.name,mc.descriptions,u.unit as unit, u.id as unitId from "+
                " m_mdc_categories mc inner join m_units u on mc.unit_id = u.id";
    }
    @Override
    public MedicineCategoryRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
        final String name= rs.getString("name");
        final String description = rs.getString("descriptions");
        final String unit = rs.getString("unit");
        final Long unitId = rs.getLong("unitId");
        MedicineCategoryRequest req = new MedicineCategoryRequest(name, description, unit);
        req.setMeasurementId(unitId);
        return req;
    }
}
