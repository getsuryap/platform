package org.ospic.platform.organization.authentication.users.services;

import org.ospic.platform.domain.CustomReponseMessage;
import org.ospic.platform.infrastructure.tenants.ThreadLocalStorage;
import org.ospic.platform.organization.authentication.users.domain.User;
import org.ospic.platform.organization.authentication.users.payload.request.LoginRequest;
import org.ospic.platform.organization.authentication.users.payload.response.JwtResponse;
import org.ospic.platform.organization.authentication.users.repository.UserJpaRepository;
import org.ospic.platform.security.jwt.JwtUtils;
import org.ospic.platform.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
@Repository
public class UsersReadPrincipleServiceImpl implements UsersReadPrincipleService {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserJpaRepository userJpaRepository;
    @Autowired
    JwtUtils jwtUtils;

    @Override
    public ResponseEntity<?> authenticateUser(LoginRequest payload) throws Exception {
        Authentication authentication = null;
        JwtResponse rs = new JwtResponse();
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getUsername(), payload.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        List<String> permissions = userDetails.getRoles().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        ThreadLocalStorage.setTenantName(payload.getTenantId());



        return ResponseEntity.ok(rs.loginResponse(jwt, userDetails.getId(), userDetails.getUsername(),
                userDetails.getEmail(),  permissions));
    }

    @Override
    public ResponseEntity<?> retrieveAllApplicationUsersResponse() {
        List<User> users = userJpaRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<?> retrieveAllSelfServiceUsersResponse() {
        return ResponseEntity.ok().body(this.userJpaRepository.findByIsSelfServiceTrue());
    }

    @Override
    public ResponseEntity<?> retrieveAllUsersWhoAreNotSelfService() {
        return ResponseEntity.ok().body(this.userJpaRepository.findByIsSelfServiceFalse());
    }

    @Override
    public ResponseEntity<?> retrieveLoggerInUser() {
        UserDetailsImpl ud = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optional = userJpaRepository.findById(ud.getId());
        return ResponseEntity.ok().body(optional.isPresent() ? optional.get() : new CustomReponseMessage(HttpStatus.NOT_FOUND.value(), String.format("User with ID %2d is not found")));

    }

    @Override
    public ResponseEntity<?> retrieveUserById(Long userId) {
        Optional<User> optional = userJpaRepository.findById(userId);
        return ResponseEntity.ok().body(optional.isPresent() ? optional.get() : new CustomReponseMessage(HttpStatus.NOT_FOUND.value(), String.format("User with ID %2d is not found", userId)));

    }

}
