package org.ospic.platform.organization.insurances.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ospic.platform.infrastructure.app.domain.AbstractPersistableCustom;
import org.ospic.platform.patient.insurancecard.domain.InsuranceCard;
import org.ospic.platform.util.constants.DatabaseConstants;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This file was created by eli on 03/06/2021 for org.ospic.platform.organization.insurances.domain
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
@Entity(name = DatabaseConstants.TABLE_INSURANCE)
@Table(name = DatabaseConstants.TABLE_INSURANCE)
@ApiModel(value = "Insurance", description = "Insurance company")
public class Insurance extends AbstractPersistableCustom implements Serializable {
    @Column(length = 100, name = "name", unique = true)
    private String name;

    @Column(name = "po_box", unique = true)
    private String poBox;

    @Column(name = "location")
    private String location;

    @Column(name = "tel_no")
    private String telephoneNo;

    @Column(name = "email_address", unique = true)
    private String emailAddress;


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @JoinColumn(name = "insurance_id")
    private List<InsuranceCard> insurances = new ArrayList<>();

    public Insurance fromJson(Insurance i){
        return new Insurance(i.name, i.poBox, i.location, i.telephoneNo, i.emailAddress);
    }

    private Insurance(String name, String poBox, String location, String telephoneNo, String emailAddress) {
        this.name = name;
        this.poBox = poBox;
        this.location = location;
        this.telephoneNo = telephoneNo;
        this.emailAddress = emailAddress;
    }
}
