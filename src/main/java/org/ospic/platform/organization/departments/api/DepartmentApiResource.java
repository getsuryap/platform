package org.ospic.platform.organization.departments.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.organization.departments.data.DepartmentReqPayload;
import org.ospic.platform.organization.departments.services.DepartmentReadServicePrinciple;
import org.ospic.platform.organization.departments.services.DepartmentWriteServicePrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

/**
 * This file was created by eli on 09/01/2021 for org.ospic.platform.organization.departments.api
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
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/departments")
@Api(value = "/api/departments", tags = "Department Api's")
@Transactional
public class DepartmentApiResource {
    @Autowired
    DepartmentReadServicePrinciple departmentRead;
    @Autowired
    DepartmentWriteServicePrinciple departmentWrite;

    @Autowired
    public DepartmentApiResource(
            DepartmentReadServicePrinciple departmentRead,
            DepartmentWriteServicePrinciple departmentWrite) {
        this.departmentRead = departmentRead;
        this.departmentWrite = departmentWrite;
    }

    @ApiOperation(value = "RETRIEVE Departments", notes = "RETRIEVE  Departments")
    @RequestMapping(value = "/", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> retrieveAllDepartments() {
        return departmentRead.retrieveAllDepartments();
    }


    @ApiOperation(value = "CREATE Departments", notes = "CREATE  Departments")
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> createNameDepartment(@Valid @RequestBody DepartmentReqPayload payload) {
        return departmentWrite.createDepartment(payload);
    }

}
