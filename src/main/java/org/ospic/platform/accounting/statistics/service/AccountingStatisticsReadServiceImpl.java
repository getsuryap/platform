package org.ospic.platform.accounting.statistics.service;

import org.ospic.platform.accounting.bills.repository.BillsJpaRepository;
import org.ospic.platform.accounting.statistics.data.BillSummations;
import org.ospic.platform.accounting.statistics.data.BillsPerDay;
import org.ospic.platform.accounting.statistics.data.TransactionSummations;
import org.ospic.platform.accounting.statistics.data.TransactionsPerDay;
import org.ospic.platform.accounting.statistics.rowmap.BillSummationsRowMap;
import org.ospic.platform.accounting.statistics.rowmap.BillsPerDayRowMap;
import org.ospic.platform.accounting.statistics.rowmap.TransactionSummationsRowMap;
import org.ospic.platform.accounting.statistics.rowmap.TransactionsPerDayRowMap;
import org.ospic.platform.accounting.transactions.repository.TransactionJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

/**
 * This file was created by eli on 27/03/2021 for org.ospic.platform.accounting.statistics.service
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
public class AccountingStatisticsReadServiceImpl implements AccountingStatisticsReadService {
   private final TransactionJpaRepository transactionJpaRepository;
   private final BillsJpaRepository billsJpaRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AccountingStatisticsReadServiceImpl(BillsJpaRepository billsJpaRepository,
            TransactionJpaRepository transactionJpaRepository, final DataSource dataSource) {
        this.transactionJpaRepository = transactionJpaRepository;
        this.billsJpaRepository = billsJpaRepository;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public ResponseEntity<?> readBillsPerDayAccountingStatistics() {
        final BillsPerDayRowMap rm = new BillsPerDayRowMap();
        final String sql = rm.schema();
        List<BillsPerDay> bills=  this.jdbcTemplate.query(sql, rm, new Object[]{});
        return ResponseEntity.ok().body(bills);
    }

    @Override
    public ResponseEntity<?> readBillSummationAccountingStatistics() {
        final BillSummationsRowMap rm = new BillSummationsRowMap();
        final String sql = rm.schema();
        List<BillSummations> billSummations = this.jdbcTemplate.query(sql, rm, new Object[]{});
        return ResponseEntity.ok().body(billSummations.get(0));
    }

    @Override
    public ResponseEntity<?> readTransactionsPerDayAccountingStatistics() {
        final TransactionsPerDayRowMap rm = new TransactionsPerDayRowMap();
        final String sql = rm.schema();
        List<TransactionsPerDay> transactionsPerDayList = this.jdbcTemplate.query(sql, rm, new Object[]{});
        return ResponseEntity.ok().body(transactionsPerDayList);
    }

    @Override
    public ResponseEntity<?> readTransactionSummationAccountingStatistics() {
        final TransactionSummationsRowMap rm = new TransactionSummationsRowMap();
        final String sql = rm.schema();
        List<TransactionSummations> transactionSummations = this.jdbcTemplate.query(sql, rm, new Object[]{});
        return ResponseEntity.ok().body(transactionSummations.get(0));
    }
}
