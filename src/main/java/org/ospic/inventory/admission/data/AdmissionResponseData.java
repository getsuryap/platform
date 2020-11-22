package org.ospic.inventory.admission.data;

import lombok.*;

import java.util.Date;

/**
 * This file was created by eli on 22/11/2020 for org.ospic.inventory.admission.data
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
@Setter(AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@NoArgsConstructor
public class AdmissionResponseData {
    public long id;
    public Date start_date;
    public Date end_date;
    public Boolean is_active;
    public long patient;
    public long bed;

    public AdmissionResponseData(Long id, Date start_date, Date end_date, Boolean is_active, long patient, long bed) {
        this.id = id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.is_active = is_active;
        this.patient = patient;
        this.bed = bed;
    }
}
