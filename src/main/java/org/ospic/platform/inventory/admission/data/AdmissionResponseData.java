package org.ospic.platform.inventory.admission.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * This file was created by eli on 22/11/2020 for org.ospic.platform.inventory.admission.data
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
public class AdmissionResponseData implements Serializable {
    private final Long id;
    @JsonFormat(pattern = "yy/MM/dd")
    private final LocalDate startDate;
    @JsonFormat(pattern = "yy/MM/dd")
    private final LocalDate endDate;
    private final Boolean isActive;
    private final Long wardId;
    private final Long bedId;
    private final String bedIdentifier;
    private final String wardName;
    private final Long serviceId;

    public static AdmissionResponseData responseTemplate(Long id, LocalDate startDate, LocalDate endDate, Boolean isActive, Long wardId,
            Long bedId, String wardName, String bedIdentifier, Long serviceId){
        return new AdmissionResponseData(id, startDate, endDate, isActive, wardId, bedId, wardName, bedIdentifier, serviceId);
    }

    public AdmissionResponseData(Long id, LocalDate startDate, LocalDate endDate, Boolean isActive,
            Long wardId, Long bedId,String wardName, String bedIdentifier, Long serviceId) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
        this.wardId = wardId;
        this.bedId = bedId;
        this.bedIdentifier = bedIdentifier;
        this.wardName = wardName;
        this.serviceId = serviceId;
    }
}
