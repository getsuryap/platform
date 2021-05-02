package org.ospic.platform.accounting.transactions.service;

import org.ospic.platform.accounting.transactions.data.TransactionResponse;
import org.ospic.platform.accounting.transactions.data.TransactionRowMap;
import org.ospic.platform.accounting.transactions.repository.TransactionJpaRepository;
import org.ospic.platform.accounting.transactions.service.mapper.TransactionDataRowMapper;
import org.ospic.platform.domain.PageableResponse;
import org.ospic.platform.patient.consultation.exception.ConsultationNotFoundExceptionPlatform;
import org.ospic.platform.patient.consultation.repository.ConsultationResourceJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collection;
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
   private final TransactionJpaRepository repository;
    private final ConsultationResourceJpaRepository consultationResourceJpaRepository;
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public TransactionReadPrincipleServiceImpl(
            ConsultationResourceJpaRepository consultationResourceJpaRepository,
            TransactionJpaRepository repository, final DataSource dataSource) {
        this.repository = repository;
        this.consultationResourceJpaRepository = consultationResourceJpaRepository;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public  Collection<TransactionRowMap> readTransactions() {
        final TransactionDataRowMapper rm = new TransactionDataRowMapper();
        final String sql = "select " + rm.schema() + "  order by tr.id DESC ";
        List <TransactionRowMap> transactions =  this.jdbcTemplate.query(sql, rm, new Object[]{});
        return transactions;
    }

    @Override
    public ResponseEntity<?> readPageableTransaction(Pageable page) {
        long total = this.repository.count();
        final TransactionDataRowMapper rm = new TransactionDataRowMapper();
        Sort.Order order = !page.getSort().isEmpty() ? page.getSort().toList().get(0) : Sort.Order.by(" tr.id ");
        final String sql = "select " + rm.schema() + "  order by "+order.getProperty() + ""+
                order.getDirection().name() + " LIMIT " + page.getPageSize() +" OFFSET "+page.getOffset();
        List<TransactionRowMap> transactions = this.jdbcTemplate.query(sql, rm, new Object[]{});
        return ResponseEntity.ok().body(new PageableResponse().instance(new PageImpl<>(transactions, page, total), TransactionRowMap.class));

    }


    @Override
    public ResponseEntity<?> readTransactionById(Long id) {
        final TransactionDataRowMapper rm = new TransactionDataRowMapper();
        final String sql = "select " + rm.schema() + " where tr.id = ? ";
        List <TransactionRowMap> transactions =  this.jdbcTemplate.query(sql, rm, new Object[]{id});
        return ResponseEntity.ok().body(transactions.get(0));
    }



    @Override
    public List <TransactionRowMap> readTransactionsByDateRange(String fromDate, String toDate) {
        final TransactionDataRowMapper rm = new TransactionDataRowMapper();
        final String sql = "select " + rm.schema() + " where tr.transaction_date between ? and  ?  order by tr.transaction_date desc";
        List <TransactionRowMap> transactions =  this.jdbcTemplate.query(sql, rm, new Object[]{fromDate, toDate});
        return transactions;
    }

    @Override
    public ResponseEntity<?> readTransactionsByBillId(Long billId) {
        final TransactionDataRowMapper rm = new TransactionDataRowMapper();
        final String sql = "select " + rm.schema() + " where bl.consultation_id = ? order by tr.id DESC ";
        List <TransactionRowMap> transactions =  this.jdbcTemplate.query(sql, rm, new Object[]{billId});
        return ResponseEntity.ok().body(new TransactionResponse().transactionResponse(transactions));
    }

    @Override
    public ResponseEntity<?> readTransactionsByConsultationId(Long consultationId) {
        return this.consultationResourceJpaRepository.findById(consultationId).map(consultation ->{
            return this.readTransactionsByBillId(consultation.getBill().getId());
        }).orElseThrow(()-> new ConsultationNotFoundExceptionPlatform(consultationId));
    }
}
