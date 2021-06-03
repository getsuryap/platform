package org.ospic.platform.patient.details.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.ospic.platform.configurations.audit.Auditable;
import org.ospic.platform.organization.authentication.users.domain.User;
import org.ospic.platform.patient.consultation.domain.ConsultationResource;
import org.ospic.platform.patient.contacts.domain.ContactsInformation;
import org.ospic.platform.patient.insurancecard.domain.InsuranceCard;
import org.ospic.platform.util.constants.DatabaseConstants;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
@Entity(name = DatabaseConstants.PATIENT_INFO_TABLE)
@Table(name = DatabaseConstants.PATIENT_INFO_TABLE)
@ApiModel(value = "Patient", description = "A Patient row containing specific patient information's")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@EqualsAndHashCode(callSuper = true)
public class Patient extends Auditable implements Serializable {
    private static final long serialVersionUID = -1L;

    @NotBlank
    @Column(length = 100)
    @ApiModelProperty(notes = "Patient First name", required = true, name = "first_name")
    private String name;

    @Column(name = "guardian_name", length = 100)
    private String guardianName;

    @NotBlank
    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "address", length = 200)
    private String address;

    @Column(name = "email_address", length = 20)
    private String emailAddress;

    @NotBlank
    @Column(name = "height", length = 10)
    private String height;

    @NotBlank
    @Column(name = "weight", length = 10)
    private String weight;

    @NotBlank
    @Column(name = "blood_pressure", length = 10)
    private String bloodPressure;

    @NotNull
    @Column(name = "age", length = 3)
    private int age;

    @Column(length = 2, nullable = false, columnDefinition = "boolean default false")
    private Boolean isAdmitted = false;

    @Column(length = 255, name = "thumbnail")
    private String patientPhoto;

    @Column(name = "blood_group", length = 2)
    private String bloodGroup;

    @NotBlank
    @Column(name = "note", length = 200)
    private String note;

    @NotBlank
    @Column(name = "allergies", length = 550)
    private String allergies;

    @NotBlank
    @Column(name ="marital_status", length = 25)
    private String marriageStatus;

    @NonNull
    @Column(name = "gender")
    private String gender;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default false")
    private Boolean isActive;

    @Column(name = "has_self_service_account", nullable = false, columnDefinition = "boolean default false")
    private Boolean hasSelfServiceUserAccount;

    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id")
    private ContactsInformation contactsInformation;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "patient_id")
    @ApiModelProperty(position = 1, required = true, hidden = true, notes = "used to display user name")
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<ConsultationResource> consultationResources = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "patient_id")
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<User> selfServiceUsers = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @JoinColumn(name = "patient_id")
    private List<InsuranceCard> insuranceCards = new ArrayList<>();



    public Patient(
            String name, String guardianName, String phone, String address, String emailAddress,
            String height, String weight, String bloodPressure, int age, Boolean isAdmitted, String patientPhoto,
            String bloodGroup, String note, String allergies, String marriageStatus, String gender,
            ContactsInformation contactsInformation, Boolean isActive,Boolean hasSelfServiceUserAccount) {
        this.name = name;
        this.guardianName = guardianName;
        this.phone = phone;
        this.address = address;
        this.emailAddress = emailAddress;
        this.height = height;
        this.weight = weight;
        this.bloodPressure = bloodPressure;
        this.age = age;
        this.isAdmitted = isAdmitted;
        this.patientPhoto = patientPhoto;
        this.bloodGroup = bloodGroup;
        this.note = note;
        this.allergies = allergies;
        this.marriageStatus = marriageStatus;
        this.gender = gender;
        this.contactsInformation = contactsInformation;
        this.isActive = isActive;
        this.hasSelfServiceUserAccount = hasSelfServiceUserAccount;
    }
}
