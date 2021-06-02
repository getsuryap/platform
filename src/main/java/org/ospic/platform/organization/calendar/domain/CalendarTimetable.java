package org.ospic.platform.organization.calendar.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.ospic.platform.configurations.audit.Auditable;
import org.ospic.platform.organization.calendar.data.EventRequest;
import org.ospic.platform.util.constants.DatabaseConstants;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * This file was created by eli on 13/03/2021 for org.ospic.platform.organization.calendar.domain
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
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@Entity(name = DatabaseConstants.TABLE_CALENDAR)
@Table(name = DatabaseConstants.TABLE_CALENDAR)
@ApiModel(value = "Calendar", description = "Calendar payload")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@EqualsAndHashCode(callSuper = false)
public class CalendarTimetable extends Auditable implements Serializable {
    @Column(name = "name")
    private String name;
    @Column(name = "start")
    private LocalDateTime start;
    @Column(name = "end")
    private  LocalDateTime end;
    @Column(name = "timed")
    private Boolean timed;
    @Column(name = "description")
    private String description;
    @Column(name = "department")
    private Long departmentId;

    public CalendarTimetable getTimetableEvent(EventRequest r){
        LocalDateTime startDateTime = LocalDateTime.of(r.getStartDate(), r.getStartTime() == null ? LocalTime.MIDNIGHT :  r.getStartTime());
        LocalDateTime endDateTime = LocalDateTime.of(r.getEndDate(), r.getEndTime() == null ? LocalTime.MIDNIGHT : r.getEndTime());
        return new CalendarTimetable(r.getName(),startDateTime, endDateTime, r.getTimed(),r.getDescription(), r.getDepartmentId());
    }

    private CalendarTimetable(String name, LocalDateTime start, LocalDateTime end,  Boolean timed,  final String description, Long departmentId) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.timed = timed;
        this.description = description;
        this.departmentId = departmentId;
    }
}
