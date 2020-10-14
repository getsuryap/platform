package com.context.springsecurity.contacts.domain;

import com.context.springsecurity.util.constants.DatabaseConstants;
import com.context.springsecurity.patient.domain.Patient;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
@Table(name = DatabaseConstants.CONTACTS_INFO_TABLE)
public class ContactsInformation {
    @Id
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "is_active")
    private Boolean isReachable;

    @NotBlank
    @Column(length = 50)
    private String email_address;

    @Column(length = 20)
    private String zipcode;

    @Column(length = 20)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 200)
    private String physical_address;

    @Column(length = 50)
    private String home_phone;

    @Column(length = 20)
    private String work_phone;


    @OneToOne
    @MapsId
    private Patient patient;

    public ContactsInformation(){}
    public ContactsInformation(
            Boolean isReachable,String email_address,
            String zipcode, String city, String state,
            String physical_address, String home_phone,
            String work_phone, Patient patient) {
        this.isReachable = isReachable;
        this.email_address = email_address;
        this.zipcode = zipcode;
        this.city = city;
        this.state = state;
        this.physical_address = physical_address;
        this.home_phone = home_phone;
        this.work_phone = work_phone;
        this.patient = patient;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public Boolean getIsReachable() {
        return isReachable;
    }

    public void setIsReachable(Boolean isReachable) {
        this.isReachable = isReachable;
    }

    public String gethome_phone() {
        return home_phone;
    }

    public void setHome_phone(String home_phone) {
        this.home_phone = home_phone;
    }

    public String getPhysical_address() {
        return physical_address;
    }

    public void setPhysical_address(String physical_address) {
        this.physical_address = physical_address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getwork_phone() {
        return work_phone;
    }

    public void setWork_phone(String work_phone) {
        this.work_phone = work_phone;
    }


    @JsonManagedReference
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

}
