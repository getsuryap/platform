<<<<<<< Updated upstream:src/main/java/org/ospic/patient/infos/repository/PatientMiscInformationRepository.java
package org.ospic.patient.infos.repository;

import org.ospic.patient.infos.domain.PatientMiscInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
=======
package org.ospic.patient.resource.repository;

import org.ospic.patient.resource.domain.ServiceResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * This file was created by eli on 23/12/2020 for org.ospic.patient.resource.repository
 * --
 * --
>>>>>>> Stashed changes:src/main/java/org/ospic/patient/resource/repository/ServiceResourceJpaRepository.java
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
<<<<<<< Updated upstream:src/main/java/org/ospic/patient/infos/repository/PatientMiscInformationRepository.java
public interface PatientMiscInformationRepository extends JpaRepository<PatientMiscInfo,Long> {
    PatientMiscInfo getById(String id);
=======
public interface ServiceResourceJpaRepository extends JpaRepository<ServiceResource, Long> {
    Optional<List<ServiceResource>> findByIsActiveTrue();
    Optional<List<ServiceResource>> findByIsActiveFalse();
    List<ServiceResource> findByPatientId(Long patientId);
    boolean existsByPatientIdAndIsActiveTrue(Long patientId);
    List<ServiceResource> findByPatientIdAndIsActiveTrue(Long patientId);
    List<ServiceResource> findByPatientIdAndIsActiveFalse(Long patientId);
>>>>>>> Stashed changes:src/main/java/org/ospic/patient/resource/repository/ServiceResourceJpaRepository.java
}
