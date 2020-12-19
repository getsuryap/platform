package org.ospic.inventory.admission.visits.service;

import org.ospic.inventory.admission.repository.AdmissionRepository;
import org.ospic.inventory.admission.visits.repository.AdmissionVisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

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
public class VisitsReadPrincipleServiceImpl implements VisitsReadPrincipleService{
    @Autowired
    AdmissionVisitRepository admissionVisitRepository;

    @Autowired
    public VisitsReadPrincipleServiceImpl(AdmissionVisitRepository admissionVisitRepository){
        this.admissionVisitRepository = admissionVisitRepository;
    }
    @Override
    public ResponseEntity<?> retrieveAdmissionVisits(Long admissionId) {
        return ResponseEntity.ok().body(admissionVisitRepository.findByAdmissionId(admissionId));
    }
}
