package org.ospic.inventory.pharmacy.Medicine.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.inventory.pharmacy.Medicine.data.MedicineDataTemplate;
import org.ospic.inventory.pharmacy.Medicine.data.MedicineRequest;
import org.ospic.inventory.pharmacy.Medicine.domains.Medicine;
import org.ospic.inventory.pharmacy.Medicine.repository.MedicineRepository;
import org.ospic.inventory.pharmacy.Medicine.service.MedicineReadService;
import org.ospic.inventory.pharmacy.Medicine.service.MedicineWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * This file was created by eli on 12/11/2020 for org.ospic.inventory.pharmacy.Medicine.api
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
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
@RestController
@RequestMapping("/api/pharmacy/medicines")
@Api(value = "/api/pharmacy/medicines", tags = "Medicines")
public class MedicineApiResource {
    @Autowired
    MedicineRepository medicineRepository;
    @Autowired
    MedicineWriteService medicineWriteService;
    @Autowired
    MedicineReadService medicineReadService;

    @Autowired
    public MedicineApiResource(MedicineRepository medicineRepository,
                               MedicineReadService medicineReadService,
                               MedicineWriteService medicineWriteService) {
        this.medicineRepository = medicineRepository;
        this.medicineWriteService = medicineWriteService;
        this.medicineReadService = medicineReadService;
    }

    @ApiOperation(value = "RETRIEVE list of available Medicines", notes = "RETRIEVE list of available Medicines", response = Medicine.class)
    @RequestMapping(value = "/", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<List<Medicine>> retrieveAllMedicineProductsOrTemplate() {
        List<Medicine> medicines = medicineReadService.fetchAllMedicine();
        if (medicines.isEmpty()){
            return new ResponseEntity<List<Medicine>>(HttpStatus.NO_CONTENT);
        }
        return  ResponseEntity.ok().body(medicines);
    }

    @ApiOperation(value = "RETRIEVE  Medicines by ID", notes = "RETRIEVE  Medicines by ID", response = Medicine.class)
    @RequestMapping(value = "/{medicineId}", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Medicine> retrieveAllMedicineProductById(@PathVariable  Long medicineId) {
        Optional<Medicine> medicine = medicineRepository.findById(medicineId);
        return medicine.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "RETRIEVE medicine templates", notes = "RETRIEVE  M medicine templates", response = MedicineDataTemplate.class)
    @RequestMapping(value = "/template", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<MedicineDataTemplate> retrieveMedicineTemplate(){
        return ResponseEntity.ok().body(medicineReadService.readMedicineDataTemplate());
    }


    @ApiOperation(value = "UPDATE medicine product", notes = "UPDATE  medicine product", response = Medicine.class)
    @RequestMapping(value = "/{medicinalId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Medicine> updateMedicineProductByItsId(@PathVariable Long medicinalId, @Valid @RequestBody MedicineRequest medicine){
        return ResponseEntity.ok().body(medicineWriteService.updateMedicineProduct(medicinalId, medicine));
    }




    @ApiOperation(
            value = "ADD new Medicine",
            notes = "ADD new Medicine",
            response = Medicine.class)
    @RequestMapping(
            value = "/",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)

    @ResponseBody
    ResponseEntity<String> addNewMedicineProduct(@Valid @RequestBody MedicineRequest medicine) {
        return medicineWriteService.createNewMedicineProduct(medicine);
    }


}
