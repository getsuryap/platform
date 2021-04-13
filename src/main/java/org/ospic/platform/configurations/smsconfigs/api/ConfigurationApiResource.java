package org.ospic.platform.configurations.smsconfigs.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.configurations.smsconfigs.config.domain.SmsConfig;
import org.ospic.platform.configurations.smsconfigs.config.service.SmsConfigurationReadService;
import org.ospic.platform.configurations.smsconfigs.config.service.SmsConfigurationWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * This file was created by eli on 02/01/2021 for org.ospic.platform.configurations.smsconfigs.api
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
@RestController
@RequestMapping("/api/configurations")
@Api(value = "/api/configurations", tags = "Configurations")
public class ConfigurationApiResource {
    private final SmsConfigurationReadService readService;
    private final SmsConfigurationWriteService writeService;

    @Autowired
    public ConfigurationApiResource(SmsConfigurationReadService readService, SmsConfigurationWriteService writeService){
        this.readService = readService;
        this.writeService = writeService;
    }

    @ApiOperation(value = "RETRIEVE all sms configurations", notes = "RETRIEVE all sms configurations", response = SmsConfig.class, responseContainer = "List")
    @RequestMapping(value = "/sms", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    
    ResponseEntity<?> retrieveSmsConfigurations() {
        return readService.retrieveSmsConfigurations();
    }

    @ApiOperation(value = "RETRIEVE active sms configurations", notes = "RETRIEVE active sms configurations", response = SmsConfig.class, responseContainer = "List")
    @RequestMapping(value = "/sms/active", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    
    ResponseEntity<?> retrieveActiveSmsConfigurations() {
        return readService.retrieveActiveSmsConfiguration();
    }

    @ApiOperation(value = "CREATE sms configurations", notes = "CREATE sms configurations", response = SmsConfig.class)
    @RequestMapping(value = "/sms", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    
    ResponseEntity<?> createSmsConfiguration(@Valid @RequestBody  SmsConfig config) {
        return writeService.createSmsConfiguration(config);
    }

    @ApiOperation(value = "ACTIVATE sms configurations", notes = "ACTIVATE sms configurations", response = SmsConfig.class)
    @RequestMapping(value = "/sms/{Id}/activate", method = RequestMethod.PUT, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    
    ResponseEntity<?> retrieveSmsConfigurations(@PathVariable Long Id) {
        return writeService.activateSmsConfiguration(Id);
    }

    @ApiOperation(value = "ACTIVATE sms configurations", notes = "ACTIVATE sms configurations", response = SmsConfig.class)
    @RequestMapping(value = "/sms/{Id}", method = RequestMethod.PUT, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    
    ResponseEntity<?> updateSmsConfiguration(@PathVariable Long Id, @Valid @RequestBody  SmsConfig config) {
        return writeService.updateSmsConfiguration(Id, config);
    }
}
