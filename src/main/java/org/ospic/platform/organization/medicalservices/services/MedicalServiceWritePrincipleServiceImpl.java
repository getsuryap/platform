package org.ospic.platform.organization.medicalservices.services;

import org.ospic.platform.organization.medicalservices.domain.MedicalService;
import org.ospic.platform.organization.medicalservices.exceptions.MedicalServiceNotFoundExceptionPlatform;
import org.ospic.platform.organization.medicalservices.repository.MedicalServiceJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

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
public class MedicalServiceWritePrincipleServiceImpl implements MedicalServiceWritePrincipleService {
    public MedicalServiceJpaRepository repository;

    @Autowired
    MedicalServiceWritePrincipleServiceImpl(MedicalServiceJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public ResponseEntity<?> createService(MedicalService payload) {
        payload.setIsActive(true);
        return ResponseEntity.ok().body(repository.save(payload));
    }

    @Override
    public ResponseEntity<?> updateService(Long id, MedicalService payload) {
        return repository.findById(id).map(medicalService -> {
            payload.setId(medicalService.getId());
            return ResponseEntity.ok().body(repository.save(payload));
        }).orElseThrow(() -> new MedicalServiceNotFoundExceptionPlatform(id));
    }

    @Override
    public ResponseEntity<?> deleteService(Long id) {
        return repository.findById(id).map(medicalService -> {
            repository.deleteById(id);
            return ResponseEntity.ok().body("Deleted successfully...");
        }).orElseThrow(() -> new MedicalServiceNotFoundExceptionPlatform(id));
    }

    @Override
    public ResponseEntity<?> enableService(Long id) {
        return repository.findById(id).map(service -> {
            if (service.getIsActive()) {
                return ResponseEntity.ok().body("Enabled");
            }
            service.setIsActive(true);
            repository.save(service);
            return ResponseEntity.ok().body("Enabled");
        }).orElseThrow(() -> new MedicalServiceNotFoundExceptionPlatform(id));
    }

    @Override
    public ResponseEntity<?> disableService(Long id) {
        return repository.findById(id).map(service -> {
            if (!service.getIsActive()) {
                return ResponseEntity.ok().body("Disabled successfully");
            }
            service.setIsActive(false);
            repository.save(service);
            return ResponseEntity.ok().body("Disabled successfully");
        }).orElseThrow(() -> new MedicalServiceNotFoundExceptionPlatform(id));
    }
}
