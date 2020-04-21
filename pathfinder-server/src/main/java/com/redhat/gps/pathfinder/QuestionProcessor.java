package com.redhat.gps.pathfinder;

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

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class QuestionProcessor {
    private final Logger log = LoggerFactory.getLogger(QuestionProcessor.class);

    /**
     * @param questionData - String containing the survey questions as a json payload
     * @return String - survey questions enriched as a json payload
     * @throws IOException
     * @throws JSONException
     */
    public static String GenerateSurveyPages(String questionData, String customQuestions, String questionSchema) throws IOException, JSONException {
        JSONObject jsonSchema;
        JSONObject customQuestionsJSON;
        JSONObject defaultQuestionsJSON;
        JSONArray customPages = null;

        jsonSchema = new JSONObject(
                new JSONTokener(questionSchema));

        Schema schema = SchemaLoader.load(jsonSchema);

        defaultQuestionsJSON = new JSONObject(
                new JSONTokener(questionData));

        schema.validate(defaultQuestionsJSON);

        if (!customQuestions.isEmpty()) {
            customQuestionsJSON = new JSONObject(
                    new JSONTokener(customQuestions));
            schema.validate(defaultQuestionsJSON);
            customPages = customQuestionsJSON.getJSONArray("pages");
        }

        JSONArray pages = defaultQuestionsJSON.getJSONArray("pages");
        JSONObject newSurvey = defaultQuestionsJSON;

        JSONArray newPages = new JSONArray();
        for (int j = 0; j < pages.length(); j++) {
            JSONObject page = pages.getJSONObject(j);
            JSONArray questions = page.getJSONArray("questions");
            JSONArray newQuestions = new JSONArray();

            for (int i = 0; i < questions.length(); i++) {
                JSONObject x = questions.getJSONObject(i);
                x.put("type", "radiogroup");
                x.put("isRequired", "false");
                x.put("colCount", "1");
                newQuestions.put(x);
            }
            JSONObject pageComment = new JSONObject();
            pageComment.put("type", "comment");
            pageComment.put("name", "NOTESONPAGE" + j);
            pageComment.put("title", "Additional notes or comments");
            pageComment.put("isRequired", "false");
            newQuestions.put(pageComment);
            page.remove("questions");
            page.put("questions", newQuestions);
            newPages.put(j, page);
        }

        if ((customPages != null)&&(customPages.length()>0)) {
            int startPages = pages.length();

            for (int j = 0; j < customPages.length(); j++) {
                JSONObject page = customPages.getJSONObject(j);
                JSONArray questions = page.getJSONArray("questions");
                JSONArray newQuestions = new JSONArray();

                for (int i = 0; i < questions.length(); i++) {
                    JSONObject x = questions.getJSONObject(i);
                    x.put("type", "radiogroup");
                    x.put("isRequired", "false");
                    x.put("colCount", "1");
                    x.put("name","customQuestion"+i);
                    newQuestions.put(x);
                }
                JSONObject pageComment = new JSONObject();
                pageComment.put("type", "comment");
                pageComment.put("name", "NOTESONPAGE" + (j+startPages));
                pageComment.put("title", "Additional notes or comments");
                pageComment.put("isRequired", "false");
                newQuestions.put(pageComment);
                page.remove("questions");
                page.put("questions", newQuestions);
                newPages.put(j+startPages, page);
            }
        }

        newSurvey.put("pages", newPages);
        newSurvey.put("title", "Application Assessment");
        newSurvey.put("sendResultOnPageNext", "true");
        newSurvey.put("requiredText", "");
        newSurvey.put("showProgressBar", "bottom");
        newSurvey.put("completedHtml", "<p><h4>Thank you for completing the Pathfinder Assessment.  Please click <a id='surveyCompleteLink' href='/assessments.jsp?customerId={CUSTID}'><b>Here</b></a> to return to the main page.");
        return newSurvey.toString();
    }
}
