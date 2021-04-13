package org.ospic.platform.patient.diagnosis.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.domain.CustomReponseMessage;
import org.ospic.platform.patient.diagnosis.domains.Diagnosis;
import org.ospic.platform.patient.diagnosis.repository.DiagnosisRepository;
import org.ospic.platform.patient.diagnosis.service.DiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * This file was created by eli on 19/10/2020 for org.ospic.platform.patient..api
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
@RequestMapping("/api/diagnoses")
@Api(value = "/api/diagnoses", tags = "Diagnoses", description = "Diagnoses reports API resources")
@Validated
public class DiagnosisApiResources {

    private final DiagnosisService diagnosisService;
    private final DiagnosisRepository diagnosisRepository;

    @Autowired
    public DiagnosisApiResources(DiagnosisService diagnosisService, DiagnosisRepository diagnosisRepository) {
        this.diagnosisService = diagnosisService;
        this.diagnosisRepository = diagnosisRepository;
    }

    @ApiOperation(value = "LIST all available diagnosis", notes = "LIST all available diagnosis",response = Diagnosis.class, responseContainer = "List")
    @RequestMapping(value = "/", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Diagnosis>> retrieveAllDiagnosisReports() {
        return diagnosisService.retrieveAllDiagnosisReports();
    }


    @ApiOperation(value = "LIST Patient diagnosis", notes = "LIST Patient diagnosis",response = Diagnosis.class, responseContainer = "List")
    @RequestMapping(value = "/{serviceId}", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> retrieveAllDiagnosisReportsByServiceId(@PathVariable("serviceId") @NotNull Long serviceId) {
        return diagnosisService.retrieveAllDiagnosisReportsByServiceId(serviceId);
    }


    @ApiOperation(value = "CREATE new diagnosis Report", notes = "CREATE new diagnosis Report",response = CustomReponseMessage.class)
    @RequestMapping(value = "/{serviceId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewPatientDiagnosisReport( @PathVariable Long serviceId, @RequestBody Diagnosis diagnosticReport) {

        return diagnosisService.saveDiagnosisReport(serviceId, diagnosticReport);
    }




}
