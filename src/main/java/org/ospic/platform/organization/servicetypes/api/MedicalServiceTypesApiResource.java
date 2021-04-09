package org.ospic.platform.organization.servicetypes.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.organization.servicetypes.domain.MedicalServiceTypes;
import org.ospic.platform.organization.servicetypes.services.MedicalServiceTypesReadPrincipleService;
import org.ospic.platform.organization.servicetypes.services.MedicalServiceTypesWritePrincipleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/api/mdservice/types")
@Api(value = "/api/mdservice/types", tags = "Medical services types")
public class MedicalServiceTypesApiResource {
    @Autowired
    MedicalServiceTypesReadPrincipleService readService;
    @Autowired
    MedicalServiceTypesWritePrincipleService writeService;

    public MedicalServiceTypesApiResource() { }

    @ApiOperation(value = "LIST medical services",notes = "LIST medical services", response =  MedicalServiceTypes.class, responseContainer = "List")
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> retrieveMedicalService() {
        return  readService.readServicesTypes();
    }


    @ApiOperation(value = "RETURN medical services by ID",notes = "RETURN medical services by ID", response =  MedicalServiceTypes.class)
    @RequestMapping(value = "/{serviceId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> retrieveMedicalServiceById(@PathVariable(name = "serviceId") Long serviceId) {
        return  readService.readServiceTypesById(serviceId);
    }


    @ApiOperation(value = "RETURN medical services by name",notes = "RETURN medical services by name", response =  MedicalServiceTypes.class)
    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> retrieveMedicalServiceByName(@PathVariable(name = "name") String name) {
        return  readService.readServiceTypeByName(name);
    }

    @ApiOperation(value = "CREATE new medical service",notes = "CREATE new medical service", response =  MedicalServiceTypes.class)
    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> createMedicalService(@RequestBody MedicalServiceTypes payload) {
        return writeService.createServiceTypes(payload);
    }

    @ApiOperation(value = "UPDATE medical services by ID",notes = "UPDATE medical services by ID", response =  MedicalServiceTypes.class)
    @RequestMapping(value = "/{serviceId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> updateMedicalServiceById(@PathVariable(name = "serviceId") Long serviceId, @RequestBody MedicalServiceTypes payload) {
        return  writeService.updateServiceType(serviceId,payload);
    }


}
