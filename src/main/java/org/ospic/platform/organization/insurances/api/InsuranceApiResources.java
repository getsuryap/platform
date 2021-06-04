package org.ospic.platform.organization.insurances.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.organization.insurances.domain.Insurance;
import org.ospic.platform.organization.insurances.service.InsuranceReadServicePrinciple;
import org.ospic.platform.organization.insurances.service.InsuranceWriteServicePrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * This file was created by eli on 03/06/2021 for org.ospic.platform.organization.insurances.api
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
@RestController()
@RequestMapping("/api/insurances")
@Api(value = "/api/insurances", tags = "Insurances", description = "Insurance companies")
public class InsuranceApiResources {
    private final InsuranceReadServicePrinciple insuranceReadServicePrinciple;
    private final InsuranceWriteServicePrinciple insuranceWriteServicePrinciple;
    @Autowired
    public InsuranceApiResources(final InsuranceReadServicePrinciple insuranceReadServicePrinciple,
                                 InsuranceWriteServicePrinciple insuranceWriteServicePrinciple){
        this.insuranceReadServicePrinciple = insuranceReadServicePrinciple;
        this.insuranceWriteServicePrinciple = insuranceWriteServicePrinciple;
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','CREATE_INSURANCE_COMPANY')")
    @ApiOperation(value = "CREATE new insurance company", notes = "CREATE new insurance company")
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createNewInsuranceCompany(@RequestBody Insurance payload) {
        return ResponseEntity.ok().body(insuranceWriteServicePrinciple.createNewInsuranceCompany(payload));
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','READ_INSURANCE_COMPANY')")
    @ApiOperation(value = "GET insurance companies", notes = "GET list of insurance companies")
    @RequestMapping(value = "/", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getListOfInsuranceCompanies() {
        return ResponseEntity.ok().body(insuranceReadServicePrinciple.getListOfInsuranceCompanies());
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','UPDATE_INSURANCE_COMPANY')")
    @ApiOperation(value = "UPDATE insurance company", notes = "UPDATE insurance company info")
    @RequestMapping(value = "/{insuranceId}", method = RequestMethod.PUT, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateInsuranceCompanyInfo(@PathVariable Long insuranceId, @RequestBody Insurance insurance ) {
        return ResponseEntity.ok().body(insuranceWriteServicePrinciple.updateInsuranceCompanyInformation(insuranceId, insurance));
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','DELETE_INSURANCE_COMPANY')")
    @ApiOperation(value = "DELETE insurance company", notes = "DELETE insurance company info")
    @RequestMapping(value = "/{insuranceId}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteInsuranceCompany(@PathVariable Long insuranceId ) {
        return ResponseEntity.ok().body(insuranceWriteServicePrinciple.deleteInsuranceCompany(insuranceId));
    }

}
