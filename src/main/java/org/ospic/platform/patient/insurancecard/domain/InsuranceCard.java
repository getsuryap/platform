package org.ospic.platform.patient.insurancecard.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ospic.platform.infrastructure.app.domain.AbstractPersistableCustom;
import org.ospic.platform.organization.insurances.domain.Insurance;
import org.ospic.platform.patient.details.domain.Patient;
import org.ospic.platform.patient.insurancecard.data.InsurancePayload;
import org.ospic.platform.util.constants.DatabaseConstants;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * This file was created by eli on 03/06/2021 for org.ospic.platform.patient.insurancecard.domain
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
@Entity(name = DatabaseConstants.TABLE_CLIENT_INSURANCE)
@Table(name = DatabaseConstants.TABLE_CLIENT_INSURANCE)
@ApiModel(value = "Client Insurance", description = "Client insurance informations")
public class InsuranceCard extends AbstractPersistableCustom implements Serializable {

    @Column(name = "patient_name", unique = true)
    private String patientName;

    @Column(name = "membership_no", unique = true)
    private String membershipNumber;

    @Column(name = "sex")
    private String sex;

    @Column(name = "vote_no")
    private String voteNo;

    @Column(name = "dob")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @Column(name = "issued_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate issuedDate;

    @Column(name = "expire_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expireDate;

    @Column(name = "code_no")
    private String codeNo;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "insurance_id")
    private Insurance insurance;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @JsonIgnore
    private Patient patient;


    public InsuranceCard fromJson(InsurancePayload p, Patient patient){
        return new InsuranceCard(patient.getName(), p.getMembershipNumber(),patient.getGender(),p.getVoteNo(),p.getDateOfBirth(),p.getIssuedDate(),p.getExpireDate(),p.getCodeNo(), true);
    }


    private InsuranceCard(String patientName, String membershipNumber, String sex, String voteNo, LocalDate dateOfBirth, LocalDate issuedDate, LocalDate expireDate, String codeNo, Boolean isActive) {
        this.patientName = patientName;
        this.membershipNumber = membershipNumber;
        this.sex = sex;
        this.voteNo = voteNo;
        this.dateOfBirth = dateOfBirth;
        this.issuedDate = issuedDate;
        this.expireDate = expireDate;
        this.codeNo = codeNo;
        this.isActive = isActive;
    }
}
