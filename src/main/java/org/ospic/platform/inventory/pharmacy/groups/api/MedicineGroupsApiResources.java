package org.ospic.platform.inventory.pharmacy.groups.api;

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
import org.ospic.platform.inventory.pharmacy.categories.domains.MedicineCategory;
import org.ospic.platform.inventory.pharmacy.categories.repository.MedicineCategoryRepository;
import org.ospic.platform.inventory.pharmacy.groups.domains.MedicineGroup;
import org.ospic.platform.inventory.pharmacy.groups.repository.MedicineGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/pharmacy/medicines/categories")
@Api(value = "/api/pharmacy/medicines/categories", tags = "Medicine Categories")
public class MedicineGroupsApiResources {
    @Autowired
    MedicineCategoryRepository medicineCategoryRepository;

    public MedicineGroupsApiResources(MedicineCategoryRepository medicineCategoryRepository) {
        this.medicineCategoryRepository = medicineCategoryRepository;
    }

    @ApiOperation(
            value = "RETRIEVE list of available Medicine categories ",
            notes = "RETRIEVE list of available Medicine categories",
            response = MedicineCategory.class)
    @RequestMapping(
            value = "/",
            method = RequestMethod.GET,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)

    @ResponseBody
    ResponseEntity<List<MedicineCategory>> retrieveAllMedicineCategories() {
        List<MedicineCategory> medicineCategoriesResponse = medicineCategoryRepository.findAll();
        return ResponseEntity.ok().body(medicineCategoriesResponse);
    }

    @ApiOperation(
            value = "RETRIEVE Medicine category by ID",
            notes = "RETRIEVE  Medicine category by ID",
            response = MedicineCategory.class)
    @RequestMapping(
            value = "/{medicineCategoryId}",
            method = RequestMethod.GET,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)

    @ResponseBody
    ResponseEntity retrieveMedicineCategoryById(@PathVariable Long medicationCategoryId) {
        if (medicineCategoryRepository.findById(medicationCategoryId).isPresent()) {
            return ResponseEntity.ok().body(medicineCategoryRepository.findById(medicationCategoryId).get());
        }
        return ResponseEntity.ok().body(String.format("Medicine Category with ID %2d not found", medicationCategoryId));
    }


    @ApiOperation(
            value = "ADD new Medicine category",
            notes = "ADD new Medicine category",
            response = MedicineCategory.class)
    @RequestMapping(
            value = "/",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    @ResponseBody
    ResponseEntity<String> addNewMedicineGroup(@Valid @RequestBody MedicineCategory medicineCategory) {
        if (medicineCategoryRepository.existsByName(medicineCategory.getName())) {
            return ResponseEntity.ok().body(String.format("Another Medicine Category with same name '%s' already exist", medicineCategory.getName()));
        }
        medicineCategoryRepository.save(medicineCategory);
        return ResponseEntity.ok().body("Medicine category added successfully");

    }

    @ApiOperation(
            value = "ADD new Medicine categories",
            notes = "ADD new Medicine categories",
            response = MedicineCategory.class)
    @RequestMapping(
            value = "/list",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    @ResponseBody
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
