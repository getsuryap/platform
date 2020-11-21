package org.ospic.inventory.admission.service;

import org.ospic.inventory.admission.data.AdmissionRequest;
import org.ospic.inventory.admission.domains.Admission;
import org.ospic.inventory.admission.repository.AdmissionRepository;
import org.ospic.inventory.beds.domains.Bed;
import org.ospic.inventory.beds.repository.BedRepository;
import org.ospic.patient.infos.domain.Patient;
import org.ospic.patient.infos.repository.PatientInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

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
public class AdmissionsWriteServiceImpl implements AdmissionsWriteService {

    @Autowired
    AdmissionRepository admissionRepository;
    @Autowired
    BedRepository bedRepository;
    @Autowired
    PatientInformationRepository patientInformationRepository;

    public AdmissionsWriteServiceImpl(
            AdmissionRepository admissionRepository,
            BedRepository bedRepository,
            PatientInformationRepository patientInformationRepository) {
        this.admissionRepository = admissionRepository;
        this.bedRepository = bedRepository;
        this.patientInformationRepository = patientInformationRepository;
    }

    @Transactional
    @Override
    public ResponseEntity<String> admitPatient(AdmissionRequest admissionRequest) {
        Optional<Bed> bedOptional = bedRepository.findById(admissionRequest.getBedId());
        Optional<Patient> patientOptional = patientInformationRepository.findById(admissionRequest.getPatientId());

        Admission admission = new Admission(admissionRequest.getIsActive(), admissionRequest.getEndDateTime(),admissionRequest.getStartDateTime());
        admission.addPatient(patientOptional.get());
        admission.addBed(bedOptional.get());

        admissionRepository.save(admission);
        return ResponseEntity.ok().body("Patient Admitted successfully");
    }

    @Override
    public ResponseEntity<String> endPatientAdmission() {
        return null;
    }

    @Override
    public ResponseEntity<String> updatePatientAdmissionInfo() {
        return null;
    }
}
