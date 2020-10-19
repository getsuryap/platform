package org.ospic.patient.infos.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ospic.util.constants.DatabaseConstants;

import javax.persistence.*;

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
@Entity(name = DatabaseConstants.MISC_INFO_TABLE)
@Table(name = DatabaseConstants.MISC_INFO_TABLE)
public class PatientMiscInfo {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(length = 200)
    private String  citizenShip;

    @Column(length = 100)
    private String  medical_enrollment;

    @Column(length = 60)
    private String  employment;

    @Column(length = 60)
    private String  school;

    @Column(length = 60)
    private String  rehabilitation;

    public PatientMiscInfo(String citizenShip, String medical_enrollment, String employment, String school, String rehabilitation) {
        this.citizenShip = citizenShip;
        this.medical_enrollment = medical_enrollment;
        this.employment = employment;
        this.school = school;
        this.rehabilitation = rehabilitation;
    }


}
