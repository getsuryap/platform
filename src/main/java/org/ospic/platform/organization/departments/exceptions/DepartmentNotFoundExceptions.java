package org.ospic.platform.organization.departments.exceptions;

import org.ospic.platform.infrastructure.app.exception.AbstractResourceNotFoundException;

/**
 * This file was created by eli on 09/01/2021 for org.ospic.platform.organization.departments.exceptions
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
public class DepartmentNotFoundExceptions extends AbstractResourceNotFoundException{
    public DepartmentNotFoundExceptions(String globalisationMessageCode, String defaultUserMessage, Object... defaultUserMessageArgs) {
        super(globalisationMessageCode, defaultUserMessage, defaultUserMessageArgs);
    }

    public DepartmentNotFoundExceptions(Long id){
        super("error.message.department.not.found", String.format("Department with id %2d is not found ", id), id);
    }
}
