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


    public static JSONObject ValidateQuestionData(String questionData, String questionSchema) throws JSONException {
        JSONObject jsonSchema;
        JSONObject QuestionsJSON;
        jsonSchema = new JSONObject(
                new JSONTokener(questionSchema));
        Schema schema = SchemaLoader.load(jsonSchema);
        QuestionsJSON = new JSONObject(
                new JSONTokener(questionData));
        schema.validate(QuestionsJSON);
        return QuestionsJSON;
    }

    /**
     * @param questionData - String containing the survey questions as a json payload
     * @return String - survey questions enriched as a json payload
     * @throws IOException
     * @throws JSONException
     */
    public String GenerateSurveyPages(String questionData, String customQuestions, String questionSchema) throws JSONException {
        JSONObject customQuestionsJSON;
        JSONObject defaultQuestionsJSON;
        JSONArray customPages = null;

        defaultQuestionsJSON = QuestionProcessor.ValidateQuestionData(questionData, questionSchema);

        if (!customQuestions.isEmpty()) {
            try {
                customQuestionsJSON = QuestionProcessor.ValidateQuestionData(customQuestions, questionSchema);
                customPages = customQuestionsJSON.getJSONArray("pages");
            } catch (Exception ex) {
                log.error("Unable to parse custom questions...continuing", ex);
            }
        }

        JSONArray pages = defaultQuestionsJSON.getJSONArray("pages");
        JSONObject newSurvey = defaultQuestionsJSON;

        JSONArray newPages = new JSONArray();
        for (int pageIndex = 0; pageIndex < pages.length(); pageIndex++) {
            JSONObject page = pages.getJSONObject(pageIndex);
            JSONArray questions = page.getJSONArray("questions");
            JSONArray newQuestions = new JSONArray();

            for (int i = 0; i < questions.length(); i++) {
                JSONObject x = questions.getJSONObject(i);
                x.put("type", "radiogroup");
                x.put("isRequired", "true");
                x.put("colCount", "1");
                newQuestions.put(x);
            }

            //Need to manually add dependencies list at the end of page 1
            if (pageIndex == 1){
                JSONObject dependenciesIN = new JSONObject();
                dependenciesIN.put("name","DEPSINLIST");
                dependenciesIN.put("type", "tagbox");
                dependenciesIN.put("renderAs", "select2");
                dependenciesIN.put("title", "Please add northbound dependencies...");
                dependenciesIN.put("visibleIf", "{DEPSIN} notcontains '5'");
                dependenciesIN.put("isRequired","false");
                dependenciesIN.put("colCount", "3");
                dependenciesIN.put("choicesByUrl",new JSONObject());
                newQuestions.put(dependenciesIN);

                JSONObject dependenciesOUT = new JSONObject();
                dependenciesIN.put("name","DEPSOUTLIST");
                dependenciesOUT.put("type", "tagbox");
                dependenciesOUT.put("renderAs", "select2");
                dependenciesOUT.put("title", "Please add southbound dependencies...");
                dependenciesOUT.put("visibleIf", "{DEPSOUT} notcontains '5'");
                dependenciesOUT.put("isRequired","false");
                dependenciesOUT.put("colCount", "3");
                dependenciesOUT.put("choicesByUrl",new JSONObject());
                newQuestions.put(dependenciesOUT);
            }

            JSONObject pageComment = new JSONObject();
            pageComment.put("type", "comment");
            pageComment.put("name", "NOTESONPAGE" + pageIndex);
            pageComment.put("title", "Additional notes or comments");
            pageComment.put("isRequired", "false");
            newQuestions.put(pageComment);
            page.remove("questions");
            page.put("questions", newQuestions);
            newPages.put(pageIndex, page);
        }

        if ((customPages != null) && (customPages.length() > 0)) {
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
                    x.put("name", "customQuestion" + i);
                    newQuestions.put(x);
                }
                JSONObject pageComment = new JSONObject();
                pageComment.put("type", "comment");
                pageComment.put("name", "NOTESONPAGE" + (j + startPages));
                pageComment.put("title", "Additional notes or comments");
                pageComment.put("isRequired", "false");
                newQuestions.put(pageComment);
                page.remove("questions");
                page.put("questions", newQuestions);
                newPages.put(j + startPages, page);
            }
        }

        newSurvey.put("pages", newPages);
        newSurvey.put("title", "Application Assessment");
        newSurvey.put("sendResultOnPageNext", "true");
        newSurvey.put("requiredText", "");
        newSurvey.put("showProgressBar", "bottom");
        newSurvey.put("completedHtml", "<p><h4>Thank you for completing the Pathfinder Assessment.  Please click <a id='surveyCompleteLink' href='/assessments.jsp?customerId={CUSTID}'><b>Here</b></a> to return to the main page.");



//        JSONObject dependenciesIN = new JSONObject();
//        dependenciesIN.put("name","DEPSINLIST");
//        dependenciesIN.put("type", "tagbox");
//        dependenciesIN.put("renderAs", "select2");
//        dependenciesIN.put("title", "Please add northbound dependencies...");
//        dependenciesIN.put("visibleIf", "{DEPSIN} notcontains '5'");
//        dependenciesIN.put("isRequired",false);
//        dependenciesIN.put("colCount", 3);
//        dependenciesIN.put("choicesByUrl",new JSONObject());
//
//        JSONObject dependenciesOUT = new JSONObject();
//        dependenciesIN.put("name","DEPSOUTLIST");
//        dependenciesOUT.put("type", "tagbox");
//        dependenciesOUT.put("renderAs", "select2");
//        dependenciesOUT.put("title", "Please add southbound dependencies...");
//        dependenciesOUT.put("visibleIf", "{DEPSOUT} notcontains '5'");
//        dependenciesOUT.put("isRequired",false);
//        dependenciesOUT.put("colCount", 3);
//        dependenciesOUT.put("choicesByUrl",new JSONObject());

        return newSurvey.toString();
    }
}
