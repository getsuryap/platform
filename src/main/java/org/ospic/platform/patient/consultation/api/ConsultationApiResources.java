package org.ospic.platform.patient.consultation.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.domain.CustomReponseMessage;
import org.ospic.platform.fileuploads.message.ResponseMessage;
import org.ospic.platform.fileuploads.service.FilesStorageService;
import org.ospic.platform.patient.consultation.domain.ConsultationResource;
import org.ospic.platform.patient.consultation.service.ConsultationResourceReadPrinciplesService;
import org.ospic.platform.patient.consultation.service.ConsultationResourceWritePrinciplesService;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(value = "/api/consultations", tags = "Consultations", description = "Patient consultation instances")
public class ConsultationApiResources {
    private final ConsultationResourceReadPrinciplesService consultationRead;
    private final ConsultationResourceWritePrinciplesService consultationWrite;
    private final FilesStorageService filesystem;

    @Autowired
    public ConsultationApiResources(
            ConsultationResourceReadPrinciplesService consultationRead, ConsultationResourceWritePrinciplesService consultationWrite,
            FilesStorageService filesystem) {
        this.consultationRead = consultationRead;
        this.consultationWrite = consultationWrite;
        this.filesystem = filesystem;
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','READ_CONSULTATION')")
    @ApiOperation(value = "RETRIEVE all consultations", notes = "RETRIEVE all patient consultations", response = ConsultationResource.class, responseContainer = "List")
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> retrieveServices(@RequestParam(value = "active", required = false) String command) {
        if (!(command == null || command.isEmpty())) {
            if (command.equals("true")) {
                return consultationRead.retrialAllActiveConsultations();
            }
            if (command.equals("false")) {
                return consultationRead.retrieveAllInactiveConsultations();
            }
            if(command.equals("activeipd")){
                return consultationRead.retrieveAllActiveConsultationsInIpd();
            }
            if (command.equals("activeopd")){
                return consultationRead.retrialAllAllActiveConsultationInOpd();
            }
        }
        return consultationRead.retrieveAllConsultations();
    }


    @ApiOperation(value = "RETRIEVE patient consultation by patient ID", notes = "RETRIEVE  patient consultation by patient ID", response = ConsultationResource.class, responseContainer = "List")
    @RequestMapping(value = "/patient/{patientId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','READ_CONSULTATION')")
    ResponseEntity<?> retrieveConsultationByPatientId(@PathVariable Long patientId,@RequestParam(value = "active", required = false) String command) {
        if (!(command == null || command.isEmpty())) {
            if (command.equals("true")) {
                return consultationRead.retrieveConsultationByPatientIdAndIsActiveTrue(patientId);
            }
            if (command.equals("false")) {
                return consultationRead.retrieveConsultationByPatientIdAndIsActiveFalse(patientId);
            }
        }
        return consultationRead.retrieveConsultationsByPatientId(patientId);
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','READ_CONSULTATION')")
    @ApiOperation(value = "RETRIEVE staff assigned consultations by staff Id", notes = "RETRIEVE staff assigned consultation by staff Id", response = ConsultationResource.class, responseContainer = "List")
    @RequestMapping(value = "/staff/{staffId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> retrieveConsultationByStaffId(@PathVariable Long staffId,@RequestParam(value = "active", required = false) String command) {
        if (!(command == null || command.isEmpty())) {
            if (command.equals("true")) {
                return consultationRead.retrieveConsultationByStaffIdAndIsActiveTrue(staffId);
            }
            if (command.equals("false")) {
                return consultationRead.retrieveConsultationByStaffIdAndIsActiveFalse(staffId);
            }
        }
        return consultationRead.retrieveConsultationByStaffIdAll(staffId);
    }

    @PreAuthorize("hasAnyAuthority('CREATE_CONSULTATION')")
    @ApiOperation(value = "CREATE new consultation consultation", notes = "CREATE new consultation consultation")
    @RequestMapping(value = "/{patientId}", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createNewPatientConsultation(@PathVariable Long patientId) {
        return consultationWrite.createNewConsultation(patientId);
    }

    @PreAuthorize("hasAnyAuthority('READ_CONSULTATION')")
    @ApiOperation(value = "RETRIEVE patient consultation by ID", notes = "RETRIEVE  patient consultation by ID", response = ConsultationResource.class)
    @RequestMapping(value = "/{consultationId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> retrieveConsultationById(@PathVariable Long consultationId) {
        return consultationRead.retrieveAConsultationById(consultationId);
    }

    @PreAuthorize("hasAnyAuthority('UPDATE_CONSULTATION')")
    @ApiOperation(value = "ASSIGN consultation to staff", notes = "ASSIGN consultation to staff", response = CustomReponseMessage.class)
    @RequestMapping(value = "/{consultationId}/{staffId}", method = RequestMethod.PUT, consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    ResponseEntity<?> createNewPatientConsultation(@PathVariable Long consultationId, @PathVariable Long staffId) {
        return consultationWrite.assignConsultationToStaff(consultationId, staffId);
    }

    @PreAuthorize("hasAnyAuthority('UPDATE_CONSULTATION')")
    @ApiOperation(value = "END patient consultation by ID", notes = "END  patient consultation by ID", response = CustomReponseMessage.class)
    @RequestMapping(value = "/{consultationId}", method = RequestMethod.PUT, produces = MediaType.ALL_VALUE)
    ResponseEntity<?> endConsultationById(@PathVariable Long consultationId) {
        return consultationWrite.endConsultationById(consultationId);
    }

    /**@PreAuthorize("hasAnyAuthority('UPDATE_CONSULTATION')")
    @ApiOperation(value = "UPLOAD consultation report file", notes = "UPLOAD consultation report file", response = ResponseMessage.class)
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
    **/

    @PreAuthorize("hasAnyAuthority('UPDATE_CONSULTATION')")
    @ApiOperation(value = "UPLOAD consultation report file", notes = "UPLOAD consultation report file", response = ResponseMessage.class)
    @RequestMapping(value = "/{consultationId}/laboratory", method = RequestMethod.PATCH, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadConsultationLaboratoryService(@RequestParam("file") MultipartFile file, @PathVariable(name = "consultationId") Long consultationId) {
       return this.consultationWrite.uploadConsultationLaboratoryReport(consultationId, file);
    }
}
