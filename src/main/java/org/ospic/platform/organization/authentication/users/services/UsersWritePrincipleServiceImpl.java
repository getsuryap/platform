package org.ospic.platform.organization.authentication.users.services;

import io.jsonwebtoken.impl.DefaultClaims;
import org.ospic.platform.domain.CustomReponseMessage;
import org.ospic.platform.fileuploads.data.EntityType;
import org.ospic.platform.fileuploads.service.FilesStorageService;
import org.ospic.platform.organization.authentication.roles.domain.Role;
import org.ospic.platform.organization.authentication.roles.repository.RoleRepository;
import org.ospic.platform.organization.authentication.selfservice.data.SelfServicePayload;
import org.ospic.platform.organization.authentication.users.data.RefreshTokenResponse;
import org.ospic.platform.organization.authentication.users.domain.User;
import org.ospic.platform.organization.authentication.users.exceptions.DuplicateUsernameException;
import org.ospic.platform.organization.authentication.users.exceptions.InvalidOldPasswordException;
import org.ospic.platform.organization.authentication.users.exceptions.InvalidUserInformationException;
import org.ospic.platform.organization.authentication.users.exceptions.UserNotFoundPlatformException;
import org.ospic.platform.organization.authentication.users.payload.request.PasswordUpdatePayload;
import org.ospic.platform.organization.authentication.users.payload.request.SignupRequest;
import org.ospic.platform.organization.authentication.users.payload.request.UpdateUserPayload;
import org.ospic.platform.organization.authentication.users.payload.response.MessageResponse;
import org.ospic.platform.organization.authentication.users.repository.UserJpaRepository;
import org.ospic.platform.organization.departments.exceptions.DepartmentNotFoundExceptionsPlatform;
import org.ospic.platform.organization.departments.repository.DepartmentJpaRepository;
import org.ospic.platform.organization.staffs.domains.Staff;
import org.ospic.platform.organization.staffs.exceptions.StaffNotFoundExceptionPlatform;
import org.ospic.platform.organization.staffs.repository.StaffsRepository;
import org.ospic.platform.organization.staffs.service.StaffsWritePrinciplesService;
import org.ospic.platform.patient.details.domain.Patient;
import org.ospic.platform.patient.details.exceptions.PatientNotFoundExceptionPlatform;
import org.ospic.platform.patient.details.repository.PatientRepository;
import org.ospic.platform.security.jwt.JwtUtils;
import org.ospic.platform.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

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
public class UsersWritePrincipleServiceImpl implements UsersWritePrincipleService {
    private final UserJpaRepository userJpaRepository;
    private final StaffsWritePrinciplesService staffsWritePrinciplesService;
    private final RoleRepository roleRepository;
    private final DepartmentJpaRepository departmentRepository;
    private final StaffsRepository staffsRepository;
    private final FilesStorageService filesStorageService;
    @Autowired
    PatientRepository patientRepository;

    @Autowired
    PasswordEncoder encoder;

    public UsersWritePrincipleServiceImpl(
            UserJpaRepository userJpaRepository,
            StaffsWritePrinciplesService staffsWritePrinciplesService,
            RoleRepository roleRepository,
            DepartmentJpaRepository departmentRepository,
            StaffsRepository staffsRepository,FilesStorageService filesStorageService) {

        this.userJpaRepository = userJpaRepository;
        this.staffsWritePrinciplesService = staffsWritePrinciplesService;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.staffsRepository = staffsRepository;
        this.filesStorageService = filesStorageService;
    }

    @Autowired
    JwtUtils jwtUtils;


    @Override
    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
        if (userJpaRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new DuplicateUsernameException(signUpRequest.getUsername());
        }

