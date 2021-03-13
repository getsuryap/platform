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
@Api(value = "/api/bills", tags="Bill's", description = "Medical consultation bills")
public class BillsApiResources {
    @Autowired
    BillReadPrincipleService readService;
    @Autowired
    BillWritePrincipleService writeService;

    @ApiOperation(value = "LIST bill's", notes = "LIST bill's", response = BillPayload.class, responseContainer = "List")
    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS', 'READ_BILL')")
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> listBills() {
        return readService.readAllBills();
    }

    @ApiOperation(value = "GET bill by ID", notes = "GET bill by ID", response = BillPayload.class)
    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS', 'READ_BILL')")
    @RequestMapping(value = "/{billId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getBillsById(@PathVariable(name = "billId") Long billId) {
        return readService.readBillById(billId);
    }


    @ApiOperation(value = "PAY Bill", notes = "PAY Bill", response = PaymentPayload.class)
    @PreAuthorize("hasAnyAuthority('UPDATE_BILL')")
    @RequestMapping(value = "/", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> payBill(@RequestBody PaymentPayload payload) {
        return writeService.payBill(payload);
    }


}
