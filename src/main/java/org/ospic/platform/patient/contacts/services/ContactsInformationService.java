package org.ospic.platform.patient.contacts.services;

import org.ospic.platform.patient.contacts.domain.ContactsInformation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
/**
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

@Component
@Service
public interface ContactsInformationService {
    public ContactsInformation createNewContact(Long patientId, ContactsInformation contactsInformation);
    public List<ContactsInformation> retrieveAllContactsInformation();
    public List<ContactsInformation> createNewContactsByIteration(List<ContactsInformation> contactsInformationList);
    public ResponseEntity retrievePatientContactByPatientId(Long patientId);
    ResponseEntity<?> updateContactInformation(Long contactId, ContactsInformation payload);
}
