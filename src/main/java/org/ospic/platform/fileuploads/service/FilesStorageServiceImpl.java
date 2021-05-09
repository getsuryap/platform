package org.ospic.platform.fileuploads.service;

import lombok.NonNull;
import org.ospic.platform.fileuploads.data.EntityType;
import org.ospic.platform.fileuploads.exceptions.InvalidFileNameException;
import org.ospic.platform.infrastructure.reports.exception.EmptyContentFileException;
import org.ospic.platform.infrastructure.reports.exception.InvalidReportNameExtensionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * This file was created by eli on 16/10/2020 for org.ospic.platform.fileuploads.service
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
@Service
public class FilesStorageServiceImpl implements FilesStorageService {
    public static final Logger logger = LoggerFactory.getLogger(FilesStorageServiceImpl.class);
    private final Path root = Paths.get("files");
    private final Path reportsRoot = Paths.get("src","main","resources","reports");

    @Override
    public void init() {
        if (!Files.exists(root)) {
            try {
                Files.createDirectory(root);

            } catch (IOException e) {
                throw new RuntimeException("Could not initialize folder for upload");
            }
        }
    }

    @Override
    public void save(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            // Copy file to the target location (Replacing existing file with the same name)

            Path targetLocation = this.root.resolve(fileName);

            logger.info("ServeletUriComponent From Current Request : " + ServletUriComponentsBuilder.fromCurrentRequest().toUriString());
            logger.info("ServeletUriComponent From Current Request Uri : " + ServletUriComponentsBuilder.fromCurrentRequestUri());

            logger.info(ServletUriComponentsBuilder.fromCurrentRequest().toUriString().concat(targetLocation.toString()));
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public String uploadReportFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new InvalidFileNameException(String.format("Sorry! Filename %s contains invalid characters sequence ", fileName));
            }
            if (!extension.equals("jrxml")){
                throw new InvalidReportNameExtensionException();
            }
            if (file.isEmpty()){
                throw new EmptyContentFileException();
            }
            // Copy file to the target location (Replacing existing file with the same name)

            Path targetLocation = this.reportsRoot.resolve(fileName);

            logger.info("ServeletUriComponent From Current Request : " + ServletUriComponentsBuilder.fromCurrentRequest().toUriString());
            logger.info("ServeletUriComponent From Current Request Uri : " + ServletUriComponentsBuilder.fromCurrentRequestUri());

            logger.info(ServletUriComponentsBuilder.fromCurrentRequest().toUriString().concat(targetLocation.toString()));
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public Resource loadImage(Long patientId, EntityType entityType,String filename, String...s) {
        try {
            Path path = this.retrieveEntityImagePath( patientId, entityType,s);
            Path file = path.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public Resource loadDocument(Long patientId, EntityType entityType, String filename,String... s) {
        try {
            Path path = this.retrieveEntityImagePath( patientId,entityType,s);
            Path file = path.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deletePatientFileOrDocument(String documentLocation, EntityType entityType, Long patientId, String filename) {

        try{
            FileSystemUtils.deleteRecursively(retrieveDocumentOrImagePath(documentLocation,entityType, patientId,filename));
        }catch (IOException e){
            throw new RuntimeException("Couldn't delete this document. Error: "+e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    @Override
    public String uploadPatientImage(@NonNull Long patientId,EntityType entityType, MultipartFile file, @NonNull String...strings) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            /** Check if the file's name contains invalid characters **/
            if (fileName.contains("..")) {
                throw new InvalidFileNameException(String.format("Sorry! Filename %s contains invalid characters sequence ", fileName));
            }

            Path targetLocation = this.createDirectoryIfNotExists(patientId, entityType, strings).resolve(fileName);

            logger.info("ServletUriComponent From Current Request Uri : " + ServletUriComponentsBuilder.fromCurrentRequest().toUriString());
            logger.info("ServletUriComponent From Current Request Uri : " + ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString());
            String servletUriComponentBuilderString = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();

            final String servletUri =  ServletUriComponentsBuilder.fromCurrentServletMapping().build().toUriString().concat("/api");
            final String resourceUri = ServletUriComponentsBuilder.fromCurrentRequest().toUriString().concat(servletUriComponentBuilderString.endsWith("/")?"":"/").concat(fileName).toString().replace(servletUri,"");

            logger.info(ServletUriComponentsBuilder.fromCurrentRequest().toUriString().concat(servletUriComponentBuilderString.endsWith("/")?"":"/").concat(fileName));

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return resourceUri;
       } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    private Path createDirectoryIfNotExists(Long patientId, EntityType entityType, String...strings) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("files/");
            sb.append(entityType.name);
            sb.append(patientId);
            sb.append("/");
            for(String str: strings){
                sb.append(str);
                sb.append("/");
            }
            Path image = Paths.get(sb.toString());
            return Files.createDirectories(image);
        } catch (IOException e) {
            throw new RuntimeException("Could not create a directory for this client. Error:  " + e.getMessage());
        }
    }

    private Path retrieveEntityImagePath(@NonNull Long patientId, EntityType entityType, String... paths) {
        StringBuilder sb = new StringBuilder();
        sb.append("files/");
        sb.append(entityType.name);
        sb.append(patientId);
        sb.append("/");
        for(String str: paths){
            sb.append(str);
            sb.append("/");
        }
        return Paths.get(sb.toString());
    }

    private Path retrieveDocumentOrImagePath(String documentLocation, EntityType entityType, @NonNull Long patientId, @NonNull String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append("files/");
        sb.append(entityType.name);
        sb.append(patientId);
        sb.append("/");
        sb.append(documentLocation);
        sb.append("/");
        sb.append(fileName);
        return Paths.get(sb.toString());
    }
}

