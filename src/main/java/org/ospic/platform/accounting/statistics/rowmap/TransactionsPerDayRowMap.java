package org.ospic.platform.accounting.statistics.rowmap;

import org.ospic.platform.accounting.statistics.data.TransactionsPerDay;
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
public class TransactionsPerDayRowMap implements RowMapper<TransactionsPerDay> {
    public String schema() {
        return " select date(transaction_date) as transactionDate, count(*) as numberOfTransactions " +
                " from m_transactions tx group by date(transaction_date) ;";
    }
    @Override
    public TransactionsPerDay mapRow(ResultSet rs, int rowNum) throws SQLException {
        return TransactionsPerDay.fromResultSet(rs);
    }
}
