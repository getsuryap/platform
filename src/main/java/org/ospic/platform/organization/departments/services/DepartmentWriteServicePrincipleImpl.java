package org.ospic.platform.organization.departments.services;

import org.ospic.platform.organization.departments.data.DepartmentReqPayload;
import org.ospic.platform.organization.departments.domain.Department;
import org.ospic.platform.organization.departments.exceptions.DepartmentNotFoundExceptionsPlatform;
import org.ospic.platform.organization.departments.repository.DepartmentJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

/**
 * This file was created by eli on 09/01/2021 for org.ospic.platform.organization.departments.services
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
public class DepartmentWriteServicePrincipleImpl implements DepartmentWriteServicePrinciple {
   private final DepartmentJpaRepository repository;


    @Autowired
    public DepartmentWriteServicePrincipleImpl(DepartmentJpaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public ResponseEntity<?> createDepartment(DepartmentReqPayload payload) {
        Optional<Department> parent = repository.findById(payload.getParent());
        Department dp = Department.withoutParentDepartment(payload.getName(), LocalDate.now(), payload.getDescriptions(), payload.getExtraId());
        dp.setParent(parent.orElse(null));
        Department response  = repository.save(dp);
        return ResponseEntity.ok().body(response);
    }

    @Override
    public Department updateDepartment(Long departmentId, DepartmentReqPayload payload) {

        return this.repository.findById(departmentId).map(department -> {
            if (null != payload.getParent()) {
                Department parent= this.repository.findById(payload.getParent()).orElseThrow(() -> new DepartmentNotFoundExceptionsPlatform(departmentId));
                department.setParent(parent);
            }
            department.setName(payload.getName());
            department.setDescriptions(payload.getDescriptions());
            department.setHierarchy(payload.getHierarchy());
            department.setExtraId(payload.getExtraId());
            return this.repository.save(department);
        }).orElseThrow(()->new DepartmentNotFoundExceptionsPlatform(departmentId));
    }
}
