package org.ospic.platform.organization.medicalservices.services;

import org.ospic.platform.organization.medicalservices.data.MedicalServicePayload;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * This file was created by eli on 02/02/2021 for org.ospic.platform.organization.medicalservices.services
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
@Component
@Service
public interface MedicalServiceReadPrincipleService {
    Collection<MedicalServicePayload> readServices();
    Collection<MedicalServicePayload> readActiveServices();
    ResponseEntity<?> readServiceById(Long id);
    Collection<MedicalServicePayload> readServiceByMedicalServiceTypeName(String name);
    ResponseEntity<?> readMedicalServicesByMedicalServiceType(Long medicalServiceTypeId);
}
