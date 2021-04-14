package org.ospic.platform.inventory.blood.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.inventory.blood.data.BloodPayload;
import org.ospic.platform.inventory.blood.domain.BloodGroup;
import org.ospic.platform.inventory.blood.service.BloodBankReadPrincipleService;
import org.ospic.platform.inventory.blood.service.BloodBankWritePrincipleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * This file was created by eli on 18/12/2020 for org.ospic.platform.inventory.blood.api
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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController()
@Component
@RequestMapping("/api/bloods")
@Api(value = "/api/bloods", tags = "Blood bank", description = "Blood bank API resources")
public class BloodBankApiResources {
    @Autowired BloodBankReadPrincipleService bloodBankReadPrincipleService;
    @Autowired BloodBankWritePrincipleService bloodBankWritePrincipleService;

    @Autowired
    public BloodBankApiResources(
            BloodBankReadPrincipleService bloodBankReadPrincipleService,
            BloodBankWritePrincipleService bloodBankWritePrincipleService) {
        this.bloodBankReadPrincipleService = bloodBankReadPrincipleService;
        this.bloodBankWritePrincipleService = bloodBankWritePrincipleService;
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','UPDATE_INVENTORY')")
    @ApiOperation(value = "RETRIEVE blood bank details", notes = "RETRIEVE blood bank details", response = BloodGroup.class, responseContainer = "List")
    @RequestMapping(value = "/", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> retrieveBloodBankData() {
        return bloodBankReadPrincipleService.fetchBloodBankList();
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','UPDATE_INVENTORY')")
    @ApiOperation(value = "UPDATE blood ground", notes = "UPDATE blood ground",response = BloodGroup.class)
    @RequestMapping(value = "/", method = RequestMethod.PATCH, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> addMoreBagsForThisGroup(@RequestBody BloodPayload payload) throws Exception{
        return bloodBankWritePrincipleService.addMoreBloodBagsForThisGroup(payload);
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','UPDATE_INVENTORY')")
    @ApiOperation(value = "UPDATE blood ground by list", notes = "UPDATE blood ground by list",response = BloodGroup.class)
    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> addMoreBagsForThisGroupList(@RequestBody List<BloodPayload> payloads) throws Exception{
        return bloodBankWritePrincipleService.addMoreBloodBagsForListOfBloodGroups(payloads);
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','UPDATE_INVENTORY')")
    @ApiOperation(value = "Create blood groups", notes = "Create blood groups")
    @RequestMapping(value = "/initiate", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    @ApiIgnore
    ResponseEntity<?> initiateData() {
         bloodBankWritePrincipleService.initiateData();
        return  ResponseEntity.ok().body(null);
    }

}
