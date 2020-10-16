package org.ospic.physicians.service;

import org.ospic.payload.response.MessageResponse;
import org.ospic.physicians.domains.Physician;
import org.ospic.physicians.repository.PhysicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

/**
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
public class PhysicianInformationServiceImpl  implements PhysicianInformationService{
    PhysicianRepository physicianRepository;

    @Autowired
    public PhysicianInformationServiceImpl(PhysicianRepository physicianRepository){
        this.physicianRepository = physicianRepository;
    }
    @Override
    public List<Physician> retrieveAllPhysicians() {
        return physicianRepository.findAll();
    }

    @Override
    public ResponseEntity createNewPhysician(Physician physician) {
        if (physicianRepository.existsByUsername(physician.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username "+ physician.getUsername() + " is already taken!"));
        }
        return ResponseEntity.ok( physicianRepository.save(physician));
    }

    @Override
    public ResponseEntity createByPhysicianListIterate(List<Physician> physiciansList) {
        for (Physician physician: physiciansList){
            if (physicianRepository.existsByUsername(physician.getUsername())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Username "+ physician.getUsername() + " is already taken!"));
            }
        }
        return ResponseEntity.ok(physicianRepository.saveAll(physiciansList));
    }

    @Override
    public Optional<Physician> retrievePhysicianById(Long id) {
       return physicianRepository.findById(id);
    }

    @Override
    public ResponseEntity updatePhysician(Long id, Physician physic) {
        return physicianRepository.findById(id)
                .map(physician -> {
                    physician.setContacts(physic.getContacts() == null ? physician.getContacts() : physic.getContacts() );
                    physician.setFirstname(physic.getFirstname() ==null? physician.getFirstname() : physic.getFirstname());
                    physician.setLastname(physic.getLastname() == null ?  physician.getLastname() : physic.getLastname());
                    physician.setLevel(physic.getLevel() == null? physician.getLevel() : physic.getLevel());
                    physician.setUsername(physic.getUsername() == null? physician.getUsername():physic.getUsername());
                    physician.setSpecialities(physic.getSpecialities() == null ? physician.getSpecialities() : physic.getSpecialities());

                    return ResponseEntity.ok(physicianRepository.save(physician));
                })
                .orElseThrow(() -> new EntityNotFoundException());
    }

    @Override
    public ResponseEntity getPhysicianById(Long id) {
        return ResponseEntity.ok(physicianRepository.findById(id).orElseThrow(() -> new EntityNotFoundException()));
    }
}
