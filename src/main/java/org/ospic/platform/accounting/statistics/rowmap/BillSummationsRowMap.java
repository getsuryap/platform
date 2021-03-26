package org.ospic.platform.accounting.statistics.rowmap;

import org.ospic.platform.accounting.statistics.data.BillSummations;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This file was created by eli on 26/03/2021 for org.ospic.platform.accounting.statistics.rowmap
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
public class BillSummationsRowMap implements RowMapper<BillSummations> {
    public String schema() {
        return " select count(*) as totalNumberOfBills, " +
                " sum(case when date(b.created_date) = curdate()  then 1 else 0 end ) as totalNumberOfBillsToday, " +
                " sum(case when date(b.created_date) = curdate()  then b.total_amount else 0 end ) as totalBillsAmountToday, " +
                " sum(case when date(b.created_date) = curdate()  then b.paid_amount else 0 end ) as totalBillsPaidAmountToday, " +
                " sum(b.total_amount) as totalBillsAmount, " +
                " sum(b.paid_amount) as totalBillsPaidAmount " +
                " FROM m_bills b;";
    };
    @Override
    public BillSummations mapRow(ResultSet rs, int rowNum) throws SQLException {
        return BillSummations.fromResultSet(rs);
    }
}
