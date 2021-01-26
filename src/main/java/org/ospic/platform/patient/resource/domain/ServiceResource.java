package org.ospic.platform.patient.resource.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.ospic.platform.inventory.admission.domains.Admission;
import org.ospic.platform.organization.staffs.domains.Staff;
import org.ospic.platform.patient.diagnosis.domains.Diagnosis;
import org.ospic.platform.patient.infos.domain.Patient;
import org.ospic.platform.util.constants.DatabaseConstants;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * This file was created by eli on 23/12/2020 for org.ospic.platform.patient.resource.domain
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
@Data
@NoArgsConstructor
@Entity(name = DatabaseConstants.SERVICE_RESOURCES_TABLE)
@Table(name = DatabaseConstants.SERVICE_RESOURCES_TABLE)
@ApiModel(value = "Patient", description = "A Patient row containing specific patient information's")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@EqualsAndHashCode
public class ServiceResource implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private @Setter(AccessLevel.PROTECTED)
    Long id;

    @Column(name = "fromdate",  nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private  LocalDate fromdate;

    @Column(name = "todate",  nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate todate;

    @Column(name = "is_active",  columnDefinition = "boolean default true")
    private Boolean isActive;

    @Column(name = "is_admitted",  columnDefinition = "boolean default false")
    private Boolean isAdmitted;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    @ApiModelProperty(position = 1, required = true, hidden = true, notes = "used to display user name")
    private Staff staff;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "sid")
    @ApiModelProperty(position = 1, required = true, hidden = true, notes = "used to display user name")

    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Diagnosis> diagnoses = new ArrayList<>();

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "sid")
    @ApiModelProperty(position = 1, required = true, hidden = true, notes = "used to display user name")

    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Admission> admissions = new ArrayList<>();

    public ServiceResource(Long id, LocalDate fromdate, LocalDate todate, Boolean isActive, Patient patient, Staff staff) {
        this.id = id;
        this.fromdate = fromdate;
        this.todate = todate;
        this.isActive = isActive;
        this.patient = patient;
        this.staff = staff;
    }

}
