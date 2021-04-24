package org.ospic.platform.infrastructure.reports.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.jasperreports.engine.JRException;
import org.ospic.platform.accounting.bills.service.BillReadPrincipleService;
import org.ospic.platform.accounting.transactions.service.TransactionReadPrincipleService;
import org.ospic.platform.fileuploads.message.ResponseMessage;
import org.ospic.platform.infrastructure.reports.domain.Reports;
import org.ospic.platform.infrastructure.reports.exception.EmptyContentFileException;
import org.ospic.platform.infrastructure.reports.exception.ReportNotFoundException;
import org.ospic.platform.infrastructure.reports.repository.ReportsJpaRepository;
import org.ospic.platform.infrastructure.reports.service.ReportReadPrincipleService;
import org.ospic.platform.infrastructure.reports.service.ReportWritePrincipleService;
import org.ospic.platform.inventory.admission.service.AdmissionsReadService;
import org.ospic.platform.inventory.blood.service.BloodBankReadPrincipleService;
import org.ospic.platform.inventory.pharmacy.medicine.service.MedicineReadService;
import org.ospic.platform.inventory.wards.service.WardReadPrincipleService;
import org.ospic.platform.organization.medicalservices.services.MedicalServiceReadPrincipleService;
import org.ospic.platform.patient.consultation.service.ConsultationReadPrinciplesService;
import org.ospic.platform.patient.details.repository.PatientRepository;
import org.ospic.platform.patient.details.service.PatientInformationReadServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;

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
    private final PatientInformationReadServices patientReadService;
    private final AdmissionsReadService admissionsReadService;
    private final TransactionReadPrincipleService transactionReadService;
    private final BillReadPrincipleService billReadPrincipleService;
    private final ConsultationReadPrinciplesService consultationReadPrinciplesService;
    private final WardReadPrincipleService wardReadPrincipleService;
    private final BloodBankReadPrincipleService bloodBankReadPrincipleService;
    private final MedicalServiceReadPrincipleService medicalServiceReadPrincipleService;
    private final MedicineReadService medicineReadService;
    private final ReportsJpaRepository reportsJpaRepository;



    @Autowired
    public ReportsApiResource(
            ReportReadPrincipleService readPrincipleService,
            ReportWritePrincipleService writePrincipleService,
            PatientRepository patientRepository, AdmissionsReadService admissionsReadService,
            PatientInformationReadServices patientReadService,TransactionReadPrincipleService transactionReadService,
            BillReadPrincipleService billReadPrincipleService,ConsultationReadPrinciplesService consultationReadPrinciplesService,
            WardReadPrincipleService wardReadPrincipleService,BloodBankReadPrincipleService bloodBankReadPrincipleService,
            MedicalServiceReadPrincipleService medicalServiceReadPrincipleService,MedicineReadService medicineReadService,
            ReportsJpaRepository reportsJpaRepository) {
        this.readPrincipleService = readPrincipleService;
        this.writePrincipleService = writePrincipleService;
        this.patientReadService = patientReadService;
        this.admissionsReadService = admissionsReadService;
        this.transactionReadService = transactionReadService;
        this.billReadPrincipleService = billReadPrincipleService;
        this.consultationReadPrinciplesService = consultationReadPrinciplesService;
        this.wardReadPrincipleService = wardReadPrincipleService;
        this.medicalServiceReadPrincipleService = medicalServiceReadPrincipleService;
        this.bloodBankReadPrincipleService = bloodBankReadPrincipleService;
        this.medicineReadService = medicineReadService;
        this.reportsJpaRepository = reportsJpaRepository;
    }

    @ApiOperation(value = "UPLOAD new report", notes = "UPLOAD new report", response = Reports.class)
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadReportFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        if (file.isEmpty()) {
            throw new EmptyContentFileException();
        }
        try {
            return writePrincipleService.createReport(file);
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @ApiOperation(value = "GET reports by type id ", notes = "GET reports by type", response = Reports.class, responseContainer = "List")
    @RequestMapping(value = "/{reportTypeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> readAllReportsByType(@PathVariable(name = "reportTypeId") Long reportTypeId) {
        return ResponseEntity.ok().body(this.readPrincipleService.readReportsByType(reportTypeId));
    }

    @ApiOperation(value = "GET reports", notes = "GET reports", response = Reports.class, responseContainer = "List")
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> readAllReports() {
        return ResponseEntity.ok().body(this.readPrincipleService.readAllReports());
    }

    @GetMapping(path="/view/{reportId}", produces = "application/pdf")
    public ResponseEntity<?> viewReport(@PathVariable(name = "reportId", required = true) Long reportId) throws IOException, JRException, ServletException, SQLException {
       Reports report = this.reportsJpaRepository.findById(reportId).orElseThrow(()->new ReportNotFoundException(reportId));
       final String entity = report.getEntity();
       final String reportName = report.getFilename();


        if (entity.equals("client")) {
            return readPrincipleService.readReport(reportName, this.patientReadService.retrieveAllPatients());
        }
        if (entity.equals("admissions")){
            return readPrincipleService.readReport(reportName, this.admissionsReadService.retrieveAllAdmissions());
        }
        if (entity.equals("transactions")){
            return readPrincipleService.readReport(reportName, this.transactionReadService.readTransactions());
        }
        if (entity.equals("bills")){
            return readPrincipleService.readReport(reportName, this.billReadPrincipleService.readAllBills());
        }
        if (entity.equals("consultations")){
            return readPrincipleService.readReport(reportName, this.consultationReadPrinciplesService.retrieveAllConsultations());
        }
        if (entity.equals("wards")){
            return readPrincipleService.readReport(reportName, this.wardReadPrincipleService.retrieveAllWardsWithBedsCounts());
        }
        if (entity.equals("bloods")){
            return readPrincipleService.readReport(reportName, this.bloodBankReadPrincipleService.fetchBloodBankList());
        }
        if (entity.equals("services")){
            return readPrincipleService.readReport(reportName, this.medicalServiceReadPrincipleService.readServices());
        }
        if (entity.equals("medicines")){
            return readPrincipleService.readReport(reportName, this.medicineReadService.retrieveAllMedicines());
        }

        else return null;
    }

}
