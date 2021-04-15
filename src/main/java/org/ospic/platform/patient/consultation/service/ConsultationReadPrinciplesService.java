package org.ospic.platform.patient.consultation.service;

import org.ospic.platform.patient.consultation.data.ConsultationPayload;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;

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
public interface ConsultationReadPrinciplesService {
    Collection<ConsultationPayload> retrieveAllConsultations();
    Collection<ConsultationPayload>  retrialAllActiveConsultations();
    Collection<ConsultationPayload>  retrieveAllInactiveConsultations();
    ResponseEntity<?> retrieveAConsultationById(Long serviceId);
    Collection<ConsultationPayload>  retrieveConsultationsByPatientId(Long patientId);
    Collection<ConsultationPayload>  retrieveConsultationByPatientIdAndIsActiveTrue(Long patientId);
    Collection<ConsultationPayload>  retrieveConsultationByPatientIdAndIsActiveFalse(Long patientId);
    Collection<ConsultationPayload>  retrieveConsultationByStaffIdAndIsActiveTrue(Long staffId);
    Collection<ConsultationPayload>  retrieveConsultationByStaffIdAndIsActiveFalse(Long staffId);
    Collection<ConsultationPayload>  retrieveConsultationByStaffIdAll(Long staffId);
    Collection<ConsultationPayload>  retrieveAllActiveConsultationsInIpd();
    Collection<ConsultationPayload>  retrialAllAllActiveConsultationInOpd();
    ResponseEntity<?> retrialAllConsultationLaboratoryReports();

}
