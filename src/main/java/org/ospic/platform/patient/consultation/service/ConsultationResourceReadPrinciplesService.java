package org.ospic.platform.patient.consultation.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * This file was created by eli on 23/12/2020 for org.ospic.platform.patient.consultation.service
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
public interface ConsultationResourceReadPrinciplesService {
    ResponseEntity<?> retrieveAllServices();
    ResponseEntity<?> retrialAllActiveServices();
    ResponseEntity<?> retrieveAllInactiveServices();
    ResponseEntity<?> retrieveAServiceById(Long serviceId);
    ResponseEntity<?> retrieveServiceByPatientId(Long patientId);
    ResponseEntity<?> retrieveServiceByPatientIdAndIsActiveTrue(Long patientId);
    ResponseEntity<?> retrieveServiceByPatientIdAndIsActiveFalse(Long patientId);
    ResponseEntity<?> retrieveServiceByStaffIdAndIsActiveTrue(Long staffId);
    ResponseEntity<?> retrieveServiceByStaffIdAndIsActiveFalse(Long staffId);
    ResponseEntity<?> retrieveServiceByStaffIdAll(Long staffId);
    ResponseEntity<?> retrieveAllActiveServicesInIpd();
    ResponseEntity<?> retrialAllAllActiveServiceInOpd();

}
