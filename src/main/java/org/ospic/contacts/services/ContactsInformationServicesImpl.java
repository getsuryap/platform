package org.ospic.contacts.services;

import org.ospic.contacts.domain.ContactsInformation;
import org.ospic.contacts.repository.ContactsInformationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
@Controller
@Service
public class ContactsInformationServicesImpl implements ContactsInformationService {
    private static final Logger logger = LoggerFactory.getLogger(ContactsInformationServicesImpl.class);

    @Autowired
    ContactsInformationRepository contactsInformationRepository;

    @Override
    public ContactsInformation createNewContact(Long id, ContactsInformation contactsInformation) {
        contactsInformation.setId(id);
        logger.info(contactsInformation.toString());
       return contactsInformation;//contactsInformationRepository.save(contactsInformation);
    }

    @Override
    public List<ContactsInformation> retrieveAllContactsInformation() {
        return contactsInformationRepository.findAll();
    }

    @Override
    public List<ContactsInformation> createNewContactsByIteration(List<ContactsInformation> contactsInformationList) {
        return contactsInformationRepository.saveAll(contactsInformationList);
    }

}
