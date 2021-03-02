package org.ospic.platform.infrastructure.reports.service;

import org.ospic.platform.infrastructure.reports.domain.Reports;
import org.ospic.platform.infrastructure.reports.repository.ReportsJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    ReportReadPrincipleServiceImpl(ReportsJpaRepository repository){
        this.repository = repository;
    }
    @Override
    public ResponseEntity<?> readAllReports() {
        List<Reports> reports = repository.findAll();
        return ResponseEntity.ok().body(reports);
    }

    @Override
    public ResponseEntity<?> readReportsByType(String type) {
        return null;
    }
}
