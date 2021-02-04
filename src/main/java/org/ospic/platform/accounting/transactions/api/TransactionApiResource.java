package org.ospic.platform.accounting.transactions.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.accounting.transactions.data.TransactionPayload;
import org.ospic.platform.accounting.transactions.domain.Transactions;
import org.ospic.platform.accounting.transactions.service.TransactionReadPrincipleService;
import org.ospic.platform.accounting.transactions.service.TransactionsWritePrincipleService;
import org.ospic.platform.organization.medicalservices.domain.MedicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * This file was created by eli on 03/02/2021 for org.ospic.platform.accounting.transactions.api
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
@RequestMapping("/api/transactions")
@Api(value = "/api/transactions", tags = "List of other medical service transaction's")
public class TransactionApiResource {
    @Autowired TransactionReadPrincipleService readService;
    @Autowired TransactionsWritePrincipleService writeService;

    @Autowired
    public TransactionApiResource(TransactionReadPrincipleService readService, TransactionsWritePrincipleService writeService) {
        this.readService = readService;
        this.writeService = writeService;
    }


    @ApiOperation(value = "CREATE new medical service transaction", notes = "CREATE new medical service transaction")
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> createMedicalService(@Valid @RequestBody TransactionPayload payload) {
        return writeService.createTransaction(payload);
    }

    @ApiOperation(value = "LIST medical service transaction's", notes = "LIST medical service transaction's")
    @RequestMapping(value = "/", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> listMedicalService() {
        return readService.readTransactions();
    }


    @ApiOperation(value = "LIST consultation transactions", notes = "LIST consultation transactions")
    @RequestMapping(value = "/{trxId}/consultation", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> listConsultationTransactions(@PathVariable(name = "trxId") Long trxId) {
        return readService.readTransactionsByConsultationId(trxId);
    }

}
