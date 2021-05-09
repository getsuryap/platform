package org.ospic.platform.fileuploads.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.ospic.platform.fileuploads.data.EntityType;
import org.ospic.platform.fileuploads.exceptions.FileUploadException;
import org.ospic.platform.fileuploads.message.ResponseMessage;
import org.ospic.platform.fileuploads.model.FileInfo;
import org.ospic.platform.fileuploads.service.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

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

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/api/upload")
@Api(value = "/api/upload", tags = "Uploads")
public class FilesUploadController {
    private final FilesStorageService storageService;

    @Autowired
    public FilesUploadController(FilesStorageService storageService) {
        this.storageService = storageService;
    }

    @ApiOperation(value = "UPLOAD files and documents in the system", notes = "UPLOAD files and documents in the system")
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            storageService.save(file);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            throw new FileUploadException("error.msg.file.upload.error", message);
        }
    }


    @ApiOperation(value = "UPLOAD patient profile picture", notes = "UPLOAD patient profile picture")
    @RequestMapping(value = "/{patientId}/images", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public ResponseEntity<ResponseMessage> uploadPatientImage(@RequestParam("file") MultipartFile file, @PathVariable Long patientId) {
        String message = "";
        try {
            String response = storageService.uploadPatientImage(patientId,EntityType.ENTITY_PATIENTS, file,"images");
            message = "Uploaded the file successfully: " + response;
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            throw new FileUploadException("error.msg.file.upload.error", message);
        }
    }

    @ApiOperation(value = "UPLOAD patient document", notes = "UPLOAD patient document")
    @RequestMapping(value = "/{patientId}/documents", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public ResponseEntity<ResponseMessage> uploadPatientFileDocument(@RequestParam("file") MultipartFile file, @PathVariable Long patientId) {
        String message = "";
        try {
            String response = storageService.uploadPatientImage(patientId,EntityType.ENTITY_PATIENTS, file,"documents");
            message = "Uploaded the file successfully: " + response;
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            throw new FileUploadException("error.msg.file.upload.error", message);
        }
    }


    @ApiOperation(value = "LIST all files/documents in a given directory", notes = "LIST all files/documents in a given directory")
    @RequestMapping(value="/files", method = RequestMethod.GET)
    public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FilesUploadController.class, "getFile", path.getFileName().toString()).build().toString();

            return new FileInfo(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @ApiOperation(value = "GET patient file from his/her file", notes = "GET patient file from his/her file")
    @RequestMapping(value = "/{patientId}/images/{filename:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getFile(@PathVariable String filename, @PathVariable Long patientId) {
        Resource file = storageService.loadImage(patientId,EntityType.ENTITY_PATIENTS, filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


    @ApiOperation(value = "GET patient document by document/file name", notes = "GET patient document by document/file name")
    @RequestMapping(value = "/{patientId}/documents/{filename:.+}", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public ResponseEntity<Resource> getDocument(@PathVariable String filename, @PathVariable Long patientId) {

        Resource file = storageService.loadDocument(patientId, EntityType.ENTITY_PATIENTS, filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


    @ApiOperation(value = "DELETE patient image by file name", notes = "DELETE patient image by file name")
    @RequestMapping(value="/{patientId}/images/{filename:.+}", method = RequestMethod.DELETE, consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public ResponseEntity<String> deletePatientImageFile(@PathVariable String filename, @PathVariable Long patientId) {
         storageService.deletePatientFileOrDocument("images",EntityType.ENTITY_PATIENTS,patientId, filename);
        return ResponseEntity.ok().body("Done");
    }

    @ApiOperation(value = "DELETE patient document", notes = "DELETE patient document")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal server Error"), @ApiResponse(code = 404, message = "Patient document not found")})
    @RequestMapping(value ="/{patientId}/documents/{filename:.+}", method = RequestMethod.DELETE, consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public ResponseEntity<String> deletePatientDocument(@PathVariable String filename, @PathVariable Long patientId) {
         storageService.deletePatientFileOrDocument("documents",EntityType.ENTITY_PATIENTS,patientId, filename);
        return ResponseEntity.ok().body("Done");
    }
}