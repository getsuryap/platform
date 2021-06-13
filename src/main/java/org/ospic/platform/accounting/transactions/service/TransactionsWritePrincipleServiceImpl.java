package org.ospic.platform.accounting.transactions.service;

import org.ospic.platform.accounting.bills.domain.Bill;
import org.ospic.platform.accounting.bills.exceptions.BillNotFoundException;
import org.ospic.platform.accounting.bills.repository.BillsJpaRepository;
import org.ospic.platform.accounting.bills.service.BillReadPrincipleService;
import org.ospic.platform.accounting.transactions.data.TransactionPayload;
import org.ospic.platform.accounting.transactions.data.TransactionRequest;
import org.ospic.platform.accounting.transactions.domain.Transactions;
import org.ospic.platform.accounting.transactions.exceptions.PaidBillTransactionException;
import org.ospic.platform.accounting.transactions.exceptions.TransactionNotFoundExceptionPlatform;
import org.ospic.platform.accounting.transactions.repository.TransactionJpaRepository;
import org.ospic.platform.inventory.pharmacy.medicine.exceptions.MedicineNotFoundExceptions;
import org.ospic.platform.inventory.pharmacy.medicine.repository.MedicineRepository;
import org.ospic.platform.organization.authentication.users.domain.User;
import org.ospic.platform.organization.authentication.users.exceptions.InsufficientRoleException;
import org.ospic.platform.organization.authentication.users.repository.UserJpaRepository;
import org.ospic.platform.organization.departments.domain.Department;
import org.ospic.platform.organization.departments.repository.DepartmentJpaRepository;
import org.ospic.platform.organization.medicalservices.exceptions.MedicalServiceNotFoundExceptionPlatform;
import org.ospic.platform.organization.medicalservices.repository.MedicalServiceJpaRepository;
import org.ospic.platform.patient.consultation.domain.ConsultationResource;
import org.ospic.platform.patient.consultation.exception.ConsultationNotFoundExceptionPlatform;
import org.ospic.platform.patient.consultation.exception.InactiveMedicalConsultationsException;
import org.ospic.platform.patient.consultation.repository.ConsultationResourceJpaRepository;
import org.ospic.platform.security.services.UserDetailsImpl;
import org.ospic.platform.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

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
	@Value("${application.default.currency}")
	private String applicationDefaultCurrency;
	
	private static final Logger logger = LoggerFactory.getLogger(TransactionsWritePrincipleServiceImpl.class);
    private final TransactionJpaRepository repository;
    private final MedicalServiceJpaRepository medicalServiceRepository;
    private final ConsultationResourceJpaRepository consultationResourceRepository;
    private final DepartmentJpaRepository departmentRepository;
    private final MedicineRepository medicineRepository;
    private final UserJpaRepository userJpaRepository;
    private final BillsJpaRepository billsJpaRepository;
    private final BillReadPrincipleService billReadPrincipleService;

    @Autowired
    public TransactionsWritePrincipleServiceImpl(
            TransactionJpaRepository repository,
            MedicalServiceJpaRepository medicalServiceRepository,
            ConsultationResourceJpaRepository consultationResourceRepository,
            DepartmentJpaRepository departmentRepository,
            MedicineRepository medicineRepository, UserJpaRepository userJpaRepository,
            BillsJpaRepository billsJpaRepository, BillReadPrincipleService billReadPrincipleService) {
        this.repository = repository;
        this.departmentRepository = departmentRepository;
        this.consultationResourceRepository = consultationResourceRepository;
        this.medicalServiceRepository = medicalServiceRepository;
        this.medicineRepository = medicineRepository;
        this.userJpaRepository = userJpaRepository;
        this.billsJpaRepository = billsJpaRepository;
        this.billReadPrincipleService = billReadPrincipleService;
    }

    @Override
    @Transactional
    public ResponseEntity<?> initiateMedicalTransaction(Long consultationId, TransactionRequest payload) {
        final String type = payload.getType();
        if (type.equals("medicine")) {
            return this.createMedicineServiceTransaction(consultationId, payload);
        }
        if (type.equals("service")) {
            return this.createMedicalServiceTransaction(consultationId, payload);
        } else return null;

    }


    @Transactional
    private ResponseEntity<?> createMedicalServiceTransaction(Long consultationId, TransactionRequest payload) {
        return this.consultationResourceRepository.findById(consultationId).map(consultation -> {
            return this.medicalServiceRepository.findById(payload.getId()).map(service -> {

                if (!consultation.getIsActive()) {
                    throw new InactiveMedicalConsultationsException(consultation.getId());
                }

                final LocalDateTime transactionDate = new DateUtil().convertToLocalDateTimeViaInstant(new Date());
                UserDetailsImpl ud = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                User user = userJpaRepository.findById(ud.getId()).orElseThrow(() -> new UsernameNotFoundException("User with is " + ud.getId() + " is not found"));

                if (!user.getIsStaff()) {
                    String message = "Insufficient role to perform this operation";
                    throw new InsufficientRoleException(user.getId(), message);
                } else if (user.getIsStaff() && user.getStaff().getDepartment() == null) {
                    throw new InsufficientRoleException(user.getId(), "You are not member of any department");
                }

                Department department = (Department) user.getStaff().getDepartment();
                if (consultation.getBill() == null) {
                    createServiceBillIfNotExists(consultation);
                }
                return this.billsJpaRepository.findByConsultationId(consultationId).map(b -> {
                    Bill bill = consultation.getBill();
                    Transactions trx = new Transactions();
                    trx.setDepartment(department);
                    trx.setTransactionDate(transactionDate);
                    trx.setIsReversed(false);
                    trx.setAmount(service.getPrice());
                    trx.setCurrencyCode(applicationDefaultCurrency);
                    trx.setMedicine(null);
                    trx.setMedicalService(service);
                    trx.setBill(b);
                    return ResponseEntity.ok().body(this.repository.save(trx));
                }).orElseThrow(() -> new BillNotFoundException(consultation.getBill().getId()));
            }).orElseThrow(() -> new ConsultationNotFoundExceptionPlatform(consultationId));
        }).orElseThrow(() -> new MedicalServiceNotFoundExceptionPlatform(payload.getId()));
    }


    @Transactional
    private ResponseEntity<?> createMedicineServiceTransaction(Long consultationId, TransactionRequest payload) {
        return this.medicineRepository.findById(payload.getId()).map(medicine -> {
            return  this.consultationResourceRepository.findById(consultationId).map(consultation -> {

                final LocalDateTime transactionDate = new DateUtil().convertToLocalDateTimeViaInstant(new Date());
                UserDetailsImpl ud = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                User user = userJpaRepository.findById(ud.getId()).orElseThrow(() -> new UsernameNotFoundException(String.format("User with ID: %s is not found/exist", ud.getId().toString())));

                if (!consultation.getIsActive()) { throw new InactiveMedicalConsultationsException(consultation.getId()); }

                if (medicine.getQuantity() < payload.getQuantity()) { throw new MedicineNotFoundExceptions(""); }

                if (!user.getIsStaff()) { throw new InsufficientRoleException(user.getId(),  "Insufficient role to perform this operation"); }
                if (user.getIsStaff() && user.getStaff().getDepartment() == null) { throw new InsufficientRoleException(user.getId(), "You are no member of any department"); }

                Department department = (Department) user.getStaff().getDepartment();

                if (consultation.getBill() == null) { createServiceBillIfNotExists(consultation); }

                return this.billsJpaRepository.findByConsultationId(consultationId).map(bill -> {
                    Transactions trx = new Transactions();
                    trx.setDepartment(department);
                    trx.setTransactionDate(transactionDate);
                    trx.setIsReversed(false);
                    final BigDecimal amount = medicine.getSellingPrice().multiply(BigDecimal.valueOf(payload.getQuantity()));
                    trx.setAmount(amount);
                    trx.setCurrencyCode(applicationDefaultCurrency);
                    trx.setMedicalService(null);
                    trx.setMedicine(medicine);
                    trx.setBill(bill);
                    medicine.setQuantity(medicine.getQuantity() - payload.getQuantity());
                    this.medicineRepository.save(medicine);
                    return ResponseEntity.ok().body(this.repository.save(trx));
                }).orElseThrow(() -> new BillNotFoundException(consultation.getBill().getId()));
            }).orElseThrow(() -> new ConsultationNotFoundExceptionPlatform(consultationId));
        }).orElseThrow(() -> new MedicineNotFoundExceptions(payload.getId()));
    }

    @Override
    public ResponseEntity<?> updateTransaction(Long id, TransactionPayload payload) {
        return null;
    }

    @Override
    @Transactional
    public ResponseEntity<?> undoTransaction(Long id) {
        return this.repository.findById(id).map(trx -> {
            if (trx.getBill().getIsPaid()) {
                throw new PaidBillTransactionException(trx.getBill().getId());
            }
            trx.setIsReversed(!trx.getIsReversed());
            return ResponseEntity.ok().body(this.repository.save(trx));
        }).orElseThrow(() -> new TransactionNotFoundExceptionPlatform(id));
    }

    private void createServiceBillIfNotExists(ConsultationResource consultation) {
        Bill bill = new Bill();
        bill.setIsPaid(false);
        bill.setExtraId(String.format("PT%2d-C0%2d", consultation.getPatient().getId(), consultation.getId()).replaceAll("\\s+", ""));
        consultation.setBill(bill);
        bill.setConsultation(consultation);
        consultationResourceRepository.save(consultation);
    }
}
