package org.ospic.organization.staffs.service;

import org.ospic.organization.staffs.domains.Staff;
import org.ospic.organization.staffs.exceptions.StaffNotFoundException;
import org.ospic.organization.staffs.repository.StaffsRepository;
import org.ospic.security.authentication.users.domain.User;
import org.ospic.security.authentication.users.payload.response.MessageResponse;
import org.ospic.security.authentication.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * This file was created by eli on 15/12/2020 for org.ospic.stffs.service
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
public class StaffWritePrinciplesServiceImpl implements StaffsWritePrinciplesService {
    StaffsRepository staffsRepository;
    UserRepository userRepository;

    @Autowired
    public StaffWritePrinciplesServiceImpl(StaffsRepository staffsRepository, UserRepository userRepository) {
        this.staffsRepository = staffsRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<?> createNewStaff(Long id) {
        return userRepository.findById(id).map(user -> {
            Staff staff = new Staff(user.getUsername(), null, null, null, null, user.getEmail());
            user.setStaff(staff);
            staff.setUser(user);
            return ResponseEntity.ok().body(userRepository.save(user));
        }).orElseGet(() -> {
            return null;
        });
    }

    @Override
    public ResponseEntity<?> createByStaffListIterate(List<Staff> staffs) {
        for (Staff staff : staffs) {
            if (staffsRepository.existsByFullName(staff.getFullName())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Username " + staff.getFullName() + " is already taken!"));
            }
        }
        return ResponseEntity.ok(staffsRepository.saveAll(staffs));
    }

    @Override
    public ResponseEntity<?> updateStaff(Long id, Staff staff) {
        return staffsRepository.findById(id).map(stff -> {
                    stff.setContacts(staff.getContacts() == null ? stff.getContacts() : staff.getContacts());
                    stff.setFullName(staff.getFullName() == null ? stff.getFullName() : staff.getFullName());
                    stff.setLevel(staff.getLevel() == null ? stff.getLevel() : staff.getLevel());
                    return ResponseEntity.ok(staffsRepository.save(stff));
                })
                .orElseThrow(() -> new StaffNotFoundException(id));
    }
}
