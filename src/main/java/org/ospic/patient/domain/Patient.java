package org.ospic.patient.domain;

import org.ospic.domain.Auditable;
import org.ospic.physicians.domains.Physician;
import org.ospic.util.constants.DatabaseConstants;
import org.ospic.contacts.domain.ContactsInformation;
import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

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
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
@EqualsAndHashCode(callSuper = true)
public class Patient extends Auditable<String> implements Serializable  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private @Setter(AccessLevel.PROTECTED)  Long id;

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


    @Column(length = 255, name = "thumbnail")
    private String imageThumbnail;

    @OneToOne(mappedBy = "patient",cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id")
    private  ContactsInformation contactsInformation;


    @ManyToOne
    @JoinColumn(name = "physician_id")
    @Getter @Setter private Physician physician;

    public Patient( String first_name, String middle_name, String last_name,  String suffix,
                    String ethnicity,  String dob,  String gender,  String ssn,  String mdn,
                    String principal_tribe,  String country,String imageThumbnail,
                    ContactsInformation contactsInformation,
                    Physician physician) {
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
        this.imageThumbnail = imageThumbnail;
        this.physician = physician;
        this.contactsInformation = contactsInformation;
    }





    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient )) return false;
        return id != null && id.equals(((Patient) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
