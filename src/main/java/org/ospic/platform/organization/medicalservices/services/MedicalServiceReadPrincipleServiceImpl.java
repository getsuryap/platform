package org.ospic.platform.organization.medicalservices.services;

import org.ospic.platform.organization.medicalservices.data.MedicalServicePayload;
import org.ospic.platform.organization.medicalservices.repository.MedicalServiceJpaRepository;
import org.ospic.platform.organization.medicalservices.rowmap.MedicalServiceRowMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collection;

/**
 * This file was created by eli on 02/02/2021 for org.ospic.platform.organization.medicalservices.services
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
public class MedicalServiceReadPrincipleServiceImpl implements MedicalServiceReadPrincipleService {
    private final MedicalServiceJpaRepository repository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    MedicalServiceReadPrincipleServiceImpl(MedicalServiceJpaRepository repository, final DataSource dataSource) {
        this.repository = repository;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Collection<MedicalServicePayload> readServices() {
        final MedicalServiceRowMap rm = new MedicalServiceRowMap();
        final String sql = rm.schema();
        Collection<MedicalServicePayload> services = this.jdbcTemplate.query(sql, rm, new Object[]{});
        return services;
    }

    @Override
    public Collection<MedicalServicePayload> readActiveServices() {
        final MedicalServiceRowMap rm = new MedicalServiceRowMap();
        final String sql = rm.schema() + " where  s.enabled ";
        Collection<MedicalServicePayload> services = this.jdbcTemplate.query(sql, rm, new Object[]{});
        return services;
    }

    @Override
    public ResponseEntity<?> readMedicalServicesByMedicalServiceType(Long medicalServiceTypeId) {
        return ResponseEntity.ok().body(this.repository.findByMedicalServiceTypeId(medicalServiceTypeId));
    }

    @Override
    public ResponseEntity<?> readServiceById(Long id) {
        return ResponseEntity.ok(repository.findById(id));
    }

    @Override
    public Collection<MedicalServicePayload> readServiceByMedicalServiceTypeName(String name) {
        final MedicalServiceRowMap rm = new MedicalServiceRowMap();
        final String sql = rm.schema()  +" where st.name = ? order by s.id DESC";
        Collection<MedicalServicePayload> services = this.jdbcTemplate.query(sql, rm, new Object[]{name});
        return services;
    }
}
