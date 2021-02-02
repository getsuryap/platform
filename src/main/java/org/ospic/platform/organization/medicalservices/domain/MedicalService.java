package org.ospic.platform.organization.medicalservices.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.ospic.platform.infrastructure.app.domain.AbstractPersistableCustom;
import org.ospic.platform.util.constants.DatabaseConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * This file was created by eli on 02/02/2021 for org.ospic.platform.organization.medicalservices.domain
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
@Entity(name = DatabaseConstants.TABLE_SERVICES)
@Table(name = DatabaseConstants.TABLE_SERVICES)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@EqualsAndHashCode(callSuper = false)
public class MedicalService extends AbstractPersistableCustom implements Serializable {

    @Column(length = 140, name = "name", unique = true)
    private String name;

    @Column(name = "enabled", nullable = false, columnDefinition = "boolean default  true")
    private Boolean isActive;

    @Column(name = "price", nullable = false, columnDefinition="Decimal(10,2) default '0.00'")
    private Double price;

    public MedicalService instance(String name, Boolean isActive, Double price){
        return new MedicalService(name, isActive, price);
    }

    private MedicalService(String name, Boolean isActive, Double price) {
        this.name = name;
        this.isActive = isActive;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MedicalService)) return false;
        MedicalService medicalService = (MedicalService) o;
        return getName().equals(medicalService.getName()) && getPrice().equals(medicalService.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPrice());
    }
}
