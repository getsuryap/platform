package org.ospic.patient.infos.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.ospic.domain.Auditable;
import org.ospic.util.constants.DatabaseConstants;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This file was created by eli on 29/10/2020 for org.ospic.patient.infos.domain
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
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@Entity(name = DatabaseConstants.GENDER_TABLE)
@Table(name = DatabaseConstants.GENDER_TABLE)
@ApiModel(value = "Patient", description = "A Patient row containing specific patient information's")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")

public class Gender implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private @Setter(AccessLevel.PROTECTED)  Long id;

    @NotBlank
    @Column(length = 20)
    @ApiModelProperty(notes = "Gender name", required = true, name = "name")
    private String name;

    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "gender_id")
    @JsonIgnore
    private List<Patient> patients = new ArrayList<>();

    public Gender(String name){
        this.name = name;
    }
}
