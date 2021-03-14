package org.ospic.platform.inventory.pharmacy.measurements.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.inventory.pharmacy.measurements.domain.MeasurementUnit;
import org.ospic.platform.inventory.pharmacy.measurements.services.MeasurementUnitsReadPrincipleService;
import org.ospic.platform.inventory.pharmacy.measurements.services.MeasurementUnitsWritePrincipleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * This file was created by eli on 26/01/2021 for org.ospic.platform.inventory.pharmacy.measurements.api
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
@RequestMapping("/api/pharmacy/measures")
@Api(value = "/api/pharmacy/measures",tags = "Measurements",description = "Medicines measurement units")
public class MeasurementsUnitsApiResource {
    private final  MeasurementUnitsReadPrincipleService readPrincipleService;
    private final MeasurementUnitsWritePrincipleService writePrincipleService;

    @Autowired
    MeasurementsUnitsApiResource(MeasurementUnitsReadPrincipleService readPrincipleService, MeasurementUnitsWritePrincipleService writePrincipleService) {
        this.readPrincipleService = readPrincipleService;
        this.writePrincipleService = writePrincipleService;
    }

    @ApiOperation(value = "RETRIEVE medicine measurement units", notes = "RETRIEVE medicine measurement units",response = MeasurementUnit.class, responseContainer = "List")
    @RequestMapping(value = "/", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> retrieveListOfMedicineMeasurementUnits() {
        return  readPrincipleService.fetchAllMedicineMeasurementUnit();
    }

    @ApiOperation(value = "RETRIEVE medicine measurement unit by id", notes = "RETRIEVE medicine measurement unit by id",response = MeasurementUnit.class)
    @RequestMapping(value = "/{unitId}", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getMedicineMeasurementUnitById(@PathVariable("unitId") Long  unitId) {
        return  readPrincipleService.fetchMeasurementUnitById(unitId);
    }

    @ApiOperation(value = "CREATE new medicine measurement units", notes = "CREATE new  medicine measurement units",response = MeasurementUnit.class)
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createNewMedicineMeasurementUnits( @Valid @RequestBody MeasurementUnit payload) {
        return  writePrincipleService.createMeasurementUnit(payload);
    }


    @ApiOperation(value = "UPDATE medicine measurement units", notes = "UPDATE  medicine measurement units",response = MeasurementUnit.class)
    @RequestMapping(value = "/{unitId}", method = RequestMethod.PUT, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateMedicineMeasurementUnits(@PathVariable("unitId") Long  unitId,  @Valid @RequestBody MeasurementUnit payload) {
        return  writePrincipleService.updateMeasurementUnit(unitId, payload);
    }
}
