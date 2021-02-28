package org.ospic.platform.laboratory.radiology.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.laboratory.radiology.domain.RadiologyService;
import org.ospic.platform.laboratory.radiology.services.RadiologyServiceReadPrincipleService;
import org.ospic.platform.laboratory.radiology.services.RadiologyServiceWritePrincipleService;
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
@RequestMapping("/api/radiology")
@Api(value = "/api/radiology", tags = "Radiology services", description = "Radiology services")
public class RadiologyTestsApiResource {
    private final RadiologyServiceReadPrincipleService readPrincipleService;
    private final RadiologyServiceWritePrincipleService writePrincipleService;

    @Autowired
    public RadiologyTestsApiResource(RadiologyServiceReadPrincipleService readPrincipleService,
            RadiologyServiceWritePrincipleService writePrincipleService) {
        this.readPrincipleService = readPrincipleService;
        this.writePrincipleService = writePrincipleService;
    }


    @ApiOperation(value = "CREATE radiology service", notes = "CREATE radiology service")
    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createRadiologyServices(@RequestBody(required = true) RadiologyService payload) {
        return this.writePrincipleService.createRadiologyService(payload);
    }

    @ApiOperation(value = "UPDATE laboratory service", notes = "UPDATE laboratory service")
    @RequestMapping(value = "/{serviceId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateRadiologyServices(@PathVariable(name = "serviceId", required = true) Long serviceId, @RequestBody(required = true) RadiologyService payload) {
        return this.writePrincipleService.updateRadiologyService(serviceId, payload);
    }

    @ApiOperation(value = "DELETE laboratory service", notes = "DELETE laboratory service")
    @RequestMapping(value = "/{serviceId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> deleteRadiologyServices(@PathVariable(name = "serviceId", required = true) Long serviceId) {
        return this.writePrincipleService.deleteRadiologyService(serviceId);
    }

    @ApiOperation(value = "DE/ACTIVATE laboratory service", notes = "DE/ACTIVATE laboratory service")
    @RequestMapping(value = "/{serviceId}/status", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> activateRadiologyServices(@PathVariable(name = "serviceId", required = true) Long serviceId, @RequestParam(name = "active", required = true, defaultValue = "true") boolean active) {
        if (active) {
            return this.writePrincipleService.activateRadiologyService(serviceId);
        } else return this.writePrincipleService.deactivateRadiologyService(serviceId);
    }

    @ApiOperation(value = "GET laboratory service", notes = "GET laboratory service")
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> readListOfRadiologyServices() {
        return this.readPrincipleService.listRadiologyServices();
    }

    @ApiOperation(value = "GET laboratory service by ID", notes = "GET laboratory service by ID")
    @RequestMapping(value = "/{serviceId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> readRadiologyServiceById(@PathVariable(name = "serviceId", required = true) Long serviceId) {
        return this.readPrincipleService.findRadiologyServiceById(serviceId);
    }


    @ApiOperation(value = "READ laboratory service by active status", notes = "READ laboratory service by active status")
    @RequestMapping(value = "/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> findRadiologyServicesByStatus(@RequestParam(name = "active", required = true, defaultValue = "true") boolean active) {
        return this.readPrincipleService.findRadiologyServiceByActiveStatus(active);
    }
}
