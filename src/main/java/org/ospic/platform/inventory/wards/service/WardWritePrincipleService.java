package org.ospic.platform.inventory.wards.service;

import org.ospic.platform.inventory.beds.domains.Bed;
import org.ospic.platform.inventory.wards.domain.Ward;
import org.ospic.platform.util.exceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * This file was created by eli on 07/11/2020 for org.ospic.platform.inventory.wards.service
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
public interface WardWritePrincipleService {
    public ResponseEntity<String> createNewWard(Ward ward);
    public ResponseEntity<Ward> updateWard(Long id, Ward ward);
    public ResponseEntity<String> addBedInWard(Long wardId, Bed bed) throws ResourceNotFoundException;
    public ResponseEntity<String> addListOfBedsInWard(Long wardId, List<Bed> beds) throws ResourceNotFoundException;

}