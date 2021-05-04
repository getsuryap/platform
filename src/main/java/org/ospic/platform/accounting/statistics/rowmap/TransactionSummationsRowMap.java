package org.ospic.platform.accounting.statistics.rowmap;

import org.ospic.platform.accounting.statistics.data.TransactionSummations;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This file was created by eli on 27/03/2021 for org.ospic.platform.accounting.statistics.rowmap
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
public class TransactionSummationsRowMap implements RowMapper<TransactionSummations> {
    public String schema() {
        return "  select count(*) as totalNumberOfTransactions, " +
                "   sum(case when date(tx.transaction_date) =curdate() then 1 else 0 end ) as totalNumberOfTransactionsToday, " +
                "   sum(tx.amount) as totalTransactionAmount, " +
                "   sum(case when date(tx.transaction_date) = curdate()  then tx.amount else 0 end ) as totalTransactionAmountToday, " +
                "   sum(case when date(tx.transaction_date) >= curdate() - interval 6 day  then 1 else 0 end ) as totalNumberOfTransactionsLast7Days,"+
                "   sum(case when date(tx.transaction_date) >= curdate() - interval 6 day  then tx.amount else 0 end ) as totalTransactionAmountLast7Days, " +
                "   sum(case when date(tx.transaction_date) >= curdate() - interval 29 day  then 1 else 0 end ) as totalNumberOfTransactionsLast30Days, " +
                "   sum(case when date(tx.transaction_date) >= curdate() - interval 29 day  then tx.amount else 0 end ) as totalTransactionAmountLast30Days"+
                "   from m_transactions tx where tx.is_reversed is false;";
    }
    @Override
    public TransactionSummations mapRow(ResultSet rs, int rowNum) throws SQLException {
        return TransactionSummations.fromResultSet(rs);
    }
}
