package org.ospic.platform.patient.consultation.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This file was created by eli on 25/12/2020 for org.ospic.platform.patient.consultation.data
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
public class ConsultationPayload {
    private Long id;
    private String fromDate;
    private String toDate;
    private Boolean isActive;
    private Boolean isAdmitted;
    private Long patientId;
    private String patientName;
    private Long staffId;
    private String staffName;

    public static ConsultationPayload instance(
            Long id, String fromDate, String toDate, Boolean isActive,
           Boolean isAdmitted,
            Long patientId, String patientName, Long staffId, String staffName) {
        return new ConsultationPayload(id, fromDate, toDate, isActive,isAdmitted, patientId, patientName, staffId, staffName);
    }

    public ConsultationPayload(Long id, String fromDate, String toDate, Boolean isActive, Boolean isAdmitted, Long patientId, String patientName, Long staffId, String staffName) {
        this.id = id;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.isActive = isActive;
        this.isAdmitted = isAdmitted;
        this.patientId = patientId;
        this.patientName = patientName;
        this.staffId = staffId;
        this.staffName = staffName;
    }
}
