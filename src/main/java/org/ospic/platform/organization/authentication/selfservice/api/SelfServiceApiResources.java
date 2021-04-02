package org.ospic.platform.organization.authentication.selfservice.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.accounting.bills.data.BillPayload;
import org.ospic.platform.accounting.bills.service.BillReadPrincipleService;
import org.ospic.platform.accounting.transactions.data.TransactionRowMap;
import org.ospic.platform.accounting.transactions.service.TransactionReadPrincipleService;
import org.ospic.platform.infrastructure.reports.domain.Reports;
import org.ospic.platform.inventory.admission.domains.Admission;
import org.ospic.platform.inventory.admission.service.AdmissionsReadService;
import org.ospic.platform.inventory.admission.visits.domain.AdmissionVisit;
import org.ospic.platform.inventory.admission.visits.service.VisitsReadPrincipleService;
import org.ospic.platform.laboratory.reports.repository.FileInformationRepository;
import org.ospic.platform.organization.authentication.selfservice.exceptions.NotSelfServiceUserException;
import org.ospic.platform.organization.authentication.users.domain.User;
import org.ospic.platform.organization.authentication.users.exceptions.InsufficientRoleException;
import org.ospic.platform.organization.authentication.users.exceptions.UserNotFoundPlatformException;
import org.ospic.platform.organization.authentication.users.payload.request.LoginRequest;
import org.ospic.platform.organization.authentication.users.payload.response.JwtResponse;
import org.ospic.platform.organization.authentication.users.repository.UserJpaRepository;
import org.ospic.platform.organization.authentication.users.services.UsersReadPrincipleService;
import org.ospic.platform.patient.consultation.domain.ConsultationResource;
import org.ospic.platform.patient.consultation.repository.ConsultationResourceJpaRepository;
import org.ospic.platform.patient.consultation.service.ConsultationResourceReadPrinciplesService;
import org.ospic.platform.patient.details.domain.Patient;
import org.ospic.platform.patient.details.service.PatientInformationReadServices;
import org.ospic.platform.patient.diagnosis.service.DiagnosisService;
import org.ospic.platform.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * This file was created by eli on 07/03/2021 for org.ospic.platform.organization.authentication.selfservice.api
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
@RestController
@RequestMapping("/api/self")
@Api(value = "/api/self", tags = "SelfService", description = "Self service user data's")
public class SelfServiceApiResources {
    @Autowired UsersReadPrincipleService usersReadPrincipleService;
    @Autowired PatientInformationReadServices patientInformationReadServices;
    @Autowired UserJpaRepository userJpaRepository;
    @Autowired
    ConsultationResourceReadPrinciplesService consultationReadService;
    @Autowired
    BillReadPrincipleService billReadPrincipleService;
    @Autowired
    DiagnosisService diagnosisService;
    @Autowired TransactionReadPrincipleService transactionReadPrincipleService;
    @Autowired
    FileInformationRepository fileInformationRepository;
    @Autowired
    AdmissionsReadService admissionsReadService;
    @Autowired
    VisitsReadPrincipleService visitsReadPrincipleService;
    @Autowired
    ConsultationResourceJpaRepository consultationResourceJpaRepository;




