package org.ospic.patient.api;

import org.ospic.fileuploads.message.ResponseMessage;
import org.ospic.patient.data.PatientData;
import org.ospic.patient.domain.Patient;
import org.ospic.patient.service.PatientInformationServices;
import org.ospic.util.exceptions.ResourceNotFoundException;
import io.swagger.annotations.*;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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

    PatientInformationServices patientInformationServices;

    @Autowired
    PatientApiResources(PatientInformationServices patientInformationServices) {
        this.patientInformationServices = patientInformationServices;
    }


    @ApiOperation(value = "GET List all patients", notes = "Get list of all patients")
    @RequestMapping(value = "/remove",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Patient[].class),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Entity not found")
    })
    List<Patient> all() {
        return patientInformationServices.retrieveAllPatients();
    }

    @ApiOperation(
            value = "RETRIEVE Patient creation Template for creating new Patient",
            notes = "RETRIEVE Patient creation Template for creating new Patient"
    )
    @RequestMapping(
            value = "/",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = PatientData.class),
            @ApiResponse(code = 500, message = "Internal Server error"),
            @ApiResponse(code = 404, message = "Error")
    })
    @ResponseBody
    ResponseEntity retrievePatientCreationTemplate(@RequestParam(value = "command", required = false) String command) {
        if (!(command == null || command.isEmpty())) {
            if (command.equals("template")) {
                return patientInformationServices.retrievePatientCreationDataTemplate();
            }
        }
        return ResponseEntity.ok().body(patientInformationServices.retrieveAllPatients());
    }


    @ApiOperation(
            value = "GET specific Patient information by patient ID",
            notes = "GET specific Patient information by patient ID")
    @RequestMapping(
            value = "/{patientId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Patient.class),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Entity not found")})
    @ResponseBody
    ResponseEntity findById(
            @ApiParam(name = "patientId", required = true)
            @PathVariable Long patientId) throws NotFoundException, ResourceNotFoundException {
        return patientInformationServices.retrievePatientById(patientId);
    }


    @ApiOperation(
            value = "UPDATE specific Patient information",
            notes = "UPDATE specific Patient information")
    @RequestMapping(
            value = "/{patientId}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 400, message = "Ba request", response = Patient.class),
            @ApiResponse(code = 404, message = "Entity not found")})
    @ResponseBody
    ResponseEntity updatePatient(
            @ApiParam(name = "patient ID", required = true) @PathVariable Long patientId,
            @ApiParam(name = "Patient Entity", required = true) @RequestBody Patient patient) {
        return patientInformationServices.updatePatient(patientId, patient);
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
        return patientInformationServices.assignPatientToPhysician(patientId, physicianId);
    }


    @ApiOperation(
            value = "CREATE new patient",
            notes = "CREATE new Patient",
            response = Patient.class)
    @RequestMapping(
            value = "/",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Created Successfully"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 400, message = "Ba request", response = Patient.class),
            @ApiResponse(code = 404, message = "Entity not found")})
    @ResponseBody
    Patient createNewPatient(@ApiParam(name = "Patient Entity", required = true) @Valid @RequestBody Patient patientInformationRequest) {
        return patientInformationServices.createNewPatient(patientInformationRequest);
    }


    @ApiOperation(
            value = "CREATE patients by posting list of patients",
            notes = "CREATE patients by posting list of patients")
    @RequestMapping(
            value = "/list",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Created Successfully"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Entity not found")})
    @ResponseBody
    List<Patient> createNewPatients(
            @ApiParam(name = "List of Patient Entity", required = true)
            @Valid @RequestBody List<Patient> patientInformationListRequest) {
        return patientInformationServices.createByPatientListIterate(patientInformationListRequest);
    }

    @ApiOperation(
            value = "UPDATE Patient upload Thumbnail image",
            notes = "UPDATE Patient upload Thumbnail image"
    )
    @RequestMapping(
            value = "/{patientId}/images",
            method = RequestMethod.PATCH,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Client image updated successfully"),
            @ApiResponse(code = 500, message = "Internal server Error"),
            @ApiResponse(code = 404, message = "Patient not found")})
    @ResponseBody
    public ResponseEntity<ResponseMessage> uploadPatientImage(@RequestParam("file") MultipartFile file, @PathVariable Long patientId) {
        String message = "";
        try {
            ResponseEntity responseEntity = patientInformationServices.uploadPatientImage(patientId, file);
            return responseEntity;
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }


    @ApiOperation(
            value = "DELETE Patient",
            notes = "DELETE Patient")
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity deletePatient(@ApiParam(name = "Patient ID", required = true) @PathVariable Long id) {
        return patientInformationServices.deletePatientById(id);
    }

}
