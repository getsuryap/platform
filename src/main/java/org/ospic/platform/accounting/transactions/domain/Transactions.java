package org.ospic.platform.accounting.transactions.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.ospic.platform.accounting.bills.domain.Bill;
import org.ospic.platform.accounting.transactions.data.TransactionPayload;
import org.ospic.platform.infrastructure.app.domain.AbstractPersistableCustom;
import org.ospic.platform.inventory.pharmacy.medicine.domains.Medicine;
import org.ospic.platform.organization.departments.domain.Department;
import org.ospic.platform.organization.medicalservices.domain.MedicalService;
import org.ospic.platform.util.constants.DatabaseConstants;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * This file was created by eli on 02/02/2021 for org.ospic.platform.accounting.transactions.domain
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
@Getter
@Setter
@NoArgsConstructor
@Entity(name = DatabaseConstants.TABLE_TRANSACTIONS)
@Table(name = DatabaseConstants.TABLE_TRANSACTIONS)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@ToString
public class Transactions extends AbstractPersistableCustom implements Serializable {


    @Column(length = 140, name = "currency_code", nullable = false)
    private String currencyCode;

    @Column(name = "amount", nullable = false, columnDefinition="Decimal(19,2) default '0.00'")
    private BigDecimal amount;

    @Column(name = "is_reversed", nullable = false, columnDefinition = "boolean default false")
    private Boolean isReversed;

    @Column(name = "transaction_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Basic(optional = false)
    private LocalDateTime transactionDate;

    @ManyToOne
    @JoinColumn(name = "medical_service_id")
    @JsonIgnore
    private MedicalService medicalService;

    @ManyToOne
    @JoinColumn(name = "department_id")
    @JsonIgnore
    private Department department;

    @ManyToOne(optional = false)
    @JoinColumn(name = "bill_id", nullable = false)
    @JsonIgnore
    private Bill bill;

    @ManyToOne
    @JoinColumn(name = "medicine_id")
    @JsonIgnore
    private Medicine medicine;

    public Transactions fromTransactionPayload(TransactionPayload payload, MedicalService service){
        return new Transactions(null, service.getPrice(), null);
    }
    public Transactions instance(String currencyCode, BigDecimal price, LocalDateTime transactionDate){
        return new Transactions(currencyCode, price, transactionDate);
    }

   public Transactions(String currencyCode,  BigDecimal amount, LocalDateTime transactionDate) {
        this.currencyCode = currencyCode;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

    public void addMedicalService(MedicalService medicalService) {
        this.medicalService = medicalService;
        medicalService.getTransactions().add(this);
    }

    //public void setBill(Bill bill) {this.bill = bill;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transactions)) return false;
        Transactions that = (Transactions) o;
        return getCurrencyCode().equals(that.getCurrencyCode()) && getAmount().equals(that.getAmount()) && getTransactionDate().equals(that.getTransactionDate()) && getMedicalService().equals(that.getMedicalService()) && getDepartment().equals(that.getDepartment()) && getBill().equals(that.getBill());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMedicalService(), getDepartment(), getBill());
    }

}
