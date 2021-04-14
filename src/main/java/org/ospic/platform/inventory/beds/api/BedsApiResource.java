package org.ospic.platform.inventory.beds.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.ospic.platform.domain.CustomReponseMessage;
import org.ospic.platform.inventory.beds.data.BedData;
import org.ospic.platform.inventory.beds.domains.Bed;
import org.ospic.platform.inventory.beds.repository.BedRepository;
import org.ospic.platform.inventory.beds.service.BedReadService;
import org.ospic.platform.inventory.beds.service.BedWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * This file was created by eli on 06/11/2020 for org.ospic.platform.inventory.beds.api
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
@RestController()
@RequestMapping("/api/beds")
@Api(value = "/api/beds", tags = "Beds", description = "Beds API resources")
public class BedsApiResource {

    private final BedReadService bedReadService;
    private final BedWriteService bedWriteService;
    private final BedRepository bedRepository;

    @Autowired
    public BedsApiResource(BedReadService bedReadService, BedWriteService bedWriteService, BedRepository bedRepository) {
        this.bedReadService = bedReadService;
        this.bedWriteService = bedWriteService;
        this.bedRepository = bedRepository;
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','UPDATE_INVENTORY')")
    @ApiOperation(value = "CREATE Bed", notes = "CREATE bed")
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.ALL_VALUE)
    ResponseEntity<String> createBed(@Valid  @ApiParam(name = "Bed Entity", required = true)  @RequestBody  Bed bedData){
        return bedWriteService.createNewBed(bedData);
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','UPDATE_INVENTORY')")
    @ApiOperation(value = "ASSIGN bed to ward", notes = "ASSIGN bed to ward")
    @RequestMapping(value = "/{bedId}/{wardId}/{action}", method = RequestMethod.PATCH, consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    ResponseEntity<String> updateBedInWard(@ApiParam(name = "Bed ID", required = true) @PathVariable Long bedId, @ApiParam(name = "Ward ID", required = true) @PathVariable Long wardId, @ApiParam(name = "Action", required = true) @PathVariable String action){
        return bedWriteService.updateBedInWardByAction(bedId, wardId, action);
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','UPDATE_INVENTORY')")
    @ApiOperation(value = "CREATE beds by array", notes = "CREATE beds by an array")
    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createBedsInThisWard(@Valid @RequestBody BedData bedData){
       return bedWriteService.addBedsInWard(bedData);
    }


    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','UPDATE_INVENTORY')")
    @ApiOperation(value = "GET Bed by ID", notes = "GET Bed by ID")
    @RequestMapping(value = "/{wardId}/ward", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Bed>> retrieveListOfBedsByInWard(@ApiParam(name = "wardId", required = true) @PathVariable Long wardId){
        return bedReadService.retrieveBedListByWard(wardId);
    }



    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','UPDATE_INVENTORY')")
    @ApiOperation(value = "GET Bed by ID", notes = "GET Bed by ID")
    @RequestMapping(value = "/{bedId}", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Bed> retrieveBedById(@ApiParam(name = "Bed ID", required = true) @PathVariable Long bedId){
        return ResponseEntity.ok().body(bedRepository.findById(bedId).get());
    }



    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','UPDATE_INVENTORY')")
    @ApiOperation(value = "GET Bed by IDentifier", notes = "GET Bed by IDentifier")
    @RequestMapping(value = "/identifier/{Identifier}", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Bed>> retrieveBedByIdentifier(@ApiParam(name = "Bed Identifier", required = true) @PathVariable String Identifier){
        return ResponseEntity.ok().body(bedRepository.findByIdentifierEquals(Identifier).get());
    }



    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','UPDATE_INVENTORY')")
    @ApiOperation(value = "RETRIEVE list of occupied beds", notes = "RETRIEVE list of occupied beds")
    @RequestMapping(value = "/", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> retrieveBedByCommand(@RequestParam(value = "command", required = false) String command){
        if (!(command == null || command.isEmpty())) {
            if (command.equals("occupied")){
                return ResponseEntity.ok().body(bedRepository.findByIsOccupiedTrue().get());
            }
            if (command.equals("unoccupied")){
                Optional<List<Bed>> beds = bedRepository.findByIsOccupiedFalse();
                return ResponseEntity.ok().body(beds.isPresent() ? beds.get() : new CustomReponseMessage(HttpStatus.NO_CONTENT.value(), "Sorry. Seems like all beds are now occupied"));
            }
        }
        return bedReadService.retrieveBedList();
    }
}
