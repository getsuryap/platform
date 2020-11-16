package org.ospic.inventory.pharmacy.Medicine.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.ospic.inventory.pharmacy.Categories.domains.MedicineCategory;
import org.ospic.inventory.pharmacy.Categories.repository.MedicineCategoryRepository;
import org.ospic.inventory.pharmacy.Groups.domains.MedicineGroup;
import org.ospic.inventory.pharmacy.Groups.repository.MedicineGroupRepository;
import org.ospic.inventory.pharmacy.Medicine.data.MedicineRequest;
import org.ospic.inventory.pharmacy.Medicine.domains.Medicine;
import org.ospic.inventory.pharmacy.Medicine.repository.MedicineRepository;
import org.ospic.patient.infos.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This file was created by eli on 12/11/2020 for org.ospic.inventory.pharmacy.Medicine.service
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
    @Autowired
    MedicineCategoryRepository medicineCategoryRepository;
    @Autowired
    MedicineGroupRepository medicineGroupRepository;
    @Autowired
    MedicineRepository medicineRepository;
    @Autowired
    SessionFactory sessionFactory;

    public MedicineWriteServiceImpl(
            MedicineRepository medicineRepository,
            MedicineGroupRepository medicineGroupRepository,
            MedicineCategoryRepository medicineCategoryRepository) {
        this.medicineCategoryRepository = medicineCategoryRepository;
        this.medicineGroupRepository = medicineGroupRepository;
        this.medicineRepository = medicineRepository;

    }

    @Override
    public ResponseEntity<String> createNewMedicineProduct(MedicineRequest medicineRequest) {
        if (!medicineGroupRepository.existsById(medicineRequest.getGroup())) {
            return ResponseEntity.badRequest().body("Medicine group not found");
        }
        if (!medicineCategoryRepository.existsById(medicineRequest.getCategory())) {
            return ResponseEntity.badRequest().body("Medicine category not found");
        }
        MedicineCategory category = medicineCategoryRepository.findById(medicineRequest.getCategory()).get();
        MedicineGroup group = medicineGroupRepository.findById(medicineRequest.getGroup()).get();
        Medicine medicine = new Medicine(medicineRequest.getName(), medicineRequest.getCompany(),
                medicineRequest.getCompositions(), medicineRequest.getUnits());
        medicine.setCategory(category);
        medicine.setGroup(group);
        group.getMedicines().add(medicine);
        medicineRepository.save(medicine);
        return ResponseEntity.ok().body("Medicine Saved Successfully");
    }

    @Override
    public Medicine updateMedicineProduct(Long medicationId, MedicineRequest req) {
        if (!medicineGroupRepository.existsById(req.getGroup())) {
            return  null;
        }
        if (!medicineCategoryRepository.existsById(req.getCategory())) {
            return null;
        }
        Session session = this.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Medicine medicine = (Medicine)session.load(Medicine.class , medicationId);
        medicine.setName(req.getName());
        medicine.setCompany(req.getCompany());
        medicine.setUnits(req.getUnits());
        medicine.setCompositions(req.getCompositions());

        MedicineCategory category = medicineCategoryRepository.findById(req.getCategory()).get();
        MedicineGroup group = medicineGroupRepository.findById(req.getGroup()).get();
        medicine.setCategory(category);
        medicine.setGroup(group);
        session.persist(medicine);

        transaction.commit();
        session.close();

        return medicine;
    }
}
