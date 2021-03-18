package org.ospic.platform.laboratory.reports.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ospic.platform.infrastructure.app.domain.AbstractPersistableCustom;
import org.ospic.platform.patient.consultation.domain.ConsultationResource;
import org.ospic.platform.util.constants.DatabaseConstants;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.Serializable;

/**
 * This file was created by eli on 18/03/2021 for org.ospic.platform.laboratory.reports.domain
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
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@Entity(name = DatabaseConstants.TABLE_FILE_INFORMATION)
@Table(name = DatabaseConstants.TABLE_FILE_INFORMATION)
@ApiModel(value = "Files", description = "Uploaded file information's")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class FileInformation extends AbstractPersistableCustom implements Serializable {

    @NotBlank
    @Column(length = 200, name = "name", unique = true, nullable = false)
    private String name;

    @NotBlank
    @Column(length = 250, name = "url",  nullable = false)
    private String url;

    @NotBlank
    @Column(length = 200, name = "location", nullable = false)
    private String location;

    @NotBlank
    @Column(length = 20, name = "type",  nullable = false)
    private String type;

    @NotBlank
    @Column(length = 200, name = "size", nullable = false)
    private String size;

    @ManyToOne
    @JoinColumn(name = "consultation_id")
    @JsonIgnore
    private ConsultationResource consultation;

    private FileInformation(String name, String url,String location, String type, String size) {
        this.name = name;
        this.url = url;
        this.type = type;
        this.size = size;
        this.location = location;
    }

    public FileInformation fromFile(MultipartFile file, String fileUrl,String location){
        String filename = file.getOriginalFilename();
        String size = String.valueOf(file.getSize()) + "KB";
        String fileType = file.getContentType();
        return new FileInformation(filename, fileUrl,location,fileType, size);
    }

    private static String getFileSizeMegaBytes(File file) {
        return (double) file.length() / (1024 * 1024) + " mb";
    }

    private static String getFileSizeKiloBytes(File file) {
        return (double) file.length() / 1024 + "  kb";
    }

    private static String getFileSizeBytes(File file) {
        return file.length() + " bytes";
    }
}
