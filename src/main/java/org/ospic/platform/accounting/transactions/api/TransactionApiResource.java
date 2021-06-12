package org.ospic.platform.accounting.transactions.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.accounting.transactions.data.TransactionRequest;
import org.ospic.platform.accounting.transactions.data.TransactionRowMap;
import org.ospic.platform.accounting.transactions.domain.Transactions;
import org.ospic.platform.accounting.transactions.service.TransactionReadPrincipleService;
import org.ospic.platform.accounting.transactions.service.TransactionsWritePrincipleService;
import org.ospic.platform.domain.CustomReponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
@Api(value = "/api/transactions",tags = "Transactions", description = "Medical service transaction's")
public class TransactionApiResource {
    private final TransactionReadPrincipleService readService;
    private final TransactionsWritePrincipleService writeService;

    @Autowired
    public TransactionApiResource(TransactionReadPrincipleService readService, TransactionsWritePrincipleService writeService) {
        this.readService = readService;
        this.writeService = writeService;
    }


    @ApiOperation(value = "CREATE new medical service transaction", notes = "CREATE new medical service transaction", response = Transactions.class, responseContainer = "List")
    @RequestMapping(value = "/{consultationId}", method = RequestMethod.POST)
    //@PreAuthorize("hasAnyAuthority('LAB_TECHNICIAN')")
    ResponseEntity<?> createMedicalService(@PathVariable(name = "consultationId") Long consultationId, @RequestBody TransactionRequest payload) {
       return this.writeService.initiateMedicalTransaction(consultationId, payload);
    }


    @ApiOperation(value = "UNDO  service transaction", notes = "UNDO service transaction", response = CustomReponseMessage.class)
    @RequestMapping(value = "/undo/{transactionId}", method = RequestMethod.PUT)
    ResponseEntity<?> undoTransaction(@PathVariable("transactionId") Long transactionId) {
        return writeService.undoTransaction(transactionId);
    }

    @ApiOperation(value = "LIST medical service transaction's", notes = "LIST medical service transaction's", response = TransactionRowMap.class, responseContainer = "List")
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> listMedicalService(@RequestParam(value = "from", required = false) Optional<String> from, @RequestParam(value = "to", required = false) Optional<String> to) {
       if (from.isPresent() && to.isPresent()){
           return ResponseEntity.ok().body(readService.readTransactionsByDateRange(from.get(), to.get()));
       }else
        return ResponseEntity.ok().body(readService.readTransactions());
    }

    @ApiOperation(value = "LIST all medical service transaction's", notes = "LIST all medical service transaction's", response = TransactionRowMap.class, responseContainer = "List")
    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getAllMedicalTransactionPageable( @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {
        Pageable paging = PageRequest.of(page, size);
        return readService.readPageableTransaction(paging);
    }

    @ApiOperation(value = "GET transaction by ID", notes = "GET transaction by ID", response = TransactionRowMap.class)
    @RequestMapping(value = "/{transactionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMedicalTransactionById( @PathVariable("transactionId") Long transactionId) {
        return readService.readTransactionById(transactionId);
    }

    @ApiOperation(value = "LIST consultation transactions", notes = "LIST consultation transactions", response = TransactionRowMap.class, responseContainer = "List")
    @RequestMapping(value = "/{trxId}/consultation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> listConsultationTransactions(@PathVariable(name = "trxId") Long trxId, @RequestParam(value = "reversed", required = false) boolean reversed) {
        int isReversed = reversed ? 1 : 0;
        switch (isReversed) {
            case 1:
                return readService.readTransactionsByBillId(trxId);
            case 0:
                return readService.readTransactionsByBillId(trxId);
            default:
                return readService.readTransactionsByBillId(trxId);
        }
    }

}
