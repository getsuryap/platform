package org.ospic.platform.patient.resource.service;

import org.ospic.platform.patient.infos.repository.PatientRepository;
import org.ospic.platform.patient.resource.data.ServicePayload;
import org.ospic.platform.patient.resource.domain.ServiceResource;
import org.ospic.platform.patient.resource.exception.ServiceNotFoundException;
import org.ospic.platform.patient.resource.mappers.ServiceResourceMapper;
import org.ospic.platform.patient.resource.repository.ServiceResourceJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;

/**
 * This file was created by eli on 23/12/2020 for org.ospic.platform.patient.resource.service
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
public class ServiceResourceReadPrinciplesServiceImpl implements ServiceResourceReadPrinciplesService {
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    ServiceResourceJpaRepository resourceJpaRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ServiceResourceReadPrinciplesServiceImpl(
            PatientRepository patientRepository, ServiceResourceJpaRepository resourceJpaRepository,
            final DataSource dataSource) {
        this.patientRepository = patientRepository;
        this.resourceJpaRepository = resourceJpaRepository;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public ResponseEntity<?> retrieveAllServices() {
        ServiceResourceMapper rm = new ServiceResourceMapper();
        final String sql = "select  " + rm.schema() + "";
        List<ServicePayload> payloads = this.jdbcTemplate.query(sql, rm, new Object[]{});
        return ResponseEntity.ok().body(payloads);
    }

    @Override
    public ResponseEntity<?> retrialAllActiveServices() {
        ServiceResourceMapper rm = new ServiceResourceMapper();
        final String sql = "select  " + rm.schema() + " WHERE s.is_active = true";
        List<ServicePayload> payloads = this.jdbcTemplate.query(sql, rm, new Object[]{});
        return ResponseEntity.ok(payloads);
    }

    @Override
    public ResponseEntity<?> retrieveAllInactiveServices() {
        ServiceResourceMapper rm = new ServiceResourceMapper();
        final String sql = "select  " + rm.schema() + " WHERE s.is_active = false";
        List<ServicePayload> payloads = this.jdbcTemplate.query(sql, rm, new Object[]{});
        return ResponseEntity.ok(payloads);
    }

    @Override
    public ResponseEntity<?> retrieveAServiceById(Long serviceId) {
        return resourceJpaRepository.findById(serviceId).map(service -> {
            return ResponseEntity.ok().body(resourceJpaRepository.findById(serviceId));
        }).orElseThrow(() -> new ServiceNotFoundException(serviceId));
    }

    @Override
    public ResponseEntity<?> retrieveServiceByPatientId(Long patientId) {
        ServiceResourceMapper rm = new ServiceResourceMapper();
        final String sql = "select  " + rm.schema() + " WHERE s.patient_id = ?";
        List<ServicePayload> payloads = this.jdbcTemplate.query(sql, rm, new Object[]{patientId});
        return ResponseEntity.ok().body(payloads);
    }

    @Override
    public ResponseEntity<?> retrieveServiceByPatientIdAndIsActiveFalse(Long patientId) {
        ServiceResourceMapper rm = new ServiceResourceMapper();
        final String sql = "select  " + rm.schema() + " WHERE s.patient_id = ? AND !s.is_active";
        List<ServicePayload> payloads = this.jdbcTemplate.query(sql, rm, new Object[]{patientId});
        return ResponseEntity.ok().body(payloads);
    }

    @Override
    public ResponseEntity<?> retrieveServiceByStaffIdAndIsActiveTrue(Long staffId) {
        ServiceResourceMapper rm = new ServiceResourceMapper();
        final String sql = "select  " + rm.schema() + " WHERE s.staff_id = ? AND s.is_active";
        List<ServicePayload> payloads = this.jdbcTemplate.query(sql, rm, new Object[]{staffId});
        return ResponseEntity.ok().body(payloads);
    }

    @Override
    public ResponseEntity<?> retrieveServiceByStaffIdAndIsActiveFalse(Long staffId) {
        ServiceResourceMapper rm = new ServiceResourceMapper();
        final String sql = "select  " + rm.schema() + " WHERE s.staff_id = ? AND! s.is_active";
        List<ServicePayload> payloads = this.jdbcTemplate.query(sql, rm, new Object[]{staffId});
        return ResponseEntity.ok().body(payloads);
    }

    @Override
    public ResponseEntity<?> retrieveServiceByStaffIdAll(Long staffId) {
        ServiceResourceMapper rm = new ServiceResourceMapper();
        final String sql = "select  " + rm.schema() + " WHERE s.staff_id = ? ";
        List<ServicePayload> payloads = this.jdbcTemplate.query(sql, rm, new Object[]{staffId});
        return ResponseEntity.ok().body(payloads);
    }

    @Override
    public ResponseEntity<?> retrieveAllActiveServicesInIpd() {
        ServiceResourceMapper rm = new ServiceResourceMapper();
        final String sql = "select  " + rm.schema() + " WHERE  s.is_active = true AND s.is_admitted = true ";
        List<ServicePayload> payloads = this.jdbcTemplate.query(sql, rm, new Object[]{});
        return ResponseEntity.ok().body(payloads);
    }

    @Override
    public ResponseEntity<?> retrialAllAllActiveServiceInOpd() {
        ServiceResourceMapper rm = new ServiceResourceMapper();
        final String sql = "select  " + rm.schema() + " WHERE s.is_active = true AND s.is_admitted =false";
        List<ServicePayload> payloads = this.jdbcTemplate.query(sql, rm, new Object[]{});
        return ResponseEntity.ok().body(payloads);
    }

    @Override
    public ResponseEntity<?> retrieveServiceByPatientIdAndIsActiveTrue(Long patientId) {
        ServiceResourceMapper rm = new ServiceResourceMapper();
        final String sql = "select  " + rm.schema() + " WHERE s.patient_id = ? AND s.is_active";
        List<ServicePayload> payloads = this.jdbcTemplate.query(sql, rm, new Object[]{patientId});
        return ResponseEntity.ok().body(payloads);
    }
}
