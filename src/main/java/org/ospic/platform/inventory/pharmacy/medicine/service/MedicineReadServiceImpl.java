package org.ospic.platform.inventory.pharmacy.medicine.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.ospic.platform.inventory.pharmacy.categories.domains.MedicineCategory;
import org.ospic.platform.inventory.pharmacy.categories.repository.MedicineCategoryRepository;
import org.ospic.platform.inventory.pharmacy.groups.domains.MedicineGroup;
import org.ospic.platform.inventory.pharmacy.groups.repository.MedicineGroupRepository;
import org.ospic.platform.inventory.pharmacy.medicine.data.MedicineDataTemplate;
import org.ospic.platform.inventory.pharmacy.medicine.domains.Medicine;
import org.ospic.platform.inventory.pharmacy.medicine.repository.MedicineRepository;
import org.ospic.platform.util.constants.DatabaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This file was created by eli on 14/11/2020 for org.ospic.platform.inventory.pharmacy.medicine.service
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
public class MedicineReadServiceImpl implements MedicineReadService {

    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    MedicineRepository medicineRepository;
    @Autowired
    MedicineGroupRepository medicineGroupRepository;
    @Autowired
    MedicineCategoryRepository medicineCategoryRepository;

    @Autowired
    public MedicineReadServiceImpl(MedicineRepository medicineRepository,
                                   MedicineGroupRepository medicineGroupRepository,
                                   MedicineCategoryRepository medicineCategoryRepository){
        this.medicineRepository = medicineRepository;
        this.medicineGroupRepository = medicineGroupRepository;
        this.medicineCategoryRepository = medicineCategoryRepository;
    }

    @Override
    public ResponseEntity<?> retrieveAllMedicines() {
        Session session = this.sessionFactory.openSession();
        List<Medicine> medicines = session.createQuery(String.format("from %s", DatabaseConstants.TABLE_PHARMACY_MEDICINES)).list();
        medicines.forEach(medicine -> {
            if(medicine.getGroup() != null){
                MedicineGroup medicineGroup = medicineGroupRepository.findById(medicine.getGroup().getId()).get();
                medicine.setGroup(medicineGroup);
            }
            if(medicine.getCategory() != null){
                MedicineCategory medicineCategory = medicineCategoryRepository.findById(medicine.getCategory().getId()).get();
                medicine.setCategory(medicineCategory);
            }
        });

        session.close();
        return ResponseEntity.ok().body(medicines);
    }

    @Override
    public MedicineDataTemplate readMedicineDataTemplate() {
        List<MedicineGroup> medicineGroups = medicineGroupRepository.findAll();
        List<MedicineCategory> medicineCategories = medicineCategoryRepository.findAll();

        return new MedicineDataTemplate(medicineCategories, medicineGroups);
    }
}
