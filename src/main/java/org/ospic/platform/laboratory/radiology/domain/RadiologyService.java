package org.ospic.platform.laboratory.radiology.domain;

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
 * This file was created by eli on 15/02/2021 for org.ospic.platform.laboratory.tests.domain
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
@Entity(name = DatabaseConstants.TABLE_RADIOLOGY_SERVICES)
@Table(name = DatabaseConstants.TABLE_RADIOLOGY_SERVICES)
@ApiModel(value = "Patient", description = "Radiology test services")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class RadiologyService extends AbstractPersistableCustom implements Serializable {
    @NotBlank
    @Column(length = 200, name = "name", unique = true, nullable = false)
    private String name;


    @Column(length = 200, name = "descriptions")
    @NotBlank
    private String  descriptions;

    @NotNull
    @Column(name = "price", nullable = false, columnDefinition="Decimal(19,2) default '0.00'")
    private Double price;

    @NotNull
    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive;

    public RadiologyService(@NotBlank String name, @NotBlank  String descriptions, @NotNull Double price, Boolean isActive) {
        this.name = name;
        this.descriptions = descriptions;
        this.price = price;
        this.isActive = isActive;
    }
}