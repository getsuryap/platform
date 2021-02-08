package org.ospic.platform.accounting.transactions.service;

import org.ospic.platform.accounting.transactions.data.TransactionPayload;
import org.ospic.platform.accounting.transactions.domain.Transactions;
import org.ospic.platform.accounting.transactions.exceptions.TransactionNotFoundExceptionPlatform;
import org.ospic.platform.accounting.transactions.repository.TransactionJpaRepository;
import org.ospic.platform.organization.departments.domain.Department;
import org.ospic.platform.organization.departments.exceptions.DepartmentNotFoundExceptionsPlatform;
import org.ospic.platform.organization.departments.repository.DepartmentJpaRepository;
import org.ospic.platform.organization.medicalservices.domain.MedicalService;
import org.ospic.platform.organization.medicalservices.exceptions.MedicalServiceNotFoundExceptionPlatform;
import org.ospic.platform.organization.medicalservices.repository.MedicalServiceJpaRepository;
import org.ospic.platform.organization.medicalservices.services.MedicalServiceWritePrincipleService;
import org.ospic.platform.patient.consultation.domain.ConsultationResource;
import org.ospic.platform.patient.consultation.exception.ConsultationNotFoundExceptionPlatform;
import org.ospic.platform.patient.consultation.repository.ConsultationResourceJpaRepository;
import org.ospic.platform.security.authentication.users.domain.User;
import org.ospic.platform.security.authentication.users.repository.UserRepository;
import org.ospic.platform.security.services.UserDetailsImpl;
import org.ospic.platform.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @Override
    public ResponseEntity<?> createTransaction(Long id, List<Long> services) {
        ConsultationResource consultation = consultationResourceRepository.findById(id)
                .orElseThrow(() -> new ConsultationNotFoundExceptionPlatform(id));


        final LocalDateTime transactionDate = new DateUtil().convertToLocalDateTimeViaInstant(new Date());
        UserDetailsImpl ud = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(ud.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User with is " + ud.getId() + " is not found"));


        Department department = (Department) user.getStaff().getDepartment();
        if (department == null) {
            throw new DepartmentNotFoundExceptionsPlatform(user.getId(), null);
        }
        List<Transactions> trxns = new ArrayList<>();
        services.forEach(servicesId -> {
            Optional<MedicalService> serviceOptional = medicalServiceRepository.findById(servicesId);
            if (serviceOptional.isPresent()) {
                MedicalService service = serviceOptional.get();
                Transactions trx = new Transactions();
                trx.setDepartment(department);
                trx.setTransactionDate(transactionDate);
                trx.setConsultation(consultation);
                trx.setIsReversed(false);
                trx.setAmount(service.getPrice());
                trx.setCurrencyCode("USD");
                trx.setMedicalService(service);
                trx.setAmount(service.getPrice());
                repository.save(trx);
                trxns.add(trx);
            }
        });
        return ResponseEntity.ok().body(new Object[]{id, trxns});
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
        }).orElseThrow(() -> new TransactionNotFoundExceptionPlatform(id));
    }
}
