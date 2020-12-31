package org.ospic.inventory.admission.visits.service;

import org.ospic.domain.CustomReponseMessage;
import org.ospic.inventory.admission.exception.AdmissionNotFoundException;
import org.ospic.inventory.admission.repository.AdmissionRepository;
import org.ospic.inventory.admission.visits.data.VisitPayload;
import org.ospic.inventory.admission.visits.domain.AdmissionVisit;
import org.ospic.inventory.admission.visits.repository.AdmissionVisitRepository;
import org.ospic.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * This file was created by eli on 19/12/2020 for org.ospic.inventory.admission.visits.service
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
                cm.setHttpStatus(HttpStatus.BAD_REQUEST.value());
                cm.setMessage("An admission with ID: "+admission.getId() + " is inactive ");
                return new ResponseEntity<CustomReponseMessage>(cm, httpHeaders, HttpStatus.BAD_REQUEST);
            }
            if (visitLocalDateTime.isBefore(admission.getFromDateTime())){
                cm.setHttpStatus(HttpStatus.BAD_REQUEST.value());
                cm.setMessage("Admission visit can not be before admission date");
                return new ResponseEntity<CustomReponseMessage>(cm, httpHeaders, HttpStatus.BAD_REQUEST);
            }

            AdmissionVisit visit = new AdmissionVisit();
            visit.setAdmission(admission);
            visit.setSymptoms(payload.getSymptoms());
            visit.setDateTime(visitLocalDateTime);
            admissionVisitRepository.save(visit);
            return ResponseEntity.ok().body(new CustomReponseMessage(HttpStatus.OK.value(), "Admission saved successfully"));
        }).orElseThrow(()->new AdmissionNotFoundException(payload.getAdmissionId()));

    }

}
