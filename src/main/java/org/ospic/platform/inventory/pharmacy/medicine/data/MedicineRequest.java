package org.ospic.platform.inventory.pharmacy.medicine.data;

import lombok.*;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Date;

/**
 * This file was created by eli on 12/11/2020 for org.ospic.platform.inventory.pharmacy.medicine.data
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
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
public class MedicineRequest {
    private String name;
    private String company;
    private String compositions;
    private String genericName;
    private String effects;
    private String units;
    @Min(0)
    private Integer quantity;
    private Long group;
    private Long category;
    private BigDecimal buyingPrice;
    private BigDecimal sellingPrice;
    private Date expireDateTime;
    private String storeBox;

}
