package org.ospic.platform.patient.insurancecard.service;

import org.ospic.platform.organization.insurances.repository.InsuranceRepository;
import org.ospic.platform.patient.insurancecard.domain.InsuranceCard;
import org.ospic.platform.patient.insurancecard.repository.InsuranceCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

/**
 * This file was created by eli on 03/06/2021 for org.ospic.platform.patient.insurancecard.service
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
public class InsuranceCardReadServicePrincipleImpl implements InsuranceCardReadServicePrinciple {

    private final InsuranceRepository insuranceRepository;
    private final InsuranceCardRepository cardRepository;

    @Autowired
    public InsuranceCardReadServicePrincipleImpl(final InsuranceRepository insuranceRepository,
                                                 final InsuranceCardRepository cardRepository){
        this.insuranceRepository = insuranceRepository;
        this.cardRepository = cardRepository;
    }
    @Override
    public Collection<InsuranceCard> getInsuranceCards() {
        return this.cardRepository.findAll();
    }

    @Override
    public Collection<InsuranceCard> getInsuranceCardsByInsure(Long insureId) {
        return this.cardRepository.findByInsuranceId(insureId);
    }

    @Override
    public Collection<InsuranceCard> getInsuranceCardsByPatientId(Long patientId) {
        return this.cardRepository.findByPatientId(patientId);
    }

    @Override
    public ResponseEntity<?> getInsuranceCard(Long insuranceCardId) {
        Optional<InsuranceCard> optionalCard = this.cardRepository.findById(insuranceCardId);
        return ResponseEntity.ok().body(optionalCard.isPresent() ?  optionalCard.get() : "Insurance card not found") ;
    }
}
