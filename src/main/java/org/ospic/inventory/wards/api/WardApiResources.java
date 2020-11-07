package org.ospic.inventory.wards.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.inventory.wards.domain.Ward;
import org.ospic.inventory.wards.repository.WardRepository;
import org.ospic.inventory.wards.service.WardReadService;
import org.ospic.inventory.wards.service.WardWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * This file was created by eli on 06/11/2020 for org.ospic.inventory.wards.api
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
@Component
@RequestMapping("/api/wards")
@Api(value = "/api/wards", tags = "Wards", description = "Wards API resources")
public class WardApiResources {
    final WardReadService wardReadService;
    final WardWriteService wardWriteService;
    final WardRepository wardRepository;
    @Autowired
    public WardApiResources(
            WardReadService wardReadService,
            WardWriteService wardWriteService,
            WardRepository wardRepository){
        this.wardReadService = wardReadService;
        this.wardWriteService = wardWriteService;
        this.wardRepository =wardRepository;
    }
    @ApiOperation(
            value = "RETRIEVE Wards",
            notes = "RETRIEVE Wards"
    )
    @RequestMapping(
            value = "/",
            method = RequestMethod.GET,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    ResponseEntity<List<Ward>> retrieveAllWards(){
        return wardReadService.retrieveListOfWards();
    }

    @ApiOperation(
            value = "CREATE new Ward",
            notes = "CREAT new Ward"
    )
    @RequestMapping(
            value = "/",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.ALL_VALUE
    )
    @ResponseBody
    ResponseEntity<String> createNewWard(@Valid @RequestBody  Ward ward){
       return wardWriteService.createNewWard(ward);
    }
}
