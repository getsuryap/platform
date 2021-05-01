package org.ospic.platform.accounting.transactions.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * This file was created by eli on 05/02/2021 for org.ospic.platform.accounting.transactions.data
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
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse implements Serializable {
    private BigDecimal totalAmount;
    private List<TransactionRowMap> transactions;

    public TransactionResponse transactionResponse(List<TransactionRowMap> transactions) {
        BigDecimal sum = new BigDecimal(0);
        for (TransactionRowMap transaction : transactions) {
            sum = sum.add(transaction.getIsReversed() ? BigDecimal.ZERO : transaction.getAmount());
        }
        return new TransactionResponse(sum, transactions);
    }

}
