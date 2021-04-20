package org.ospic.platform.inventory.pharmacy.medicine.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.ospic.platform.inventory.pharmacy.categories.exception.MedicineCategoryNotFoundException;
import org.ospic.platform.inventory.pharmacy.categories.repository.MedicineCategoryRepository;
import org.ospic.platform.inventory.pharmacy.groups.exception.MedicineGroupNotFoundExceptionPlatform;
import org.ospic.platform.inventory.pharmacy.groups.repository.MedicineGroupRepository;
import org.ospic.platform.inventory.pharmacy.medicine.data.MedicineRequest;
import org.ospic.platform.inventory.pharmacy.medicine.domains.Medicine;
import org.ospic.platform.inventory.pharmacy.medicine.repository.MedicineRepository;
import org.ospic.platform.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * This file was created by eli on 12/11/2020 for org.ospic.platform.inventory.pharmacy.medicine.service
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
public class MedicineWriteServiceImpl implements MedicineWriteService {
    private final Logger logger = LoggerFactory.getLogger(MedicineWriteServiceImpl.class);
    private final MedicineCategoryRepository medicineCategoryRepository;
    private final MedicineGroupRepository medicineGroupRepository;
    private final MedicineRepository medicineRepository;
    @Autowired
    SessionFactory sessionFactory;

    public MedicineWriteServiceImpl(MedicineRepository medicineRepository, MedicineGroupRepository medicineGroupRepository,
                                    MedicineCategoryRepository medicineCategoryRepository) {
        this.medicineCategoryRepository = medicineCategoryRepository;
        this.medicineGroupRepository = medicineGroupRepository;
        this.medicineRepository = medicineRepository;

    }

    @Override
    public ResponseEntity<?> createNewMedicineProduct(MedicineRequest payload) {
        return this.medicineGroupRepository.findById(payload.getGroup()).map(group -> {
            return this.medicineCategoryRepository.findById(payload.getCategory()).map(category -> {

                final LocalDateTime expireDateTime = new DateUtil().convertToLocalDateTimeViaInstant(payload.getExpireDateTime());

                Medicine medicine = new Medicine().fromJson(payload);
                medicine.setExpireDateTime(expireDateTime);
                medicine.setCategory(category);
                medicine.setGroup(group);
                group.getMedicines().add(medicine);
                return ResponseEntity.ok().body(this.medicineRepository.save(medicine));
            }).orElseThrow(() -> new MedicineCategoryNotFoundException(payload.getGroup()));
        }).orElseThrow(() -> new MedicineGroupNotFoundExceptionPlatform(payload.getGroup()));

    }

    @Override
    public Medicine updateMedicineProduct(Long medicationId, MedicineRequest req) {
        return this.medicineGroupRepository.findById(req.getGroup()).map(group -> {
            return this.medicineCategoryRepository.findById(req.getCategory()).map(category -> {
                Session session = this.sessionFactory.openSession();
                Transaction transaction = session.beginTransaction();
                final LocalDateTime expireDateTime = new DateUtil().convertToLocalDateTimeViaInstant(req.getExpireDateTime());

                Medicine medicine = (Medicine) session.load(Medicine.class, medicationId);
                medicine.setName(req.getName());
                medicine.setCompany(req.getCompany());
                medicine.setUnit(req.getUnits());
                medicine.setCompositions(req.getCompositions());
                medicine.setQuantity(req.getQuantity());
                medicine.setEffects(req.getEffects());
                medicine.setExpireDateTime(expireDateTime);
                medicine.setBuyingPrice(req.getBuyingPrice());
                medicine.setSellingPrice(req.getSellingPrice());
                medicine.setStoreBox(req.getStoreBox());

                medicine.setCategory(category);
                medicine.setGroup(group);
                session.persist(medicine);

                transaction.commit();
                session.close();
                return medicine;
            }).orElseThrow(() -> new MedicineCategoryNotFoundException(req.getGroup()));
        }).orElseThrow(() -> new MedicineGroupNotFoundExceptionPlatform(req.getGroup()));
    }
}
