package org.ospic.platform.organization.statistics.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.organization.statistics.data.StatisticResponseData;
import org.ospic.platform.organization.statistics.service.StatisticsReadPrincipleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * This file was created by eli on 08/01/2021 for org.ospic.platform.organization.statistics.api
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
@RequestMapping("/api/statistics")
@Api(value = "/api/statistics", tags = "Statistics", description = "Statistics Api's")
public class StatisticsApiResource {
    private final StatisticsReadPrincipleService readPrincipleService;
    @Autowired
    public StatisticsApiResource(StatisticsReadPrincipleService readPrincipleService){
        this.readPrincipleService = readPrincipleService;
    }

    @ApiOperation(value = "GET statistical data's", notes = "Get  statistical data's", response = StatisticResponseData.class)
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getAllUnassignedPatients() {
        return readPrincipleService.retrieveStatistic();
    }
}
