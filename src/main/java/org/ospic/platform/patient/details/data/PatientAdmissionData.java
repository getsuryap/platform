package org.ospic.platform.patient.details.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This file was created by eli on 08/12/2020 for org.ospic.platform.patient.details.data
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
@Setter(AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@NoArgsConstructor
public class PatientAdmissionData {
    private Long id;
    private String name;
    private String phone;
    private String gender;
    private String height;
    private String weight;
    private String guardianName;
    private String bloodPressure;
    private Integer age;
    private String email;
    private String martiaStatus;
    private String patientPhoto;
    private String bloodGroup;
    private String address;

    public PatientAdmissionData(Long id, String name, String phone, String gender, String height, String weight, String guardianName, String bloodPressure, Integer age, String email, String martiaStatus, String patientPhoto, String bloodGroup, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.guardianName = guardianName;
        this.bloodPressure = bloodPressure;
        this.age = age;
        this.email = email;
        this.martiaStatus = martiaStatus;
        this.patientPhoto = patientPhoto;
        this.bloodGroup = bloodGroup;
        this.address = address;
    }
}
