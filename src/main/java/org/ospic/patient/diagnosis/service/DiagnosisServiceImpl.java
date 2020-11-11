package org.ospic.patient.diagnosis.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.ospic.patient.diagnosis.domains.Diagnosis;
import org.ospic.patient.diagnosis.repository.DiagnosisRepository;
import org.ospic.patient.infos.domain.Patient;
import org.ospic.patient.infos.repository.PatientInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This file was created by eli on 19/10/2020 for org.ospic.patient.diagnosis.service
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
    PatientInformationRepository patientInformationRepository;
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    public DiagnosisServiceImpl(
            DiagnosisRepository diagnosisRepository,
            PatientInformationRepository patientInformationRepository){
        this.diagnosisRepository = diagnosisRepository;
        this.patientInformationRepository = patientInformationRepository;
    }
    @Override
    public ResponseEntity<String> saveDiagnosisReport(Long patientId, Diagnosis diagnosis) {
        return patientInformationRepository.findById(patientId).map(patient -> {
            diagnosis.setPatient(patient);
            diagnosisRepository.save(diagnosis);
            return ResponseEntity.ok().body("Diagnosis added successfully ");
        }).orElseGet(() -> {
            return null;
        });
    }

    @Override
    public ResponseEntity<List<Diagnosis>>  retrieveAllDiagnosisReports() {
        Session session = this.sessionFactory.openSession();
        List<Diagnosis> diagnoses = session.createQuery("from m_diagnosis").list();
        session.close();
        return ResponseEntity.ok().body(diagnoses);
    }

    @Override
    public ResponseEntity<List<Diagnosis>>  retrieveAllDiagnosisReportsByPatientId(Long patientId) {
        return null;
    }
}
