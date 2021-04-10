package org.ospic.platform.organization.servicetypes.services;

import org.ospic.platform.organization.servicetypes.domain.MedicalServiceTypes;
import org.ospic.platform.organization.servicetypes.repository.MedicalServiceTypesJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

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
public class MedicalServiceTypesReadPrincipleServiceImpl implements MedicalServiceTypesReadPrincipleService {
    public MedicalServiceTypesJpaRepository repository;

    @Autowired
    MedicalServiceTypesReadPrincipleServiceImpl(MedicalServiceTypesJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public ResponseEntity<?> readServicesTypes() {
        List<MedicalServiceTypes> services = repository.findAll();
        return ResponseEntity.ok().body(repository.findAll());
    }

    @Override
    public ResponseEntity<?> readServiceTypesById(Long id) {
        return ResponseEntity.ok(repository.findById(id));
    }

    @Override
    public ResponseEntity<?> readServiceTypeByName(String name) {
        return ResponseEntity.ok(repository.findByName(name));
    }
}
