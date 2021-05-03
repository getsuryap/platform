package org.ospic.platform.inventory.pharmacy.medicine.domains;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.ospic.platform.accounting.transactions.domain.Transactions;
import org.ospic.platform.inventory.pharmacy.categories.domains.MedicineCategory;
import org.ospic.platform.inventory.pharmacy.groups.domains.MedicineGroup;
import org.ospic.platform.inventory.pharmacy.medicine.data.MedicineRequest;
import org.ospic.platform.util.constants.DatabaseConstants;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This file was created by eli on 12/11/2020 for org.ospic.platform.inventory.pharmacy.medicine.domains
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
@Entity(name = DatabaseConstants.TABLE_PHARMACY_MEDICINES)
@Table(name = DatabaseConstants.TABLE_PHARMACY_MEDICINES,
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name","company","compositions"}),
        })
@ApiModel(value = "Medicine", description = "Contain all medicine's available")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Medicine implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;


    @NotBlank
    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @NotBlank
    @Column(name = "generic_name", length = 200, nullable = false)
    private String genericName;

    @NotBlank
    @Column(name = "effects", length = 250, nullable = false)
    private String effects;

    @Column(name = "expire_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Basic(optional = false)
    private LocalDateTime expireDateTime;


    @NotBlank
    @Column(name = "company", length = 200, nullable = false)
    private String company;

    @NotBlank
    @Column(name = "compositions", length = 200, nullable = false)
    private String compositions;

    @Column(name = "units", length = 5)
    private String unit;

    @Column(name = "store_box", length = 5)
    private String storeBox;


    @Column(name = "quantity", length = 5)
    private int quantity;

    @Column(name = "buying_price", nullable = false, columnDefinition="Decimal(19,2) default '0.00'")
    private BigDecimal buyingPrice;

    @Column(name = "selling_price", nullable = false, columnDefinition="Decimal(19,2) default '0.00'")
    private BigDecimal sellingPrice;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private MedicineGroup group;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private MedicineCategory category;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "medicine_id")
    @JsonIgnore
    private List<Transactions> transactions = new ArrayList<>();

    public Medicine fromJson(MedicineRequest rq){
        return new Medicine(rq.getName(), rq.getGenericName(), rq.getEffects(), rq.getCompany(),rq.getCompositions(),
                rq.getUnits(),rq.getQuantity(), rq.getBuyingPrice(),rq.getSellingPrice(), rq.getStoreBox());
    }

    private Medicine(String name, String genericName, String effects,  String company, String compositions,
                     String units,int quantity, BigDecimal buyingPrice, BigDecimal sellingPrice, final String storeBox) {
        this.name = name;
        this.genericName = genericName;
        this.effects = effects;
        this.company = company;
        this.compositions = compositions;
        this.unit = units;
        this.quantity = quantity;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.storeBox = storeBox;
    }

}
