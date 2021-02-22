package org.ospic.platform.accounting.bills.service;

import org.ospic.platform.accounting.bills.data.PaymentPayload;
import org.ospic.platform.accounting.bills.exceptions.BillNotFoundException;
import org.ospic.platform.accounting.bills.exceptions.InsufficientBillPaymentAmountException;
import org.ospic.platform.accounting.bills.repository.BillsJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

/**
 * This file was created by eli on 18/02/2021 for org.ospic.platform.accounting.bills.service
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
public class BillWritePrincipleServiceImpl implements BillWritePrincipleService {
    private final BillsJpaRepository repository;

    @Autowired
    public BillWritePrincipleServiceImpl(BillsJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public ResponseEntity<?> payBill(PaymentPayload payload) {
        return repository.findById(payload.getConsultationId()).map(bill -> {
            if (payload.getAmount().compareTo(bill.getTotalAmount())>0){
                throw  new InsufficientBillPaymentAmountException(payload.getConsultationId());
            }else  if (payload.getAmount().compareTo(bill.getTotalAmount())<0){
                throw  new InsufficientBillPaymentAmountException(payload.getAmount());
            }
            bill.setPaidAmount(payload.getAmount());
            bill.setIsPaid(true);
            return ResponseEntity.ok().body(this.repository.save(bill));

        }).orElseThrow(()->new BillNotFoundException(payload.getConsultationId()));
    }
}
