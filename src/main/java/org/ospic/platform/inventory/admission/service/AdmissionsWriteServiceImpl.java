package org.ospic.platform.inventory.admission.service;

import org.ospic.platform.domain.CustomReponseMessage;
import org.ospic.platform.inventory.admission.data.AdmissionRequest;
import org.ospic.platform.inventory.admission.data.EndAdmissionRequest;
import org.ospic.platform.inventory.admission.domains.Admission;
import org.ospic.platform.inventory.admission.exception.AdmissionNotFoundException;
import org.ospic.platform.inventory.admission.repository.AdmissionRepository;
import org.ospic.platform.inventory.beds.domains.Bed;
import org.ospic.platform.inventory.beds.exception.BedNotFoundException;
import org.ospic.platform.inventory.beds.repository.BedRepository;
import org.ospic.platform.patient.resource.exception.ServiceNotFoundException;
import org.ospic.platform.patient.resource.repository.ServiceResourceJpaRepository;
import org.ospic.platform.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This file was created by eli on 09/11/2020 for org.ospic.platform.inventory.admission.service
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
@Repository
public class AdmissionsWriteServiceImpl implements AdmissionsWriteService {
    private static final Logger logger = LoggerFactory.getLogger(AdmissionsWriteServiceImpl.class);


    @Autowired
    AdmissionRepository admissionRepository;
    @Autowired
    BedRepository bedRepository;
    @Autowired
    ServiceResourceJpaRepository serviceResourceJpaRepository;

    public AdmissionsWriteServiceImpl(
            AdmissionRepository admissionRepository,
            BedRepository bedRepository,
            ServiceResourceJpaRepository serviceResourceJpaRepository) {
        this.admissionRepository = admissionRepository;
        this.bedRepository = bedRepository;
        this.serviceResourceJpaRepository = serviceResourceJpaRepository;
    }

    @Transactional
    @Override
    public ResponseEntity<?> admitPatient(AdmissionRequest admissionRequest) {
        final LocalDateTime startLocalDateTime = new DateUtil().convertToLocalDateTimeViaInstant(admissionRequest.getStartDateTime());
        final LocalDateTime endLocalDateTime = new DateUtil().convertToLocalDateTimeViaInstant(admissionRequest.getEndDateTime());

        return serviceResourceJpaRepository.findById(admissionRequest.getServiceId()).map(service -> {
            CustomReponseMessage cm = new CustomReponseMessage();
            HttpHeaders httpHeaders = new HttpHeaders();
            if (endLocalDateTime.isBefore(startLocalDateTime)){
                return ResponseEntity.ok().body(new CustomReponseMessage(HttpStatus.BAD_REQUEST.value(), "Admission and date can not be day before it's start date"));
            }
            if (!service.getIsActive()){
                return ResponseEntity.ok().body("Cannot admit patient in inactive service");
            }
            if (service.getPatient().getIsAdmitted()) {
                cm.setMessage("Cannot re-admit an admitted patient");
                return new ResponseEntity<CustomReponseMessage>(cm, httpHeaders, HttpStatus.CONFLICT);
            }
            if (!bedRepository.existsById(admissionRequest.getBedId())){
                cm.setMessage(String.format("Cannot find a Bed with an ID %2d ", admissionRequest.getBedId()));
                return new ResponseEntity<CustomReponseMessage>(cm, httpHeaders, HttpStatus.CONFLICT);
            }
            Bed bed = bedRepository.findById(admissionRequest.getBedId()).get();
            if (bed.getIsOccupied()){
                cm.setMessage(String.format("Bed with an ID %2d is occupied", admissionRequest.getBedId()));
                return new ResponseEntity<CustomReponseMessage>(cm, httpHeaders, HttpStatus.CONFLICT);
            }
            /**Create new admission **/
            Admission admission = new Admission(admissionRequest.getIsActive(), startLocalDateTime, endLocalDateTime);
            admission.setService(service);
            admission.setIsActive(true);
            admission.addBed(bed);
            admissionRepository.save(admission);

            /** Update Bed set it as active. Not open for new admission **/
            bed.setIsOccupied(true);
            bedRepository.save(bed);

            /**
             * Update patient set as admitted to prevent re-admission**/
            service.setIsAdmitted(true);
            service.getPatient().setIsAdmitted(true);
            serviceResourceJpaRepository.save(service);


            /**Return Message **/

            return ResponseEntity.ok().body(admissionRequest.getServiceId());
        }).orElseThrow(() -> new EntityNotFoundException());
    }

    @Transactional
    @Override
    public ResponseEntity<?> endPatientAdmission(EndAdmissionRequest request) {
        CustomReponseMessage cm = new CustomReponseMessage();
        HttpHeaders httpHeaders = new HttpHeaders();
        final LocalDateTime endLocalDateTime = new DateUtil().convertToLocalDateTimeViaInstant(request.getEndDateTime());
        return serviceResourceJpaRepository.findById(request.getServiceId()).map(service -> {
            return admissionRepository.findById(request.getAdmissionId()).map(admission -> {
              return bedRepository.findById(request.getBedId()).map(bed ->{
                  if (!(admission.getIsActive() || service.getPatient().getIsAdmitted())) {
                      cm.setHttpStatus(HttpStatus.BAD_REQUEST.value());
                      cm.setMessage("Can't end inactive admission or un-admitted service");
                      return new ResponseEntity<>(cm, httpHeaders, HttpStatus.OK);
                  }
                  if (endLocalDateTime.isBefore(admission.getFromDateTime())){
                      cm.setHttpStatus(HttpStatus.BAD_REQUEST.value());
                      cm.setMessage("Admission end date can not be before admission start date");
                      return new ResponseEntity<>(cm, httpHeaders, HttpStatus.OK);
                  }

                  /** Update this service set as no longer admitted
                   * Update patient under this service set as no longer admitted **/
                  service.setIsAdmitted(false);
                  service.getPatient().setIsAdmitted(false);
                  serviceResourceJpaRepository.save(service);

                  /** Update Admission set as no longer active **/
                  admission.setIsActive(false);
                  admission.setToDateTime(endLocalDateTime);
                  admissionRepository.save(admission);

                  /** Update bed set as active open for another admission **/
                  bed.setIsOccupied(false);
                  bedRepository.save(bed);

                  /**Return Message status back to user**/
                  cm.setMessage(String.format("Admission %2d for service %s has being ended on %s ", request.getAdmissionId(), service.getPatient().getName(), request.getEndDateTime()));
                  return new ResponseEntity<>(cm, httpHeaders, HttpStatus.OK);
              }).orElseThrow(()-> new BedNotFoundException(request.getBedId()));
            }).orElseThrow(() -> new AdmissionNotFoundException(request.getAdmissionId()));
        }).orElseThrow(() -> new ServiceNotFoundException(request.getServiceId()));

    }

    @Transactional
    @Override
    public ResponseEntity<String> updatePatientAdmissionInfo() {
        return null;
    }
}
