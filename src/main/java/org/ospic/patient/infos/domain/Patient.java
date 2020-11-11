package org.ospic.patient.infos.domain;

import lombok.experimental.PackagePrivate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.ospic.domain.Auditable;
import org.ospic.inventory.admission.domains.Admission;
import org.ospic.patient.diagnosis.domains.Diagnosis;
import org.ospic.physicians.domains.Physician;
import org.ospic.util.constants.DatabaseConstants;
import org.ospic.patient.contacts.domain.ContactsInformation;
import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.ospic.util.enums.Gender;

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
@Data
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@Entity(name = DatabaseConstants.PATIENT_INFO_TABLE)
@Table(name = DatabaseConstants.PATIENT_INFO_TABLE)
@ApiModel(value = "Patient", description = "A Patient row containing specific patient information's")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@EqualsAndHashCode(callSuper = true)
public class Patient extends Auditable<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private @Setter(AccessLevel.PROTECTED)
    Long id;

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
    @Column(name = "symptoms", length = 250)
    private String symptoms;

    @NotBlank
    @Column(name ="marital_status", length = 25)
    private String marriageStatus;

    @NonNull
    @Column(name = "gender")
    private Gender gender;



    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id")
    @JsonIgnore
    private ContactsInformation contactsInformation;


    @ManyToOne
    @JoinColumn(name = "physician_id")
    @ApiModelProperty(position = 1, required = true, hidden = true, notes = "used to display user name")
    private Physician physician;

    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "patient_id")
    @ApiModelProperty(position = 1, required = true, hidden = true, notes = "used to display user name")
    @JsonIgnore
    private List<Diagnosis> diagnoses = new ArrayList<>();

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "patient")

    @ApiModelProperty(position = 1, required = true, hidden = true, notes = "used to display user name")
    @JsonIgnore
    private List<Admission> admissions = new ArrayList<>();


    public Patient(
            String name, String guardianName, String phone, String address, String emailAddress,
            String height,  String weight, String bloodPressure,  int age, Boolean isAdmitted, String patientPhoto,
            String bloodGroup,String note,String symptoms, String marriageStatus, Gender gender,
            ContactsInformation contactsInformation, Physician physician, List<Diagnosis> diagnoses) {
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
        this.symptoms = symptoms;
        this.marriageStatus = marriageStatus;
        this.gender = gender;
        this.contactsInformation = contactsInformation;
        this.physician = physician;
        this.diagnoses = diagnoses;
    }






    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;
        return id != null && id.equals(((Patient) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
