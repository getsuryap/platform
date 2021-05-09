package org.ospic.platform.organization.authentication.users.services;

import org.ospic.platform.organization.authentication.selfservice.data.SelfServicePayload;
import org.ospic.platform.organization.authentication.users.payload.request.SignupRequest;
import org.ospic.platform.organization.authentication.users.payload.request.PasswordUpdatePayload;
import org.ospic.platform.organization.authentication.users.payload.request.UpdateUserPayload;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This file was created by eli on 11/02/2021 for org.ospic.platform.organization.authentication.users.services
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
@Service
@Component
public interface UsersWritePrincipleService {
    ResponseEntity<?> registerUser(SignupRequest payload);
    ResponseEntity<?> registerSelfServiceUser(SelfServicePayload payload);
    ResponseEntity<?> updateUserPassword(PasswordUpdatePayload payload);
    ResponseEntity<?> refreshToken(HttpServletRequest request);
    ResponseEntity<?> updateUserDetails(Long id, UpdateUserPayload payload);
    ResponseEntity<?> deleteSelfServiceUser(Long id);
    ResponseEntity<?> updateProfileImage(Long userId,  MultipartFile file);
     ResponseEntity<String> logoutSession(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

}
