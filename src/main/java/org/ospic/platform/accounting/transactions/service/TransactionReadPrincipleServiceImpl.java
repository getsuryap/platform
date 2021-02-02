package org.ospic.platform.accounting.transactions.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

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
public class TransactionReadPrincipleServiceImpl implements TransactionReadPrincipleService{
    @Override
    public ResponseEntity<?> readTransactions() {
        return null;
    }

    @Override
    public ResponseEntity<?> readTransactionById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<?> readTransactionByDate(LocalDateTime dateTime) {
        return null;
    }

    @Override
    public ResponseEntity<?> readTransactionsByConsultationId(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<?> readTransactionByMedicalServiceId(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<?> readTransactionByMedicalServiceIdAndDate(Long id, LocalDateTime date) {
        return null;
    }

    @Override
    public ResponseEntity<?> readTransactionsByDepartmentId(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<?> readTransactionByDepartmentIdAndDate(Long id, LocalDateTime date) {
        return null;
    }
}