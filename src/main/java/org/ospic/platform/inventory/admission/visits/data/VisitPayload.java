package org.ospic.platform.inventory.admission.visits.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * This file was created by eli on 19/12/2020 for org.ospic.platform.inventory.admission.visits
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
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
public class VisitPayload implements Serializable {
    private Long admissionId;
    private String symptoms;
    private Date dateTime;

    public VisitPayload(Long admissionId, String symptoms, Date dateTime) {
        this.admissionId = admissionId;
        this.symptoms = symptoms;
        this.dateTime = dateTime;
    }

    public static VisitPayload instance(Long admissionId, String symptoms, Date dateTime) {
        return new VisitPayload(admissionId, symptoms, dateTime);
    }
}
