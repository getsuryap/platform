package org.ospic.platform.infrastructure.reports.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ospic.platform.infrastructure.app.domain.AbstractPersistableCustom;
import org.ospic.platform.util.constants.DatabaseConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * This file was created by eli on 02/03/2021 for org.ospic.platform.infrastructure.reports.domain
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
@Entity(name = DatabaseConstants.TABLE_REPORTS)
@Table(name = DatabaseConstants.TABLE_REPORTS)
@ApiModel(value = "Reports", description = "Reports ")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Reports extends AbstractPersistableCustom implements Serializable {
    @NotBlank
    @Column(length = 200, name = "name", unique = true, nullable = false)
    private String name;

    @NotBlank
    @Column(length = 200, name = "file", unique = true, nullable = false)
    private String filename;

    @Column(length = 200, name = "descriptions")
    @NotBlank
    private String  descriptions;

    @NotNull
    @Column(name = "type", nullable = false)
    private Long type;

    public Reports fromFileStorage(String filename){
        String reportName = filename.replaceAll("_", "");
        return new Reports(reportName,filename,reportName + " Report",1L);
    }

    private Reports(@NotBlank String name, @NotBlank String filename, @NotBlank String descriptions, @NotNull Long type) {
        this.name = name;
        this.filename = filename;
        this.descriptions = descriptions;
        this.type = type;
    }
}
