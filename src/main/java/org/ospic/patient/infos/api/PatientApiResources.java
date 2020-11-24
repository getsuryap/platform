package org.ospic.patient.infos.api;

import org.ospic.fileuploads.message.ResponseMessage;
import org.ospic.fileuploads.service.FilesStorageService;
import org.ospic.patient.infos.data.PatientData;
import org.ospic.patient.infos.data.PatientTrendDatas;
import org.ospic.patient.infos.domain.Patient;
import org.ospic.patient.infos.service.PatientInformationReadServices;
import org.ospic.patient.infos.service.PatientInformationWriteService;
import org.ospic.util.exceptions.ResourceNotFoundException;
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

    PatientInformationReadServices patientInformationReadServices;
    PatientInformationWriteService patientInformationWriteService;
    FilesStorageService filesStorageService;

    @Autowired
    public PatientApiResources(PatientInformationReadServices patientInformationReadServices,
                        PatientInformationWriteService patientInformationWriteService,
                        FilesStorageService filesStorageService) {
        this.patientInformationReadServices = patientInformationReadServices;
        this.patientInformationWriteService = patientInformationWriteService;
        this.filesStorageService = filesStorageService;
    }


    @ApiOperation(value = "GET List all un-assigned patients", notes = "Get list of all un-assigned patients")
    @RequestMapping(value = "/unassigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody

    ResponseEntity<List<Patient>> getAllUnassignedPatients() {
        return patientInformationReadServices.retrieveAllUnAssignedPatients();
    }

    @ApiOperation(value = "RETRIEVE list all assigned patients", notes = "RETRIEVE list of all assigned patients")
    @RequestMapping(value = "/assigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody

    ResponseEntity<List<Patient>> getAllAssignedPatients() {
        return patientInformationReadServices.retrieveAllAssignedPatients();
    }

    @ApiOperation(
            value = "RETRIEVE Patient creation Template for creating new Patient",
            notes = "RETRIEVE Patient creation Template for creating new Patient"
    )
    @RequestMapping(
            value = "/",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)

    @ResponseBody
    ResponseEntity retrievePatientCreationTemplate(@RequestParam(value = "command", required = false) String command) {
        if (!(command == null || command.isEmpty())) {
            if (command.equals("template")) {
                return patientInformationReadServices.retrievePatientCreationDataTemplate();
            }
            if (command.equals("trends")){
                return patientInformationReadServices.retrieveAllPatientTrendData();
            }
        }
        return ResponseEntity.ok().body(patientInformationReadServices.retrieveAllPatients());
    }


    @ApiOperation(value = "GET specific Patient information by patient ID", notes = "GET specific Patient information by patient ID")
    @RequestMapping(value = "/{patientId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Patient> findById(@ApiParam(name = "patientId", required = true) @PathVariable Long patientId) throws NotFoundException, ResourceNotFoundException {
        return patientInformationReadServices.retrievePatientById(patientId);
    }

    @ApiOperation(value = "GET patient admitted in this bedId", notes = "GET patient admitted in this bedId")
    @RequestMapping(value = "/{bedId}/admitted", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<List<Patient>> findPatientAdmittedInBedId( @PathVariable Long bedId) {
        return patientInformationReadServices.retrievePatientAdmittedInThisBed(bedId);
    }


    @ApiOperation(
            value = "GET patients trend data by sex and date",
            notes = "GET patients trend data by sex and date")
    @RequestMapping(
            value = "/trends",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    @ResponseBody
    ResponseEntity<List<PatientTrendDatas>> getPatientTrendBySexAndDate() {
        return patientInformationReadServices.retrieveAllPatientTrendData();
    }


    @ApiOperation(
            value = "UPDATE specific Patient information",
            notes = "UPDATE specific Patient information")
    @RequestMapping(
            value = "/{patientId}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)

    @ResponseBody
    ResponseEntity updatePatient(
            @ApiParam(name = "patient ID", required = true) @PathVariable Long patientId,
            @ApiParam(name = "Patient Entity", required = true) @RequestBody Patient patient) {
        return patientInformationWriteService.updatePatient(patientId, patient);
    }

    @ApiOperation(
            value = "ASSIGN patient to Physician",
            notes = "ASSIGN Patient to Physician"
    )
    @RequestMapping(
            value = "/{patientId}/{physicianId}",
            method = RequestMethod.PUT,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity assignPatientToPhysician(
            @ApiParam(name = "Patient ID", required = true) @PathVariable Long patientId,
            @ApiParam(name = "Physician ID", required = true) @PathVariable Long physicianId) throws ResourceNotFoundException {
        return patientInformationWriteService.assignPatientToPhysician(patientId, physicianId);
    }


    @ApiOperation(value = "CREATE new patient", notes = "CREATE new Patient", response = Patient.class)
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)

    @ResponseBody
    Patient createNewPatient(@ApiParam(name = "Patient Entity", required = true) @Valid @RequestBody Patient patientInformationRequest) {
        return patientInformationWriteService.createNewPatient(patientInformationRequest);
    }


    @ApiOperation(value = "CREATE patients by posting list of patients", notes = "CREATE patients by posting list of patients")
    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)

    @ResponseBody
    List<Patient> createNewPatients(
            @ApiParam(name = "List of Patient Entity", required = true)
            @Valid @RequestBody List<Patient> patientInformationListRequest) {
        return patientInformationWriteService.createByPatientListIterate(patientInformationListRequest);
    }

    @ApiOperation(value = "UPDATE Patient upload Thumbnail image", notes = "UPDATE Patient upload Thumbnail image")
    @RequestMapping(value = "/{patientId}/images", method = RequestMethod.PATCH, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)

    @ResponseBody
    public ResponseEntity<ResponseMessage> uploadPatientImage(@RequestParam("file") MultipartFile file, @PathVariable Long patientId) {
        String message = "";
        try {
            ResponseEntity responseEntity = patientInformationWriteService.uploadPatientImage(patientId, file);
            return responseEntity;
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }


    @ApiOperation(value = "DELETE Patient", notes = "DELETE Patient")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity deletePatient(@ApiParam(name = "Patient ID", required = true) @PathVariable Long id) {
        return patientInformationWriteService.deletePatientById(id);
    }


    @GetMapping("/{patientId}/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename, @PathVariable Long patientId) {
        Resource file = filesStorageService.loadImage(patientId, filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @RequestMapping(value = "/{patientId}/documents/{filename:.+}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> getDocument(@PathVariable String filename, @PathVariable Long patientId) {
        Resource file = filesStorageService.loadDocument(patientId, filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @RequestMapping(value = "/{patientId}/images/{filename:.+}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<String> deletePatientImageFile(@PathVariable String filename, @PathVariable Long patientId) {
        //filesStorageService.deletePatientFileOrDocument("images",patientId, filename);
        return patientInformationWriteService.deletePatientImage(patientId, filename);
    }

}
