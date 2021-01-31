package org.ospic.platform.patient.diagnosis.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.ospic.platform.domain.CustomReponseMessage;
import org.ospic.platform.patient.diagnosis.domains.Diagnosis;
import org.ospic.platform.patient.diagnosis.repository.DiagnosisRepository;
import org.ospic.platform.patient.consultation.repository.ConsultationResourceJpaRepository;
import org.ospic.platform.util.constants.DatabaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This file was created by eli on 19/10/2020 for org.ospic.platform.patient.diagnosis.service
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
public class DiagnosisServiceImpl implements DiagnosisService {

    DiagnosisRepository diagnosisRepository;
    ConsultationResourceJpaRepository serviceRepository;
    @Autowired
    SessionFactory sessionFactory;
    CustomReponseMessage cm;
    HttpHeaders httpHeaders;

    @Autowired
    public DiagnosisServiceImpl(
            DiagnosisRepository diagnosisRepository,
            ConsultationResourceJpaRepository serviceRepository) {
        this.diagnosisRepository = diagnosisRepository;
        this.serviceRepository = serviceRepository;
        cm  = new CustomReponseMessage();
        httpHeaders = new HttpHeaders();
    }

    @Override
    public ResponseEntity<?> saveDiagnosisReport(Long serviceId, Diagnosis diagnosis) {
        return serviceRepository.findById(serviceId).map(service -> {
            if (!service.getIsActive()){
                cm.setMessage( String.format("A service with ID: %2d does not exist ", serviceId));
                return new ResponseEntity<CustomReponseMessage>(cm, httpHeaders, HttpStatus.BAD_REQUEST);
            }
            diagnosis.setService(service);
            diagnosisRepository.save(diagnosis);
            cm.setMessage(String.format("Diagnosis added in service %2d successfully ", serviceId));
            return new ResponseEntity<CustomReponseMessage>(cm, httpHeaders, HttpStatus.OK);
        }).orElseGet(() -> { return null; });
    }

    @Override
    public ResponseEntity<List<Diagnosis>> retrieveAllDiagnosisReports() {
        Session session = this.sessionFactory.openSession();
        List<Diagnosis> diagnoses = session.createQuery(String.format("from %s order by date ASC", DatabaseConstants.DIAGNOSES_TABLE)).list();
        session.close();
        return ResponseEntity.ok().body(diagnoses);
    }

    @Override
    public ResponseEntity<List<Diagnosis>> retrieveAllDiagnosisReportsByServiceId(Long serviceId) {
        Session session = this.sessionFactory.openSession();
        List<Diagnosis> diagnoses = session.createQuery(String.format("from %s WHERE cid = %2d order by date ASC", DatabaseConstants.DIAGNOSES_TABLE, serviceId)).list();
        session.close();
        return ResponseEntity.ok().body(diagnoses);
    }
}
