package org.ospic.platform.organization.departments.services;

import org.ospic.platform.organization.departments.domain.Department;
import org.ospic.platform.organization.departments.repository.DepartmentJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.Collection;
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
public class DepartmentReadServicePrincipleImpl implements DepartmentReadServicePrinciple{
    @Autowired
    DepartmentJpaRepository repository;
    @Autowired
    public DepartmentReadServicePrincipleImpl(DepartmentJpaRepository repository){
        this.repository = repository;
    }

    @Override
    public ResponseEntity<?> retrieveAllDepartments() {
        Collection<Department> departments = repository.findAll();
        return ResponseEntity.ok(departments);
    }

    @Override
    public ResponseEntity<?> retrieveDepartmentsById(Long departmentId) {
        Optional<Department> departmentOptional = repository.findById(departmentId);
        return ResponseEntity.ok().body(departmentOptional.isPresent() ? departmentOptional.get() :  "No department with such an ID");
    }
}
