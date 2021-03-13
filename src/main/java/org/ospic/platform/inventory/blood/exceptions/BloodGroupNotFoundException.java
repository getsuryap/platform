package org.ospic.platform.inventory.blood.exceptions;

import org.ospic.platform.infrastructure.app.exception.AbstractPlatformException;

/**
 * This file was created by eli on 13/03/2021 for org.ospic.platform.inventory.blood.exceptions
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
public class BloodGroupNotFoundException extends AbstractPlatformException {
    protected BloodGroupNotFoundException(String globalisationMessageCode, String defaultUserMessage) {
        super(globalisationMessageCode, defaultUserMessage);
    }

    public BloodGroupNotFoundException(Long id){
        super("error.msg.blood.group.not.found", String.format("Blood group with ID %2d is not found", id));
    }
}
