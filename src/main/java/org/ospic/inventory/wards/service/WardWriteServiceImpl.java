package org.ospic.inventory.wards.service;

import org.hibernate.SessionFactory;
import org.ospic.inventory.beds.repository.BedRepository;
import org.ospic.inventory.wards.domain.Ward;
import org.ospic.inventory.wards.repository.WardRepository;
import org.ospic.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

/**
 * This file was created by eli on 07/11/2020 for org.ospic.inventory.wards.service
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
public class WardWriteServiceImpl implements WardWriteService {

    @Autowired
    SessionFactory sessionFactory;
    @Autowired WardRepository wardRepository;

    public WardWriteServiceImpl(WardRepository wardRepository) {
        this.wardRepository= wardRepository;
    }

    @Override
    public ResponseEntity<String> createNewWard(Ward ward) {
      if(wardRepository.existsByName(ward.getName())){
          return ResponseEntity.badRequest().body(String.format("A ward with name `%s`is Already Exist",ward.getName()));
      }
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(ward);
        entityManager.getTransaction().commit();
        entityManager.close();
        return ResponseEntity.ok().body("Ward Created Successfully");
    }

    @Override
    public ResponseEntity<Ward> editWard(Ward ward) {
        return null;
    }
}
