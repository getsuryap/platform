package org.ospic.platform.patient.infos.data;

import org.springframework.jdbc.core.RowMapper;
import javax.swing.tree.TreePath;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This file was created by eli on 02/11/2020 for org.ospic.platform.patient.infos.data
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
public class PatientTrendsDataRowMapper implements RowMapper<PatientTrendDatas> {
    @Override
    public PatientTrendDatas mapRow(ResultSet resultSet, int i) throws SQLException {
        PatientTrendDatas pt = new PatientTrendDatas();
        pt.setDate(resultSet.getString("date"));
        pt.setFemale(resultSet.getLong("female"));
        pt.setMale(resultSet.getLong("male"));
        pt.setOther(resultSet.getLong("other"));
        pt.setTotal(resultSet.getLong("total"));
        return pt;
    }

}
