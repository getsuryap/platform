package com.context.springsecurity.ward.domain;

import com.context.springsecurity.constants.DatabaseConstants;

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
@Table(name = DatabaseConstants.BEDS_TABLE)
public class Bed {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true)
    private Long id;

    @NotBlank
    @Column(name = "patient_id",unique = true)
    private Long patientId;

    @NotBlank
    @Column(
            name = "is_occupied",
            nullable = false,
            columnDefinition = "bit default 0"
    )
    private Boolean isOccupied;

    public Bed(){}
    public Bed(Long patientId, Boolean isOccupied){
        this.isOccupied = isOccupied;
        this.patientId = patientId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOccupied(Boolean occupied) {
        isOccupied = occupied;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    /**Getters **/
    public Long getId() {
        return id;
    }

    public Boolean getOccupied() {
        return isOccupied;
    }

    public Long getPatientId() {
        return patientId;
    }
}