    @PostMapping("/login")
    @ApiOperation(value = "AUTHENTICATE self service user ", notes = "AUTHENTICATE self service user", response = JwtResponse.class)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        return this.usersReadPrincipleService.authenticateUser(loginRequest);
    }



    @PreAuthorize("hasAnyAuthority('READ_SELF_SERVICE', 'UPDATE_SELF_SERVICE')")
    @GetMapping("/users")
    @ApiOperation(value = "GET self service user ", notes = "GET self service user", response = User.class)
    public ResponseEntity<?> getUser() throws Exception {
        return this.usersReadPrincipleService.retrieveUserById(this.validateForUserIsSelfServiceReturnUserId());
    }

    @PreAuthorize("hasAnyAuthority('READ_SELF_SERVICE', 'UPDATE_SELF_SERVICE')")
    @GetMapping("/bills")
    @ApiOperation(value = "GET list of bills", notes = "GET list of bills", response = BillPayload.class, responseContainer = "List")
    public ResponseEntity<?> getUserBills() throws Exception {
        return this.billReadPrincipleService.readBillsByPatientId(this.validateForUserIsSelfServiceReturnUserId());
    }

    @PreAuthorize("hasAnyAuthority('READ_SELF_SERVICE', 'UPDATE_SELF_SERVICE')")
    @GetMapping("/bills/{billId}")
    @ApiOperation(value = "GET bill by Id", notes = "GET bill by Id", response = BillPayload.class)
    public ResponseEntity<?> getUserBillsByBillId(@PathVariable("billId") Long billId) throws Exception {
        this.validateForUserIsSelfService();
        return this.billReadPrincipleService.readBillById(billId);
    }

    @PreAuthorize("hasAnyAuthority('READ_SELF_SERVICE', 'UPDATE_SELF_SERVICE')")
    @GetMapping("/patients")
    @ApiOperation(value = "GET self service user patient linked account ", notes = "GET self service user patient linked account", response = Patient.class)
    public ResponseEntity<?> getPatient() throws Exception {
        return this.patientInformationReadServices.retrievePatientById(this.validateForUserIsSelfServiceReturnUserId());
    }

    @PreAuthorize("hasAnyAuthority('READ_SELF_SERVICE', 'UPDATE_SELF_SERVICE')")
    @GetMapping("/consultations")
    @ApiOperation(value = "GET self-service consultations ", notes = "GET self-service consultations", response = ConsultationResource.class, responseContainer = "List")
    public ResponseEntity<?> readConsultations() throws Exception {
        return this.consultationReadService.retrieveConsultationsByPatientId(this.validateForUserIsSelfServiceReturnUserId());
    }

    @PreAuthorize("hasAnyAuthority('READ_SELF_SERVICE', 'UPDATE_SELF_SERVICE')")
    @GetMapping("/consultations/{consultationId}")
    @ApiOperation(value = "GET self-service consultations by ID ", notes = "GET self-service consultations by ID", response = ConsultationResource.class, responseContainer = "List")
    public ResponseEntity<?> readConsultationsById(@PathVariable(name = "consultationId") Long consultationId) throws Exception {
       this.validateForUserIsSelfServiceAndConsultationBelongsToHim(consultationId);
        return this.consultationReadService.retrieveAConsultationById(consultationId);
    }

    @PreAuthorize("hasAnyAuthority('READ_SELF_SERVICE', 'UPDATE_SELF_SERVICE')")
    @GetMapping("/consultations/{consultationId}/reports")
    @ApiOperation(value = "GET consultation report by consultation ID ", notes = "GET consultation report by consultation ID", response = Reports.class, responseContainer = "List")
    public ResponseEntity<?> readConsultationsReportsByConsultationId(@PathVariable(name = "consultationId") Long consultationId) throws Exception {
        this.validateForUserIsSelfServiceAndConsultationBelongsToHim(consultationId);
        return ResponseEntity.ok().body(this.fileInformationRepository.findByConsultationId(consultationId));
    }



    @PreAuthorize("hasAnyAuthority('READ_SELF_SERVICE', 'UPDATE_SELF_SERVICE')")
    @GetMapping("/diagnoses/{consultationId}")
    @ApiOperation(value = "GET self-service consultation diagnoses ", notes = "GET self-service consultations diagnoses", response = ConsultationResource.class, responseContainer = "List")
    public ResponseEntity<?> readConsultationDiagnoses(@PathVariable(name = "consultationId") Long consultationId) throws Exception {
       validateForUserIsSelfServiceAndConsultationBelongsToHim(consultationId);
        return this.diagnosisService.retrieveAllDiagnosisReportsByServiceId(consultationId);
    }

    @ApiOperation(value = "LIST consultation transactions", notes = "LIST consultation transactions", response = TransactionRowMap.class, responseContainer = "List")
    @RequestMapping(value = "/{consultationId}/consultation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> listConsultationTransactions(@PathVariable(name = "consultationId") Long consultationId, @RequestParam(value = "reversed", required = false) boolean reversed) {
        validateForUserIsSelfServiceAndConsultationBelongsToHim(consultationId);
        int isReversed = reversed ? 1 : 0;
        switch (isReversed) {
            case 1:
                return transactionReadPrincipleService.readTransactionsByConsultationIdAndReversed(consultationId);
            case 0:
                return transactionReadPrincipleService.readTransactionsByConsultationIdAndNotReversed(consultationId);
            default:
                return transactionReadPrincipleService.readTransactionsByConsultationId(consultationId);
        }
    }



    @PreAuthorize("hasAnyAuthority('READ_SELF_SERVICE', 'UPDATE_SELF_SERVICE')")
    @GetMapping("/consultations/{consultationId}/admissions")
    @ApiOperation(value = "GET consultation admissions by consultation ID ", notes = "GET consultation admissions by consultation ID", response = Admission.class, responseContainer = "List")
    public ResponseEntity<?> readConsultationsAdmissionByConsultationId(@PathVariable(name = "consultationId") Long consultationId) throws Exception {
       this.validateForUserIsSelfServiceAndConsultationBelongsToHim(consultationId);
        return this.admissionsReadService.retrieveListOfServiceAdmission(consultationId);
    }

    @PreAuthorize("hasAnyAuthority('READ_SELF_SERVICE', 'UPDATE_SELF_SERVICE')")
    @GetMapping("/consultations/admissions/{admissionId}")
    @ApiOperation(value = "GET consultation admission by ID ", notes = "GET consultation admission by  ID", response = Admission.class)
    public ResponseEntity<?> readConsultationsAdmissionByAdmissionsId(@PathVariable(name = "admissionId") Long admissionId) throws Exception {
        validateForUserIsSelfService();
        return this.admissionsReadService.retrieveAdmissionById(admissionId);
    }

    @PreAuthorize("hasAnyAuthority('READ_SELF_SERVICE', 'UPDATE_SELF_SERVICE')")
    @GetMapping("/consultations/admissions/{admissionId}/visits")
    @ApiOperation(value = "GET consultation admission visits by ID ", notes = "GET consultation admission visits by  ID", response = AdmissionVisit.class, responseContainer = "List")
    public ResponseEntity<?> readConsultationsAdmissionVisitsAdmissionsId(@PathVariable(name = "admissionId") Long admissionId) throws Exception {
        this.validateForUserIsSelfService();

        return this.visitsReadPrincipleService.retrieveAdmissionVisits(admissionId);
    }

    private void validateForUserIsSelfService(){
        UserDetailsImpl ud = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User u = this.userJpaRepository.findById(ud.getId()).orElseThrow(()-> new UserNotFoundPlatformException(ud.getId()));
        if (!u.getIsSelfService()){
            throw new NotSelfServiceUserException(u.getUsername());
        }
    }

    private void validateForUserIsSelfServiceAndConsultationBelongsToHim(Long consultantId){
        UserDetailsImpl ud = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User u = this.userJpaRepository.findById(ud.getId()).orElseThrow(()-> new UserNotFoundPlatformException(ud.getId()));
        if (!u.getIsSelfService()){
            throw new NotSelfServiceUserException(u.getUsername());
        }
        this.consultationResourceJpaRepository.findById(consultantId).map(consultation->{
            if (consultation.getPatient().getId() != u.getPatient().getId()){
                throw new InsufficientRoleException(2L,"Insufficient role to access this resource");
            }
            return null;
        });
    }

    private Long validateForUserIsSelfServiceReturnUserId(){
        UserDetailsImpl ud = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User u = this.userJpaRepository.findById(ud.getId()).orElseThrow(()-> new UserNotFoundPlatformException(ud.getId()));
        if (!u.getIsSelfService()){
            throw new NotSelfServiceUserException(u.getUsername());
        }
        return u.getId();
    }
}
