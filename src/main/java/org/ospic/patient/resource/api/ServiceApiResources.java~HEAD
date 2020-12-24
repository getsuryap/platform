package org.ospic.patient.resource.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.ospic.patient.infos.domain.Patient;
import org.ospic.patient.resource.service.ServiceResourceReadPrinciplesService;
import org.ospic.patient.resource.service.ServiceResourceWritePrinciplesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * This file was created by eli on 23/12/2020 for org.ospic.patient.resource.api
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
@RequestMapping("/api/services")
@Api(value = "/api/services", tags = "Patients service instances", description = "Patient  services instances")
public class ServiceApiResources {
    ServiceResourceReadPrinciplesService serviceRead;
    ServiceResourceWritePrinciplesService serviceWrite;

    @Autowired
    public ServiceApiResources(
            ServiceResourceReadPrinciplesService serviceRead, ServiceResourceWritePrinciplesService serviceWrite) {
        this.serviceRead = serviceRead;
        this.serviceWrite = serviceWrite;
    }


    @ApiOperation(value = "RETRIEVE all patient service", notes = "RETRIEVE all patient service")
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> retrieveServices(@RequestParam(value = "active", required = false) String command) {
        if (!(command == null || command.isEmpty())) {
            if (command.equals("true")) {
                return serviceRead.retrialAllActiveServices();
            }
            if (command.equals("false")) {
                return serviceRead.retrieveAllInactiveServices();
            }
        }
        return ResponseEntity.ok().body(serviceRead.retrieveAllServices());
    }


    @ApiOperation(value = "RETRIEVE patient service by patient ID", notes = "RETRIEVE  patient service by patient ID")
    @RequestMapping(value = "/patient/{patientId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> retrieveServiceByPatientId(@PathVariable Long patientId,@RequestParam(value = "active", required = false) String command) {
        if (!(command == null || command.isEmpty())) {
            if (command.equals("true")) {
                return serviceRead.retrieveServiceByPatientIdAndIsActiveTrue(patientId);
            }
            if (command.equals("false")) {
                return serviceRead.retrieveServiceByPatientIdAndIsActiveFalse(patientId);
            }
        }
        return serviceRead.retrieveServiceByPatientId(patientId);
    }

    @ApiOperation(value = "CREATE new patient service", notes = "CREATE new patient service")
    @RequestMapping(value = "/{patientId}", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> createNewPatientService(@PathVariable Long patientId) {
        return serviceWrite.createNewService(patientId);
    }

    @ApiOperation(value = "RETRIEVE patient service by ID", notes = "RETRIEVE  patient service by ID")
    @RequestMapping(value = "/{serviceId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> retrieveServiceById(@PathVariable Long serviceId) {
        return serviceRead.retrieveAServiceById(serviceId);
    }

    @ApiOperation(value = "ASSIGN service to staff", notes = "ASSIGN service to staff")
    @RequestMapping(value = "/{serviceId}/{staffId}", method = RequestMethod.PUT, consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    @ResponseBody
    ResponseEntity<?> createNewPatientService(@PathVariable Long serviceId, @PathVariable Long staffId) {
        return serviceWrite.assignServiceToStaff(serviceId, staffId);
    }

    @ApiOperation(value = "END patient service by ID", notes = "END  patient service by ID")
    @RequestMapping(value = "/{serviceId}", method = RequestMethod.PUT, produces = MediaType.ALL_VALUE)
    @ResponseBody
    ResponseEntity<?> endServiceById(@PathVariable Long serviceId) {
        return serviceWrite.endServiceById(serviceId);
    }
}
