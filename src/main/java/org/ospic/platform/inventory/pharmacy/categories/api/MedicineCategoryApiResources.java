package org.ospic.platform.inventory.pharmacy.categories.api;

/**
 * This file was created by eli on 12/11/2020 for org.ospic.platform.inventory.pharmacy.groups.api
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.inventory.pharmacy.categories.data.MedicineCategoryRequest;
import org.ospic.platform.inventory.pharmacy.categories.data.MedicineCategoryRowMapper;
import org.ospic.platform.inventory.pharmacy.categories.domains.MedicineCategory;
import org.ospic.platform.inventory.pharmacy.categories.repository.MedicineCategoryRepository;
import org.ospic.platform.inventory.pharmacy.groups.exception.MedicineGroupNotFoundExceptionPlatform;
import org.ospic.platform.inventory.pharmacy.measurements.exception.MeasurementUnitNotFoundExceptionsPlatform;
import org.ospic.platform.inventory.pharmacy.measurements.repository.MeasurementUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.sql.DataSource;
import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/pharmacy/medicines/categories")
@Api(value = "/api/pharmacy/medicines/categories", tags = "Medicine Categories")
public class MedicineCategoryApiResources {

    private final MedicineCategoryRepository medicineCategoryRepository;
    private final MeasurementUnitRepository measurementUnitRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
   MedicineCategoryApiResources(MedicineCategoryRepository medicineCategoryRepository,
                                        MeasurementUnitRepository measurementUnitRepository,
                                        final DataSource dataSource) {
        this.medicineCategoryRepository = medicineCategoryRepository;
        this.measurementUnitRepository = measurementUnitRepository;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @ApiOperation(value = "RETRIEVE list of available Medicine categories ", notes = "RETRIEVE list of available Medicine categories", response = MedicineCategory.class, responseContainer = "List")
    @RequestMapping(value = "", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    
    ResponseEntity<?> retrieveAllMedicineCategories() {
        final MedicineCategoryRowMapper rm = new MedicineCategoryRowMapper();
        final String sql = rm.schema();
        return ResponseEntity.ok().body(this.jdbcTemplate.query(sql, rm, new Object[]{}));
    }

    @ApiOperation(value = "RETRIEVE Medicine category by ID", notes = "RETRIEVE  Medicine category by ID", response = MedicineCategory.class)
    @RequestMapping(value = "/{medicineCategoryId}", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    
    ResponseEntity<?> retrieveMedicineCategoryById(@PathVariable Long medicineCategoryId) {
        if (medicineCategoryRepository.findById(medicineCategoryId).isPresent()) {
            return ResponseEntity.ok().body(medicineCategoryRepository.findById(medicineCategoryId).get());
        }
        return ResponseEntity.ok().body(String.format("Medicine Category with ID %2d not found", medicineCategoryId));
    }

    @ApiOperation(value = "UPDATE Medicine category", notes = "UPDATE Medicine category", response = MedicineCategory.class)
    @RequestMapping(value = "/{medicineCategoryId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    
    ResponseEntity<?> updateMedicineCategoryById(@PathVariable Long medicineCategoryId, @Valid @RequestBody MedicineCategoryRequest request) {
        return medicineCategoryRepository.findById(medicineCategoryId).map(md -> {
            return measurementUnitRepository.findById(request.getMeasurementId()).map(unit -> {
                md.setMeasurementUnit(unit);
                md.setName(request.getName());
                md.setDescriptions(request.getDescriptions());
                return ResponseEntity.ok().body(medicineCategoryRepository.save(md));
            }).orElseThrow(() -> new MeasurementUnitNotFoundExceptionsPlatform(request.getMeasurementId()));
        }).orElseThrow(() -> new MedicineGroupNotFoundExceptionPlatform(medicineCategoryId));
    }

    @ApiOperation(value = "ADD new Medicine category", notes = "ADD new Medicine category", response = MedicineCategory.class)
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    
    ResponseEntity<String> addNewMedicineGroup(@Valid @RequestBody MedicineCategoryRequest payload) throws SQLIntegrityConstraintViolationException {
        try {
            MedicineCategory category = new MedicineCategory().instance(payload.getName(), payload.getDescriptions());
            measurementUnitRepository.findById(payload.getMeasurementId()).map(measurementUnit -> {
                category.setMeasurementUnit(measurementUnit);
                return ResponseEntity.ok(medicineCategoryRepository.save(category));
            });

        } catch (EntityNotFoundException e) {
            throw new MeasurementUnitNotFoundExceptionsPlatform(payload.getMeasurementId());
        }
        return ResponseEntity.ok("Medicine category saved successfully...");
    }

    @ApiOperation(value = "ADD new Medicine categories", notes = "ADD new Medicine categories", response = MedicineCategory.class)
    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    
    ResponseEntity<String> addNewMedicineCategories(@Valid @RequestBody List<MedicineCategory> medicineCategories) {
        StringBuilder sb = new StringBuilder();
        medicineCategories.forEach(category -> {
            if (!medicineCategoryRepository.existsByName(category.getName())) {
                medicineCategoryRepository.save(category);
            } else {
                sb.append(String.format("Medicine category Category with name: `%s` already exist \n", category.getName()));
            }
        });
        String sbs = sb.toString();
        return ResponseEntity.ok().body(sbs.isEmpty() ? "All Categories Added Successfully" : sbs);

    }
}
