package org.ospic.platform.patient.details.api;

import org.ospic.platform.fileuploads.message.ResponseMessage;
import org.ospic.platform.fileuploads.service.FilesStorageService;
import org.ospic.platform.infrastructure.app.exception.AbstractPlatformInactiveResourceException;
import org.ospic.platform.organization.authentication.users.payload.response.MessageResponse;
import org.ospic.platform.organization.staffs.domains.Staff;
import org.ospic.platform.patient.details.data.PatientData;
import org.ospic.platform.patient.details.domain.Patient;
import org.ospic.platform.patient.details.repository.PatientRepository;
import org.ospic.platform.patient.details.service.PatientInformationReadServices;
import org.ospic.platform.patient.details.service.PatientInformationWriteService;
import io.swagger.annotations.*;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.List;

/**
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
@RequestMapping("/api/patients")
@Api(value = "/api/patients", tags = "Patients", description = "Patient  API resources")
public class PatientApiResources {

    private final PatientInformationReadServices patientInformationReadServices;
    private final PatientInformationWriteService patientInformationWriteService;
    private final FilesStorageService filesStorageService;
    private final PatientRepository patientRepository;

    @Autowired
    public PatientApiResources(PatientInformationReadServices patientInformationReadServices,
                               PatientInformationWriteService patientInformationWriteService,
                               FilesStorageService filesStorageService, PatientRepository patientRepository) {
        this.patientInformationReadServices = patientInformationReadServices;
        this.patientInformationWriteService = patientInformationWriteService;
        this.filesStorageService = filesStorageService;
        this.patientRepository = patientRepository;
    }


    @ApiOperation(value = "GET List all un-assigned patients", notes = "Get list of all un-assigned patients", response = Patient.class, responseContainer = "List")
    @RequestMapping(value = "/unassigned", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getAllUnassignedPatients() {
        return patientInformationReadServices.retrieveAllUnAssignedPatients();
    }

    @ApiOperation(value = "GET patient statistical data", notes = "Get Patient statistical data")
    @RequestMapping(value = "/statistics", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getPatientStatisticalData() {
        return patientInformationReadServices.retrieveStatisticalData();
    }

    @ApiOperation(value = "RETRIEVE list all assigned patients", notes = "RETRIEVE list of all assigned patients", response = Patient.class, responseContainer = "List")
    @RequestMapping(value = "/assigned", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getAllAssignedPatients() {
        return patientInformationReadServices.retrieveAllAssignedPatients();
    }

    @ApiOperation(value = "RETRIEVE pageable list of assigned patients", notes = "RETRIEVE pageable list of  assigned patients", response = Patient.class, responseContainer = "List")
    @RequestMapping(value = "/page", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getAllPatientPageable(@RequestParam(required = false) String group, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {
        Pageable paging = PageRequest.of(page, size);
        return patientInformationReadServices.retrievePageablePatients(group, paging);
    }

    @ApiOperation(value = "RETRIEVE Patient creation Template for creating new Patient", notes = "RETRIEVE Patient creation Template for creating new Patient", response = PatientData.class, responseContainer = "List")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    ResponseEntity<?> retrievePatientCreationTemplate(@RequestParam(value = "command", required = false) String command) {
        if (!(command == null || command.isEmpty())) {
            if (command.equals("template")) {
                return patientInformationReadServices.retrievePatientCreationDataTemplate();
            }
        }
        return patientInformationReadServices.retrieveAllPatients();
    }


    @ApiOperation(value = "GET specific Patient information by patient ID", notes = "GET specific Patient information by patient ID", response = Patient.class)
    @RequestMapping(value = "/{patientId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> findById(@ApiParam(name = "patientId", required = true) @PathVariable Long patientId) throws NotFoundException, AbstractPlatformInactiveResourceException.ResourceNotFoundException {
        return patientInformationReadServices.retrievePatientById(patientId);
    }

    @ApiOperation(value = "GET patient admitted in this bedId", notes = "GET patient admitted in this bedId", response = Patient.class)
    @RequestMapping(value = "/{bedId}/admitted", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> findPatientAdmittedInBedId(@PathVariable Long bedId) {
        return patientInformationReadServices.retrievePatientAdmittedInThisBed(bedId);
    }


    @ApiOperation(value = "UPDATE specific Patient information", notes = "UPDATE specific Patient information", response = Patient.class)
    @RequestMapping(value = "/{patientId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updatePatient(@ApiParam(name = "patient ID", required = true) @PathVariable Long patientId, @ApiParam(name = "Patient Entity", required = true) @RequestBody Patient patient) {
        return patientInformationWriteService.updatePatient(patientId, patient);
    }

    @ApiOperation(value = "ASSIGN patient to Staff", notes = "ASSIGN Patient to Staff", response = Staff.class)
    @RequestMapping(value = "/{patientId}/{physicianId}", method = RequestMethod.PUT, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> assignPatientToPhysician(
            @ApiParam(name = "Patient ID", required = true) @PathVariable Long patientId,
            @ApiParam(name = "Staff ID", required = true) @PathVariable Long physicianId) throws AbstractPlatformInactiveResourceException.ResourceNotFoundException {
        return patientInformationWriteService.assignPatientToPhysician(patientId, physicianId);
    }


    @ApiOperation(value = "CREATE new patient", notes = "CREATE new Patient", response = Patient.class)
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Patient createNewPatient(@ApiParam(name = "Patient Entity", required = true) @Valid @RequestBody Patient patientInformationRequest) {
        return patientInformationWriteService.createNewPatient(patientInformationRequest);
    }


    @ApiOperation(value = "CREATE patients by posting list of patients", notes = "CREATE patients by posting list of patients", response = Patient.class, responseContainer = "List")
    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    List<Patient> createNewPatients(
            @ApiParam(name = "List of Patient Entity", required = true)
            @Valid @RequestBody List<Patient> patientInformationListRequest) {
        return patientInformationWriteService.createByPatientListIterate(patientInformationListRequest);
    }

    @ApiOperation(value = "UPDATE Patient upload Thumbnail image", notes = "UPDATE Patient upload Thumbnail image", response = Patient.class)
    @RequestMapping(value = "/{patientId}/images", method = RequestMethod.PATCH, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadPatientImage(@RequestParam("file") MultipartFile file, @PathVariable(name = "patientId") Long patientId) {
        String message = "";
        try {
            return patientInformationWriteService.uploadPatientImage(patientId, file);
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }


    @ApiOperation(value = "DELETE Patient", notes = "DELETE Patient", response = MessageResponse.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> deletePatient(@ApiParam(name = "Patient ID", required = true) @PathVariable Long id) {
        return patientInformationWriteService.deletePatientById(id);
    }


    @GetMapping("/{patientId}/images/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename, @PathVariable Long patientId) {
        Resource file = filesStorageService.loadImage(patientId, filename,"images");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @RequestMapping(value = "/{patientId}/documents/{filename:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getDocument(@PathVariable String filename, @PathVariable Long patientId) {
        Resource file = filesStorageService.loadDocument(patientId, filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @RequestMapping(value = "/{patientId}/images/{filename:.+}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePatientImageFile(@PathVariable String filename, @PathVariable Long patientId) {
        //filesStorageService.deletePatientFileOrDocument("images",patientId, filename);
        return patientInformationWriteService.deletePatientImage(patientId, filename);
    }

}
