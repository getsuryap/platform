package org.ospic.platform.inventory.admission.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.domain.CustomReponseMessage;
import org.ospic.platform.inventory.admission.data.AdmissionRequest;
import org.ospic.platform.inventory.admission.data.AdmissionResponseData;
import org.ospic.platform.inventory.admission.data.EndAdmissionRequest;
import org.ospic.platform.inventory.admission.domains.Admission;
import org.ospic.platform.inventory.admission.repository.AdmissionRepository;
import org.ospic.platform.inventory.admission.service.AdmissionsReadService;
import org.ospic.platform.inventory.admission.service.AdmissionsWriteService;
import org.ospic.platform.inventory.admission.visits.data.VisitPayload;
import org.ospic.platform.inventory.admission.visits.domain.AdmissionVisit;
import org.ospic.platform.inventory.admission.visits.service.VisitsReadPrincipleService;
import org.ospic.platform.inventory.admission.visits.service.VisitsWritePrincipleService;
import org.ospic.platform.inventory.admission.visits.service.VisitsWritePrincipleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * This file was created by eli on 09/11/2020 for org.ospic.platform.inventory.admission.api
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
    @Autowired
    VisitsWritePrincipleService visitsWritePrincipleService;
    @Autowired
    VisitsReadPrincipleService visitsReadPrincipleService;

    @Autowired
    public AdmissionsApiResources(AdmissionsWriteService admissionsWriteService,
                                  AdmissionsReadService admissionsReadService,
                                  AdmissionRepository admissionRepository,
                                  VisitsWritePrincipleServiceImpl visitsWritePrincipleService,
                                  VisitsReadPrincipleService visitsReadPrincipleService) {
        this.admissionsWriteService = admissionsWriteService;
        this.admissionsReadService = admissionsReadService;
        this.admissionRepository = admissionRepository;
        this.visitsReadPrincipleService = visitsReadPrincipleService;
        this.visitsWritePrincipleService = visitsWritePrincipleService;
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','READ_CONSULTATION')")
    @ApiOperation(value = "RETRIEVE Admissions", notes = "RETRIEVE Admissions", response = AdmissionResponseData.class, responseContainer = "List")
    @RequestMapping(value = "/", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> retrieveAllAdmissions() {
        return ResponseEntity.ok().body(admissionsReadService.retrieveAllAdmissions());
    }


    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','READ_CONSULTATION')")
    @ApiOperation(value = "RETRIEVE Admission by ID", notes = "RETRIEVE Admission by ID", response = AdmissionResponseData.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> retrieveAdmissionByID(@NotNull @PathVariable("id") Long id, @RequestParam(value = "command", required = false) String command) {
        if (null != command){
            if (command.equals("bed")){
                return admissionsReadService.retrieveListOfAdmissionInBedId(id);
            }
            if (command.equals("service")){
                return this.admissionsReadService.retrieveListOfServiceAdmission(id);
            }

        }
        return ResponseEntity.ok().body(admissionsReadService.retrieveAdmissionById(id));
    }




    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','CREATE_CONSULTATION','UPDATE_CONSULTATION')")
    @ApiOperation(value = "CREATE new  admission", notes = "CREATE new admission", response = Long.class)
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> requestPatientAdmission(@Valid @RequestBody AdmissionRequest admissionRequest) {
        return admissionsWriteService.admitPatient(admissionRequest);
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','CREATE_CONSULTATION','UPDATE_CONSULTATION')")
    @ApiOperation(value = "End patient admission", notes = "End patient admission admission", response = String.class)
    @RequestMapping(value = "/end", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> requestPatientUnAdmission( @Valid @RequestBody EndAdmissionRequest r) {
        return admissionsWriteService.endPatientAdmission(r);
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','READ_CONSULTATION')")
    @ApiOperation(value = "RETRIEVE Active admission in this bed", notes = "RETRIEVE active admission in this bed", response = AdmissionResponseData.class)
    @RequestMapping(value = "/inbed/{bedId}",method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> retrieveAdmissionInThisBed(@NotNull @PathVariable("bedId") Long bedId){
        return admissionsReadService.retrieveAdmissionInThisBed(bedId);
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','READ_CONSULTATION')")
    @ApiOperation(value = "RETRIEVE Admission visits", notes = "RETRIEVE Admission visits",response = AdmissionVisit.class, responseContainer = "List")
    @RequestMapping(value = "/{admissionId}/visits", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> retrieveAdmissionVisits( @PathVariable("admissionId") Long admissionId){
        return visitsReadPrincipleService.retrieveAdmissionVisits(admissionId);
    }


    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','CREATE_CONSULTATION','UPDATE_CONSULTATION')")
    @ApiOperation(value = "CREATE Admission visits", notes = "CREATE Admission visits", response = CustomReponseMessage.class)
    @RequestMapping(value = "/visits", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> visitAdmission(@Valid @RequestBody VisitPayload visitPayload){
        return visitsWritePrincipleService.createVisits(visitPayload);
    }
    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','READ_CONSULTATION','READ_SELF_SERVICE', 'UPDATE_SELF_SERVICE')")
    @GetMapping("/consultations/{consultationId}/admissions")
    @ApiOperation(value = "GET consultation admissions by consultation ID ", notes = "GET consultation admissions by consultation ID", response = Admission.class, responseContainer = "List")
    public ResponseEntity<?> readConsultationsAdmissionByConsultationId(@PathVariable(name = "consultationId") Long consultationId) throws Exception {
        return this.admissionsReadService.retrieveListOfServiceAdmission(consultationId);
    }
}
