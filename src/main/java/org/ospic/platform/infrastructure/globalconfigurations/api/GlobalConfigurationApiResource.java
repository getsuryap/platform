package org.ospic.platform.infrastructure.globalconfigurations.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.infrastructure.globalconfigurations.data.Config;
import org.ospic.platform.infrastructure.globalconfigurations.domain.GlobalConfigurations;
import org.ospic.platform.infrastructure.globalconfigurations.service.GlobalConfigurationService;
import org.ospic.platform.patient.consultation.domain.ConsultationResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * This file was created by eli on 18/06/2021 for org.ospic.platform.infrastructure.globalconfigurations.api
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
@RequestMapping("/api/configurations")
@Api(value = "/api/configurations", tags = "Configurations", description = "Application global configurations")
public class GlobalConfigurationApiResource {
    private final GlobalConfigurationService configurationService;
    @Autowired
    public GlobalConfigurationApiResource(final GlobalConfigurationService configurationService){
        this.configurationService = configurationService;
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','READ_CONFIGURATION')")
    @ApiOperation(value = "RETRIEVE all configurations", notes = "RETRIEVE all application global configurations", response = GlobalConfigurations.class, responseContainer = "List")
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> retrieveGlobalConfigurations() {
        return ResponseEntity.ok().body(configurationService.getConfigurations());
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','READ_CONFIGURATION')")
    @ApiOperation(value = "RETRIEVE all configuration by Id", notes = "RETRIEVE all application global configuration by id", response = GlobalConfigurations.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> retrieveGlobalConfigurationsById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(configurationService.getConfigurationById(id));
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','UPDATE_CONFIGURATION')")
    @ApiOperation(value = "UPDATE configuration status", notes = "UPDATE global configuration status", response = ConsultationResource.class)
    @RequestMapping(value = "/{configurationId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateConfigurationStatus(@PathVariable("configurationId") Long configurationId, @RequestParam(value = "command", required = true) String command) {
        if (!(command == null || command.isEmpty())) {
            if (command.equals("activate")) {
                return ResponseEntity.ok().body(this.configurationService.updateConfigurationStatus(configurationId, true));
            }
            if (command.equals("deactivate")) {
                return ResponseEntity.ok().body(this.configurationService.updateConfigurationStatus(configurationId, false));
            }
        }
        return null;
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','UPDATE_CONFIGURATION')")
    @ApiOperation(value = "UPDATE configuration value", notes = "UPDATE application global configuration  value", response = GlobalConfigurations.class)
    @RequestMapping(value = "/", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> retrieveGlobalConfigurationsById( @Valid @RequestBody Config config) {
        return ResponseEntity.ok().body(configurationService.updateConfigurationValue(config));
    }


}
