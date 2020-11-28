package org.ospic.inventory.admission.service;

import org.ospic.authentication.AuthController;
import org.ospic.domain.CustomReponseMessage;
import org.ospic.inventory.admission.data.AdmissionRequest;
import org.ospic.inventory.admission.data.EndAdmissionRequest;
import org.ospic.inventory.admission.domains.Admission;
import org.ospic.inventory.admission.exception.AdmissionNotFoundException;
import org.ospic.inventory.admission.repository.AdmissionRepository;
import org.ospic.inventory.beds.domains.Bed;
import org.ospic.inventory.beds.repository.BedRepository;
import org.ospic.patient.infos.PatientNotFoundException;
import org.ospic.patient.infos.repository.PatientInformationRepository;
import org.ospic.util.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;

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
    private static final Logger logger = LoggerFactory.getLogger(AdmissionsWriteServiceImpl.class);



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
    public ResponseEntity<CustomReponseMessage> admitPatient(AdmissionRequest admissionRequest) {
        return patientInformationRepository.findById(admissionRequest.getPatientId()).map(patient -> {
            CustomReponseMessage cm = new CustomReponseMessage();
            HttpHeaders httpHeaders = new HttpHeaders();

            if (patient.getIsAdmitted()){
              cm.setMessage("Cannot re-admit an admitted patient");
                return new ResponseEntity<CustomReponseMessage>( cm, httpHeaders, HttpStatus.CONFLICT);
            }
            Optional<Bed> bedOptional = bedRepository.findById(admissionRequest.getBedId());
            Admission admission = new Admission(admissionRequest.getIsActive(), admissionRequest.getEndDateTime(), admissionRequest.getStartDateTime());
            admission.addPatient(patient);
            admission.addBed(bedOptional.get());
            patient.setIsAdmitted(true);
            patientInformationRepository.save(patient);
            admissionRepository.save(admission);
            cm.setMessage("Patient Admitted successfully");
            return new ResponseEntity<CustomReponseMessage>(cm,httpHeaders,HttpStatus.OK);
        }).orElseThrow(() -> new EntityNotFoundException());
    }

    @Transactional
    @Override
    public ResponseEntity<?> endPatientAdmission(EndAdmissionRequest request) {
        CustomReponseMessage cm = new CustomReponseMessage();
        HttpHeaders httpHeaders = new HttpHeaders();
        return patientInformationRepository.findById(request.getPatientId()).map(patient -> {
            return admissionRepository.findById(request.getAdmissionId()).map(admission -> {
                if (!(admission.getIsActive() || patient.getIsAdmitted())){
                    cm.setMessage("Can't end inactive admission or un-admitted patient");
                    return new ResponseEntity<>(cm, httpHeaders, HttpStatus.OK) ;
                }
                patient.setIsAdmitted(false);
                patientInformationRepository.save(patient);
                admission.setIsActive(false);
                admission.setToDateTime(request.getEndDateTime());


                admissionRepository.save(admission);
                cm.setMessage(String.format("Admission %2d for patient %s has being ended on %s ", request.getAdmissionId(), patient.getName(),request.getEndDateTime()));
                return new ResponseEntity<>(cm, httpHeaders, HttpStatus.OK) ;
            }).orElseThrow(() -> new AdmissionNotFoundException(request.getAdmissionId()));
        }).orElseThrow(() -> new PatientNotFoundException(request.getPatientId()));

    }

    @Transactional
    @Override
    public ResponseEntity<String> updatePatientAdmissionInfo() {
        return null;
    }
}
