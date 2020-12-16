package org.ospic.security.authentication.roles.services;

import org.ospic.security.authentication.roles.data.RolePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

/**
 * This file was created by eli on 16/12/2020 for org.ospic.security.authentication.roles.services
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
@Repository
public class RoleReadPrincipleServicesImpl implements RoleReadPrincipleServices{

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    RoleReadPrincipleServicesImpl(final DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    @Override
    public ResponseEntity<?> retrieveAllRoles() {
        final RolePayloadDataRowMapper rm = new RolePayloadDataRowMapper();
        final String sql =  "select " +rm.schema() +  " from m_roles";

        return ResponseEntity.ok().body(this.jdbcTemplate.query(sql,rm,new Object[]{}));
    }

    public static final class RolePayloadDataRowMapper implements RowMapper<RolePayload>{
        public String schema() {
            return "id as id, name as name, role_id as roleId ";
        }

        @Override
        public RolePayload mapRow(ResultSet rs, int i) throws SQLException {
            final Long id = rs.getLong("id");
            final String name = rs.getString("name").toLowerCase(Locale.ROOT);
            final String roleId = rs.getString("roleId").toLowerCase(Locale.ROOT);
            return RolePayload.instance(id,name,roleId);
        }
    }
}
