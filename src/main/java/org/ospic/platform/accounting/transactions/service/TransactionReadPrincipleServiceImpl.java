package org.ospic.platform.accounting.transactions.service;

import org.ospic.platform.accounting.transactions.data.TransactionResponse;
import org.ospic.platform.accounting.transactions.data.TransactionRowMap;
import org.ospic.platform.accounting.transactions.repository.TransactionJpaRepository;
import org.ospic.platform.accounting.transactions.service.mapper.TransactionDataRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

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
public class TransactionReadPrincipleServiceImpl implements TransactionReadPrincipleService {
    @Autowired
    TransactionJpaRepository repository;
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public TransactionReadPrincipleServiceImpl(
            TransactionJpaRepository repository, final DataSource dataSource) {
        this.repository = repository;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public ResponseEntity<?> readTransactions() {
        final TransactionDataRowMapper rm = new TransactionDataRowMapper();
        final String sql = "select " + rm.schema() + "  order by tr.id DESC ";
        List <TransactionRowMap> transactions =  this.jdbcTemplate.query(sql, rm, new Object[]{});
        return ResponseEntity.ok().body(new TransactionResponse().transactionResponse(transactions));
    }

    @Override
    public ResponseEntity<?> readReversedTransactions() {
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
    public ResponseEntity<TransactionResponse> readTransactionsByConsultationId(Long id) {
        final TransactionDataRowMapper rm = new TransactionDataRowMapper();
        final String sql = "select " + rm.schema() + " where co.id = ?  order by tr.id DESC ";
        List <TransactionRowMap> transactions =  this.jdbcTemplate.query(sql, rm, id);
        return ResponseEntity.ok().body(new TransactionResponse().transactionResponse(transactions));
    }

    @Override
    public ResponseEntity<?> readTransactionsByConsultationIdAndReversed(Long id) {
        final TransactionDataRowMapper rm = new TransactionDataRowMapper();
        final String sql = "select " + rm.schema() + " where co.id = ? and tr.is_reversed = true order by tr.id DESC ";
        List <TransactionRowMap> transactions =  this.jdbcTemplate.query(sql, rm, new Object[]{id});
        return ResponseEntity.ok().body(new TransactionResponse().transactionResponse(transactions));
    }

    @Override
    public ResponseEntity<?> readTransactionsByConsultationIdAndNotReversed(Long id) {
        final TransactionDataRowMapper rm = new TransactionDataRowMapper();
        final String sql = "select " + rm.schema() + " where co.id = ? and tr.is_reversed = false  order by tr.id DESC ";
        List <TransactionRowMap> transactions =  this.jdbcTemplate.query(sql, rm, new Object[]{id});
        return ResponseEntity.ok().body(new TransactionResponse().transactionResponse(transactions));
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

    @Override
    public ResponseEntity<?> readTransactionsByDateRange(String fromDate, String toDate) {
        final TransactionDataRowMapper rm = new TransactionDataRowMapper();
        final String sql = "select " + rm.schema() + " where tr.transaction_date between ? and  ?  order by tr.transaction_date desc";
        List <TransactionRowMap> transactions =  this.jdbcTemplate.query(sql, rm, new Object[]{fromDate, toDate});
        return ResponseEntity.ok().body(new TransactionResponse().transactionResponse(transactions));
    }

}
