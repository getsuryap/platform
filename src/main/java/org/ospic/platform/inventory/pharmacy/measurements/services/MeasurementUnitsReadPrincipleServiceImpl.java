package org.ospic.platform.inventory.pharmacy.measurements.services;

import org.ospic.platform.inventory.pharmacy.measurements.exception.MeasurementUnitNotFoundExceptionsPlatform;
import org.ospic.platform.inventory.pharmacy.measurements.repository.MeasurementUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

/**
 * This file was created by eli on 26/01/2021 for org.ospic.platform.inventory.pharmacy.measurements.services
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
public class MeasurementUnitsReadPrincipleServiceImpl implements MeasurementUnitsReadPrincipleService {
    public MeasurementUnitRepository repository;

    @Autowired
    public MeasurementUnitsReadPrincipleServiceImpl(MeasurementUnitRepository repository) {
        this.repository = repository;
    }

    @Override
    public ResponseEntity<?> fetchAllMedicineMeasurementUnit() {
        return ResponseEntity.ok().body(repository.findAll());
    }

    @Override
    public ResponseEntity<?> fetchMeasurementUnitById(Long id) {
        return repository.findById(id).map(unit -> {
            return ResponseEntity.ok().body(unit);
        }).orElseThrow(() -> new MeasurementUnitNotFoundExceptionsPlatform(id));
    }
}
