package org.ospic.platform.organization.medicalservices.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ospic.platform.accounting.transactions.domain.Transactions;
import org.ospic.platform.infrastructure.app.domain.AbstractPersistableCustom;
import org.ospic.platform.organization.medicalservices.data.MedicalServicePayload;
import org.ospic.platform.organization.servicetypes.domain.MedicalServiceTypes;
import org.ospic.platform.util.constants.DatabaseConstants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
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
@Entity(name = DatabaseConstants.TABLE_SERVICES)
@Table(name = DatabaseConstants.TABLE_SERVICES)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class MedicalService extends AbstractPersistableCustom implements Serializable {

    @Column(length = 140, name = "name", unique = true)
    private String name;

    @Column(name = "enabled", nullable = false, columnDefinition = "boolean default  true")
    private Boolean isActive;

    @Column(name = "price", nullable = false, columnDefinition="Decimal(10,2) default '0.00'")
    private BigDecimal price;

    @Column(name = "is_measurable", nullable = false, columnDefinition = "boolean default  true")
    private Boolean isMeasurable;

    @NotNull
    @Column(name = "units", length = 5)
    private String units;

    @ManyToOne
    @JoinColumn(name = "service_type_id")
    private MedicalServiceTypes medicalServiceType;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "medical_service_id")
    @JsonIgnore
    @ApiModelProperty(position = 1, required = true, hidden = true, notes = "used to display medical serviec transactions")
    private List<Transactions> transactions = new ArrayList<>();


    public MedicalService instance(final MedicalServicePayload payload){
        return new MedicalService(payload.getName(), payload.getIsActive(), payload.getPrice(), payload.getIsMeasurable(), payload.getUnits());
    }

    public MedicalService(String name, Boolean isActive, BigDecimal price,Boolean isMeasurable, String units) {
        this.name = name;
        this.isActive = isActive;
        this.price = price;
        this.isMeasurable = isMeasurable;
        this.units = units;
    }

    public void setMedicalServiceType(MedicalServiceTypes medicalServiceType) {
        this.medicalServiceType = medicalServiceType;
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
