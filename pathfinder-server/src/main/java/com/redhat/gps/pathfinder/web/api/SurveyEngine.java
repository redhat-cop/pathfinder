package com.redhat.gps.pathfinder.web.api;

/*-
 * #%L
 * com.redhat.gps.pathfinder.pathfinder-server
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2018 - 2020 RedHat Inc
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


import com.redhat.gps.pathfinder.QuestionProcessor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

@Component
public class SurveyEngine {
    private final Logger log = LoggerFactory.getLogger(SurveyEngine.class);
    private String SurveyQuestionsJSON;
    private String finalJScriptDefn;
    private HashMap<String, List<String>> QNAStore;

    @Value("${CUSTOM_QUESTIONS:}")
    private String customQuestionsFileLocation;



    @PostConstruct
    public void init() throws IOException, JSONException {
        this.getSurveyContent();
        this.QNAStore = new QuestionProcessor().GenerateSurveyQA(this.SurveyQuestionsJSON);
    }

    @Bean
    @Scope("singleton")
    public SurveyPayload surveySingleton() {
        return new SurveyPayload(this.finalJScriptDefn,this.SurveyQuestionsJSON,this.QNAStore);
    }


    private void getSurveyContent() throws IOException {
        String rawQuestionsJson = "";
        String questionsJsonSchema = "";
        String customQuestionsJson = "";

        if ((customQuestionsFileLocation != null) && (!customQuestionsFileLocation.isEmpty())) {
            File customQuestionsFile = new File(customQuestionsFileLocation);
            try (InputStream cqis = new FileInputStream(customQuestionsFile);) {
                customQuestionsJson = getResourceAsString(cqis);
                log.info("Successfully read custom questions file {}", customQuestionsFileLocation);
            } catch (Exception ex) {
                log.error("Unable to load custom questions file {}", customQuestionsFileLocation);
                customQuestionsJson = "";
            }
        }

        try (InputStream is1 = CustomerAPIImpl.class.getClassLoader().getResourceAsStream("questions/base-questions-data-default.json");
             InputStream is2 = CustomerAPIImpl.class.getClassLoader().getResourceAsStream("questions/question-schema.json");
        ) {
            rawQuestionsJson = getResourceAsString(is1);
            questionsJsonSchema = getResourceAsString(is2);
            this.SurveyQuestionsJSON = new QuestionProcessor().GenerateSurveyPages(rawQuestionsJson, customQuestionsJson, questionsJsonSchema);
            log.info("Successfully generated Survey Questions");
        } catch (Exception e) {
            InputStream is3 = CustomerAPIImpl.class.getClassLoader().getResourceAsStream("questions/default-survey-materialised.json");
            this.SurveyQuestionsJSON = getResourceAsString(is3);
            if (is3 != null) is3.close();
            log.error("Unable to find/parse question-data-default...using default-survey...turn on debug for more info");
            log.trace("getSurveyContent raw {} schema {}", rawQuestionsJson, questionsJsonSchema);
            e.printStackTrace();
        }

        try (InputStream is4 = CustomerAPIImpl.class.getClassLoader().getResourceAsStream("questions/application-survey.js");) {
            String surveyJs = getResourceAsString(is4);
            this.finalJScriptDefn = (surveyJs.replace("$$QUESTIONS_JSON$$", this.SurveyQuestionsJSON));
        } catch (Exception ex) {
            log.error("Unable to process and enrich the question template....FATAL ERROR ", ex);
            System.exit(42);
        }
    }

    private static String getResourceAsString(InputStream is) throws IOException {
        Validate.notNull(is);
        return IOUtils.toString(is, "UTF-8");
    }


}
