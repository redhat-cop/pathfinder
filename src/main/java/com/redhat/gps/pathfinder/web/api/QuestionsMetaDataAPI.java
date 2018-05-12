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

import com.codahale.metrics.annotation.Timed;
import com.redhat.gps.pathfinder.domain.Assessments;
import com.redhat.gps.pathfinder.domain.QuestionMetaData;
import com.redhat.gps.pathfinder.repository.AssessmentsRepository;
import com.redhat.gps.pathfinder.repository.QuestionMetaDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for retrieving question metadata.
 */
@RestController
@RequestMapping("/api/static")
public class QuestionsMetaDataAPI {
    public final static String MIN_ASSESSMENT_VALUES="MIN_ASSESSMENT_VALUE";

    private final Logger log = LoggerFactory.getLogger(QuestionsMetaDataAPI.class);

    private static final String ENTITY_NAME = "customer";

    private final QuestionMetaDataRepository questionRepository;
    private final AssessmentsRepository assmRepository;

    public QuestionsMetaDataAPI(QuestionMetaDataRepository questionRepository,AssessmentsRepository assmRepo) {
        this.questionRepository = questionRepository;
        this.assmRepository = assmRepo;
    }


    /**
     * GET  /questions : get all the get question metadata.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of customers in body
     */
    @GetMapping(value = "/questions",produces = "application/json")
    @Timed
    public List<QuestionMetaData> getAllQuestionMetadata() {
        log.debug("REST request to get question metadata ");
        return questionRepository.findAll();
    }

    @GetMapping(value= "/minimum",produces = "application/json")
    public Assessments getMinValues(){
        log.debug("REST request to get minimum values ");
        return assmRepository.findOne(MIN_ASSESSMENT_VALUES);
    }

}
