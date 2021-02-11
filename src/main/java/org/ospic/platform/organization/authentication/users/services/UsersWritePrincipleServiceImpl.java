package org.ospic.platform.organization.authentication.users.services;

import io.jsonwebtoken.impl.DefaultClaims;
import org.ospic.platform.domain.CustomReponseMessage;
import org.ospic.platform.organization.authentication.roles.domain.Role;
import org.ospic.platform.organization.authentication.roles.repository.RoleRepository;
import org.ospic.platform.organization.authentication.users.data.RefreshTokenResponse;
import org.ospic.platform.organization.authentication.users.domain.User;
import org.ospic.platform.organization.authentication.users.exceptions.UserAuthenticationExceptionPlatform;
import org.ospic.platform.organization.authentication.users.payload.request.SignupRequest;
import org.ospic.platform.organization.authentication.users.payload.request.UserRequestData;
import org.ospic.platform.organization.authentication.users.payload.response.MessageResponse;
import org.ospic.platform.organization.authentication.users.repository.UserJpaRepository;
import org.ospic.platform.organization.staffs.service.StaffsWritePrinciplesService;
import org.ospic.platform.security.jwt.JwtUtils;
import org.ospic.platform.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
public class UsersWritePrincipleServiceImpl implements UsersWritePrincipleService{
    @Autowired
    UserJpaRepository userJpaRepository;
    @Autowired
    StaffsWritePrinciplesService staffsWritePrinciplesService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;


    @Override
    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
        if (userJpaRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userJpaRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }


        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()), signUpRequest.getIsStaff());

        Set<Long> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();



        if (strRoles == null) {
            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                Optional<Role> optionalRole = roleRepository.findById(role);
                if (optionalRole.isPresent()) {
                    Role userRole = optionalRole.get();
                    roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        User _user = userJpaRepository.save(user);
        if (_user.getIsStaff()) {
            staffsWritePrinciplesService.createNewStaff(_user.getId(), signUpRequest.getDepartmentId());
        }

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @Override
    public ResponseEntity<?> updateUserPassword(UserRequestData payload) {
        CustomReponseMessage cm = new CustomReponseMessage();
        HttpHeaders httpHeaders = new HttpHeaders();
        UserDetailsImpl ud = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userJpaRepository.findById(ud.getId()).map(user -> {
            String userPassword = user.getPassword();
            if (!(encoder.matches(payload.getOldPassword(), userPassword))) {
                cm.setMessage("Invalid old Password");
                cm.setHttpStatus(HttpStatus.FORBIDDEN.value());
                return new ResponseEntity<CustomReponseMessage>(cm, httpHeaders, HttpStatus.BAD_REQUEST);
            }
            user.setPassword(encoder.encode(payload.getNewPassword()));
            userJpaRepository.save(user);
            cm.setMessage("Password Updated Successfully ...");
            cm.setHttpStatus(HttpStatus.OK.value());
            return new ResponseEntity<CustomReponseMessage>(cm, httpHeaders, HttpStatus.OK);


        }).orElseThrow(() -> new UserAuthenticationExceptionPlatform(ud.getId()));
    }


    @Override
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        // From the HttpRequest get the claims
        DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");

        Map<String, Object> expectedMap = getMapFromIoJsonWebTokenClaims(claims);
        String token = jwtUtils.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
        return ResponseEntity.ok(new RefreshTokenResponse(token));
    }

    private Map<String, Object> getMapFromIoJsonWebTokenClaims(DefaultClaims claims) {
        Map<String, Object> expectedMap = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            expectedMap.put(entry.getKey(), entry.getValue());
        }
        return expectedMap;
    }

    @Override
    public ResponseEntity<String> logoutSession(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
            logoutHandler.setInvalidateHttpSession(true);
            logoutHandler.logout(httpServletRequest, httpServletResponse, authentication);
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return ResponseEntity.ok().body("Logged out !!!");
    }
}
