package org.ospic.platform.inventory.blood.service;

import org.ospic.platform.domain.CustomReponseMessage;
import org.ospic.platform.inventory.blood.data.BloodPayload;
import org.ospic.platform.inventory.blood.domain.BloodGroup;
import org.ospic.platform.inventory.blood.repository.BloodBankRepository;
import org.ospic.platform.util.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This file was created by eli on 18/12/2020 for org.ospic.platform.inventory.blood.service
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
public class BloodBankWritePrincipleServiceImpl implements BloodBankWritePrincipleService{

    @Autowired
    BloodBankRepository bloodBankRepository;
    @Autowired
    public BloodBankWritePrincipleServiceImpl(BloodBankRepository bloodBankRepository){
        this.bloodBankRepository = bloodBankRepository;
    }

    @Override
    public void initiateData() {
        BloodGroup group;
        List<BloodGroup> groupList = new ArrayList<>();
        String[] strings = {"A+","A-","B+","B-","O+","O-","AB+","AB-"};
        for (String string : strings) { group = new BloodGroup(string, 0); groupList.add(group); }
        bloodBankRepository.saveAll(groupList);

    }

    @Override
    public ResponseEntity<?> addMoreBloodBagsForThisGroup(BloodPayload payload) throws ResourceNotFoundException{
        return bloodBankRepository.findById(payload.getGroupId()).map(group ->{
            group.setCounts(group.getCounts() + payload.getBagsCount());
            bloodBankRepository.save(group);
            return ResponseEntity.ok().body(new CustomReponseMessage(HttpStatus.OK.value(),"Blood group updated successfully"));
        }).orElseThrow(() -> new ResourceNotFoundException("Blood group with such an ID os not found"));
    }

    @Override
    public ResponseEntity<?> addMoreBloodBagsForListOfBloodGroups(Collection<BloodPayload> payloads) {
        payloads.forEach(payload -> {
            bloodBankRepository.findById(payload.getGroupId()).map(group ->{
               group.setCounts(payload.getBagsCount() + payload.getBagsCount());
               bloodBankRepository.save(group);
               return null;
            });
        });
        return ResponseEntity.ok().body(new CustomReponseMessage(HttpStatus.OK.value(),"Blood group updated successfully"));
    }
}
