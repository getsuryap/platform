package org.ospic.platform.organization.authentication.selfservice.api;

import io.swagger.annotations.Api;
import org.ospic.platform.organization.authentication.users.domain.User;
import org.ospic.platform.organization.authentication.users.exceptions.UserNotFoundPlatformException;
import org.ospic.platform.organization.authentication.users.repository.UserJpaRepository;
import org.ospic.platform.organization.authentication.users.services.UsersReadPrincipleService;
import org.ospic.platform.patient.details.service.PatientInformationReadServices;
import org.ospic.platform.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

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
@ApiIgnore
@RequestMapping("/api/self")
@Api(value = "/api/self", tags = "Self service user data's")
public class SelfServiceApiResources {
    @Autowired UsersReadPrincipleService usersReadPrincipleService;
    @Autowired PatientInformationReadServices patientInformationReadServices;
    @Autowired UserJpaRepository userJpaRepository;



    @GetMapping("/users")
    public ResponseEntity<?> getUser() throws Exception {
         UserDetailsImpl ud = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return this.usersReadPrincipleService.retrieveUserById(ud.getId());
    }
    @GetMapping("/patients")
    public ResponseEntity<?> getPatient() throws Exception {
        UserDetailsImpl ud = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User u = this.userJpaRepository.findById(ud.getId()).orElseThrow(()-> new UserNotFoundPlatformException(ud.getId()));
        return this.patientInformationReadServices.retrievePatientById(u.getPatient().getId());
    }
}
