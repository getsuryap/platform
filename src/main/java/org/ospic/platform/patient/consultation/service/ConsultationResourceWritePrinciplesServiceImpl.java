package org.ospic.platform.patient.consultation.service;

import org.ospic.platform.domain.CustomReponseMessage;
import org.ospic.platform.fileuploads.data.EntityType;
import org.ospic.platform.fileuploads.exceptions.FileUploadException;
import org.ospic.platform.fileuploads.message.ResponseMessage;
import org.ospic.platform.fileuploads.service.FilesStorageService;
import org.ospic.platform.inventory.admission.domains.Admission;
import org.ospic.platform.laboratory.reports.domain.FileInformation;
import org.ospic.platform.laboratory.reports.repository.FileInformationRepository;
import org.ospic.platform.organization.staffs.exceptions.StaffNotFoundExceptionPlatform;
import org.ospic.platform.organization.staffs.repository.StaffsRepository;
import org.ospic.platform.patient.consultation.domain.ConsultationResource;
import org.ospic.platform.patient.consultation.exception.ConsultationNotFoundExceptionPlatform;
import org.ospic.platform.patient.consultation.repository.ConsultationResourceJpaRepository;
import org.ospic.platform.patient.details.exceptions.PatientNotFoundExceptionPlatform;
import org.ospic.platform.patient.details.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This file was created by eli on 23/12/2020 for org.ospic.platform.patient.consultation.service
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
public class ConsultationResourceWritePrinciplesServiceImpl implements ConsultationResourceWritePrinciplesService {
    private final PatientRepository patientRepository;
    private final ConsultationResourceJpaRepository resourceJpaRepository;
    private final StaffsRepository staffsRepository;
    private final FilesStorageService filesStorageService;
    private final FileInformationRepository fileInformationRepository;

    @Autowired
    public ConsultationResourceWritePrinciplesServiceImpl(
            PatientRepository patientRepository, ConsultationResourceJpaRepository resourceJpaRepository,
            StaffsRepository staffsRepository,  FilesStorageService filesStorageService,
            FileInformationRepository fileInformationRepository) {
        this.patientRepository = patientRepository;
        this.resourceJpaRepository = resourceJpaRepository;
        this.staffsRepository = staffsRepository;
        this.filesStorageService = filesStorageService;
        this.fileInformationRepository = fileInformationRepository;
    }

    @Override
    public ResponseEntity<?> createNewConsultation(Long patientId) {
        return patientRepository.findById(patientId).map(patient -> {
            if (resourceJpaRepository.existsByPatientIdAndIsActiveTrue(patientId)) {
                return ResponseEntity.badRequest().body(String.format("A patient with ID: %2d already have an active instance running", patientId));
            }
            ConsultationResource sr = new ConsultationResource();
            sr.setPatient(patient);
            sr.setIsActive(true);
            sr.setIsAdmitted(false);
            sr.setFromdate(LocalDate.now());
            sr.setTodate(LocalDate.now());
            patient.setIsActive(true);
            patientRepository.save(patient);
            return ResponseEntity.ok().body(resourceJpaRepository.save(sr).getId());
        }).orElseThrow(()-> new PatientNotFoundExceptionPlatform(patientId));

    }

    @Override
    public ResponseEntity<?> assignConsultationToStaff(Long serviceId, Long staffId) {
        CustomReponseMessage cm = new CustomReponseMessage();
        HttpHeaders httpHeaders = new HttpHeaders();
        return this.staffsRepository.findById(staffId).map(staff -> {
            return this.resourceJpaRepository.findById(serviceId).map(service -> {
                service.setStaff(staff);
                staff.addService(service);
                staffsRepository.save(staff);
                cm.setMessage("A MedicalService assigned successfully ");
                return ResponseEntity.ok().body("MedicalService assigned successfully");
            }).orElseThrow(() -> new ConsultationNotFoundExceptionPlatform(serviceId));
        }).orElseThrow(() -> new StaffNotFoundExceptionPlatform(staffId));
    }

    @Override
    public ResponseEntity<?> endConsultationById(Long serviceId) {
        List<Admission> active = new ArrayList<>();
        return this.resourceJpaRepository.findById(serviceId).map(service -> {
            if (service.getPatient().getIsAdmitted()) {
                return ResponseEntity.ok().body("MedicalService has active admission. End admission before you close the service");
            }
            service.getPatient().setIsAdmitted(false);
            service.getPatient().setIsActive(false);
            service.setIsActive(false);
            service.setTodate(LocalDate.now());
            patientRepository.save(service.getPatient());
            resourceJpaRepository.save(service);
            return ResponseEntity.ok().body("MedicalService de-activated successfully");
        }).orElseThrow(() -> new ConsultationNotFoundExceptionPlatform(serviceId));
    }

    @Override
    public ResponseEntity<?> uploadConsultationLaboratoryReport(Long consultationId,String fileLocation, MultipartFile file) {
        return resourceJpaRepository.findById(consultationId).map(consultation->{
           String imageFile = filesStorageService.uploadPatientImage(consultation.getPatient().getId(), EntityType.ENTITY_PATIENTS, file, "consultations",String.valueOf(consultationId),fileLocation);
            FileInformation fileInfo = new FileInformation().fromFile(file, imageFile,fileLocation);
            fileInfo.setConsultation(consultation);
            return ResponseEntity.ok().body(this.fileInformationRepository.save(fileInfo));
        }).orElseThrow(()->new ConsultationNotFoundExceptionPlatform(consultationId));
    }

    @Override
    public ResponseEntity<?> deleteConsultationLaboratoryReport(Long consultationId, Long fileId) {
        return resourceJpaRepository.findById(consultationId).map(consultation->{
            return this.fileInformationRepository.findById(fileId).map(file->{
                if (!file.getConsultation().getId().equals(consultation.getId())){
                    throw new FileUploadException("error.msg.invalid.request", "This file does not belong to this consultation");
                }

                filesStorageService.deletePatientFileOrDocument("consultations/"+String.valueOf(consultationId)+"/"+file.getLocation(),EntityType.ENTITY_PATIENTS, consultation.getPatient().getId(), file.getName());
                this.fileInformationRepository.deleteById(file.getId());
                return ResponseEntity.ok().body(new ResponseMessage("File deleted successfully ..."));
            }).orElseThrow(()->new FileUploadException("error.msg.file.not.found", "File was not found"));

        }).orElseThrow(()->new ConsultationNotFoundExceptionPlatform(consultationId));
    }
}
