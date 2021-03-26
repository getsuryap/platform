package org.ospic.platform.accounting.statistics.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.accounting.statistics.service.AccountingStatisticsReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This file was created by eli on 27/03/2021 for org.ospic.platform.accounting.statistics.api
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
@RequestMapping("/api/accounting/statistics")
@Api(value = "/api/accounting/statistics", tags="Accounting statistics", description = "Accounting statistics")
public class AccountingStatisticsApiResources {
    @Autowired
    AccountingStatisticsReadService readService;

    @ApiOperation(value = "READ accounting statistics ", notes = "READ accounting statistics")
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> readAccountingStatistics(@RequestParam(value = "type", required = true) String type) {
        switch (type) {
            case "billsperday":
                return readService.readBillsPerDayAccountingStatistics();

            case "billsummation":
                return readService.readBillSummationAccountingStatistics();

            case "transactionsperday":
                return readService.readTransactionsPerDayAccountingStatistics();

            case "transactionsummation":
                return readService.readTransactionSummationAccountingStatistics();


        }
        return null;
    }
}

