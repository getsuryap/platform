package org.ospic.organization.staffs.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.ospic.patient.infos.domain.Patient;
import org.ospic.util.constants.DatabaseConstants;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
@Entity(name = DatabaseConstants.STAFFS_TABLE)
@Table(name = DatabaseConstants.STAFFS_TABLE)
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @NotBlank
    @NotNull
    @Column(length = 20, name = "first_name")
    private String firstname;

    @NotBlank
    @NotNull
    @Column(length = 20, name = "last_name")
    private String lastname;

    @NotBlank
    @NotNull
    @Column(name = "user_name",unique = true)
    private String username;

    @Column(name = "contacts")
    private String contacts;

    @Column(name = "specialities")
    private String specialities;

    @Column(name = "doc_type")
    private String level;

    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "physician_id")
    @JsonIgnore
    private List<Patient> patients = new ArrayList<>();

    public Staff(
            String firstname, String lastname,String username, String contacts,
            String specialities, String level) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.contacts = contacts;
        this.specialities = specialities;
        this.level = level;
    }

    public void addPatient(Patient patient){
        patients.add(patient);
        patient.setStaff(this);
    }

    public void deletePatient(Patient patient){
        patients.remove(patient);
        patient.setStaff(null);
    }

}
