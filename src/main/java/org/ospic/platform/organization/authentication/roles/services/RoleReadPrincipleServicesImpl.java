package org.ospic.platform.organization.authentication.roles.services;

import org.ospic.platform.organization.authentication.roles.exceptions.RoleNotFoundExceptionPlatform;
import org.ospic.platform.organization.authentication.roles.privileges.repository.PrivilegesRepository;
import org.ospic.platform.organization.authentication.roles.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

/**
 * This file was created by eli on 16/12/2020 for org.ospic.platform.organization.authentication.roles.services
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
public class RoleReadPrincipleServicesImpl implements RoleReadPrincipleServices {
    private final RoleRepository roleRepository;
    private final PrivilegesRepository privilegeRepository;

    @Autowired
    RoleReadPrincipleServicesImpl( final RoleRepository roleRepository,final PrivilegesRepository privilegeRepository) {
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    public ResponseEntity<?> retrieveAllRoles() {
        return ResponseEntity.ok().body(this.roleRepository.findAll());
    }

    @Override
    public ResponseEntity<?> fetchRoleById(Long roleId)  {
       return this.roleRepository.findById(roleId).map(role -> {
           return ResponseEntity.ok().body(role);
       }).orElseThrow(()-> new RoleNotFoundExceptionPlatform(roleId));
    }

    @Override
    public ResponseEntity<?> fetchAuthorities() {
        return ResponseEntity.ok().body(privilegeRepository.findAll());
    }
}
