package org.ospic.platform.accounting.transactions.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.accounting.transactions.service.TransactionReadPrincipleService;
import org.ospic.platform.accounting.transactions.service.TransactionsWritePrincipleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
@Api(value = "/api/transactions", tags = "Medical service transaction's")

public class TransactionApiResource {
    private final TransactionReadPrincipleService readService;
    private final TransactionsWritePrincipleService writeService;

    @Autowired
    public TransactionApiResource(TransactionReadPrincipleService readService, TransactionsWritePrincipleService writeService) {
        this.readService = readService;
        this.writeService = writeService;
    }


    @ApiOperation(value = "CREATE new medical service transaction", notes = "CREATE new medical service transaction")
    @RequestMapping(value = "/{serviceId}/{type}", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('LAB_TECHNICIAN')")
    ResponseEntity<?> createMedicalService(@PathVariable(name = "serviceId") Long serviceId, @PathVariable(name = "type") String type, @RequestBody List<Long> list) {
        if (type.equals("medicine")) {

            return writeService.createMedicineServiceTransaction(serviceId, list);
        }
        if (type.equals("service")) {
            return writeService.createMedicalServiceTransaction(serviceId, list);
        } else return null;
    }


    @ApiOperation(value = "UNDO  service transaction", notes = "UNDO service transaction")
    @RequestMapping(value = "/undo/{trxId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> undoTransaction(@PathVariable("trxId") Long trxId) {
        return writeService.undoTransaction(trxId);
    }

    @ApiOperation(value = "LIST medical service transaction's", notes = "LIST medical service transaction's")
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> listMedicalService(@RequestParam(value = "from", required = false) Optional<String> from, @RequestParam(value = "to", required = false) Optional<String> to) {
       if (from.isPresent() && to.isPresent()){
           return readService.readTransactionsByDateRange(from.get(), to.get());
       }else
        return readService.readTransactions();
    }


    @ApiOperation(value = "LIST consultation transactions", notes = "LIST consultation transactions")
    @RequestMapping(value = "/{trxId}/consultation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> listConsultationTransactions(@PathVariable(name = "trxId") Long trxId, @RequestParam(value = "reversed", required = false) boolean reversed) {
        int isReversed = reversed ? 1 : 0;
        switch (isReversed) {
            case 1:
                return readService.readTransactionsByConsultationIdAndReversed(trxId);
            case 0:
                return readService.readTransactionsByConsultationIdAndNotReversed(trxId);
            default:
                return readService.readTransactionsByConsultationId(trxId);
        }
    }

}
