package org.ospic.platform.fileuploads.controller;

import org.ospic.platform.fileuploads.data.EntityType;
import org.ospic.platform.fileuploads.service.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This file was created by eli on 16/10/2020 for org.ospic.platform.fileuploads.controller
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

public class FileUploadBaseController {
    FilesStorageService storageService;

    @Autowired
    public FileUploadBaseController (FilesStorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/{patientId}/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename, @PathVariable Long patientId) {
        Resource file = storageService.loadImage(patientId, EntityType.ENTITY_PATIENTS, filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @RequestMapping(value = "/{patientId}/documents/{filename:.+}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> getDocument(@PathVariable String filename, @PathVariable Long patientId) {
        Resource file = storageService.loadDocument(patientId,EntityType.ENTITY_PATIENTS, filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


}
