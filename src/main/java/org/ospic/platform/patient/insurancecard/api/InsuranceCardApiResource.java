package org.ospic.platform.patient.insurancecard.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.fileuploads.message.ResponseMessage;
import org.ospic.platform.patient.insurancecard.data.InsurancePayload;
import org.ospic.platform.patient.insurancecard.service.InsuranceCardReadServicePrinciple;
import org.ospic.platform.patient.insurancecard.service.InsuranceCardWriteServicePrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * This file was created by eli on 03/06/2021 for org.ospic.platform.patient.insurancecard.api
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
@RequestMapping("/api/insurance/cards")
@Api(value = "/api/insurance/cards", tags = "Insurances cards", description = "Insurance cards")
public class InsuranceCardApiResource {
    private final InsuranceCardReadServicePrinciple insuranceCardReadServicePrinciple;
    private final InsuranceCardWriteServicePrinciple insuranceCardWriteServicePrinciple;

    @Autowired
    public InsuranceCardApiResource(final InsuranceCardReadServicePrinciple insuranceCardReadServicePrinciple,
                                    final InsuranceCardWriteServicePrinciple insuranceCardWriteServicePrinciple) {
        this.insuranceCardReadServicePrinciple = insuranceCardReadServicePrinciple;
        this.insuranceCardWriteServicePrinciple = insuranceCardWriteServicePrinciple;
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','CREATE_INSURANCE')")
    @ApiOperation(value = "CREATE new patient insurance info", notes = "CREATE new patient insurance info")
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> addInsuranceCard(@RequestBody InsurancePayload payload) {
        return ResponseEntity.ok().body(insuranceCardWriteServicePrinciple.addInsuranceCard(payload));
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','READ_INSURANCE')")
    @ApiOperation(value = "GET patient insurances", notes = "GET patient insurances")
    @RequestMapping(value = "/", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getInsuranceCards() {
        return ResponseEntity.ok().body(insuranceCardReadServicePrinciple.getInsuranceCards());
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','UPDATE_INSURANCE')")
    @ApiOperation(value = "UPDATE patient insurance info", notes = "UPDATE patient insurance info")
    @RequestMapping(value = "/{insuranceCardId}", method = RequestMethod.PUT, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateInsuranceCard(@Valid @PathVariable("insuranceCardId") Long insuranceCardId, @RequestBody InsurancePayload payload) {
        return ResponseEntity.ok().body(insuranceCardWriteServicePrinciple.updateInsuranceCard(insuranceCardId, payload));
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','UPDATE_INSURANCE')")
    @ApiOperation(value = "ACTIVATE patient insurance", notes = "ACTIVATE patient insurance ")
    @RequestMapping(value = "/{insuranceCardId}/{action}", method = RequestMethod.PUT, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateInsuranceCardStatus(@Valid @PathVariable("insuranceCardId") Long insuranceCardId, @Valid @PathVariable("action") String action) {
        if ("activate".equals(action)) {
            return ResponseEntity.ok().body(this.insuranceCardWriteServicePrinciple.activateInsuranceCard(insuranceCardId));
        } else if ("deactivate".equals(action)) {
            return ResponseEntity.ok().body(this.insuranceCardWriteServicePrinciple.deactivateInsuranceCard(insuranceCardId));
        } else {
            return ResponseEntity.ok().body(new ResponseMessage(String.format("Action %s is not allowed",action)));
        }

    }



    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','READ_INSURANCE')")
    @ApiOperation(value = "GET issuer insurance ", notes = "GET patient insurances by issuer id")
    @RequestMapping(value = "/issuer/{issuerId}", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getInsuranceCardsByIssuer(@Valid @PathVariable("issuerId") Long issuerId) {
        return ResponseEntity.ok().body(insuranceCardReadServicePrinciple.getInsuranceCardsByInsure(issuerId));
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','READ_INSURANCE')")
    @ApiOperation(value = "GET patient insurances ", notes = "GET patient insurances by  patient id")
    @RequestMapping(value = "/patient/{patientId}", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getInsuranceCardsByPatientId(@Valid @PathVariable("patientId") Long patientId) {
        return ResponseEntity.ok().body(insuranceCardReadServicePrinciple.getInsuranceCardsByPatientId(patientId));
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','READ_INSURANCE')")
    @ApiOperation(value = "GET patient insurance by id", notes = "GET patient insurances by id")
    @RequestMapping(value = "/{cardId}", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getInsuranceCardById(@PathVariable("cardId") Long cardId) {
        return insuranceCardReadServicePrinciple.getInsuranceCard(cardId);
    }
}
