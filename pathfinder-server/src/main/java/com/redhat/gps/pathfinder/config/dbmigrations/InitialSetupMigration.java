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



//    @ChangeSet(order = "01", author = "initiator", id = "01-addQuestionWeights",runAlways = true)
//    public void addQuestionWeights(MongoTemplate mongoTemplate) {
//        QuestionMetaData currData = null;
//        currData = PathfinderQuestionConfig.QuestionARCHTYPE();
//        mongoTemplate.save(currData);
//        currData = PathfinderQuestionConfig.QuestionCLUSTER();
//        mongoTemplate.save(currData);
//        currData = PathfinderQuestionConfig.QuestionCOMMS();
//        mongoTemplate.save(currData);
//        currData = PathfinderQuestionConfig.QuestionCOMPLIANCE();
//        mongoTemplate.save(currData);
//        currData = PathfinderQuestionConfig.QuestionCONFIG();
//        mongoTemplate.save(currData);
//        currData = PathfinderQuestionConfig.QuestionCONTAINERS();
//        mongoTemplate.save(currData);
//        currData = PathfinderQuestionConfig.QuestionDEPLOY();
//        mongoTemplate.save(currData);
//        currData = PathfinderQuestionConfig.QuestionDEPS3RD();
//        mongoTemplate.save(currData);
//        currData = PathfinderQuestionConfig.QuestionDEPSHW();
//        mongoTemplate.save(currData);
//        currData = PathfinderQuestionConfig.QuestionDEPSIN();
//        mongoTemplate.save(currData);
//        currData = PathfinderQuestionConfig.QuestionDEPSOS();
//        mongoTemplate.save(currData);
//        currData = PathfinderQuestionConfig.QuestionDEPSOUT();
//        mongoTemplate.save(currData);
//        currData = PathfinderQuestionConfig.QuestionHA();
//        mongoTemplate.save(currData);
//        currData = PathfinderQuestionConfig.QuestionHEALTH();
//        mongoTemplate.save(currData);
//        currData = PathfinderQuestionConfig.QuestionLOGS();
//        mongoTemplate.save(currData);
//        currData = PathfinderQuestionConfig.QuestionMETRICS();
//        mongoTemplate.save(currData);
//        currData = PathfinderQuestionConfig.QuestionOWNER();
//        mongoTemplate.save(currData);
//        currData = PathfinderQuestionConfig.QuestionPROFILE();
//        mongoTemplate.save(currData);
//        currData = PathfinderQuestionConfig.QuestionOWNER();
//        mongoTemplate.save(currData);
//        currData = PathfinderQuestionConfig.QuestionRESILIENCY();
//        mongoTemplate.save(currData);
//        currData = PathfinderQuestionConfig.QuestionSECURITY();
//        mongoTemplate.save(currData);
//        currData = PathfinderQuestionConfig.QuestionSTATE();
//        mongoTemplate.save(currData);
//        currData = PathfinderQuestionConfig.QuestionTEST();
//        mongoTemplate.save(currData);
//
//    }

}
