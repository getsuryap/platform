package org.ospic.platform.accounting.transactions.service;

import org.ospic.platform.accounting.transactions.data.TransactionPayload;
import org.ospic.platform.accounting.transactions.domain.Transactions;
import org.ospic.platform.accounting.transactions.exceptions.TransactionNotFoundException;
import org.ospic.platform.accounting.transactions.repository.TransactionJpaRepository;
import org.ospic.platform.organization.departments.exceptions.DepartmentNotFoundExceptions;
import org.ospic.platform.organization.departments.repository.DepartmentJpaRepository;
import org.ospic.platform.organization.medicalservices.exceptions.MedicalServiceNotFoundException;
import org.ospic.platform.organization.medicalservices.repository.MedicalServiceJpaRepository;
import org.ospic.platform.patient.consultation.exception.ConsultationNotFoundException;
import org.ospic.platform.patient.consultation.repository.ConsultationResourceJpaRepository;
import org.ospic.platform.security.authentication.users.domain.User;
import org.ospic.platform.security.authentication.users.repository.UserRepository;
import org.ospic.platform.security.services.UserDetailsImpl;
import org.ospic.platform.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

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
    @Autowired
    MedicalServiceJpaRepository medicalServiceRepository;
    @Autowired
    ConsultationResourceJpaRepository consultationResourceRepository;
    @Autowired
    DepartmentJpaRepository departmentRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    public TransactionsWritePrincipleServiceImpl(
            TransactionJpaRepository repository,
            MedicalServiceJpaRepository medicalServiceRepository,
            ConsultationResourceJpaRepository consultationResourceRepository,
            DepartmentJpaRepository departmentRepository) {
        this.repository = repository;
        this.departmentRepository = departmentRepository;
        this.consultationResourceRepository = consultationResourceRepository;
        this.medicalServiceRepository = medicalServiceRepository;
    }

    @Transactional
    @Override
    public ResponseEntity<?> createTransaction(TransactionPayload payload) {
        final LocalDateTime transactionDate = new DateUtil().convertToLocalDateTimeViaInstant(new Date());
        UserDetailsImpl ud = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optional = userRepository.findById(ud.getId());
        payload.getMedicalServices().forEach(serviceId->{
            medicalServiceRepository.findById(serviceId).map(service ->
                    consultationResourceRepository.findById(payload.getConsultationId()).map(consultation -> {

                        Transactions trx = new Transactions().fromTransactionPayload(payload, service);
                        optional.ifPresent(user -> {
                            trx.setDepartment(user.getStaff().getDepartment());
                        });
                        trx.setTransactionDate(transactionDate);
                        trx.setConsultation(consultation);
                        trx.setIsReversed(false);
                        trx.setCurrencyCode("USD");
                        trx.setMedicalService(service);
                        trx.setAmount(service.getPrice());

                        return ResponseEntity.ok().body(repository.save(trx));
                    }).orElseThrow(() -> new ConsultationNotFoundException(payload.getConsultationId())))
                    .orElseThrow(() -> new MedicalServiceNotFoundException(serviceId));
        });
        return ResponseEntity.ok().body("Service added");
    }

    @Override
    public ResponseEntity<?> updateTransaction(Long id, TransactionPayload payload) {
        return null;
    }

    @Override
    public ResponseEntity<?> undoTransaction(Long id) {
        return repository.findById(id).map(trx -> {
            trx.setIsReversed(true);
            repository.save(trx);
            return ResponseEntity.ok().body("Transaction reversed");
        }).orElseThrow(() -> new TransactionNotFoundException(id));
    }
}
