package org.ospic.inventory.admission.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.inventory.admission.data.AdmissionRequest;
import org.ospic.inventory.admission.domains.Admission;
import org.ospic.inventory.admission.repository.AdmissionRepository;
import org.ospic.inventory.admission.service.AdmissionsReadService;
import org.ospic.inventory.admission.service.AdmissionsWriteService;
import org.ospic.inventory.wards.domain.Ward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * This file was created by eli on 09/11/2020 for org.ospic.inventory.admission.api
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
@Component
@RequestMapping("/api/admissions")
@Api(value = "/api/admissions", tags = "Admissions", description = "Admissions API resources")
public class AdmissionsApiResources {
    @Autowired
    AdmissionsWriteService admissionsWriteService;
    @Autowired
    AdmissionsReadService admissionsReadService;
    @Autowired
    AdmissionRepository admissionRepository;

    public AdmissionsApiResources(AdmissionsWriteService admissionsWriteService,
                                  AdmissionsReadService admissionsReadService,
                                  AdmissionRepository admissionRepository) {
        this.admissionsWriteService = admissionsWriteService;
        this.admissionsReadService = admissionsReadService;
        this.admissionRepository = admissionRepository;
    }

    @ApiOperation(value = "RETRIEVE Admissions", notes = "RETRIEVE Admissions")
    @RequestMapping(value = "/", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<List<Admission>> retrieveAllAdmissions() {
        return admissionsReadService.retrieveAllAdmissions();
    }

    @ApiOperation(value = "RETRIEVE Admission by ID", notes = "RETRIEVE Admission by ID")
    @RequestMapping(value = "/{admissionId}", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Admission> retrieveAdmissionByID(@NotNull @PathVariable("admissionId") Long admissionId) {
        Optional<Admission> admission = admissionRepository.findById(admissionId);
        return admission.map(value -> ResponseEntity.ok().body(value)).orElse(null);
    }



    @ApiOperation(value = "CREATE new  admission", notes = "CREATE new admission")
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.ALL_VALUE)
    @ResponseBody
    ResponseEntity<String> createNewWard(@Valid @RequestBody AdmissionRequest admissionRequest) {
        return admissionsWriteService.admitPatient(admissionRequest);
    }

}