        if (userJpaRepository.existsByEmail(signUpRequest.getEmail())) {
            String code = "error.msg.duplicate.email.address";
            String message = "Duplicate email address";
            throw new DuplicateUsernameException(code, message);
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getIsStaff());
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
    public ResponseEntity<?> registerSelfServiceUser(SelfServicePayload payload){
        User user = new User(payload.getUsername(), payload.getEmail(), encoder.encode(payload.getPassword()), false);
        Set<Role> roles = new HashSet<>();
        Optional<Role> optionalRole = roleRepository.findByName("SELF SERVICE");
        roles.add(optionalRole.isPresent() ? optionalRole.get(): null);
        Optional<Patient> patientOptional = patientRepository.findById(payload.getPatientId());

        if (userJpaRepository.existsByUsername(payload.getUsername())){
            throw new DuplicateUsernameException(payload.getUsername());
        }
        if (userJpaRepository.existsByEmail(payload.getEmail())) {
            String code = "error.msg.duplicate.email.address";
            String message = "Duplicate email address";
            throw new DuplicateUsernameException(code, message);
        }
        if (!patientOptional.isPresent()){
            throw new PatientNotFoundExceptionPlatform(payload.getPatientId());
        }
        Patient patient = patientOptional.get();
        if (patient.getHasSelfServiceUserAccount()) {
            String code = "error.msg.duplicate.self.service";
            String message = String.format("Self service account for patient %s already exist", patient.getName());
            throw new DuplicateUsernameException(code, message);
        }
        user.setIsSelfService(true);
        user.setRoles(roles);
        user.setPatient(patient);
        patient.setHasSelfServiceUserAccount(true);
        this.patientRepository.save(patient);
        return ResponseEntity.ok().body(this.userJpaRepository.save(user));
    }

    @Override
    public ResponseEntity<?> updateUserDetails(Long id, UpdateUserPayload payload) {
        return this.userJpaRepository.findById(id).map(user -> {
            return this.departmentRepository.findById(payload.getDepartmentId()).map(department -> {
                Staff staff = user.getStaff();
                if (payload.getIsStaff() && staff == null && payload.getDepartmentId() != null) {
                    //Create new staff
                    return staffsWritePrinciplesService.createNewStaff(user.getId(), payload.getDepartmentId());
                } else if (staff != null) {
                    //Move
                    if (!payload.getIsStaff()) {
                        staff.setIsActive(false);
                        staff.setIsAvailable(false);
                    }else {
                        staff.setIsActive(true);
                        staff.setIsAvailable(true);
                        user.setIsStaff(true);
                    }
                    staff.setDepartment(department);
                    Set<Long> strRoles = payload.getRoles();
                    Set<Role> roles = new HashSet<>();


                    if (strRoles == null) {
                        roles.addAll(user.getRoles());
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
                    this.staffsRepository.save(staff);
                    user.setIsStaff(payload.getIsStaff());

                    User u = userJpaRepository.save(user);
                    return ResponseEntity.ok().body(u);
                }
                return ResponseEntity.ok().body("Updated");
            }).orElseThrow(() -> new DepartmentNotFoundExceptionsPlatform(payload.getDepartmentId()));
        }).orElseThrow(() -> new UserNotFoundPlatformException(id));
    }

    @Override
    public ResponseEntity<?> updateUserPassword(PasswordUpdatePayload payload) {
        CustomReponseMessage cm = new CustomReponseMessage();
        UserDetailsImpl ud = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userJpaRepository.findById(ud.getId()).map(user -> {
            String userPassword = user.getPassword();
            if (!(encoder.matches(payload.getOldPassword(), userPassword))) {
              throw new InvalidOldPasswordException();
            }
            user.setPassword(encoder.encode(payload.getNewPassword()));
            userJpaRepository.save(user);
            cm.setMessage("Password Updated Successfully ...");
            cm.setHttpStatus(HttpStatus.OK.value());
            return  ResponseEntity.ok().body(cm);
        }).orElseThrow(() -> new UserNotFoundPlatformException(ud.getId()));
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
    public ResponseEntity<?> deleteSelfServiceUser(Long id) {
        return this.userJpaRepository.findById(id).map(user->{
            if (!user.getIsSelfService()){
                throw new InvalidUserInformationException("User is not self service");
            }
           Long patientId = user.getPatient().getId();
            return this.patientRepository.findById(patientId).map(patient ->{
                patient.setHasSelfServiceUserAccount(false);
                this.patientRepository.save(patient);
                
                this.userJpaRepository.deleteById(id);
                return ResponseEntity.ok().body("user deleted");
            }).orElseThrow(()->new PatientNotFoundExceptionPlatform(patientId));

        }).orElseThrow(()->new UserNotFoundPlatformException(id));
    }

    @Override
    public ResponseEntity<?> updateProfileImage(Long userId, MultipartFile file) {
        return this.userJpaRepository.findById(userId).map(user -> {
            String imagePath = filesStorageService.uploadPatientImage(userId, EntityType.ENTITY_USER,  file,"images");
           if(!user.getIsStaff() || user.getStaff()==null){
               throw new StaffNotFoundExceptionPlatform(userId);
           }
           user.getStaff().setImageUrl(imagePath);
            return ResponseEntity.ok().body(this.userJpaRepository.save(user));
        }).orElseThrow(() -> new UserNotFoundPlatformException(userId));
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
