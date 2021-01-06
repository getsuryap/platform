package org.ospic.platform.inventory.pharmacy.Categories.api;

/**
 * This file was created by eli on 12/11/2020 for org.ospic.platform.inventory.pharmacy.Groups.api
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
import org.ospic.platform.inventory.pharmacy.Groups.domains.MedicineGroup;
import org.ospic.platform.inventory.pharmacy.Groups.repository.MedicineGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/pharmacy/medicines/groups")
@Api(value = "/api/pharmacy/medicines/groups", tags = "Medicine Groups")
public class MedicineCategoriesApiResources {
    @Autowired
    MedicineGroupRepository medicineGroupRepository;

    public MedicineCategoriesApiResources(MedicineGroupRepository medicineGroupRepository) {
        this.medicineGroupRepository = medicineGroupRepository;
    }

    @ApiOperation(value = "RETRIEVE list of available Medicine groups available", notes = "RETRIEVE list of available Medicine groups available", response = MedicineGroup.class)
    @RequestMapping(value = "/", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)

    @ResponseBody
    ResponseEntity<List<MedicineGroup>> retrieveAllMedicineGroups() {
        List<MedicineGroup> medicineGroupsResponse = medicineGroupRepository.findAll();
        return ResponseEntity.ok().body(medicineGroupsResponse);
    }


    @ApiOperation(value = "ADD new Medicine group", notes = "ADD new Medicine group", response = MedicineGroup.class)
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<String> addNewMedicineGroup(@Valid @RequestBody MedicineGroup medicineGroup) {
        if (medicineGroupRepository.existsByName(medicineGroup.getName())) {
            return ResponseEntity.ok().body(String.format("Another Medicine Group with same name '%s' already exist", medicineGroup.getName()));
        }
        medicineGroupRepository.save(medicineGroup);
        return ResponseEntity.ok().body("Medicine group added successfully");
    }

    @ApiOperation(value = "RETRIEVE Medicine group by ID", notes = "RETRIEVE Medicine group by ID")
    @RequestMapping(value = "/{medicineGroupId}", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> retrieveMedicineGroupById(@PathVariable Long medicineGroupId) {
        Optional<MedicineGroup> medicineGroupsResponse = medicineGroupRepository.findById(medicineGroupId);
        return ResponseEntity.ok().body(medicineGroupsResponse.get());
    }



}