package com.redhat.gps.pathfinder.questions;
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
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class QuestionValidationTest {
    @Test
    public void givenValidInput_whenValidating_thenValid() throws ValidationException, JSONException {
        JSONObject jsonSchema;
        InputStream schemaFile = QuestionValidationTest.class.getResourceAsStream("../../../../../questions/question-schema.json");
        InputStream questions = QuestionValidationTest.class.getResourceAsStream("../../../../../test-data/question-data.json");
        jsonSchema = new JSONObject(
                new JSONTokener(new InputStreamReader(schemaFile)));
        JSONObject jsonSubject = new JSONObject(
                new JSONTokener(new InputStreamReader(questions)));

        Schema schema = SchemaLoader.load(jsonSchema);
        schema.validate(jsonSubject);
        System.out.println("Validation passed " + jsonSubject.toString());
    }

    @Test
    public void givenCorruptInput_whenValidating_thenValid() throws ValidationException, JSONException {
        Exception exception = assertThrows(org.json.JSONException.class, () -> {
            JSONObject jsonSchema;
            InputStream schemaFile = QuestionValidationTest.class.getResourceAsStream("../../../../../questions/question-schema.json");
            InputStream questions = QuestionValidationTest.class.getResourceAsStream("../../../../../test-data/question-data-corrupt.json");

            jsonSchema = new JSONObject(
                    new JSONTokener(new InputStreamReader(schemaFile)));
            JSONObject jsonSubject = new JSONObject(
                    new JSONTokener(new InputStreamReader(questions)));

            Schema schema = SchemaLoader.load(jsonSchema);
            schema.validate(jsonSubject);
        });
        System.out.println("Exception as expected :" +exception.getMessage());
    }

    @Test
    public void givenInvalidQuestionInput_whenValidating_thenValid() throws ValidationException, JSONException {
        Exception exception = assertThrows(org.everit.json.schema.ValidationException.class, () -> {
            JSONObject jsonSchema;
            InputStream schemaFile = QuestionValidationTest.class.getResourceAsStream("../../../../../questions/question-schema.json");
            InputStream questions = QuestionValidationTest.class.getResourceAsStream("../../../../../test-data/question-data-invalid-questions.json");

            jsonSchema = new JSONObject(
                    new JSONTokener(new InputStreamReader(schemaFile)));
            JSONObject jsonSubject = new JSONObject(
                    new JSONTokener(new InputStreamReader(questions)));

            Schema schema = SchemaLoader.load(jsonSchema);
            schema.validate(jsonSubject);
        });
        System.out.println("Exception as expected :" +exception.getMessage());
    }

    @Test
    public void givenValidInput_Enrich() throws ValidationException, JSONException {
        JSONObject jsonSchema;
        InputStream schemaFile = QuestionValidationTest.class.getResourceAsStream("../../../../../questions/question-schema.json");
        InputStream jsonquestions = QuestionValidationTest.class.getResourceAsStream("../../../../../test-data/question-data.json");
        jsonSchema = new JSONObject(
                new JSONTokener(new InputStreamReader(schemaFile)));
        JSONObject jsonSubject = new JSONObject(
                new JSONTokener(new InputStreamReader(jsonquestions)));

        Schema schema = SchemaLoader.load(jsonSchema);
        schema.validate(jsonSubject);
        System.out.println("Validation passed");


        JSONArray pages = jsonSubject.getJSONArray("pages");
        JSONObject newSurvey = jsonSubject;
        JSONArray newPages = new JSONArray();
        for (int j = 0; j < pages.length(); j++) {
            JSONObject page = pages.getJSONObject(j);
            JSONArray questions = page.getJSONArray("questions");
            JSONArray newQuestions = new JSONArray();

            for (int i = 0; i < questions.length(); i++) {
                JSONObject x = questions.getJSONObject(i);
                x.put("type", "radiogroup");
                x.put("isRequired", "true");
                x.put("colCount", "1");
                newQuestions.put(x);
            }
            JSONObject pageComment = new JSONObject();
            pageComment.put("type", "comment");
            pageComment.put("name", "NOTES" + j);
            pageComment.put("title", "Additional notes or comments");
            pageComment.put("isRequired", "false");
            newQuestions.put(pageComment);
            page.remove("questions");
            page.put("quesions", newQuestions);
            newPages.put(j, page);
        }

        newSurvey.put("pages", newPages);
        newSurvey.put("title", "Application Assessment");
        newSurvey.put("sendResultOnPageNext", "true");
        newSurvey.put("requiredText", "");
        newSurvey.put("showProgressBar", "bottom");
        newSurvey.put("completedHtml", "<p><h4>Thank you for completing the Pathfinder Assessment.  Please click <a id='surveyCompleteLink' href='/assessments.jsp?customerId={CUSTID}'>Here</a> to return to the main page.");

        System.out.println(newSurvey);
    }
}
