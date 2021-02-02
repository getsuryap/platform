package org.ospic.platform.accounting.transactions.service;

import org.hibernate.Transaction;
import org.ospic.platform.accounting.transactions.data.TransactionPayload;
import org.ospic.platform.accounting.transactions.domain.Transactions;
import org.ospic.platform.accounting.transactions.repository.TransactionJpaRepository;
import org.ospic.platform.organization.departments.exceptions.DepartmentNotFoundExceptions;
import org.ospic.platform.organization.departments.repository.DepartmentJpaRepository;
import org.ospic.platform.organization.medicalservices.exceptions.MedicalServiceNotFoundException;
import org.ospic.platform.organization.medicalservices.repository.MedicalServiceJpaRepository;
import org.ospic.platform.patient.consultation.exception.ConsultationNotFoundException;
import org.ospic.platform.patient.consultation.repository.ConsultationResourceJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

/**
 * This file was created by eli on 03/02/2021 for org.ospic.platform.accounting.transactions.service
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
public class TransactionsWritePrincipleServiceImpl implements TransactionsWritePrincipleService {
    @Autowired
    TransactionJpaRepository repository;
    MedicalServiceJpaRepository medicalServiceRepository;
    ConsultationResourceJpaRepository consultationResourceRepository;
    DepartmentJpaRepository departmentRepository;

    @Autowired
    public void TransactionWritePrincipleServiceImpl(
            TransactionJpaRepository repository,
            MedicalServiceJpaRepository medicalServiceRepository,
            ConsultationResourceJpaRepository consultationResourceRepository,
            DepartmentJpaRepository departmentRepository) {
        this.repository = repository;
        this.departmentRepository = departmentRepository;
        this.consultationResourceRepository = consultationResourceRepository;
        this.medicalServiceRepository = medicalServiceRepository;
    }

    @Override
    public ResponseEntity<?> createTransaction(TransactionPayload payload) {
        Transactions transactions = new Transactions().fromTransactionPayload(payload);
        return departmentRepository.findById(payload.getDepartmentId()).map(department -> {
            return medicalServiceRepository.findById(payload.getMedicalServiceId()).map(service -> {
                return consultationResourceRepository.findById(payload.getConsultationId()).map(consultation -> {
                    transactions.setConsultation(consultation);
                    transactions.setDepartment(department);
                    transactions.setMedicalService(service);
                    Transactions res = repository.save(transactions);
                    return ResponseEntity.ok().body(res);
                }).orElseThrow(() -> new ConsultationNotFoundException(payload.getConsultationId()));
            }).orElseThrow(() -> new MedicalServiceNotFoundException(payload.getMedicalServiceId()));
        }).orElseThrow(() -> new DepartmentNotFoundExceptions(payload.getDepartmentId()));
    }

    @Override
    public ResponseEntity<?> updateTransaction(Long id, TransactionPayload payload) {
        return null;
    }

    @Override
    public ResponseEntity<?> undoTransaction(Long id) {
        return null;
    }
}
