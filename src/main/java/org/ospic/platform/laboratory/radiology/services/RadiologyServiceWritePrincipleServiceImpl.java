package org.ospic.platform.laboratory.radiology.services;

import org.ospic.platform.domain.CustomReponseMessage;
import org.ospic.platform.laboratory.radiology.domain.RadiologyService;
import org.ospic.platform.laboratory.radiology.exceptions.RadiologyServiceNotFoundException;
import org.ospic.platform.laboratory.radiology.repository.RadiologyServiceJpaRepository;
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
public class RadiologyServiceWritePrincipleServiceImpl implements RadiologyServiceWritePrincipleService {
    @Autowired
    RadiologyServiceJpaRepository repository;

    @Override
    public ResponseEntity<?> createRadiologyService(RadiologyService payload) {
        if (payload.getIsActive() == null){
            payload.setIsActive(true);
        }
       RadiologyService ls = this.repository.save(payload);
        return ResponseEntity.ok().body(ls);
    }

    @Override
    public ResponseEntity<?> updateRadiologyService(Long id, RadiologyService payload) {
        return this.repository.findById(id).map(service->{
            service.setIsActive(payload.getIsActive() ==null? service.getIsActive() : payload.getIsActive());
            service.setDescriptions(payload.getDescriptions()==null? service.getDescriptions() : payload.getDescriptions());
            service.setName(payload.getName() == null?  service.getName() :  payload.getName());
            service.setPrice(payload.getPrice() ==null? service.getPrice() : payload.getPrice());
            RadiologyService ups = this.repository.save(service);
            return ResponseEntity.status(HttpStatus.OK).body(ups);
        }).orElseThrow(()->new RadiologyServiceNotFoundException(id));
    }

    @Override
    public ResponseEntity<?> deleteRadiologyService(Long id) {
        return this.repository.findById(id).map(service->{
            this.repository.deleteById(id);
            return ResponseEntity.ok().body(new CustomReponseMessage(HttpStatus.OK.value(),String.format("Radiology service %2d deleted",id)));
        }).orElseThrow(()->new RadiologyServiceNotFoundException(id));
    }

    @Override
    public ResponseEntity<?> activateRadiologyService(Long id) {
        return this.repository.findById(id).map(service->{
            service.setIsActive(true);
            RadiologyService ups = this.repository.save(service);
            return ResponseEntity.ok().body(ups);
        }).orElseThrow(()->new RadiologyServiceNotFoundException(id));
    }

    @Override
    public ResponseEntity<?> deactivateRadiologyService(Long id) {
        return this.repository.findById(id).map(service->{
            service.setIsActive(false);
            RadiologyService ups = this.repository.save(service);
            return ResponseEntity.ok().body(ups);
        }).orElseThrow(()->new RadiologyServiceNotFoundException(id));
    }
}
