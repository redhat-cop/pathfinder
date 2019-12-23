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
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class QuestionTemplateTest {
    @Test
    public void givenValidInput_whenValidating_thenValid() throws ValidationException, JSONException {
        JSONObject jsonSchema;
        try {
            InputStream schemaFile = QuestionTemplateTest.class.getResourceAsStream("../../../../../question-schema.json");
            InputStream questions = QuestionTemplateTest.class.getResourceAsStream("../../../../../question-data.json");
            jsonSchema = new JSONObject(
                    new JSONTokener(new InputStreamReader(schemaFile)));
            JSONObject jsonSubject = new JSONObject(
                    new JSONTokener(new InputStreamReader(questions)));

            Schema schema = SchemaLoader.load(jsonSchema);
            schema.validate(jsonSubject);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void givenCorruptInput_whenValidating_thenValid() throws ValidationException, JSONException {

        Exception exception = assertThrows(org.json.JSONException.class, () -> {
            JSONObject jsonSchema;
            InputStream schemaFile = QuestionTemplateTest.class.getResourceAsStream("../../../../../question-schema.json");
            InputStream questions = QuestionTemplateTest.class.getResourceAsStream("../../../../../question-data-corrupt.json");

            jsonSchema = new JSONObject(
                    new JSONTokener(new InputStreamReader(schemaFile)));
            JSONObject jsonSubject = new JSONObject(
                    new JSONTokener(new InputStreamReader(questions)));

            Schema schema = SchemaLoader.load(jsonSchema);
            schema.validate(jsonSubject);
        });
    }

    @Test
    public void givenInvalidQuestionInput_whenValidating_thenValid() throws ValidationException, JSONException {

        Exception exception = assertThrows(org.everit.json.schema.ValidationException.class, () -> {
            JSONObject jsonSchema;
            InputStream schemaFile = QuestionTemplateTest.class.getResourceAsStream("../../../../../question-schema.json");
            InputStream questions = QuestionTemplateTest.class.getResourceAsStream("../../../../../question-data-invalid-questions.json");

            jsonSchema = new JSONObject(
                    new JSONTokener(new InputStreamReader(schemaFile)));
            JSONObject jsonSubject = new JSONObject(
                    new JSONTokener(new InputStreamReader(questions)));

            Schema schema = SchemaLoader.load(jsonSchema);
            schema.validate(jsonSubject);
        });

        System.out.println(exception.getMessage());
    }

}
