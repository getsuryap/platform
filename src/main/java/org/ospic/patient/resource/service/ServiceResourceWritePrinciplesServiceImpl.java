package org.ospic.patient.resource.service;

import org.ospic.domain.CustomReponseMessage;
import org.ospic.inventory.admission.domains.Admission;
import org.ospic.organization.staffs.exceptions.StaffNotFoundException;
import org.ospic.organization.staffs.repository.StaffsRepository;
import org.ospic.patient.infos.domain.Patient;
import org.ospic.patient.infos.exceptions.PatientNotFoundException;
import org.ospic.patient.infos.repository.PatientRepository;
import org.ospic.patient.resource.domain.ServiceResource;
import org.ospic.patient.resource.exception.ServiceNotFoundException;
import org.ospic.patient.resource.repository.ServiceResourceJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * This file was created by eli on 23/12/2020 for org.ospic.patient.resource.service
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
public class ServiceResourceWritePrinciplesServiceImpl implements ServiceResourceWritePrinciplesService {
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    ServiceResourceJpaRepository resourceJpaRepository;
    @Autowired
    StaffsRepository staffsRepository;

    @Autowired
    public ServiceResourceWritePrinciplesServiceImpl(
            PatientRepository patientRepository, ServiceResourceJpaRepository resourceJpaRepository,
            StaffsRepository staffsRepository) {
        this.patientRepository = patientRepository;
        this.resourceJpaRepository = resourceJpaRepository;
        this.staffsRepository = staffsRepository;
    }

    @Override
    public ResponseEntity<?> createNewService(Long patientId) {
        return patientRepository.findById(patientId).map(patient -> {
            if (resourceJpaRepository.existsByPatientIdAndIsActiveTrue(patientId)) {
                return ResponseEntity.badRequest().body(String.format("A patient with ID: %2d already have an active instance running", patientId));
            }
            ServiceResource sr = new ServiceResource();
            sr.setPatient(patient);
            sr.setIsActive(true);
            sr.setFromdate(new Date());
            sr.setTodate(new Date());
            return ResponseEntity.ok().body(resourceJpaRepository.save(sr).getId());
        }).orElseThrow(()-> new PatientNotFoundException(patientId));

    }

    @Override
    public ResponseEntity<?> assignServiceToStaff(Long serviceId, Long staffId) {
        CustomReponseMessage cm = new CustomReponseMessage();
        HttpHeaders httpHeaders = new HttpHeaders();
        return this.staffsRepository.findById(staffId).map(staff -> {
            return this.resourceJpaRepository.findById(serviceId).map(service -> {
                service.setStaff(staff);
                staff.addService(service);
                staffsRepository.save(staff);
                cm.setMessage("A Service assigned successfully ");
                return ResponseEntity.ok().body("Service assigned successfully");
            }).orElseThrow(() -> new ServiceNotFoundException(serviceId));
        }).orElseThrow(() -> new StaffNotFoundException(staffId));
    }

    @Override
    public ResponseEntity<?> endServiceById(Long serviceId) {
        List<Admission> active = new ArrayList<>();
        return this.resourceJpaRepository.findById(serviceId).map(service -> {
            if (service.getPatient().getIsAdmitted()) {
                return ResponseEntity.ok().body("Service has active admission. End admission before you close the service");
            }
            service.getPatient().setIsAdmitted(false);
            service.getPatient().setIsActive(false);
            service.setIsActive(false);
            resourceJpaRepository.save(service);
            return ResponseEntity.ok().body("Service de-activated successfully");
        }).orElseThrow(() -> new ServiceNotFoundException(serviceId));
    }
}
