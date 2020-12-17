package org.ospic.organization.staffs.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.ospic.patient.contacts.domain.ContactsInformation;
import org.ospic.patient.infos.domain.Patient;
import org.ospic.security.authentication.users.domain.User;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Column(name = "id", unique = true)
    private Long id;

    @Column(length = 20, name = "username", unique = true)
    private String username;

    @Column(length = 20, name = "fullname")
    private String fullName;

    @Column(name = "contacts")
    private String contacts;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "doc_type")
    private String level;

    @Column(name = "email")
    private String email;

    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "staff_id")
    @JsonIgnore
    private Set<Patient> patients = new HashSet<>();

    @OneToOne
    @MapsId
    @ApiModelProperty(position = 1, required = false, hidden=true, notes = "used to display user name")
    private User user;

    public Staff(String username, String fullName, String contacts,
                 String imageUrl, String level, String email) {
        this.fullName = fullName;
        this.contacts = contacts;
        this.imageUrl = imageUrl;
        this.level = level;
        this.email = email;
        this.username = username;
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
