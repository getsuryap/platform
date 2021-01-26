package org.ospic.platform.inventory.pharmacy.categories.repository;

import org.ospic.platform.inventory.pharmacy.categories.domains.MedicineCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * This file was created by eli on 12/11/2020 for org.ospic.platform.inventory.pharmacy.groups.repository
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
public interface MedicineCategoryRepository extends JpaRepository<MedicineCategory, Long> {
    @Override
    Optional<MedicineCategory> findById(Long aLong);

    @Override
    List<MedicineCategory> findAll();

    boolean existsByName(String name);
    boolean existsById(Long id);

}
