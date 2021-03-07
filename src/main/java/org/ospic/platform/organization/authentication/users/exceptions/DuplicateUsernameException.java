package org.ospic.platform.organization.authentication.users.exceptions;

import org.ospic.platform.infrastructure.app.exception.AbstractPlatformException;

/**
 * This file was created by eli on 16/02/2021 for org.ospic.platform.organization.authentication.users.exceptions
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
public class DuplicateUsernameException extends AbstractPlatformException {
    protected DuplicateUsernameException(String globalisationMessageCode, String defaultUserMessage, Object... defaultUserMessageArgs) {
        super(globalisationMessageCode, defaultUserMessage, defaultUserMessageArgs);
    }

    public DuplicateUsernameException(String username){
        super("error.msg.duplicate.username", String.format("Warning! Duplicate username. Another user is using the same username %s", username));
    }
    public DuplicateUsernameException(String code, String message){
        super(code, message);
    }
}
