package com.context.springsecurity.patient.domain;

import com.context.springsecurity.util.constants.DatabaseConstants;
import com.context.springsecurity.patient.contacts.domain.ContactsInformation;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
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
@Entity
@Table(name = DatabaseConstants.PATIENT_INFO_TABLE)
@ApiModel(value = "Patient", description = "A Patient row containing specific patient information's")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @NotBlank
    @Column(length = 20)
    @ApiModelProperty(notes = "Patient First name", required = true, name = "first_name")
    private String first_name;

    @NotBlank
    @Column(length = 20)
    private String middle_name;

    @NotBlank
    @Column(length = 20)
    private String last_name;

    @NotBlank
    @Column(length = 20)
    private String suffix;

    @NotBlank
    @Column(length = 20)
    private String ethnicity;

    @NotBlank
    @Column(length = 50)
    private String dob;

    @NotBlank
    @Column(length = 5)
    private String gender;

    @NotBlank
    @Column(length = 20)
    private String ssn;

    @NotBlank
    @Column(length = 20)
    private String mdn;

    @NotBlank
    @Column(length = 200)
    private String principal_tribe;

    @NotBlank
    @Column(length = 20)
    private String country;

    @OneToOne(mappedBy = "patient",cascade = CascadeType.ALL)
    private  ContactsInformation contactsInformation;

    public  Patient(){ }
    public Patient( String first_name, String middle_name, String last_name,  String suffix,
                    String ethnicity,  String dob,  String gender,  String ssn,  String mdn,
                    String principal_tribe,  String country, ContactsInformation contactsInformation) {
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.suffix = suffix;
        this.ethnicity = ethnicity;
        this.dob = dob;
        this.gender = gender;
        this.ssn = ssn;
        this.mdn = mdn;
        this.principal_tribe = principal_tribe;
        this.country = country;
        this.contactsInformation = contactsInformation;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getDOB() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getMdn() {
        return mdn;
    }

    public void setMdn(String mdn) {
        this.mdn = mdn;
    }

    public String getPrincipal_tribe() {
        return principal_tribe;
    }

    public void setPrincipal_tribe(String principal_tribe) {
        this.principal_tribe = principal_tribe;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @JsonBackReference
    public ContactsInformation getContactsInformation() {
        return contactsInformation;
    }

    public void setContactsInformation(ContactsInformation contactsInformation) {
        this.contactsInformation = contactsInformation;
    }
}
