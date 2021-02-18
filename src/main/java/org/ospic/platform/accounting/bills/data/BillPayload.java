package org.ospic.platform.accounting.bills.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.ospic.platform.accounting.transactions.data.TransactionResponse;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This file was created by eli on 18/02/2021 for org.ospic.platform.accounting.bills.data
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
public class BillPayload {
    private  Long id;
    private Boolean isPaid;
    private String extraId;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private Long consultationId;
    private Long patientId;
    private Boolean isActive;
    private String patientName;
    private String phoneNumber;
    private String address;
    private String emailAddress;
    private String createdDate;
    private String createdBy;
    private String lastUpdatedDate;
    private String lastUpdatedBy;
    private TransactionResponse transactionResponse;

    public static BillPayload fromResultSet(ResultSet rs) throws SQLException {
        final Long id = rs.getLong("id");
        final Boolean isPaid = rs.getBoolean("is_paid");
        final String extraId = rs.getString("extra_id");
        final BigDecimal totalAmount = rs.getBigDecimal("total_amount");
        final BigDecimal paidAmount = rs.getBigDecimal("paid_amount");
        final String createdDate = rs.getString("createdDate");
        final String createdBy = rs.getString("created_by");
        final String lastModifiedDate = rs.getString("lastModifiedDate");
        final String lastModifiedBy = rs.getString("last_modified_by");
        final Long consultationId = rs.getLong("consultation_id");
        final Long patientId = rs.getLong("patientId");
        final Boolean isActive = rs.getBoolean("isActive");
        final String patientName = rs.getString("patientName");
        final String phoneNumbers = rs.getString("phoneNumber");
        final String addresses = rs.getString("address");
        final String email = rs.getString("emailAddress");
        return new BillPayload(id,isPaid,extraId,totalAmount,paidAmount,consultationId,patientId,isActive,patientName,
                phoneNumbers,addresses,email,createdDate,createdBy,lastModifiedDate,lastModifiedBy);
    }

    private BillPayload(Long id, Boolean isPaid, String extraId, BigDecimal totalAmount, BigDecimal paidAmount, Long consultationId, Long patientId, Boolean isActive, String patientName, String phoneNumber, String address, String emailAddress, String createdDate, String createdBy, String lastUpdatedDate, String lastUpdatedBy) {
        this.id = id;
        this.isPaid = isPaid;
        this.extraId = extraId;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.consultationId = consultationId;
        this.patientId = patientId;
        this.isActive = isActive;
        this.patientName = patientName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.emailAddress = emailAddress;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastUpdatedDate = lastUpdatedDate;
        this.lastUpdatedBy = lastUpdatedBy;
    }
}
