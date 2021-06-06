package org.ospic.platform.patient.insurancecard.service;

import org.ospic.platform.organization.insurances.exceptions.InsuranceNotFoundException;
import org.ospic.platform.organization.insurances.repository.InsuranceRepository;
import org.ospic.platform.patient.details.exceptions.PatientNotFoundExceptionPlatform;
import org.ospic.platform.patient.details.repository.PatientRepository;
import org.ospic.platform.patient.insurancecard.data.InsurancePayload;
import org.ospic.platform.patient.insurancecard.domain.InsuranceCard;
import org.ospic.platform.patient.insurancecard.exceptions.InsuranceCardDateException;
import org.ospic.platform.patient.insurancecard.exceptions.InsuranceCardNotFoundException;
import org.ospic.platform.patient.insurancecard.repository.InsuranceCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;

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
@Service
@Component
public class InsuranceCardWriteServicePrincipleImpl implements InsuranceCardWriteServicePrinciple {
    private static final Logger log = LoggerFactory.getLogger(InsuranceCardWriteServicePrincipleImpl.class);
    private final InsuranceRepository insuranceRepository;
    private final InsuranceCardRepository cardRepository;
    private final PatientRepository patientRepository;


    @Autowired
    public InsuranceCardWriteServicePrincipleImpl(final InsuranceRepository insuranceRepository,
                                                 final InsuranceCardRepository cardRepository,
                                                  final PatientRepository patientRepository){
        this.insuranceRepository = insuranceRepository;
        this.cardRepository = cardRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public InsuranceCard addInsuranceCard(InsurancePayload payload) {
        return this.insuranceRepository.findById(payload.getInsuranceId()).map(insurance -> {
            return this.patientRepository.findById(payload.getPatientId()).map(patient -> {
                if (payload.getExpireDate().isBefore(payload.getIssuedDate())){
                    throw new InsuranceCardDateException();
                };
                InsuranceCard card = new InsuranceCard().fromJson(payload, patient);
                card.setPatient(patient);
                card.setInsurance(insurance);
                return this.cardRepository.save(card);
            }).orElseThrow(()-> new PatientNotFoundExceptionPlatform(payload.getPatientId()));
        }).orElseThrow(()->new InsuranceNotFoundException(payload.getInsuranceId()));
    }

    @Override
    public InsuranceCard updateInsuranceCard(Long id, InsurancePayload payload) {
        return this.cardRepository.findById(id).map(card -> {
            return this.patientRepository.findById(payload.getPatientId()).map(patient -> {
                card.setCodeNo(payload.getCodeNo());
                card.setExpireDate(payload.getExpireDate());
                card.setIssuedDate(payload.getIssuedDate());
                card.setMembershipNumber(payload.getMembershipNumber());
                card.setVoteNo(payload.getVoteNo());
                card.setPatientName(patient.getName());
                card.setSex(patient.getGender());
                return this.cardRepository.save(card);
            }).orElseThrow(()->new PatientNotFoundExceptionPlatform(payload.getPatientId()));
        }).orElseThrow(()->new InsuranceCardNotFoundException(id));
    }


    @Override
    public InsuranceCard activateInsuranceCard(Long insuranceCardId) {
        return this.cardRepository.findById(insuranceCardId).map(card -> {
            card.setIsActive(true);
            return this.cardRepository.save(card);
        }).orElseThrow(()->new InsuranceCardNotFoundException(insuranceCardId));
    }

    @Override
    public InsuranceCard deactivateInsuranceCard(Long insuranceCardId) {
        return this.cardRepository.findById(insuranceCardId).map(card -> {
            card.setIsActive(false);
            return this.cardRepository.save(card);
        }).orElseThrow(()->new InsuranceCardNotFoundException(insuranceCardId));
    }

    @Scheduled(cron = "0 59 23 * * ?")
    public void checkCards(){
        Collection<InsuranceCard> insuranceCards = this.cardRepository.findAll();
        insuranceCards.forEach(card->{
            if (card.getExpireDate().isBefore(LocalDate.now())){
                card.setIsActive(false);
            }
            this.cardRepository.save(card);
        });

    }


}
