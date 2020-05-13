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

import com.redhat.gps.pathfinder.QuestionProcessor;
import com.redhat.gps.pathfinder.web.api.CustomerAPIImpl;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import org.apache.commons.io.IOUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

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
        System.out.println("Exception as expected :" + exception.getMessage());
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
        System.out.println("Exception as expected :" + exception.getMessage());
    }

//    @Test
//    public void givenValidInput_Enrich() throws ValidationException, JSONException {
//        JSONObject jsonSchema;
//        InputStream schemaFile = QuestionValidationTest.class.getResourceAsStream("../../../../../questions/question-schema.json");
//        InputStream jsonquestions = QuestionValidationTest.class.getResourceAsStream("../../../../../test-data/question-data.json");
//        jsonSchema = new JSONObject(
//                new JSONTokener(new InputStreamReader(schemaFile)));
//        JSONObject jsonSubject = new JSONObject(
//                new JSONTokener(new InputStreamReader(jsonquestions)));
//
//        Schema schema = SchemaLoader.load(jsonSchema);
//        schema.validate(jsonSubject);
//        System.out.println("Validation passed");
//
//
//        JSONArray pages = jsonSubject.getJSONArray("pages");
//        JSONObject newSurvey = jsonSubject;
//        JSONArray newPages = new JSONArray();
//        for (int j = 0; j < pages.length(); j++) {
//            JSONObject page = pages.getJSONObject(j);
//            JSONArray questions = page.getJSONArray("questions");
//            JSONArray newQuestions = new JSONArray();
//
//            for (int i = 0; i < questions.length(); i++) {
//                JSONObject x = questions.getJSONObject(i);
//                x.put("type", "radiogroup");
//                x.put("isRequired", "true");
//                x.put("colCount", "1");
//                newQuestions.put(x);
//            }
//            JSONObject pageComment = new JSONObject();
//            pageComment.put("type", "comment");
//            pageComment.put("name", "NOTES" + j);
//            pageComment.put("title", "Additional notes or comments");
//            pageComment.put("isRequired", "false");
//            newQuestions.put(pageComment);
//            page.remove("questions");
//            page.put("quesions", newQuestions);
//            newPages.put(j, page);
//        }
//
//        newSurvey.put("pages", newPages);
//        newSurvey.put("title", "Application Assessment");
//        newSurvey.put("sendResultOnPageNext", "true");
//        newSurvey.put("requiredText", "");
//        newSurvey.put("showProgressBar", "bottom");
//        newSurvey.put("completedHtml", "<p><h4>Thank you for completing the Pathfinder Assessment.  Please click <a id='surveyCompleteLink' href='/assessments.jsp?customerId={CUSTID}'>Here</a> to return to the main page.");
//
//        System.out.println(newSurvey);
//    }

    @Test
    public void givenEmptyCustomFile() throws ValidationException, JSONException, IOException {
        InputStream schemaFile = QuestionValidationTest.class.getResourceAsStream("../../../../../questions/question-schema.json");
        InputStream questions = QuestionValidationTest.class.getResourceAsStream("../../../../../test-data/question-data.json");
        InputStream customQuestionsCorrupt = QuestionValidationTest.class.getResourceAsStream("../../../../../test-data/custom-question-data-no-questions.json");

        String schemaString = IOUtils.toString(schemaFile, StandardCharsets.UTF_8.name());
        String questionsString = IOUtils.toString(questions, StandardCharsets.UTF_8.name());

        String res1 = new QuestionProcessor().GenerateSurveyPages(questionsString, "", schemaString);
        assertNotEquals(res1, "", "Result should not be empty");

        String questionsStringCorrupt = IOUtils.toString(questions, StandardCharsets.UTF_8.name());
        String res2 = new QuestionProcessor().GenerateSurveyPages(questionsString, questionsStringCorrupt, schemaString);
        assertNotEquals(res2, "", "Result should not be empty");
    }

    @Test
    public void givenCorruptCustomFile1() throws ValidationException, JSONException, IOException {
        InputStream schemaFile = QuestionValidationTest.class.getResourceAsStream("../../../../../questions/question-schema.json");
        InputStream questions = QuestionValidationTest.class.getResourceAsStream("../../../../../test-data/question-data.json");
        InputStream customQuestionsCorrupt = QuestionValidationTest.class.getResourceAsStream("../../../../../test-data/custom-question-data-no-questions.json");

        String schemaString = IOUtils.toString(schemaFile, StandardCharsets.UTF_8.name());
        String questionsString = IOUtils.toString(questions, StandardCharsets.UTF_8.name());

        String questionsStringCorrupt = IOUtils.toString(customQuestionsCorrupt, StandardCharsets.UTF_8.name());
        String res2 = new QuestionProcessor().GenerateSurveyPages(questionsString, questionsStringCorrupt, schemaString);
        assertNotEquals(res2, "", "Result should not be empty");
    }

    @Test
    public void givenCorruptCustomFile2() throws JSONException, IOException {

        InputStream schemaFile = QuestionValidationTest.class.getResourceAsStream("../../../../../questions/question-schema.json");
        InputStream questions = QuestionValidationTest.class.getResourceAsStream("../../../../../test-data/question-data.json");
        InputStream customQuestionsCorrupt = QuestionValidationTest.class.getResourceAsStream("../../../../../test-data/custom-question-data-corrupt.json");

        String schemaString = IOUtils.toString(schemaFile, StandardCharsets.UTF_8.name());
        String questionsString = IOUtils.toString(questions, StandardCharsets.UTF_8.name());

        String questionsStringCorrupt = IOUtils.toString(customQuestionsCorrupt, StandardCharsets.UTF_8.name());
        String res2 = new QuestionProcessor().GenerateSurveyPages(questionsString, questionsStringCorrupt, schemaString);
        assertNotEquals(res2, "", "Result should not be empty");
    }

    @Test
    public void givenDuplicateQuestionNames() throws JSONException, IOException {
        InputStream schemaFile = QuestionValidationTest.class.getResourceAsStream("../../../../../questions/question-schema.json");
        InputStream questions = QuestionValidationTest.class.getResourceAsStream("../../../../../test-data/question-data.json");
        InputStream customQuestionsCorrupt = QuestionValidationTest.class.getResourceAsStream("../../../../../test-data/custom-question-data-1page-8duplicate-names.json");

        String schemaString = IOUtils.toString(schemaFile, StandardCharsets.UTF_8.name());
        String questionsString = IOUtils.toString(questions, StandardCharsets.UTF_8.name());

        String questionsStringCorrupt = IOUtils.toString(customQuestionsCorrupt, StandardCharsets.UTF_8.name());
        String res2 = new QuestionProcessor().GenerateSurveyPages(questionsString, questionsStringCorrupt, schemaString);
        assertEquals(res2.lastIndexOf("DEPLOYFREQ"), res2.indexOf("DEPLOYFREQ"), "There should only be one \"DEPLOYFREQ\" string");
    }


    @Test
    public void givenValidFileCheckMerge() throws ValidationException, JSONException, IOException {
        InputStream schemaFile = QuestionValidationTest.class.getResourceAsStream("../../../../../questions/question-schema.json");
        InputStream questions = QuestionValidationTest.class.getResourceAsStream("../../../../../test-data/question-data.json");
        InputStream customQuestions = QuestionValidationTest.class.getResourceAsStream("../../../../../test-data/custom-question-data-1page-8duplicate-names.json");

        String schemaString = IOUtils.toString(schemaFile, StandardCharsets.UTF_8.name());
        String questionsString = IOUtils.toString(questions, StandardCharsets.UTF_8.name());
        String questionsStringCustom = IOUtils.toString(customQuestions, StandardCharsets.UTF_8.name());

        String res2 = new QuestionProcessor().GenerateSurveyPages(questionsString, questionsStringCustom, schemaString);

        JSONObject stdQuestions = new JSONObject(
                new JSONTokener(questionsString));
        JSONArray stdPages = stdQuestions.getJSONArray("pages");

        JSONObject resultQuestions = new JSONObject(
                new JSONTokener(res2));
        JSONArray resPages = resultQuestions.getJSONArray("pages");

        JSONObject customQuestionsO = new JSONObject(
                new JSONTokener(questionsStringCustom));
        JSONArray cusPages = customQuestionsO.getJSONArray("pages");

        assertEquals((stdPages.length() + cusPages.length()), resPages.length(), "Number of pages isn't what's expected");

        // Need to count the extra comment question added to each page during final survey generation as well as the additional depedency questions
        assertEquals((countQuestions(stdPages) + countQuestions(cusPages) + resPages.length()+2), (countQuestions(resPages)), "Number of questions isn't what's expected");
    }

    @Test
    public void givenInvalidValuesProcessingContinues() throws JSONException, IOException {
        InputStream schemaFile = QuestionValidationTest.class.getResourceAsStream("../../../../../questions/question-schema.json");
        InputStream questions = QuestionValidationTest.class.getResourceAsStream("../../../../../test-data/question-data.json");
        InputStream customQuestionsCorrupt = QuestionValidationTest.class.getResourceAsStream("../../../../../test-data/custom-question-data-invalid-values.json");

        String schemaString = IOUtils.toString(schemaFile, StandardCharsets.UTF_8.name());
        String questionsString = IOUtils.toString(questions, StandardCharsets.UTF_8.name());
        String questionsStringCorrupt = IOUtils.toString(customQuestionsCorrupt, StandardCharsets.UTF_8.name());

        String res2 = new QuestionProcessor().GenerateSurveyPages(questionsString, questionsStringCorrupt, schemaString);
        assertNotEquals(res2, "", "Result should not be empty");
    }

    @Test
    public void givenInvalidValuesCheckValidator() throws JSONException, IOException {
        InputStream schemaFile = QuestionValidationTest.class.getResourceAsStream("../../../../../questions/question-schema.json");
        InputStream questions = QuestionValidationTest.class.getResourceAsStream("../../../../../test-data/question-data.json");
        InputStream customQuestions = QuestionValidationTest.class.getResourceAsStream("../../../../../test-data/custom-question-data-invalid-values.json");

        String schemaString = IOUtils.toString(schemaFile, StandardCharsets.UTF_8.name());
        String questionsString = IOUtils.toString(questions, StandardCharsets.UTF_8.name());
        String questionsStringCorrupt = IOUtils.toString(customQuestions, StandardCharsets.UTF_8.name());

        JSONObject res2 = QuestionProcessor.ValidateQuestionData(questionsString, schemaString);
        assertNotNull(res2, "Result should not be null");

        Exception exception = assertThrows(org.everit.json.schema.ValidationException.class, () -> {
            JSONObject res3 = QuestionProcessor.ValidateQuestionData(questionsStringCorrupt, schemaString);
            assertNotNull(res3, "Result should not be null");
        });
    }

    private int countQuestions(JSONArray pages) throws JSONException {
        int count = 0;
        for (int j = 0; j < pages.length(); j++) {
            JSONObject page = pages.getJSONObject(j);
            count += page.getJSONArray("questions").length();
        }
        return count;
    }

    @Test
    public void  validJSTest() throws Exception {
        InputStream baseQFile =  QuestionValidationTest.class.getResourceAsStream("../../../../../questions/base-questions-data-default.json");
        InputStream schemaFile = QuestionValidationTest.class.getResourceAsStream("../../../../../questions/question-schema.json");
        InputStream jsBase =     QuestionValidationTest.class.getResourceAsStream("../../../../../questions/application-survey.js");
        InputStream customQue  = QuestionValidationTest.class.getResourceAsStream("../../../../../test-data/custom-question-data-1page-2-valid.json");

        String rawQuestionsJson = IOUtils.toString(baseQFile, StandardCharsets.UTF_8.name());
        String questionsJsonSchema = IOUtils.toString(schemaFile, StandardCharsets.UTF_8.name());
        String customQuestionsJson = IOUtils.toString(customQue, StandardCharsets.UTF_8.name());
        String processedQ = new QuestionProcessor().GenerateSurveyPages(rawQuestionsJson, customQuestionsJson, questionsJsonSchema);
        String surveyJs = IOUtils.toString(jsBase, StandardCharsets.UTF_8.name());
        String finalJScriptDefn = (surveyJs.replace("$$QUESTIONS_JSON$$", processedQ));

        //Ensure the resultant javascript compiles
        NashornScriptEngine engine = (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn");
        engine.compile(finalJScriptDefn);
        System.out.println(finalJScriptDefn);
    }
}
