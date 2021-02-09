package org.ospic.platform.organization.staffs.api;

import io.swagger.annotations.ApiOperation;
import org.ospic.platform.organization.staffs.data.StaffToDepartmentRequest;
import org.ospic.platform.organization.staffs.domains.Staff;
import org.ospic.platform.organization.staffs.service.StaffsReadPrinciplesService;
import io.swagger.annotations.Api;
import org.ospic.platform.organization.staffs.service.StaffsWritePrinciplesService;
import org.ospic.platform.patient.infos.service.PatientInformationReadServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/staffs")
@Api(value = "/api/staffs", tags = "Physicians")
public class StaffsApiResource {

    StaffsReadPrinciplesService readServices;
    StaffsWritePrinciplesService writeServices;
    PatientInformationReadServices patientReadServices;
    

    @Autowired
    StaffsApiResource(StaffsReadPrinciplesService readServices,
                      StaffsWritePrinciplesService writeServices, PatientInformationReadServices patientReadServices) {
        this.readServices = readServices;
        this.writeServices = writeServices;
        this.patientReadServices = patientReadServices;
    }

    @ApiOperation(value = "LIST staffs", notes = "LIST staffs ")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    List<Staff> all() {
        return readServices.retrieveAllStaffs();
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "CREATE staffs by array", notes = "CREATE staffs by array")
    ResponseEntity<?> createWithIteration(@Valid @RequestBody List<Staff> staffs) {
        return writeServices.createByStaffListIterate(staffs);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "RETRIEVE staff by ID", notes = "RETRIEVE staff by ID")
    ResponseEntity<?> getOne(@PathVariable Long id) {
        return readServices.getStaffById(id);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "UPDATE staff", notes = "UPDATE staff")
    ResponseEntity<?> update(@PathVariable Long id, @RequestBody Staff staff){
        return  writeServices.updateStaff(id, staff);
    }

    @ApiOperation(value = "GET patients assigned to this staff",notes = "GET patients assigned to this staff")
    @RequestMapping(value = "/{staffId}/patients", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getPatientAssignedToThisStaff(@PathVariable Long staffId) {
        return ResponseEntity.ok().body(patientReadServices.retrievePatientAssignedToThisStaff(staffId));
    }

    @ApiOperation(value = "ASSIGN staff to department", notes = "ASSIGN staff to department")
    @RequestMapping(value = "/assign", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> assignStaffToDepartment(@Valid @RequestBody StaffToDepartmentRequest request){
        return writeServices.assignStaffToDepartment(request);
    }

    @ApiOperation(value = "RETRIEVE staff in department", notes =  "RETRIEVE staff in department")
    @RequestMapping(value = "/dep/{departmentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getStaffOfThisDepartment(@PathVariable(name = "departmentId") Long departmentId){
        return readServices.getStaffInDepartment(departmentId);
    }


}
