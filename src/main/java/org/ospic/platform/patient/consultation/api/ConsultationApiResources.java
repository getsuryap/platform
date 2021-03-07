package org.ospic.platform.patient.consultation.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.fileuploads.message.ResponseMessage;
import org.ospic.platform.fileuploads.service.FilesStorageService;
import org.ospic.platform.patient.consultation.service.ConsultationResourceReadPrinciplesService;
import org.ospic.platform.patient.consultation.service.ConsultationResourceWritePrinciplesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * This file was created by eli on 23/12/2020 for org.ospic.platform.patient.consultation.api
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
@RequestMapping("/api/consultations")
@Api(value = "/api/consultations", tags = "Patients service instances", description = "Patient  services instances")
public class ConsultationApiResources {
    ConsultationResourceReadPrinciplesService serviceRead;
    ConsultationResourceWritePrinciplesService serviceWrite;
    private final FilesStorageService filesystem;

    @Autowired
    public ConsultationApiResources(
            ConsultationResourceReadPrinciplesService serviceRead, ConsultationResourceWritePrinciplesService serviceWrite,
            FilesStorageService filesystem) {
        this.serviceRead = serviceRead;
        this.serviceWrite = serviceWrite;
        this.filesystem = filesystem;
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','READ_CONSULTATION')")
    @ApiOperation(value = "RETRIEVE all consultations", notes = "RETRIEVE all patient consultations")
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> retrieveServices(@RequestParam(value = "active", required = false) String command) {
        if (!(command == null || command.isEmpty())) {
            if (command.equals("true")) {
                return serviceRead.retrialAllActiveConsultations();
            }
            if (command.equals("false")) {
                return serviceRead.retrieveAllInactiveConsultations();
            }
            if(command.equals("activeipd")){
                return serviceRead.retrieveAllActiveConsultationsInIpd();
            }
            if (command.equals("activeopd")){
                return serviceRead.retrialAllAllActiveConsultationInOpd();
            }
        }
        return serviceRead.retrieveAllConsultations();
    }


    @ApiOperation(value = "RETRIEVE patient service by patient ID", notes = "RETRIEVE  patient service by patient ID")
    @RequestMapping(value = "/patient/{patientId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','READ_CONSULTATION')")
    ResponseEntity<?> retrieveConsultationByPatientId(@PathVariable Long patientId,@RequestParam(value = "active", required = false) String command) {
        if (!(command == null || command.isEmpty())) {
            if (command.equals("true")) {
                return serviceRead.retrieveConsultationByPatientIdAndIsActiveTrue(patientId);
            }
            if (command.equals("false")) {
                return serviceRead.retrieveConsultationByPatientIdAndIsActiveFalse(patientId);
            }
        }
        return serviceRead.retrieveConsultationByPatientId(patientId);
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','READ_CONSULTATION')")
    @ApiOperation(value = "RETRIEVE staff assigned services by staff Id", notes = "RETRIEVE staff assigned service by staff Id")
    @RequestMapping(value = "/staff/{staffId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> retrieveConsultationByStaffId(@PathVariable Long staffId,@RequestParam(value = "active", required = false) String command) {
        if (!(command == null || command.isEmpty())) {
            if (command.equals("true")) {
                return serviceRead.retrieveConsultationByStaffIdAndIsActiveTrue(staffId);
            }
            if (command.equals("false")) {
                return serviceRead.retrieveConsultationByStaffIdAndIsActiveFalse(staffId);
            }
        }
        return serviceRead.retrieveConsultationByStaffIdAll(staffId);
    }

    @PreAuthorize("hasAnyAuthority('CREATE_CONSULTATION')")
    @ApiOperation(value = "CREATE new consultation service", notes = "CREATE new consultation service")
    @RequestMapping(value = "/{patientId}", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createNewPatientConsultation(@PathVariable Long patientId) {
        return serviceWrite.createNewConsultation(patientId);
    }

    @PreAuthorize("hasAnyAuthority('READ_CONSULTATION')")
    @ApiOperation(value = "RETRIEVE patient service by ID", notes = "RETRIEVE  patient service by ID")
    @RequestMapping(value = "/{serviceId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> retrieveConsultationById(@PathVariable Long serviceId) {
        return serviceRead.retrieveAConsultationById(serviceId);
    }

    @PreAuthorize("hasAnyAuthority('UPDATE_CONSULTATION')")
    @ApiOperation(value = "ASSIGN service to staff", notes = "ASSIGN service to staff")
    @RequestMapping(value = "/{serviceId}/{staffId}", method = RequestMethod.PUT, consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    ResponseEntity<?> createNewPatientConsultation(@PathVariable Long serviceId, @PathVariable Long staffId) {
        return serviceWrite.assignConsultationToStaff(serviceId, staffId);
    }

    @PreAuthorize("hasAnyAuthority('UPDATE_CONSULTATION')")
    @ApiOperation(value = "END patient service by ID", notes = "END  patient service by ID")
    @RequestMapping(value = "/{serviceId}", method = RequestMethod.PUT, produces = MediaType.ALL_VALUE)
    ResponseEntity<?> endConsultationById(@PathVariable Long serviceId) {
        return serviceWrite.endConsultationById(serviceId);
    }

    @PreAuthorize("hasAnyAuthority('UPDATE_CONSULTATION')")
    @ApiOperation(value = "UPLOAD consultation report file", notes = "UPLOAD consultation report file")
    @RequestMapping(value = "/{consultationId}/images", method = RequestMethod.PATCH, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadPatientImage(@RequestParam("file") MultipartFile file, @PathVariable(name = "consultationId") Long consultationId) {
        String message = "";
        try {
            String imageFile = filesystem.uploadPatientImage(consultationId, file, "consultations",String.valueOf(consultationId),"laboratory");
            return ResponseEntity.ok().body(imageFile.trim());
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }
}
