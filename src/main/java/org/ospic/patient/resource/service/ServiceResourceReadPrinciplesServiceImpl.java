package org.ospic.patient.resource.service;

import org.ospic.patient.infos.repository.PatientRepository;
import org.ospic.patient.resource.domain.ServiceResource;
import org.ospic.patient.resource.exception.ServiceNotFoundException;
import org.ospic.patient.resource.repository.ServiceResourceJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.Collection;

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
public class ServiceResourceReadPrinciplesServiceImpl implements ServiceResourceReadPrinciplesService {
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    ServiceResourceJpaRepository resourceJpaRepository;

    @Autowired
    public ServiceResourceReadPrinciplesServiceImpl(
            PatientRepository patientRepository, ServiceResourceJpaRepository resourceJpaRepository) {
        this.patientRepository = patientRepository;
        this.resourceJpaRepository = resourceJpaRepository;
    }

    @Override
    public ResponseEntity<?> retrieveAllServices() {
        Collection<ServiceResource> resources = resourceJpaRepository.findAll();
        return ResponseEntity.ok().body(resources);
    }

    @Override
    public ResponseEntity<?> retrialAllActiveServices() {
        return ResponseEntity.ok(resourceJpaRepository.findByIsActiveTrue());
    }

    @Override
    public ResponseEntity<?> retrieveAllInactiveServices() {
        return ResponseEntity.ok(resourceJpaRepository.findByIsActiveFalse());
    }

    @Override
    public ResponseEntity<?> retrieveAServiceById(Long serviceId) {
        return resourceJpaRepository.findById(serviceId).map(service->{
            return ResponseEntity.ok().body(resourceJpaRepository.findById(serviceId));
        }).orElseThrow(()-> new ServiceNotFoundException(serviceId));
    }

    @Override
    public ResponseEntity<?> retrieveServiceByPatientId(Long patientId) {
        return ResponseEntity.ok().body(resourceJpaRepository.findByPatientId(patientId));
    }

    @Override
    public ResponseEntity<?> retrieveServiceByPatientIdAndIsActiveFalse(Long patientId) {
        return ResponseEntity.ok().body(resourceJpaRepository.findByPatientIdAndIsActiveFalse(patientId));
    }

    @Override
    public ResponseEntity<?> retrieveServiceByPatientIdAndIsActiveTrue(Long patientId) {
        return ResponseEntity.ok().body(resourceJpaRepository.findByPatientIdAndIsActiveTrue(patientId));
    }
}
