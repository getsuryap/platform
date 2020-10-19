package org.ospic.patient.diagnosis.api;

import io.swagger.annotations.*;
import org.ospic.patient.diagnosis.domains.Diagnosis;
import org.ospic.patient.diagnosis.service.DiagnosisService;
import org.ospic.patient.infos.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * This file was created by eli on 19/10/2020 for org.ospic.patient..api
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
@RequestMapping("/api/diagnosis")
@Api(value = "/api/diagnosis", tags = "Diagnoses", description = "Diagnoses reports API resources")
@Controller()
public class DiagnosisApiResources {

    @Autowired DiagnosisService diagnosisService;

    @Autowired
    public DiagnosisApiResources(DiagnosisService diagnosisService) {
        this.diagnosisService = diagnosisService;
    }

    @ApiOperation(
            value = "CREATE new diagnosis Report",
            notes = "CREATE new diagnosis Report")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Diagnosis[].class),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Entity not found")
    })
    @RequestMapping(
            value = "/{patientId}",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity createNewPatientDiagnosisReport( @PathVariable Long patientId, @RequestBody Diagnosis diagnosticReport) {

        return diagnosisService.saveDiagnosisReport(patientId, diagnosticReport);
    }

}
