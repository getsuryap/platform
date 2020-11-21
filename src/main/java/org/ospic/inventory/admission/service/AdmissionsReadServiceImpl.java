package org.ospic.inventory.admission.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ospic.inventory.admission.domains.Admission;
import org.ospic.inventory.admission.repository.AdmissionRepository;
import org.ospic.inventory.beds.domains.Bed;
import org.ospic.inventory.beds.repository.BedRepository;
import org.ospic.patient.infos.repository.PatientInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This file was created by eli on 09/11/2020 for org.ospic.inventory.admission.service
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
public class AdmissionsReadServiceImpl implements AdmissionsReadService {

    @Autowired
    AdmissionRepository admissionRepository;
    @Autowired
    BedRepository bedRepository;
    @Autowired
    PatientInformationRepository patientRepository;

    public AdmissionsReadServiceImpl(
            BedRepository bedRepository,
            PatientInformationRepository patientRepository,
            AdmissionRepository admissionRepository) {
        this.admissionRepository = admissionRepository;
        this.bedRepository = bedRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public ResponseEntity<List<Admission>> retrieveAllAdmissions() {
        List<Admission> admissions = admissionRepository.findAll();

        return ResponseEntity.ok(admissions);
    }

    @Override
    public ResponseEntity<List<Admission>> retrieveListOfAdmissionInBedId(Long bedId) {

        return ResponseEntity.ok().body(admissionRepository.findByBedsId(bedId));
    }

    @Override
    public ResponseEntity<List<Admission>> retrieveListOfAdmissionInWardId(Long wardId) {
        return null;
    }

    @Override
    public ResponseEntity<List<Admission>> retrieveListOfPatientAdmission(Long patientId) {
        return ResponseEntity.ok().body(admissionRepository.findByPatientsId(patientId));
    }

    @Override
    public ResponseEntity<List<Admission>> retrieveListOfActiveAdmissions() {
        return null;
    }

    @Override
    public ResponseEntity<List<Admission>> retrieveListOfInactiveAdmissions() {
        return null;
    }
}
