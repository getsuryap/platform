package org.ospic.platform.inventory.beds.repository;

import org.ospic.platform.inventory.beds.domains.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
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
@Repository
public interface BedRepository extends JpaRepository<Bed, Long> {
    @Override
    Optional<Bed> findById(Long aLong);
    Optional<List<Bed>> findByIdentifierEquals(String identifier);
    Optional<List<Bed>> findByIsOccupiedFalse();
    Optional<List<Bed>> findByIsOccupiedTrue();
    List<Bed> findByWardId(Long wardId);


    Boolean existsByIdentifier(String identifier);
    boolean existsById(Long bedId);

}
