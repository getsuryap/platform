package org.ospic.platform.organization.authentication.selfservice.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.organization.authentication.selfservice.exceptions.NotSelfServiceUserException;
import org.ospic.platform.organization.authentication.users.domain.User;
import org.ospic.platform.organization.authentication.users.exceptions.UserNotFoundPlatformException;
import org.ospic.platform.organization.authentication.users.payload.request.LoginRequest;
import org.ospic.platform.organization.authentication.users.repository.UserJpaRepository;
import org.ospic.platform.organization.authentication.users.services.UsersReadPrincipleService;
import org.ospic.platform.patient.consultation.service.ConsultationResourceReadPrinciplesService;
import org.ospic.platform.patient.details.service.PatientInformationReadServices;
import org.ospic.platform.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(value = "/api/self", tags = "Self service user data's")
public class SelfServiceApiResources {
    @Autowired UsersReadPrincipleService usersReadPrincipleService;
    @Autowired PatientInformationReadServices patientInformationReadServices;
    @Autowired UserJpaRepository userJpaRepository;
    @Autowired
    ConsultationResourceReadPrinciplesService consultationReadService;



    @PostMapping("/login")
    @ApiOperation(value = "AUTHENTICATE self service user ", notes = "AUTHENTICATE self service user")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        return this.usersReadPrincipleService.authenticateUser(loginRequest);
    }



    @PreAuthorize("hasAnyAuthority('READ_SELF_SERVICE', 'UPDATE_SELF_SERVICE')")
    @GetMapping("/users")
    @ApiOperation(value = "GET self service user ", notes = "GET self service user")
    public ResponseEntity<?> getUser() throws Exception {
         UserDetailsImpl ud = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.usersReadPrincipleService.retrieveUserById(ud.getId());
    }

    @PreAuthorize("hasAnyAuthority('READ_SELF_SERVICE', 'UPDATE_SELF_SERVICE')")
    @GetMapping("/patients")
    @ApiOperation(value = "GET self service user patient linked account ", notes = "GET self service user patient linked account")
    public ResponseEntity<?> getPatient() throws Exception {
        UserDetailsImpl ud = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User u = this.userJpaRepository.findById(ud.getId()).orElseThrow(()-> new UserNotFoundPlatformException(ud.getId()));
        if (!u.getIsSelfService()){
            throw new NotSelfServiceUserException(u.getUsername());
        }
        return this.patientInformationReadServices.retrievePatientById(u.getPatient().getId());
    }

    @GetMapping("/consultations")
    public ResponseEntity<?> readConsultations() throws Exception {
        UserDetailsImpl ud = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User u = this.userJpaRepository.findById(ud.getId()).orElseThrow(()-> new UserNotFoundPlatformException(ud.getId()));
        if (!u.getIsSelfService()){
            throw new NotSelfServiceUserException(u.getUsername());
        }
        return this.consultationReadService.retrieveConsultationByPatientId(u.getPatient().getId());
    }
}
