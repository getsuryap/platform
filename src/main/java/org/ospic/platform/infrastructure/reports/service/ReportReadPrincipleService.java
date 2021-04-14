package org.ospic.platform.infrastructure.reports.service;

import net.sf.jasperreports.engine.JRException;
import org.ospic.platform.infrastructure.reports.domain.Reports;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collection;

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
@Component
@Service
public interface ReportReadPrincipleService {
     Collection<Reports> readAllReports();
    ResponseEntity<?> readReportsByType(String type);
    ResponseEntity<?> readReport(String reportName,final Collection<?> collection) throws ServletException, IOException, JRException;
}
