package com.redhat.gps.pathfinder.web.api;

/*-
 * #%L
 * com.redhat.gps.pathfinder.pathfinder-server
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2018 - 2019 RedHat Inc
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

import com.redhat.gps.pathfinder.domain.Assessments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class QuestionReader<T> {

    private final Logger log = LoggerFactory.getLogger(QuestionReader.class);

    public T read(T result, String survey, Assessments assessment, CustomerAPIImpl.QuestionParser<T> parser) {
        mjson.Json surveyJson = mjson.Json.read(survey);
        mjson.Json pages = surveyJson.at("pages");
        for (mjson.Json page : pages.asJsonList()) {
            for (mjson.Json question : page.at("questions").asJsonList()) {

                if (question.at("type").asString().equals("radiogroup")) {

                    Map<String, String> answerRankingMap = new HashMap<>();
                    for (mjson.Json a : question.at("choices").asJsonList()) {
                        String answer = a.asString();
                        answerRankingMap.put(answer.substring(0, answer.indexOf("-")), answer.substring(answer.indexOf("-") + 1)); // answer id to ranking map
                    }

                    try {
                        if (assessment.getResults().containsKey(question.at("name").asString())) {

                            String name = question.at("name").asString();
                            String answerOrdinal = ((String) assessment.getResults().get(question.at("name").asString())).split("-")[0]; // should return integer of the value chosen
                            String answerRating = answerRankingMap.get(answerOrdinal).split("\\|")[0];
                            String answerText = answerRankingMap.get(answerOrdinal).split("\\|")[1];
                            String questionText = question.at("title").asString();
                            parser.parse(result, name, answerOrdinal, answerRating, answerText, questionText);
                        }

                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        log.error("Error on: assessment.results=" + assessment.getResults());
                        log.error("Error on: assessment.results.containsKey(" + question.at("name").asString() + ")=" + assessment.getResults().containsKey(question.at("name").asString()));
                        log.error("Error on: question.name=" + question.at("name").asString());
                        log.error("Error on: assessment.results[" + question.at("name").asString() + "]=" + assessment.getResults().get(question.at("name").asString()));
                    }

                } else  {
                    // leave this out since it's things like "Select the app..."
                }
            }
        }
        return result;
    }
}
