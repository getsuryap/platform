package org.ospic.platform.laboratory.tests.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.domain.CustomReponseMessage;
import org.ospic.platform.laboratory.tests.domain.LaboratoryService;
import org.ospic.platform.laboratory.tests.services.LaboratoryServiceReadPrincipleService;
import org.ospic.platform.laboratory.tests.services.LaboratoryServiceWritePrincipleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This file was created by eli on 15/02/2021 for org.ospic.platform.laboratory.tests.api
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
@RequestMapping("/api/lab/services")
@Api(value = "/api/lab/services", tags = "Laboratory", description = "Laboratory api services")
public class LaboratoryTestsApiResource {
    private final LaboratoryServiceReadPrincipleService readPrincipleService;
    private final LaboratoryServiceWritePrincipleService writePrincipleService;

    @Autowired
    public LaboratoryTestsApiResource(
            LaboratoryServiceReadPrincipleService readPrincipleService,
            LaboratoryServiceWritePrincipleService writePrincipleService) {
        this.readPrincipleService = readPrincipleService;
        this.writePrincipleService = writePrincipleService;
    }


    @ApiOperation(value = "CREATE laboratory service", notes = "CREATE laboratory service", response = LaboratoryService.class)
    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createLabServices(@RequestBody(required = true) LaboratoryService payload) {
        return this.writePrincipleService.createLaboratoryService(payload);
    }

    @ApiOperation(value = "UPDATE laboratory service", notes = "UPDATE laboratory service", response = LaboratoryService.class)
    @RequestMapping(value = "/{serviceId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateLabServices(@PathVariable(name = "serviceId", required = true) Long serviceId, @RequestBody(required = true) LaboratoryService payload) {
        return this.writePrincipleService.updateLaboratoryService(serviceId, payload);
    }

    @ApiOperation(value = "DELETE laboratory service", notes = "DELETE laboratory service", response = CustomReponseMessage.class)
    @RequestMapping(value = "/{serviceId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> deleteLabServices(@PathVariable(name = "serviceId", required = true) Long serviceId) {
        return this.writePrincipleService.deleteLaboratoryService(serviceId);
    }

    @ApiOperation(value = "DE/ACTIVATE laboratory service", notes = "DE/ACTIVATE laboratory service", response = LaboratoryService.class)
    @RequestMapping(value = "/{serviceId}/status", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> activateLabServices(@PathVariable(name = "serviceId", required = true) Long serviceId, @RequestParam(name = "active", required = true, defaultValue = "true") boolean active) {
        if (active) {
            return this.writePrincipleService.activateLaboratoryService(serviceId);
        } else return this.writePrincipleService.deactivateLaboratoryService(serviceId);
    }

    @ApiOperation(value = "GET laboratory services", notes = "GET laboratory services", response = LaboratoryService.class, responseContainer = "List")
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> readListOfLaboratoryServices() {
        return this.readPrincipleService.listLaboratoryServices();
    }

    @ApiOperation(value = "GET laboratory service by ID", notes = "GET laboratory service by ID", response = LaboratoryService.class)
    @RequestMapping(value = "/{serviceId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> readLabServiceById(@PathVariable(name = "serviceId", required = true) Long serviceId) {
        return this.readPrincipleService.findLaboratoryServiceById(serviceId);
    }


    @ApiOperation(value = "READ laboratory service by active status", notes = "READ laboratory service by active status", response = LaboratoryService.class, responseContainer = "List")
    @RequestMapping(value = "/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> findLabServicesByStatus(@RequestParam(name = "active", required = true, defaultValue = "true") boolean active) {
        return this.readPrincipleService.findLaboratoryServiceByActiveStatus(active);
    }
}
