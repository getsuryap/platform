package org.ospic.platform.organization.servicetypes.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ospic.platform.infrastructure.app.domain.AbstractPersistableCustom;
import org.ospic.platform.organization.medicalservices.domain.MedicalService;
import org.ospic.platform.util.constants.DatabaseConstants;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
@Entity(name = DatabaseConstants.TABLE_SERVICES_TYPES)
@Table(name = DatabaseConstants.TABLE_SERVICES_TYPES)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class MedicalServiceTypes extends AbstractPersistableCustom implements Serializable {

    @Column(length = 140, name = "name", unique = true)
    private String name;

    @Column(length = 140, name = "description")
    private String descriptions;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @JoinColumn(name = "service_type_id")
    private List<MedicalService> medicalServices = new ArrayList<>();



    public MedicalServiceTypes instance(String name,String descriptions){
        return new MedicalServiceTypes(name, descriptions);
    }

    public MedicalServiceTypes(String name,String descriptions) {
        this.name = name;
        this.descriptions = descriptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MedicalServiceTypes)) return false;
        MedicalServiceTypes that = (MedicalServiceTypes) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getDescriptions(), that.getDescriptions()) && Objects.equals(getMedicalServices(), that.getMedicalServices());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescriptions(), getMedicalServices());
    }
}
