package org.ospic.platform.inventory.pharmacy.categories.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * This file was created by eli on 26/01/2021 for org.ospic.platform.inventory.pharmacy.categories.data
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
@EqualsAndHashCode()
public class MedicineCategoryRequest {
    private Long id;
    private String name;
    private String descriptions;
    private Long measurementId;
    private String measureName;

    public MedicineCategoryRequest(String name, String descriptions, Long measurementId) {
        this.name = name;
        this.descriptions = descriptions;
        this.measurementId = measurementId;
    }

    public MedicineCategoryRequest(String name, String descriptions, String measureName) {
        this.name = name;
        this.descriptions = descriptions;
        this.measureName = measureName;
    }
}
