package org.ospic.platform.organization.staffs.repository;

import org.ospic.platform.organization.staffs.domains.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

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
public interface StaffsRepository extends JpaRepository<Staff, Long> {
    Boolean existsByFullName(String fullName);
    Boolean existsByUsername(String username);
    Staff getById(Long id);
    Collection<Staff> findByDepartmentId(Long departmentId);
    List<Staff> findByIsActiveTrue();
    List<Staff> findByIsActiveFalse();
    List<Staff> findByIsAvailableTrue();
    List<Staff> findByIsAvailableFalse();
    List<Staff> findByIsActiveTrueAndIsAvailableFalse();
    List<Staff> findByIsAvailableTrueAndIsActiveTrue();

}
