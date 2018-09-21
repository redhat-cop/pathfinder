package com.redhat.gps.pathfinder.web.api;

/*-
 * #%L
 * Pathfinder
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2018 RedHat
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.redhat.gps.pathfinder.domain.Member;
import com.redhat.gps.pathfinder.repository.MembersRepository;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
    private final Logger log = LoggerFactory.getLogger(ApplicationStartup.class);
  
    @Autowired
    private MembersRepository membersRepo;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
      if (0==membersRepo.findAll().size()){
        log.debug("No users found in database, creating default \"admin\" user with username & password as \"admin\"");
        Member admin=new Member();
        admin.setUsername("admin");
        admin.setRoles(Arrays.asList("SUPER"));
        admin.setDisplayName("Admin");
        admin.setPassword("admin");
        admin.setCustomerId(null);
        
        membersRepo.save(admin);
      }
    }
 }
