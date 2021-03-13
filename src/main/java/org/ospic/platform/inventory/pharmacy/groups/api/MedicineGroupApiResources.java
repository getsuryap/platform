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
import org.ospic.platform.inventory.pharmacy.groups.domains.MedicineGroup;
import org.ospic.platform.inventory.pharmacy.groups.exception.DuplicateMedicineGroupNameException;
import org.ospic.platform.inventory.pharmacy.groups.exception.MedicineGroupNotFoundExceptionPlatform;
import org.ospic.platform.inventory.pharmacy.groups.repository.MedicineGroupRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/pharmacy/medicines/groups")
@Api(value = "/api/pharmacy/medicines/groups", tags = "Medicine Groups")
public class MedicineGroupApiResources {
    private final MedicineGroupRepository medicineGroupRepository;
    public MedicineGroupApiResources(MedicineGroupRepository medicineGroupRepository) {
        this.medicineGroupRepository = medicineGroupRepository;
    }

    @ApiOperation(value = "RETRIEVE list of available Medicine groups available", notes = "RETRIEVE list of available Medicine groups available", response = MedicineGroup.class, responseContainer = "List")
    @RequestMapping(value = "/", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> retrieveAllMedicineGroups() {
        List<MedicineGroup> medicineGroupsResponse = medicineGroupRepository.findAll();
        return ResponseEntity.ok().body(medicineGroupsResponse);
    }


    @ApiOperation(value = "ADD new Medicine group", notes = "ADD new Medicine group", response = MedicineGroup.class)
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> addNewMedicineGroup(@Valid @RequestBody MedicineGroup medicineGroup) {
        if (medicineGroupRepository.existsByName(medicineGroup.getName())) {
            throw new DuplicateMedicineGroupNameException(medicineGroup.getName());
       }
        return ResponseEntity.ok().body(medicineGroupRepository.save(medicineGroup));
    }

    @ApiOperation(value = "RETRIEVE Medicine group by ID", notes = "RETRIEVE Medicine group by ID", response = MedicineGroup.class)
    @RequestMapping(value = "/{medicineGroupId}", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> retrieveMedicineGroupById(@PathVariable Long medicineGroupId) {
        return medicineGroupRepository.findById(medicineGroupId).map(md ->{
            return ResponseEntity.ok().body(md);
        }).orElseThrow(()->new MedicineGroupNotFoundExceptionPlatform(medicineGroupId));

    }


    @ApiOperation(value = "UPDATE Medicine group", notes = "UPDATE Medicine group", response = MedicineGroup.class)
    @RequestMapping(value = "/{medicineGroupId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> updateMedicineGroupById(@PathVariable Long medicineGroupId, @Valid @RequestBody MedicineGroup request) {
        return medicineGroupRepository.findById(medicineGroupId).map(mg -> {
            mg.setName(request.getName());
            mg.setDescriptions(request.getDescriptions());
            return ResponseEntity.ok().body(medicineGroupRepository.save(mg));
        }).orElseThrow(()-> new MedicineGroupNotFoundExceptionPlatform(medicineGroupId));

    }



}
