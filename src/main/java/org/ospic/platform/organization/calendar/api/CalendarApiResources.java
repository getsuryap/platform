package org.ospic.platform.organization.calendar.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.organization.calendar.data.EventRequest;
import org.ospic.platform.organization.calendar.domain.CalendarTimetable;
import org.ospic.platform.organization.calendar.services.CalendarReadPrincipleService;
import org.ospic.platform.organization.calendar.services.CalendarWritePrincipleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * This file was created by eli on 13/03/2021 for org.ospic.platform.organization.calendar.api
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
@RequestMapping("/api/calendar")
@Api(value = "/api/calendar", tags = "Calendar", description = "Institution calendar events")
public class CalendarApiResources {
    private final CalendarWritePrincipleService writeService;
    private final CalendarReadPrincipleService readService;

    @Autowired
    CalendarApiResources(
            CalendarReadPrincipleService readService,
            CalendarWritePrincipleService writeService) {
        this.readService = readService;
        this.writeService = writeService;
    }

    @ApiOperation(value = "CREATE new event", notes = "CREATE new event", response = CalendarTimetable.class, nickname = "Create new event")
    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createNewEvent(@Valid @RequestBody EventRequest payload) {
        return this.writeService.createCalendarEvent(payload);
    }

    @ApiOperation(value = "RETRIEVE all institution events", notes = "RETRIEVE all institution events",response = CalendarTimetable.class,responseContainer = "List")
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getAllEvents() {
        return this.readService.retrieveAllCalendarEvents();
    }

    @ApiOperation(value = "UPDATE calendar event", notes = "UPDATE calendar event", response = CalendarTimetable.class, nickname = "Update event")
    @RequestMapping(value = "/{eventId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createNewEvent(@PathVariable("eventId") Long eventId,@Valid @RequestBody EventRequest payload) {
        return this.writeService.updateCalendarEvent(eventId,payload);
    }

    @ApiOperation(value = "DELETE calendar event", notes = "DELETE calendar event",  nickname = "Delete event")
    @RequestMapping(value = "/{eventId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> deleteCalendarEvent(@PathVariable("eventId") Long eventId) {
        return this.writeService.deleteCalendarEvent(eventId);
    }

}
