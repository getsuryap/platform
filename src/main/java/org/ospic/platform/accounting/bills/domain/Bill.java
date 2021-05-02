package org.ospic.platform.accounting.bills.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.ospic.platform.accounting.transactions.domain.Transactions;
import org.ospic.platform.configurations.audit.Auditable;
import org.ospic.platform.patient.consultation.domain.ConsultationResource;
import org.ospic.platform.util.constants.DatabaseConstants;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * This file was created by eli on 18/02/2021 for org.ospic.platform.accounting.bills.domain
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
@ToString
@Entity(name = DatabaseConstants.TABLE_BILLS)
@Table(name = DatabaseConstants.TABLE_BILLS)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class Bill extends  Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "extra_id", nullable = false)
    private String extraId;

    @Column(name = "is_paid", nullable = false, columnDefinition = "boolean default false")
    private Boolean isPaid;

    @Column(name = "total_amount", nullable = true, columnDefinition = "Decimal(13,4) default '0.00'")
    private BigDecimal totalAmount;

    @Column(name = "paid_amount", nullable = true, columnDefinition = "Decimal(13,4) default '0.00'")
    private BigDecimal paidAmount;

    @OneToOne(cascade = CascadeType.ALL)
    //@MapsId
    @ApiModelProperty(position = 1, required = true, hidden = true, notes = "Consultation respective id")
    private ConsultationResource consultation;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "bill_id")
    @ApiModelProperty(position = 1, required = true, hidden = true, notes = "used to display department transactions")
    @JsonIgnore
    private List<Transactions> transactions = new ArrayList<>();

    public Bill(Long id, String extraId, Boolean isPaid, BigDecimal totalAmount, BigDecimal paidAmount) {
        this.id = id;
        this.extraId = extraId;
        this.isPaid = isPaid;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
    }
}
