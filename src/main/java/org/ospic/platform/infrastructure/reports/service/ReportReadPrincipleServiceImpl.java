package org.ospic.platform.infrastructure.reports.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.ospic.platform.infrastructure.reports.domain.Reports;
import org.ospic.platform.infrastructure.reports.repository.ReportsJpaRepository;
import org.ospic.platform.patient.details.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This file was created by eli on 02/03/2021 for org.ospic.platform.infrastructure.reports.service
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
@Repository
public class ReportReadPrincipleServiceImpl implements ReportReadPrincipleService{
    private final ReportsJpaRepository repository;
    @Autowired
    ApplicationContext context;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    ReportReadPrincipleServiceImpl(ReportsJpaRepository repository){
        this.repository = repository;
    }
    @Override
    public Collection<Reports> readAllReports() {
        List<Reports> reports = repository.findAll();
        return reports;
    }

    @Override
    public Collection<Reports> readReportsByType(Long type) {
       Collection<Reports> reports = repository.findByType(type);
        return reports;
    }

    @Override
    public ResponseEntity<?> readReport(String reportname,final Collection<?> collection) throws ServletException, IOException, JRException {
        HttpHeaders headers = new HttpHeaders();
        String filename = "pdf1.pdf";
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add("content-disposition", "inline;filename=" + filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        byte[] bytes = this.exportPdfReport(reportname, collection);
        return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);

    }

    private byte[]  exportPdfReport(final String reportName, final Collection<?> collection) throws ServletException, JRException, IOException {

        try {
            // Fetching the .jrxml file from the resources folder.
            Resource resource = context.getResource("classpath:reports/" +reportName+".jrxml");
            final InputStream stream = resource.getInputStream();

            // Compile the Jasper report from .jrxml to .japser
            final JasperReport report = JasperCompileManager.compileReport(stream);

            // Fetching the employees from the data source.
            final JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(collection);


            // Adding the additional parameters to the pdf.
            final Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", "javacodegeek.com");

            // Filling the report with the employee data and additional parameters information.
            final JasperPrint print = JasperFillManager.fillReport(report, parameters, source);
            final String filePath = "\\";
            // Export the report to a PDF file.
            return JasperExportManager.exportReportToPdf(print);
        } catch (JRException ex) {
            throw new ServletException(ex);
        }
    }
}
