package org.ospic.platform.infrastructure.reports.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.fileuploads.message.ResponseMessage;
import org.ospic.platform.infrastructure.reports.exception.EmptyContentFileException;
import org.ospic.platform.infrastructure.reports.service.ReportReadPrincipleService;
import org.ospic.platform.infrastructure.reports.service.ReportWritePrincipleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * This file was created by eli on 02/03/2021 for org.ospic.platform.infrastructure.reports.api
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
@Controller
@RequestMapping("/api/reports")
@Api(value = "/api/reports", tags = "Reports", description = "Reports")
public class ReportsApiResource {
    private final ReportReadPrincipleService readPrincipleService;
    private final ReportWritePrincipleService writePrincipleService;
    @Autowired
    public ReportsApiResource(ReportReadPrincipleService readPrincipleService, ReportWritePrincipleService writePrincipleService){
        this.readPrincipleService = readPrincipleService;
        this.writePrincipleService = writePrincipleService;
    }

    @ApiOperation(value = "UPLOAD new report", notes = "UPLOAD new report")
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.ALL_VALUE)
    public ResponseEntity<?> uploadReportFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        if (file.isEmpty()){
            throw new EmptyContentFileException();
        }
        try {
            return writePrincipleService.createReport(file);
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @ApiOperation(value = "CREATE Bed", notes = "CREATE bed")
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    ResponseEntity<?> readAllReports(){
        return readPrincipleService.readAllReports();
    }

}