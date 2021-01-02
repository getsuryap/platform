package org.ospic.util.reporting.api;

import io.swagger.annotations.Api;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.jdbc.Work;
import org.ospic.patient.contacts.domain.ContactsInformation;
import org.ospic.patient.contacts.services.ContactsInformationService;
import org.ospic.patient.infos.service.PatientInformationWriteService;
import org.ospic.util.reporting.service.PatientReportingReadPrincipleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * This file was created by eli on 02/01/2021 for org.ospic.util.reporting.api
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
@RequestMapping("/api/reports")
@Api(value = "/api/reports", tags = "Patient Reports")
public class ReportsApiResource {

    @Autowired
    PatientReportingReadPrincipleService patientReportingReadPrincipleService;

    @Autowired
    public ReportsApiResource(PatientReportingReadPrincipleService patientReportingReadPrincipleService) {
        this.patientReportingReadPrincipleService = patientReportingReadPrincipleService;
    }

    @RequestMapping(value = "/patients", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?>  patients(HttpServletResponse response) throws IOException {
        Workbook wb = patientReportingReadPrincipleService.workbook();
        response.setHeader("Content-disposition", "attachment; filename=test.xlsx");
        wb.write( response.getOutputStream() );
        return  ResponseEntity.ok().body(wb);
    }

}
