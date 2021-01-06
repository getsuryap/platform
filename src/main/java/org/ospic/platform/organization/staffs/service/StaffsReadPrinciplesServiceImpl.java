package org.ospic.platform.organization.staffs.service;

import org.ospic.platform.organization.staffs.domains.Staff;
import org.ospic.platform.organization.staffs.repository.StaffsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

/**
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
public class StaffsReadPrinciplesServiceImpl implements StaffsReadPrinciplesService {
    StaffsRepository staffsRepository;

    @Autowired
    public StaffsReadPrinciplesServiceImpl(StaffsRepository staffsRepository) {
        this.staffsRepository = staffsRepository;
    }

    @Override
    public List<Staff> retrieveAllStaffs() {
        return staffsRepository.findAll();
    }
    @Override
    public Optional<Staff> retrieveStaffById(Long id) {
        return staffsRepository.findById(id);
    }
    @Override
    public ResponseEntity<?> getStaffById(Long id) {
        return ResponseEntity.ok(staffsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException()));
    }
}
