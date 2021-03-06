package org.ospic.platform.organization.calendar.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This file was created by eli on 04/05/2021 for org.ospic.platform.organization.calendar.data
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
public enum EventColor {
    BLUE("blue"),
    INDIGO("indigo"),
    DEEP_PURPLE("deep-purple"),
    CYAN("cyan"),
    GREEN("green"),
    ORANGE("orange"),
    GREY_DARKEN("grey darken-1");

    public final String color;
    private EventColor(String color) { this.color = color; }
    public String getStatus() { return this.color; }
    public boolean equals(String s) { return this.color.equals(s); }


    private static final List<EventColor> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public static EventColor randomColors()  { return VALUES.get(RANDOM.nextInt(SIZE)); }

}
