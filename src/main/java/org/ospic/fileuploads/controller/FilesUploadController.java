package org.ospic.fileuploads.controller;

import org.ospic.fileuploads.message.ResponseMessage;
import org.ospic.fileuploads.model.FileInfo;
import org.ospic.fileuploads.service.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This file was created by eli on 16/10/2020 for org.ospic.fileuploads.controller
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
@RequestMapping("/api/upload")
public class FilesUploadController {
    FilesStorageService storageService;

    @Autowired
    public FilesUploadController(FilesStorageService storageService) {
        this.storageService = storageService;
    }



    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            storageService.save(file);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }


    @RequestMapping(value = "/{patientId}/images", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> uploadPatientImage(@RequestParam("file") MultipartFile file, @PathVariable Long patientId) {
        String message = "";
        try {
            String response = storageService.uploadPatientImage(patientId, "images", file);
            message = "Uploaded the file successfully: " + response;
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @RequestMapping(value = "/{patientId}/documents", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> uploadPatientFileDocument(@RequestParam("file") MultipartFile file, @PathVariable Long patientId) {
        String message = "";
        try {
            String response = storageService.uploadPatientImage(patientId, "documents", file);
            message = "Uploaded the file successfully: " + response;
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FilesUploadController.class, "getFile", path.getFileName().toString()).build().toString();

            return new FileInfo(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @GetMapping("/{patientId}/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename, @PathVariable Long patientId) {
        Resource file = storageService.loadImage(patientId, filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


    @GetMapping("/{patientId}/documents/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getDocument(@PathVariable String filename, @PathVariable Long patientId) {
        Resource file = storageService.loadDocument(patientId, filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


    @RequestMapping(value="/{patientId}/images/{filename:.+}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<String> deletePatientImageFile(@PathVariable String filename, @PathVariable Long patientId) {
         storageService.deletePatientFileOrDocument("images",patientId, filename);
        return ResponseEntity.ok().body("Done");
    }

    @RequestMapping(value ="/{patientId}/documents/{filename:.+}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<String> deletePatientDocument(@PathVariable String filename, @PathVariable Long patientId) {
         storageService.deletePatientFileOrDocument("documents",patientId, filename);
        return ResponseEntity.ok().body("Done");
    }
}