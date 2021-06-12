package org.ospic.platform.accounting.bills.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.accounting.bills.data.BillPayload;
import org.ospic.platform.accounting.bills.data.PaymentPayload;
import org.ospic.platform.accounting.bills.service.BillReadPrincipleService;
import org.ospic.platform.accounting.bills.service.BillWritePrincipleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

/**
 * This file was created by eli on 18/02/2021 for org.ospic.platform.accounting.bills.api
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
@RequestMapping("/api/bills")
@Api(value = "/api/bills", tags = "Bills", description = "Medical consultation bills")
public class BillsApiResources {
    private final BillReadPrincipleService readService;
    private final BillWritePrincipleService writeService;

    @Autowired
    public BillsApiResources(final BillReadPrincipleService readService, final BillWritePrincipleService writeService) {
        this.readService = readService;
        this.writeService = writeService;
    }

    @ApiOperation(value = "LIST bill's", notes = "LIST bill's", response = BillPayload.class, responseContainer = "List")
    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS', 'READ_BILL')")
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> listBills(@RequestParam(value = "command", required = false) String command) {
        if (!(command == null || command.isEmpty())) {
            if ("all".equals(command)) {
                readService.readAllBills();
            }
            if ("unpaid".equals(command)) {
                return ResponseEntity.ok().body(readService.readUnpaidBillsBills());
            }
        }
        return ResponseEntity.ok().body(readService.readAllBills());
    }

    @ApiOperation(value = "GET bill by ID", notes = "GET bill by ID", response = BillPayload.class)
    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS', 'READ_BILL')")
    @RequestMapping(value = "/{billId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBillsById(@PathVariable(name = "billId") Long billId) {
        return readService.readBillById(billId);
    }


    @ApiOperation(value = "PAY Bill", notes = "PAY Bill", response = PaymentPayload.class)
    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','UPDATE_BILL')")
    @RequestMapping(value = "/", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> payBill(@RequestBody PaymentPayload payload) {
        return writeService.payBill(payload);
    }

    @ApiOperation(value = "GET patient bill", notes = "GET patient bill's history", response = BillPayload.class, responseContainer = "List")
    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS', 'READ_BILL')")
    @RequestMapping(value = "/patient/{patientId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBillByPatientId(@Valid @PathVariable("patientId") Long patientId) {
        Collection<BillPayload> billPayloadCollection = this.readService.readBillsByPatientId(patientId);
        return ResponseEntity.ok().body(billPayloadCollection);
    }


}
