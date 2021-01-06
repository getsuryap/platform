package org.ospic.platform.inventory.admission.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.ospic.platform.inventory.beds.domains.Bed;

import java.io.IOException;

/**
 * This file was created by eli on 21/11/2020 for org.ospic.platform.inventory.admission
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
public class WardsBedSerializer extends StdSerializer<Bed> {

    public WardsBedSerializer(){
        this(null);
    }
    public WardsBedSerializer(Class<Bed> t){
        super(t);
    }
    @Override
    public void serialize(Bed bed, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeNumberField("id", bed.getId());
        jgen.writeStringField("identifier",bed.getIdentifier());
        jgen.writeBooleanField("isOccupied", bed.getIsOccupied());
        jgen.writeNumberField("wardId", bed.getWard().getId());
        jgen.writeStringField("wardName", bed.getWard().getName());
        jgen.writeEndObject();
    }
}
