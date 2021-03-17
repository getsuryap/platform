package org.ospic.platform.patient.consultation.service;

import org.ospic.platform.patient.consultation.data.ConsultationPayload;
import org.ospic.platform.patient.consultation.exception.ConsultationNotFoundExceptionPlatform;
import org.ospic.platform.patient.consultation.mappers.ConsultationResourceMapper;
import org.ospic.platform.patient.consultation.repository.ConsultationResourceJpaRepository;
import org.ospic.platform.patient.details.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

/**
 * This file was created by eli on 23/12/2020 for org.ospic.platform.patient.consultation.service
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
public class ConsultationResourceReadPrinciplesServiceImpl implements ConsultationResourceReadPrinciplesService {
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    ConsultationResourceJpaRepository resourceJpaRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ConsultationResourceReadPrinciplesServiceImpl(
            PatientRepository patientRepository, ConsultationResourceJpaRepository resourceJpaRepository,
            final DataSource dataSource) {
        this.patientRepository = patientRepository;
        this.resourceJpaRepository = resourceJpaRepository;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public ResponseEntity<?> retrieveAllConsultations() {
        ConsultationResourceMapper rm = new ConsultationResourceMapper();
        final String sql = "select  " + rm.schema() + "";
        List<ConsultationPayload> payloads = this.jdbcTemplate.query(sql, rm, new Object[]{});
        return ResponseEntity.ok().body(payloads);
    }

    @Override
    public ResponseEntity<?> retrialAllActiveConsultations() {
        ConsultationResourceMapper rm = new ConsultationResourceMapper();
        final String sql = "select  " + rm.schema() + " WHERE s.is_active = true";
        List<ConsultationPayload> payloads = this.jdbcTemplate.query(sql, rm, new Object[]{});
        return ResponseEntity.ok(payloads);
    }

    @Override
    public ResponseEntity<?> retrieveAllInactiveConsultations() {
        ConsultationResourceMapper rm = new ConsultationResourceMapper();
        final String sql = "select  " + rm.schema() + " WHERE s.is_active = false";
        List<ConsultationPayload> payloads = this.jdbcTemplate.query(sql, rm, new Object[]{});
        return ResponseEntity.ok(payloads);
    }

    @Override
    public ResponseEntity<?> retrieveAConsultationById(Long serviceId) {
      return resourceJpaRepository.findById(serviceId).map(service ->
                ResponseEntity.ok().body(service)
        ).orElseThrow(() -> new ConsultationNotFoundExceptionPlatform(serviceId));
       
    }

    @Override
    public ResponseEntity<?> retrieveConsultationsByPatientId(Long patientId) {
        ConsultationResourceMapper rm = new ConsultationResourceMapper();
        final String sql = "select  " + rm.schema() + " WHERE s.patient_id = ?";
        List<ConsultationPayload> payloads = this.jdbcTemplate.query(sql, rm, new Object[]{patientId});
        return ResponseEntity.ok().body(payloads);
    }

    @Override
    public ResponseEntity<?> retrieveConsultationByPatientIdAndIsActiveFalse(Long patientId) {
        ConsultationResourceMapper rm = new ConsultationResourceMapper();
        final String sql = "select  " + rm.schema() + " WHERE s.patient_id = ? AND !s.is_active";
        List<ConsultationPayload> payloads = this.jdbcTemplate.query(sql, rm, new Object[]{patientId});
        return ResponseEntity.ok().body(payloads);
    }

    @Override
    public ResponseEntity<?> retrieveConsultationByStaffIdAndIsActiveTrue(Long staffId) {
        ConsultationResourceMapper rm = new ConsultationResourceMapper();
        final String sql = "select  " + rm.schema() + " WHERE s.staff_id = ? AND s.is_active";
        List<ConsultationPayload> payloads = this.jdbcTemplate.query(sql, rm, new Object[]{staffId});
        return ResponseEntity.ok().body(payloads);
    }

    @Override
    public ResponseEntity<?> retrieveConsultationByStaffIdAndIsActiveFalse(Long staffId) {
        ConsultationResourceMapper rm = new ConsultationResourceMapper();
        final String sql = "select  " + rm.schema() + " WHERE s.staff_id = ? AND! s.is_active";
        List<ConsultationPayload> payloads = this.jdbcTemplate.query(sql, rm, new Object[]{staffId});
        return ResponseEntity.ok().body(payloads);
    }

    @Override
    public ResponseEntity<?> retrieveConsultationByStaffIdAll(Long staffId) {
        ConsultationResourceMapper rm = new ConsultationResourceMapper();
        final String sql = "select  " + rm.schema() + " WHERE s.staff_id = ? ";
        List<ConsultationPayload> payloads = this.jdbcTemplate.query(sql, rm, new Object[]{staffId});
        return ResponseEntity.ok().body(payloads);
    }

    @Override
    public ResponseEntity<?> retrieveAllActiveConsultationsInIpd() {
        ConsultationResourceMapper rm = new ConsultationResourceMapper();
        final String sql = "select  " + rm.schema() + " WHERE  s.is_active = true AND s.is_admitted = true ";
        List<ConsultationPayload> payloads = this.jdbcTemplate.query(sql, rm, new Object[]{});
        return ResponseEntity.ok().body(payloads);
    }

    @Override
    public ResponseEntity<?> retrialAllAllActiveConsultationInOpd() {
        ConsultationResourceMapper rm = new ConsultationResourceMapper();
        final String sql = "select  " + rm.schema() + " WHERE s.is_active = true AND s.is_admitted =false";
        List<ConsultationPayload> payloads = this.jdbcTemplate.query(sql, rm, new Object[]{});
        return ResponseEntity.ok().body(payloads);
    }

    @Override
    public ResponseEntity<?> retrieveConsultationByPatientIdAndIsActiveTrue(Long patientId) {
        ConsultationResourceMapper rm = new ConsultationResourceMapper();
        final String sql = "select  " + rm.schema() + " WHERE s.patient_id = ? AND s.is_active";
        List<ConsultationPayload> payloads = this.jdbcTemplate.query(sql, rm, new Object[]{patientId});
        return ResponseEntity.ok().body(payloads);
    }
}
