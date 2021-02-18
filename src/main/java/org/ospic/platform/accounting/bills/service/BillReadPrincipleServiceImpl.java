package org.ospic.platform.accounting.bills.service;

import org.ospic.platform.accounting.bills.data.BillPayload;
import org.ospic.platform.accounting.bills.exceptions.BillNotFoundException;
import org.ospic.platform.accounting.bills.repository.BillsJpaRepository;
import org.ospic.platform.accounting.bills.service.mapper.BillsRowMapper;
import org.ospic.platform.accounting.transactions.data.TransactionResponse;
import org.ospic.platform.accounting.transactions.service.TransactionReadPrincipleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

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
public class BillReadPrincipleServiceImpl implements BillReadPrincipleService {
    private final BillsJpaRepository repository;
    private final JdbcTemplate jdbcTemplate;
    private final TransactionReadPrincipleService transactionReadPrincipleService;

    @Autowired
    public BillReadPrincipleServiceImpl(
            final BillsJpaRepository repository, final DataSource dataSource,
            final TransactionReadPrincipleService transactionReadPrincipleService) {
        this.repository = repository;
        this.transactionReadPrincipleService = transactionReadPrincipleService;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public ResponseEntity<?> readAllBills() {
        BillsRowMapper rm = new BillsRowMapper();
        final String sql = rm.schema() + "  order by b.id DESC ";
        List<BillPayload> bills = this.jdbcTemplate.query(sql, rm, new Object[]{});
        return ResponseEntity.ok().body(bills);
    }

    @Override
    public ResponseEntity<?> readBillById(Long id) {
        return this.repository.findById(id).map(b -> {
            BillsRowMapper rm = new BillsRowMapper();
            final String sql = rm.schema() + " where b.id = ? order by b.id DESC ";
            List<BillPayload> bill = this.jdbcTemplate.query(sql, rm, new Object[]{id});
            BillPayload billPayload = bill.get(0);
            ResponseEntity<TransactionResponse> tresponse = this.transactionReadPrincipleService.readTransactionsByConsultationId(billPayload.getConsultationId());
            TransactionResponse tr = tresponse.getBody();

            assert tr != null;
            b.setTotalAmount(tr.getTotalAmount());
            billPayload.setTotalAmount(this.repository.save(b).getTotalAmount());
            billPayload.setTransactionResponse(tr);
            return ResponseEntity.ok().body(billPayload);
        }).orElseThrow(() -> new BillNotFoundException(id));

    }
}
