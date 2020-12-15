package org.ospic.organization.staffs.service;

import org.ospic.organization.staffs.domains.Staff;
import org.ospic.organization.staffs.repository.StaffsRepository;
import org.ospic.security.authentication.users.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * This file was created by eli on 15/12/2020 for org.ospic.physicians.service
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
public class StaffWritePrinciplesServiceImpl implements StaffsWritePrinciplesService{
    StaffsRepository staffsRepository;

    @Autowired
    public StaffWritePrinciplesServiceImpl(StaffsRepository staffsRepository){
        this.staffsRepository = staffsRepository;
    }
    @Override
    public ResponseEntity<?> createNewStaff(Staff staff) {
        if (staffsRepository.existsByUsername(staff.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username "+ staff.getUsername() + " is already taken!"));
        }
        return ResponseEntity.ok( staffsRepository.save(staff));
    }

    @Override
    public ResponseEntity<?> createByStaffListIterate(List<Staff> staffs) {
        for (Staff staff : staffs){
            if (staffsRepository.existsByUsername(staff.getUsername())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Username "+ staff.getUsername() + " is already taken!"));
            }
        }
        return ResponseEntity.ok(staffsRepository.saveAll(staffs));
    }

    @Override
    public ResponseEntity<?> updateStaff(Long id, Staff staff) {
        return staffsRepository.findById(id)
                .map(physician -> {
                    physician.setContacts(staff.getContacts() == null ? physician.getContacts() : staff.getContacts() );
                    physician.setFirstname(staff.getFirstname() ==null? physician.getFirstname() : staff.getFirstname());
                    physician.setLastname(staff.getLastname() == null ?  physician.getLastname() : staff.getLastname());
                    physician.setLevel(staff.getLevel() == null? physician.getLevel() : staff.getLevel());
                    physician.setUsername(staff.getUsername() == null? physician.getUsername():staff.getUsername());
                    physician.setSpecialities(staff.getSpecialities() == null ? physician.getSpecialities() : staff.getSpecialities());

                    return ResponseEntity.ok(staffsRepository.save(physician));
                })
                .orElseThrow(() -> new EntityNotFoundException());
    }
}
