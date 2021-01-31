package org.ospic.platform.patient.consultation.repository;

import org.ospic.platform.patient.consultation.domain.ConsultationResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This file was created by eli on 23/12/2020 for org.ospic.platform.patient.consultation.repository
 * --
 * --
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
public interface ConsultationResourceJpaRepository extends JpaRepository<ConsultationResource, Long> {
    List<ConsultationResource> findByIsActiveFalse();
    List<ConsultationResource> findByIsActiveTrue();
    List<ConsultationResource> findByPatientId(Long patientId);
    List<ConsultationResource> findByPatientIdAndIsActiveFalse(Long patientId);
    List<ConsultationResource> findByPatientIdAndIsActiveTrue(Long patientId);
    boolean existsByPatientIdAndIsActiveTrue(Long patientId);
}
