package org.ospic.platform.organization.medicalservices.api;

import io.swagger.annotations.Api;
import org.ospic.platform.organization.medicalservices.services.MedicalServiceReadPrincipleService;
import org.ospic.platform.organization.medicalservices.services.MedicalServiceWritePrincipleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

/**
 * This file was created by eli on 02/02/2021 for org.ospic.platform.organization.medicalservices.api
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
@RequestMapping("/api/services")
@Api(value = "/api/services", tags = "List of other medical service's provided by this hospital institution")
public class MedicalServiceApiResource {
    @Autowired
    MedicalServiceReadPrincipleService readService;
    @Autowired
    MedicalServiceWritePrincipleService writeService;

    public MedicalServiceApiResource(MedicalServiceReadPrincipleService readService,
            MedicalServiceWritePrincipleService writeService) {
        this.readService = readService;
        this.writeService = writeService;
    }

}
