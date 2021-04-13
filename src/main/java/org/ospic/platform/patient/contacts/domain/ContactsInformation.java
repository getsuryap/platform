package org.ospic.platform.patient.contacts.domain;

import io.swagger.annotations.ApiModelProperty;
import org.ospic.platform.util.constants.DatabaseConstants;
import org.ospic.platform.patient.details.domain.Patient;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@Entity(name = DatabaseConstants.CONTACTS_INFO_TABLE)
@Table(name = DatabaseConstants.CONTACTS_INFO_TABLE)
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
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
    @ApiModelProperty(position = 1, required = true, hidden=true, notes = "used to display user name")
    private Patient patient;
    
    public ContactsInformation fromRequest(ContactsInformation c){
        return  new ContactsInformation(
                c.getIsReachable(), c.getEmail_address(), c.getZipcode(), c.getCity(),
                c.getState(), c.getPhysical_address(), c.getWork_phone(), c.getWork_phone(),null
        );
    }

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


}
