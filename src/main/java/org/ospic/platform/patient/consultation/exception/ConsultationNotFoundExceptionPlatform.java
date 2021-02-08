
package org.ospic.platform.patient.consultation.exception;

import org.ospic.platform.infrastructure.app.exception.AbstractPlatformResourceNotFoundException;

/**

 * This file was created by eli on 23/12/2020 for org.ospic.platform.patient.consultation.exceptions
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
 **/

public class ConsultationNotFoundExceptionPlatform extends AbstractPlatformResourceNotFoundException {
    public ConsultationNotFoundExceptionPlatform(String globalisationMessageCode, String defaultUserMessage, Object... defaultUserMessageArgs) {
        super(globalisationMessageCode, defaultUserMessage, defaultUserMessageArgs);
    }

    public ConsultationNotFoundExceptionPlatform(Long id) {
        super("error.message.service.not.found", String.format("Consultation with id %2d is not found ", id), id);
    }
}