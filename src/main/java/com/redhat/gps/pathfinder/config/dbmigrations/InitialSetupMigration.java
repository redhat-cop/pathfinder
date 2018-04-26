package com.redhat.gps.pathfinder.config.dbmigrations;

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

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.redhat.gps.pathfinder.config.PathfinderQuestionConfig;
import com.redhat.gps.pathfinder.domain.*;
import com.redhat.gps.pathfinder.security.AuthoritiesConstants;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Instant;
import java.util.ArrayList;

import static com.redhat.gps.pathfinder.web.api.QuestionsMetaDataAPI.MIN_ASSESSMENT_VALUES;

/**
 * Creates the initial database setup
 */
@ChangeLog(order = "001")
public class InitialSetupMigration {

    @ChangeSet(order = "01", author = "initiator", id = "01-addAuthorities")
    public void addAuthorities(MongoTemplate mongoTemplate) {
        Authority adminAuthority = new Authority();
        adminAuthority.setName(AuthoritiesConstants.ADMIN);
        Authority userAuthority = new Authority();
        userAuthority.setName(AuthoritiesConstants.USER);
        mongoTemplate.save(adminAuthority);
        mongoTemplate.save(userAuthority);
    }

    @ChangeSet(order = "02", author = "initiator", id = "02-addUsers")
    public void addUsers(MongoTemplate mongoTemplate) {
        Authority adminAuthority = new Authority();
        adminAuthority.setName(AuthoritiesConstants.ADMIN);
        Authority userAuthority = new Authority();
        userAuthority.setName(AuthoritiesConstants.USER);

        User systemUser = new User();
        systemUser.setId("user-0");
        systemUser.setLogin("system");
        systemUser.setPassword("$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG");
        systemUser.setFirstName("");
        systemUser.setLastName("System");
        systemUser.setEmail("system@localhost");
        systemUser.setActivated(true);
        systemUser.setLangKey("en");
        systemUser.setCreatedBy(systemUser.getLogin());
        systemUser.setCreatedDate(Instant.now());
        systemUser.getAuthorities().add(adminAuthority);
        systemUser.getAuthorities().add(userAuthority);
        mongoTemplate.save(systemUser);

        User anonymousUser = new User();
        anonymousUser.setId("user-1");
        anonymousUser.setLogin("anonymoususer");
        anonymousUser.setPassword("$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO");
        anonymousUser.setFirstName("Anonymous");
        anonymousUser.setLastName("User");
        anonymousUser.setEmail("anonymous@localhost");
        anonymousUser.setActivated(true);
        anonymousUser.setLangKey("en");
        anonymousUser.setCreatedBy(systemUser.getLogin());
        anonymousUser.setCreatedDate(Instant.now());
        mongoTemplate.save(anonymousUser);

        User adminUser = new User();
        adminUser.setId("user-2");
        adminUser.setLogin("admin");
        adminUser.setPassword("$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC");
        adminUser.setFirstName("admin");
        adminUser.setLastName("Administrator");
        adminUser.setEmail("admin@localhost");
        adminUser.setActivated(true);
        adminUser.setLangKey("en");
        adminUser.setCreatedBy(systemUser.getLogin());
        adminUser.setCreatedDate(Instant.now());
        adminUser.getAuthorities().add(adminAuthority);
        adminUser.getAuthorities().add(userAuthority);
        mongoTemplate.save(adminUser);

        User userUser = new User();
        userUser.setId("user-3");
        userUser.setLogin("user");
        userUser.setPassword("$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K");
        userUser.setFirstName("");
        userUser.setLastName("User");
        userUser.setEmail("user@localhost");
        userUser.setActivated(true);
        userUser.setLangKey("en");
        userUser.setCreatedBy(systemUser.getLogin());
        userUser.setCreatedDate(Instant.now());
        userUser.getAuthorities().add(userAuthority);
        mongoTemplate.save(userUser);
    }

        @ChangeSet(order = "03", author = "initiator", id = "03-addQuestionWeights",runAlways = true)
    public void addQuestionWeights(MongoTemplate mongoTemplate) {
        QuestionMetaData currData = null;
        currData = PathfinderQuestionConfig.QuestionARCHTYPE();
        mongoTemplate.save(currData);
        currData = PathfinderQuestionConfig.QuestionCLUSTER();
        mongoTemplate.save(currData);
        currData = PathfinderQuestionConfig.QuestionCOMMS();
        mongoTemplate.save(currData);
        currData = PathfinderQuestionConfig.QuestionCOMPLIANCE();
        mongoTemplate.save(currData);
        currData = PathfinderQuestionConfig.QuestionCONFIG();
        mongoTemplate.save(currData);
        currData = PathfinderQuestionConfig.QuestionCONTAINERS();
        mongoTemplate.save(currData);
        currData = PathfinderQuestionConfig.QuestionDEPLOY();
        mongoTemplate.save(currData);
        currData = PathfinderQuestionConfig.QuestionDEPS3RD();
        mongoTemplate.save(currData);
        currData = PathfinderQuestionConfig.QuestionDEPSHW();
        mongoTemplate.save(currData);
        currData = PathfinderQuestionConfig.QuestionDEPSIN();
        mongoTemplate.save(currData);
        currData = PathfinderQuestionConfig.QuestionDEPSOS();
        mongoTemplate.save(currData);
        currData = PathfinderQuestionConfig.QuestionDEPSOUT();
        mongoTemplate.save(currData);
        currData = PathfinderQuestionConfig.QuestionHA();
        mongoTemplate.save(currData);
        currData = PathfinderQuestionConfig.QuestionHEALTH();
        mongoTemplate.save(currData);
        currData = PathfinderQuestionConfig.QuestionLOGS();
        mongoTemplate.save(currData);
        currData = PathfinderQuestionConfig.QuestionMETRICS();
        mongoTemplate.save(currData);
        currData = PathfinderQuestionConfig.QuestionOWNER();
        mongoTemplate.save(currData);
        currData = PathfinderQuestionConfig.QuestionPROFILE();
        mongoTemplate.save(currData);
        currData = PathfinderQuestionConfig.QuestionOWNER();
        mongoTemplate.save(currData);
        currData = PathfinderQuestionConfig.QuestionRESILIENCY();
        mongoTemplate.save(currData);
        currData = PathfinderQuestionConfig.QuestionSECURITY();
        mongoTemplate.save(currData);
        currData = PathfinderQuestionConfig.QuestionSTATE();
        mongoTemplate.save(currData);
        currData = PathfinderQuestionConfig.QuestionTEST();
        mongoTemplate.save(currData);

    }

}
