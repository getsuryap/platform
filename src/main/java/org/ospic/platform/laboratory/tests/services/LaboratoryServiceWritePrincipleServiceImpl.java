package org.ospic.platform.laboratory.tests.services;

import org.ospic.platform.domain.CustomReponseMessage;
import org.ospic.platform.laboratory.tests.domain.LaboratoryService;
import org.ospic.platform.laboratory.tests.exceptions.LaboratoryServiceNotFoundException;
import org.ospic.platform.laboratory.tests.repository.LaboratoryServiceJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

/**
 * This file was created by eli on 15/02/2021 for org.ospic.platform.laboratory.tests.services
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
public class LaboratoryServiceWritePrincipleServiceImpl implements LaboratoryServiceWritePrincipleService {
    @Autowired
    LaboratoryServiceJpaRepository repository;

    @Override
    public ResponseEntity<?> createLaboratoryService(LaboratoryService  payload) {
       LaboratoryService ls = this.repository.save(payload);
        return ResponseEntity.ok().body(ls);
    }

    @Override
    public ResponseEntity<?> updateLaboratoryService(Long id, LaboratoryService payload) {
        return this.repository.findById(id).map(service->{
            payload.setId(service.getId());
            LaboratoryService ups = this.repository.save(payload);
            return ResponseEntity.ok().body(ups);
        }).orElseThrow(()->new LaboratoryServiceNotFoundException(id));
    }

    @Override
    public ResponseEntity<?> deleteLaboratoryService(Long id) {
        return this.repository.findById(id).map(service->{
            this.repository.deleteById(id);
            return ResponseEntity.ok().body(new CustomReponseMessage(HttpStatus.OK.value(),String.format("Laboratory service %2d deleted",id)));
        }).orElseThrow(()->new LaboratoryServiceNotFoundException(id));
    }

    @Override
    public ResponseEntity<?> activateLaboratoryService(Long id) {
        return this.repository.findById(id).map(service->{
            service.setIsActive(true);
            LaboratoryService ups = this.repository.save(service);
            return ResponseEntity.ok().body(ups);
        }).orElseThrow(()->new LaboratoryServiceNotFoundException(id));
    }

    @Override
    public ResponseEntity<?> deactivateLaboratoryService(Long id) {
        return this.repository.findById(id).map(service->{
            service.setIsActive(false);
            LaboratoryService ups = this.repository.save(service);
            return ResponseEntity.ok().body(ups);
        }).orElseThrow(()->new LaboratoryServiceNotFoundException(id));
    }
}
