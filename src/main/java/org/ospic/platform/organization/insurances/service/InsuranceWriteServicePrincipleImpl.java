package org.ospic.platform.organization.insurances.service;

import org.ospic.platform.organization.insurances.domain.Insurance;
import org.ospic.platform.organization.insurances.exceptions.InsuranceNotFoundException;
import org.ospic.platform.organization.insurances.repository.InsuranceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

/**
 * This file was created by eli on 03/06/2021 for org.ospic.platform.organization.insurances.service
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
public class InsuranceWriteServicePrincipleImpl implements InsuranceWriteServicePrinciple {
    private final InsuranceRepository insuranceRepository;

    @Autowired
    public InsuranceWriteServicePrincipleImpl(final InsuranceRepository insuranceRepository){
        this.insuranceRepository = insuranceRepository;
    }
    @Override
    public Insurance createNewInsuranceCompany(Insurance insurance) {
        return this.insuranceRepository.save(insurance);
    }

    @Override
    public Insurance updateInsuranceCompanyInformation(Long id, Insurance  payload) {
        return this.insuranceRepository.findById(id).map(insurance -> {
            insurance.setEmailAddress(payload.getEmailAddress());
            insurance.setLocation(payload.getLocation());
            insurance.setName(payload.getName());
            insurance.setPoBox(payload.getPoBox());
            insurance.setTelephoneNo(payload.getTelephoneNo());

            return this.insuranceRepository.save(insurance);
        }).orElseThrow(()->new InsuranceNotFoundException(id));
    }

    @Override
    public ResponseEntity<?> deleteInsuranceCompany(Long id) {
        return this.insuranceRepository.findById(id).map(insurance -> {
            this.insuranceRepository.deleteById(id);
            return ResponseEntity.ok().body("Insurance deleted successfully");
        }).orElseThrow(()-> new InsuranceNotFoundException(id));
    }
}
