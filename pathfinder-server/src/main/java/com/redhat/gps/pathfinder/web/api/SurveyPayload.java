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

import java.util.HashMap;
import java.util.List;

public class SurveyPayload {
    private String jsonQNAPayload;
    private HashMap<String, List<String>> surveyQuestionAnswerMap;
    private String CompleteJSSurvey;

    public SurveyPayload(String jsSurvey,String jsonPayload, HashMap<String, List<String>> surveyQuestionAnswerMap) {
        this.jsonQNAPayload = jsonPayload;
        this.surveyQuestionAnswerMap = surveyQuestionAnswerMap;
        this.CompleteJSSurvey = jsSurvey;
    }

    public String getQNAPayload() {
        return this.jsonQNAPayload;
    }

    public String getSurveyJSPayload() {
        return this.CompleteJSSurvey;
    }

    public String getAnswerText(String questionName, int index) {
        String result = null;
        try {
            List<String> x = surveyQuestionAnswerMap.get(questionName);
            if (x != null) {
                result = x.get(index);
            }
        } catch (Exception ex) {
            //return null by default
        }
        return result;
    }
}
