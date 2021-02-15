package org.ospic.platform.inventory.beds.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.ospic.platform.inventory.beds.data.BedData;
import org.ospic.platform.inventory.beds.domains.Bed;
import org.ospic.platform.inventory.beds.repository.BedRepository;
import org.ospic.platform.inventory.wards.domain.Ward;
import org.ospic.platform.inventory.wards.repository.WardRepository;
import org.ospic.platform.util.constants.DatabaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * This file was created by eli on 06/11/2020 for org.ospic.platform.inventory.beds.service
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
public class BedWriteServiceImpl implements BedWriteService {

    @Autowired
    BedRepository bedRepository;
    @Autowired
    WardRepository wardRepository;
    @Autowired
    SessionFactory sessionFactory;

    public BedWriteServiceImpl(BedRepository bedRepository, WardRepository wardRepository) {
        this.bedRepository = bedRepository;
        this.wardRepository = wardRepository;
    }

    @Override
    public ResponseEntity<String> createNewBed(Bed bed) {
        if (bedRepository.existsByIdentifier(bed.getIdentifier())) {
            return ResponseEntity.badRequest().body(String.format("Bed with the same identifier %s already exist", bed.getIdentifier()));
        }
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(bed);
        entityManager.getTransaction().commit();
        entityManager.close();
        return ResponseEntity.ok().body("Bed Created Successfully");
    }

    @Override
    public ResponseEntity<String> updateBedInWardByAction(Long bedId, Long wardId, String action) {
       if (!bedRepository.existsById(bedId)) {
            return ResponseEntity.ok().body(String.format("Bed with id %2d does not exist...", bedId));
        }
        if (!wardRepository.existsById(wardId)) {
            return ResponseEntity.ok().body(String.format("Ward with id %2d does not exist...", wardId));
        }

        if ("assign".equals(action)) {
             bedRepository.findById(bedId).map(bed -> {
                wardRepository.findById(wardId).ifPresent(ward -> {
                    bed.setWard(ward);
                    bedRepository.save(bed);
                });
                return null;
            });
            return ResponseEntity.ok().body(String.format("Bed with ID %2d has assigned to ward with ID %s", bedId, wardId));
        }
        if ("remove".equals(action)){
            bedRepository.findById(bedId).map(bed->{
                bed.setWard(null);
                bedRepository.save(bed);
                return null;
            });
            return ResponseEntity.ok().body(String.format("Bed with ID %2d has being removed from ward with ID %2d", bedId, wardId ));
        }
        return null;

    }

    @Override
    public ResponseEntity<?> addBedsInWard(BedData bedData) {
        List<Bed> beds = new ArrayList<>();
        if (!wardRepository.existsById(bedData.getWardId())){
            return ResponseEntity.ok().body(String.format("Ward with ID: %2d does not exist",bedData.getWardId()));
        }
        Ward ward = wardRepository.getOne(bedData.getWardId());
        int count = bedData.getNumberOfBeds();
        Bed bed = null;
        for(int i=1; i<= count; i++){
            bed = new Bed();
            bed.setWard(ward);
            bed.setIsOccupied(false);
            bed.setIdentifier(null);
            beds.add(bed);

        }
        bedRepository.saveAll(beds);
        this.updateBedsIdentifiers(ward.getId());
        return ResponseEntity.ok().body(String.format("%2d beds were added in ward %2d ", count, bedData.getWardId()));
    }

    private void updateBedsIdentifiers(Long wardId){
        Session session = this.sessionFactory.openSession();
        String queryString = String.format("from %s WHERE ward_id = %2d", DatabaseConstants.BEDS_TABLE, wardId);
        List<Bed> beds = session.createQuery(queryString).list();
        beds.forEach(bed ->{
            Long wd = bed.getWard().getId() == null ? 1: bed.getWard().getId();
            bed.setIdentifier(bed.getId() + "["+wd+"]");
            bedRepository.save(bed);
        });
        session.close();
    }
}
