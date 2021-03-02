package org.ospic.platform.laboratory.radiology.services;

import org.ospic.platform.laboratory.radiology.domain.RadiologyService;
import org.ospic.platform.laboratory.radiology.exceptions.RadiologyServiceNotFoundException;
import org.ospic.platform.laboratory.radiology.repository.RadiologyServiceJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

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
public class RadiologyServiceReadPrincipleServiceImpl implements RadiologyServiceReadPrincipleService {
    @Autowired
    RadiologyServiceJpaRepository repository;

    @Override
    public ResponseEntity<?> listRadiologyServices() {
        List<RadiologyService> list = this.repository.findAll();
        return ResponseEntity.ok().body(list);
    }

    @Override
    public ResponseEntity<?> findRadiologyServiceById(Long id) {
        return this.repository.findById(id).map(service->{
            return ResponseEntity.ok().body(service);
        }).orElseThrow(()->new RadiologyServiceNotFoundException(id));
    }

    @Override
    public ResponseEntity<?> findRadiologyServiceByActiveStatus(Boolean status) {
        List<RadiologyService> services;
        if (status){
            services = this.repository.findByIsActiveTrue();
        }else services = this.repository.findByIsActiveFalse();

        return ResponseEntity.ok().body(services);
    }
}