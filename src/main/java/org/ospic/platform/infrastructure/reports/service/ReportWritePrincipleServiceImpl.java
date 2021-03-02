package org.ospic.platform.infrastructure.reports.service;

import org.ospic.platform.fileuploads.service.FilesStorageService;
import org.ospic.platform.infrastructure.reports.domain.Reports;
import org.ospic.platform.infrastructure.reports.repository.ReportsJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

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
public class ReportWritePrincipleServiceImpl implements ReportWritePrincipleService{
    private final ReportsJpaRepository repository;
    private final FilesStorageService filesystemService;
    @Autowired
    public ReportWritePrincipleServiceImpl(ReportsJpaRepository repository, FilesStorageService filesystemService){
        this.repository = repository;
        this.filesystemService = filesystemService;
    }

    @Override
    public ResponseEntity<?> createReport(MultipartFile file) {
        String filename = filesystemService.uploadReportFile(file);
        Reports report = new Reports().fromFileStorage(filename);
        Optional<Reports> reportOptional = this.repository.findByfilename(report.getFilename());
        if (reportOptional.isPresent()){
            Reports rpt = reportOptional.get();
            rpt.setDescriptions(report.getDescriptions());
            rpt.setFilename(report.getFilename());
            rpt.setName(report.getName());
            rpt.setType(report.getType());
            Reports rep= repository.save(rpt);
            return ResponseEntity.ok().body(rep);
        }else
        return ResponseEntity.ok().body(repository.save(report));
    }
}
