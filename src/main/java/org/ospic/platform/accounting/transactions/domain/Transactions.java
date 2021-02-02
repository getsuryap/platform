package org.ospic.platform.accounting.transactions.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.hibernate.Transaction;
import org.ospic.platform.accounting.transactions.data.TransactionPayload;
import org.ospic.platform.infrastructure.app.domain.AbstractPersistableCustom;
import org.ospic.platform.organization.departments.domain.Department;
import org.ospic.platform.organization.medicalservices.domain.MedicalService;
import org.ospic.platform.patient.consultation.domain.ConsultationResource;
import org.ospic.platform.util.constants.DatabaseConstants;

import javax.persistence.*;
import java.io.Serializable;
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
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@Entity(name = DatabaseConstants.TABLE_TRANSACTIONS)
@Table(name = DatabaseConstants.TABLE_TRANSACTIONS)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Transactions  extends AbstractPersistableCustom implements Serializable {

    @Column(length = 140, name = "currency_code", nullable = false)
    private String currencyCode;

    @Column(name = "price", nullable = false, columnDefinition="Decimal(10,2) default '0.00'")
    private Double price;

    @Column(name = "is_reversed", nullable = false, columnDefinition = "boolean default false")
    private Boolean isReversed;

    @Column(name = "transaction_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Basic(optional = false)
    private LocalDateTime transactionDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medical_service_id", nullable = false)
    private MedicalService medicalService;

    @ManyToOne(optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(optional = false)
    @JoinColumn(name = "consultation_id", nullable = false)
    private ConsultationResource consultation;

    public Transactions fromTransactionPayload(final TransactionPayload payload){
        return new Transactions(payload.getCurrencyCode(), payload.getAmount(), payload.getTransactionDate());
    }
    public Transactions instance(String currencyCode, Double price, LocalDateTime transactionDate){
        return new Transactions(currencyCode, price, transactionDate);
    }

    private Transactions(String currencyCode, Double price, LocalDateTime transactionDate) {
        this.currencyCode = currencyCode;
        this.price = price;
        this.transactionDate = transactionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transactions)) return false;
        Transactions that = (Transactions) o;
        return getCurrencyCode().equals(that.getCurrencyCode()) && getPrice().equals(that.getPrice()) && getTransactionDate().equals(that.getTransactionDate()) && getMedicalService().equals(that.getMedicalService()) && getDepartment().equals(that.getDepartment()) && getConsultation().equals(that.getConsultation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMedicalService(), getDepartment(), getConsultation());
    }
}
