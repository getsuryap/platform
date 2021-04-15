package org.ospic.platform.inventory.wards.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.infrastructure.app.exception.AbstractPlatformInactiveResourceException;
import org.ospic.platform.inventory.beds.domains.Bed;
import org.ospic.platform.inventory.beds.repository.BedRepository;
import org.ospic.platform.inventory.wards.data.WardResponseData;
import org.ospic.platform.inventory.wards.domain.Ward;
import org.ospic.platform.inventory.wards.repository.WardRepository;
import org.ospic.platform.inventory.wards.service.WardReadPrincipleService;
import org.ospic.platform.inventory.wards.service.WardWritePrincipleService;
import org.ospic.platform.organization.authentication.users.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * This file was created by eli on 06/11/2020 for org.ospic.platform.inventory.wards.api
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
@RequestMapping("/api/wards")
@Api(value = "/api/wards", tags = "Wards", description = "Wards API resources")
public class WardApiResources {
    private final WardReadPrincipleService wardReadPrincipleService;
    private final WardWritePrincipleService wardWritePrincipleService;
    private final WardRepository wardRepository;
    private final BedRepository bedRepository;

    @Autowired
    public WardApiResources(final WardReadPrincipleService wardReadPrincipleService, final WardWritePrincipleService wardWritePrincipleService,
                            final WardRepository wardRepository, BedRepository bedRepository) {
        this.wardReadPrincipleService = wardReadPrincipleService;
        this.wardWritePrincipleService = wardWritePrincipleService;
        this.wardRepository = wardRepository;
        this.bedRepository = bedRepository;
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','UPDATE_INVENTORY')")
    @ApiOperation(value = "RETRIEVE Wards", notes = "RETRIEVE Wards",response = Ward.class, responseContainer = "List")
    @RequestMapping(value = "/", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Ward>> retrieveAllWards() {
        return ResponseEntity.ok(this.wardReadPrincipleService.retrieveListOfWards());
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','UPDATE_INVENTORY')")
    @ApiOperation(value = "RETRIEVE Wards with beds count", notes = "RETRIEVE Wards with beds count",response = WardResponseData.class, responseContainer = "List")
    @RequestMapping(value = "/beds", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<WardResponseData>> retrieveAllWardsWithBedsCounts() {
        return ResponseEntity.ok(this.wardReadPrincipleService.retrieveAllWardsWithBedsCounts());
    }


    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','UPDATE_INVENTORY')")
    @ApiOperation(value = "RETRIEVE ward by ID", notes = "RETRIEVE ward by ID",response = Ward.class)
    @RequestMapping(value = "/{wardId}", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> retrieveWardById(@PathVariable(value = "wardId", required = true) Long wardId) {
        return wardReadPrincipleService.findById(wardId);
    }


    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','UPDATE_INVENTORY')")
    @ApiOperation(value = "CREATE new Ward", notes = "CREATE new Ward",response = Ward.class)
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.ALL_VALUE)
    ResponseEntity<?> createNewWard(@Valid @RequestBody Ward ward) {
        return wardWritePrincipleService.createNewWard(ward);
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','UPDATE_INVENTORY')")
    @ApiOperation(value = "UPDATE Ward", notes = "UPDATE Ward",response = Ward.class)
    @RequestMapping(value = "/{wardId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createNewWard(@PathVariable(value = "wardId", required = true) Long wardId, @Valid @RequestBody Ward ward) {
        return wardWritePrincipleService.updateWard(wardId, ward);
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','UPDATE_INVENTORY')")
    @ApiOperation(value = "DELETE Ward", notes = "DELETE Ward",response = MessageResponse.class)
    @RequestMapping(value = "/{wardId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> deleteWard(@PathVariable(value = "wardId", required = true) Long wardId) {
        return wardWritePrincipleService.deleteWard(wardId);
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','UPDATE_INVENTORY')")
    @ApiOperation(value = "CREATE Wards by list array", notes = "CREATE Wards by array",response = Ward.class, responseContainer = "List")
    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> createWardsByList(@Valid @RequestBody List<Ward> wards) {
        wards.forEach(ward -> {
            if (!wardRepository.existsByName(ward.getName())) {
                wardWritePrincipleService.createNewWard(ward);
            }
        });
        return ResponseEntity.ok().body("All wards created successfully if it was not present");
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','UPDATE_INVENTORY')")
    @ApiOperation(value = "ADD new bed in Ward", notes = "ADD new bed in Ward")
    @RequestMapping(value = "/{wardId}/bed", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.ALL_VALUE)
    ResponseEntity<?> addNewBedInWard(@PathVariable(value = "wardId", required = true) Long wardId, @RequestBody @Valid Bed bed) throws AbstractPlatformInactiveResourceException.ResourceNotFoundException {
        return wardWritePrincipleService.addBedInWard(wardId, bed);
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','UPDATE_INVENTORY')")
    @ApiOperation(value = "ADD new bed in Ward", notes = "ADD new bed in Ward")
    @RequestMapping(value = "/{wardId}/beds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.ALL_VALUE)
    ResponseEntity<?> addNewListOfBedsInWard(@PathVariable(value = "wardId", required = true) Long wardId, @RequestBody @Valid List<Bed> beds) throws AbstractPlatformInactiveResourceException.ResourceNotFoundException {
        return wardWritePrincipleService.addListOfBedsInWard(wardId, beds);
    }


}
