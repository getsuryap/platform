package org.ospic.platform.configurations.smsconfigs.sms.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.twilio.rest.api.v2010.account.Message;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.joda.time.DateTime;
import org.ospic.platform.util.constants.DatabaseConstants;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

/**
 * This file was created by eli on 02/01/2021 for org.ospic.platform.configurations.smsconfigs.message.domain
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
@Data
@NoArgsConstructor
@Entity(name = DatabaseConstants.SMS_MESSAGE_TABLE)
@Table(name = DatabaseConstants.SMS_MESSAGE_TABLE)
@ApiModel(value = "SMS campaign", description = "Message campaign")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SmsCampaign implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private @Setter(AccessLevel.PROTECTED) Long id;


    @Column(name = "api_version")
    String apiVersion;
    @Column(name = "body")
    String body;
    @Column(name = "created")
    DateTime dateCreated;
    @Column(name = "updated")
    String dateUpdated;
    @Column(name = "sent")
    String dateSent;
    @Column(name = "direction")
    String direction;
    @Column(name = "error_code")
    Integer errorCode;
    @Column(name = "error_msg")
    String errorMessage;
    @Column(name = "from_phone")
    String from;
    @Column(name = "media_number")
    String numMedia;
    @Column(name = "segment_number")
    String numSegments;
    @Column(name = "price")
    BigDecimal price;
    @Column(name = "price_unit")
    Currency priceUnit;

    @Column(name = "status")
    String status;
    @Column(name = "to_phone")
    String toPhone;


    public SmsCampaign(String apiVersion, String body, DateTime dateCreated,
            String dateUpdated, String dateSent, String direction,
            Integer errorCode, String errorMessage, String from, String numMedia,
            String numSegments, BigDecimal price, Currency priceUnit,
            String status, String to) {

        this.apiVersion = apiVersion;
        this.body = body;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.dateSent = dateSent;
        this.direction = direction;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.from = from;
        this.numMedia = numMedia;
        this.numSegments = numSegments;
        this.price = price;
        this.priceUnit = priceUnit;

        this.status = status;
        this.toPhone = to;
    }

    public SmsCampaign instance(Message message){
        return new SmsCampaign( message.getApiVersion(), message.getBody(), getDateCreated(),
                ""+message.getDateUpdated(), ""+message.getDateSent(),
                message.getDirection().toString(), message.getErrorCode(), message.getErrorMessage(),
                message.getFrom().toString(), message.getNumMedia(), message.getNumSegments(), message.getPrice(), message.getPriceUnit(),
                 message.getStatus().toString(), message.getTo());
    }

}
