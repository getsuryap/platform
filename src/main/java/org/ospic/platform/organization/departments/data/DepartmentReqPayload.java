package org.ospic.platform.organization.departments.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.ospic.platform.organization.departments.domain.Department;

import javax.persistence.*;
import java.util.Date;

/**
 * This file was created by eli on 09/01/2021 for org.ospic.platform.organization.departments.data
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
public class DepartmentReqPayload {
    private Long parent;
    private String name;
    private String hierarchy;
    private String knownAs;
    private String extraId;

    public static DepartmentReqPayload fromJson(final Long parent, final String name, final String hierarchy, final String knownAs, final String extraId){
        return new DepartmentReqPayload(parent, name, hierarchy, knownAs, extraId);
    }

    private DepartmentReqPayload(final Long parent, final String name, final String hierarchy, final String knownAs, final String extraId) {
        this.parent = parent;
        this.name = name;
        this.hierarchy = hierarchy;
        this.knownAs = knownAs;
        this.extraId = extraId;
    }
}
