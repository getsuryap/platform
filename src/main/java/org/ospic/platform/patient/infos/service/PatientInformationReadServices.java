package org.ospic.platform.patient.infos.service;

import org.ospic.platform.patient.infos.domain.Patient;
import org.ospic.platform.util.exceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
@Component
@Service
public interface PatientInformationReadServices {
    @Transactional
    public ResponseEntity<List<Patient>> retrieveAllPatients();

    public ResponseEntity<List<Patient>> retrieveAllAssignedPatients();

    public ResponseEntity<List<Patient>> retrieveAllUnAssignedPatients();

    public ResponseEntity<?> retrieveStatisticalData();


    public ResponseEntity<?> retrievePageablePatients(String bloodGroup, Pageable pageable);

    @Transactional
    public ResponseEntity<?> retrievePatientById(Long id) throws ResourceNotFoundException;

    public ResponseEntity<?> retrievePatientCreationDataTemplate();


    public ResponseEntity<List<Patient>> retrievePatientAdmittedInThisBed(Long bedId);


    public ResponseEntity<?> retrievePatientAssignedToThisStaff(Long staffId);

}
