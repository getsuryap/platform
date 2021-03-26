package org.ospic.platform.accounting.statistics.data;

import lombok.*;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This file was created by eli on 26/03/2021 for org.ospic.platform.accounting.statistics.data
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
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSummations {
    private Long totalNumberOfTransactions;
    private Long totalNumberOfTransactionsToday;
    private BigDecimal totalTransactionAmount;
    private BigDecimal totalTransactionAmountToday;
    private BigDecimal totalNumberOfTransactionsLast7Days;
    private BigDecimal totalTransactionAmountLast7Days;
    private BigDecimal totalNumberOfTransactionsLast30Days;
    private BigDecimal totalTransactionAmountLast30Days;


    public static TransactionSummations fromResultSet(ResultSet rs) throws SQLException{
        final Long totalNumberOfTransactions = rs.getLong("totalNumberOfTransactions");
        final Long totalNumberOfTransactionsToday = rs.getLong("totalNumberOfTransactionsToday");
        final BigDecimal totalTransactionAmount = rs.getBigDecimal("totalTransactionAmount");
        final BigDecimal totalTransactionAmountToday = rs.getBigDecimal("totalTransactionAmountToday");
        final BigDecimal totalNumberOfTransactionsLast7Days = rs.getBigDecimal("totalNumberOfTransactionsLast7Days");
        final BigDecimal totalTransactionAmountLast7Days =rs.getBigDecimal("totalTransactionAmountLast7Days");
        final BigDecimal totalNumberOfTransactionsLast30Days = rs.getBigDecimal("totalNumberOfTransactionsLast30Days");
        final BigDecimal totalTransactionAmountLast30Days = rs.getBigDecimal("totalTransactionAmountLast30Days");
        return new TransactionSummations(totalNumberOfTransactions,totalNumberOfTransactionsToday,totalTransactionAmount,totalTransactionAmountToday,
                totalNumberOfTransactionsLast7Days,totalTransactionAmountLast7Days,totalNumberOfTransactionsLast30Days,totalTransactionAmountLast30Days);
    }
}
