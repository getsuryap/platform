package org.ospic.platform.organization.calendar.services;

import org.ospic.platform.organization.calendar.data.EventRequest;
import org.ospic.platform.organization.calendar.domain.CalendarTimetable;
import org.ospic.platform.organization.calendar.exceptions.CalendarEventNotFoundException;
import org.ospic.platform.organization.calendar.exceptions.InvalidStartOrEndDateException;
import org.ospic.platform.organization.calendar.repository.CalendarJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * This file was created by eli on 13/03/2021 for org.ospic.platform.organization.calendar.services
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
public class CalendarWritePrincipleServiceImpl  implements CalendarWritePrincipleService{
    private final CalendarJpaRepository calendarJpaRepository;
    @Autowired
    CalendarWritePrincipleServiceImpl(CalendarJpaRepository calendarJpaRepository){
        this.calendarJpaRepository = calendarJpaRepository;
    }

    @Override
    public ResponseEntity<?> createCalendarEvent(EventRequest request) {
        if ( request.getStartDate() ==null || request.getEndDate()==null){ throw new InvalidStartOrEndDateException(); }

        CalendarTimetable calendarTimetable = new CalendarTimetable().getTimetableEvent(request);
        return ResponseEntity.ok().body(this.calendarJpaRepository.save(calendarTimetable));
    }

    @Override
    public ResponseEntity<?> updateCalendarEvent(Long eventId, EventRequest payload) {
        return this.calendarJpaRepository.findById(eventId).map(event->{
            LocalDateTime startDateTime = LocalDateTime.of(payload.getStartDate(), payload.getStartTime() == null ? LocalTime.MIDNIGHT :  payload.getStartTime());
            LocalDateTime endDateTime = LocalDateTime.of(payload.getEndDate(), payload.getEndTime() == null ? LocalTime.MIDNIGHT : payload.getEndTime());
            event.setName(payload.getName());
            event.setStart(startDateTime);
            event.setEnd(endDateTime);
            return ResponseEntity.ok().body(this.calendarJpaRepository.save(event));
        }).orElseThrow(()-> new CalendarEventNotFoundException());
    }
}
