package com.context.springsecurity.patient.data;

import com.context.springsecurity.physicians.domains.Physician;

import java.io.Serializable;
import java.util.List;

/**
 * This file was created by eli on 14/10/2020 for com.context.springsecurity.patient.data
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
public class PatientData implements Serializable {
    private final Long id;
    private final String name;
    private final List<Physician> physicianOptions;

    public static PatientData patientCreationTemplate(final List<Physician> physicianOptions){
        return new PatientData(null,null, physicianOptions);
    }

    public PatientData(final Long id, final String name, final List<Physician> physicianOptions) {
        this.id = id;
        this.name = name;
        this.physicianOptions = physicianOptions;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Physician> getPhysicianOptions() {
        return physicianOptions;
    }
}
