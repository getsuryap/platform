package org.ospic.platform.organization.medicalservices.data;

import lombok.*;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This file was created by eli on 10/04/2021 for org.ospic.platform.organization.medicalservices.data
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
@AllArgsConstructor
public class MedicalServicePayload {
      private Long id;
      private String name;
      private Boolean isActive;
      private BigDecimal price;
      private Boolean isMeasurable;
      private String units;
      private Long medicalServiceType;
      private String serviceTypeName;

      public static MedicalServicePayload fromResultSet(ResultSet rs) throws SQLException {
            final Long id = rs.getLong("id");
            final String name = rs.getString("name");
            final Boolean isActive = rs.getBoolean("enabled");
            final BigDecimal price = rs.getBigDecimal("price");
            final Boolean isMeasurable = rs.getBoolean("is_measurable");
            final String units = rs.getString("units");
            final Long medicalServiceTypeId = rs.getLong("serviceTypeId");
            final String serviceTypeName = rs.getString("serviceTypeName");
            return new MedicalServicePayload(id, name,isActive,price,isMeasurable, units, medicalServiceTypeId, serviceTypeName );

      }
}
