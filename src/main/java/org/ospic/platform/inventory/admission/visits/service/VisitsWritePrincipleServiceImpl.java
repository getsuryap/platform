package org.ospic.platform.inventory.admission.visits.service;

import org.ospic.platform.domain.CustomReponseMessage;
import org.ospic.platform.inventory.admission.exception.AdmissionNotFoundExceptionPlatform;
import org.ospic.platform.inventory.admission.repository.AdmissionRepository;
import org.ospic.platform.inventory.admission.visits.data.VisitPayload;
import org.ospic.platform.inventory.admission.visits.domain.AdmissionVisit;
import org.ospic.platform.inventory.admission.visits.repository.AdmissionVisitRepository;
import org.ospic.platform.patient.consultation.exception.InactiveMedicalConsultationsException;
import org.ospic.platform.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * This file was created by eli on 19/12/2020 for org.ospic.platform.inventory.admission.visits.service
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
@Repository
public class VisitsWritePrincipleServiceImpl implements VisitsWritePrincipleService{
    @Autowired
    AdmissionRepository admissionRepository;
    @Autowired
    AdmissionVisitRepository admissionVisitRepository;

    @Autowired
    public VisitsWritePrincipleServiceImpl(AdmissionVisitRepository admissionVisitRepository, AdmissionRepository admissionRepository){
        this.admissionRepository = admissionRepository;
        this.admissionVisitRepository = admissionVisitRepository;
    }
    @Override
    public ResponseEntity<?> createVisits(VisitPayload payload) {
        CustomReponseMessage cm = new CustomReponseMessage();
        HttpHeaders httpHeaders = new HttpHeaders();
        final LocalDateTime visitLocalDateTime = new DateUtil().convertToLocalDateTimeViaInstant(payload.getDateTime());

        return admissionRepository.findById(payload.getAdmissionId()).map(admission -> {
            if (!admission.getIsActive()){
                throw new InactiveMedicalConsultationsException(admission.getId());
            }
            if (visitLocalDateTime.isBefore(admission.getFromDateTime())){
                String code = "error.msg.admission.is.before.start.date";
                String message = "Admission visit can not be before admission date";
                throw new InactiveMedicalConsultationsException(code, message);
            }

            AdmissionVisit visit = new AdmissionVisit();
            visit.setAdmission(admission);
            visit.setSymptoms(payload.getSymptoms());
            visit.setDateTime(visitLocalDateTime);
            admissionVisitRepository.save(visit);
            return ResponseEntity.ok().body(new CustomReponseMessage(HttpStatus.OK.value(), "Admission saved successfully"));
        }).orElseThrow(()->new AdmissionNotFoundExceptionPlatform(payload.getAdmissionId()));

    }

}
