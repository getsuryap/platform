package org.ospic.platform.inventory.wards.service;

import org.hibernate.SessionFactory;
import org.ospic.platform.infrastructure.app.exception.AbstractPlatformInactiveResourceException;
import org.ospic.platform.inventory.beds.domains.Bed;
import org.ospic.platform.inventory.beds.repository.BedRepository;
import org.ospic.platform.inventory.wards.domain.Ward;
import org.ospic.platform.inventory.wards.exceptions.DuplicateWardFoundExceptions;
import org.ospic.platform.inventory.wards.exceptions.WardNotFoundExceptions;
import org.ospic.platform.inventory.wards.repository.WardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This file was created by eli on 07/11/2020 for org.ospic.platform.inventory.wards.service
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
public class WardWritePrincipleServiceImpl implements WardWritePrincipleService {

    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    WardRepository wardRepository;
    @Autowired
    BedRepository bedRepository;

    public WardWritePrincipleServiceImpl(WardRepository wardRepository, BedRepository bedRepository) {
        this.wardRepository = wardRepository;
        this.bedRepository = bedRepository;
    }

    @Override
    public ResponseEntity<?>createNewWard(Ward ward) {
        if (wardRepository.existsByName(ward.getName())) {
            throw new DuplicateWardFoundExceptions( ward.getName());
        }
        return ResponseEntity.ok().body(this.wardRepository.save(ward));
    }

    @Override
    public ResponseEntity<?> updateWard(Long id, Ward payload) {
        return this.wardRepository.findById(id).map(ward -> {
            if (wardRepository.existsByName(payload.getName())) {
                throw new DuplicateWardFoundExceptions(payload.getName());
            }
            ward.setName(payload.getName());
            return ResponseEntity.ok().body(this.wardRepository.save(ward));
        }).orElseThrow(() -> new WardNotFoundExceptions(id));
    }

    @Override
    public ResponseEntity<?>addBedInWard(Long wardId, Bed bed) throws AbstractPlatformInactiveResourceException.ResourceNotFoundException {
        return wardRepository.findById(wardId).map(ward -> {
            if (bedRepository.existsByIdentifier(bed.getIdentifier())) {
                return ResponseEntity.badRequest().body(String.format("Bed with the same Identifier %s is already exist", bed.getIdentifier()));
            }
            ward.addBed(bed);
            wardRepository.save(ward);
            return ResponseEntity.ok().body("Bed added successfully...");
        }).orElseThrow(() -> new AbstractPlatformInactiveResourceException.ResourceNotFoundException("Ward with such an ID os not found"));
    }

    @Override
    public ResponseEntity<?>addListOfBedsInWard(Long wardId, List<Bed> beds) throws AbstractPlatformInactiveResourceException.ResourceNotFoundException {
        return wardRepository.findById(wardId).map(ward -> {
            beds.forEach(bed -> {
                if (!bedRepository.existsByIdentifier(bed.getIdentifier())) {
                    ward.addBed(bed);
                }
            });
            wardRepository.save(ward);
            return ResponseEntity.ok().body("Bed added successfully...");
        }).orElseThrow(() -> new AbstractPlatformInactiveResourceException.ResourceNotFoundException("Ward with such an ID os not found"));
    }

    @Override
    public ResponseEntity<?>deleteWard(Long id) {
        return this.wardRepository.findById(id).map(ward->{
            wardRepository.delete(ward);
            return ResponseEntity.ok().body("Deleted ");
        }).orElseThrow(()->new WardNotFoundExceptions(id));
    }
}
