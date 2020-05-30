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
import java.util.regex.Pattern;

public class SurveyPayload {
    private String jsonQNAPayload;
    private HashMap<String, List<String>> surveyQuestionAnswerMap;
    private String CompleteJSSurvey;
    private Pattern ragPattern;

    public SurveyPayload(String jsSurvey,String jsonPayload, HashMap<String, List<String>> surveyQuestionAnswerMap) {
        this.jsonQNAPayload = jsonPayload;
        this.surveyQuestionAnswerMap = surveyQuestionAnswerMap;
        this.CompleteJSSurvey = jsSurvey;
        ragPattern = Pattern.compile("^[\\d-]+[(UNKNOWN)(RED)(AMBER)(GREEN)]+\\|(.*)$");
    }

    public String getQNAPayload() {
        return this.jsonQNAPayload;
    }

    public String getSurveyJSPayload() {
        return this.CompleteJSSurvey;
    }

    public String getAnswerText(String questionName, String ragValue) {
        String result=null;
        try {
            Pattern ragPattern = Pattern.compile("-");
            String[] res = ragPattern.split(ragValue);
            int index = Integer.valueOf(res[0]);
            List<String> x = surveyQuestionAnswerMap.get(questionName);
            if (x != null) result = x.get(index);

        } catch (Exception ex) {
            result = null;
        }
        return result;
    }
}
