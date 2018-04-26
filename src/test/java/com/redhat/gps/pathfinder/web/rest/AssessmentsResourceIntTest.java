package com.redhat.gps.pathfinder.web.rest;

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

import com.redhat.gps.pathfinder.PathfinderApp;

import com.redhat.gps.pathfinder.domain.Assessments;
import com.redhat.gps.pathfinder.repository.AssessmentsRepository;
import com.redhat.gps.pathfinder.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static com.redhat.gps.pathfinder.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AssessmentsResource REST controller.
 *
 * @see AssessmentsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PathfinderApp.class)
public class AssessmentsResourceIntTest {

    private static final String DEFAULT_RESULTS = "AAAAAAAAAA";
    private static final String UPDATED_RESULTS = "BBBBBBBBBB";

    @Autowired
    private AssessmentsRepository assessmentsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restAssessmentsMockMvc;

    private Assessments assessments;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AssessmentsResource assessmentsResource = new AssessmentsResource(assessmentsRepository);
        this.restAssessmentsMockMvc = MockMvcBuilders.standaloneSetup(assessmentsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
//    public static Assessments createEntity() {
//        Assessments assessments = new Assessments()
//            .results(DEFAULT_RESULTS);
//        return assessments;
//    }

//    @Before
//    public void initTest() {
//        assessmentsRepository.deleteAll();
//        assessments = createEntity();
//    }

//    @Test
//    public void createAssessments() throws Exception {
//        int databaseSizeBeforeCreate = assessmentsRepository.findAll().size();
//
//        // Create the Assessments
//        restAssessmentsMockMvc.perform(post("/api/assessments")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(assessments)))
//            .andExpect(status().isCreated());
//
//        // Validate the Assessments in the database
//        List<Assessments> assessmentsList = assessmentsRepository.findAll();
//        assertThat(assessmentsList).hasSize(databaseSizeBeforeCreate + 1);
//        Assessments testAssessments = assessmentsList.get(assessmentsList.size() - 1);
//        assertThat(testAssessments.getResults()).isEqualTo(DEFAULT_RESULTS);
//    }

    @Test
    public void createAssessmentsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = assessmentsRepository.findAll().size();

        // Create the Assessments with an existing ID
        assessments.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssessmentsMockMvc.perform(post("/api/assessments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assessments)))
            .andExpect(status().isBadRequest());

        // Validate the Assessments in the database
        List<Assessments> assessmentsList = assessmentsRepository.findAll();
        assertThat(assessmentsList).hasSize(databaseSizeBeforeCreate);
    }

//    @Test
//    public void checkResultsIsRequired() throws Exception {
//        int databaseSizeBeforeTest = assessmentsRepository.findAll().size();
//        // set the field null
//        assessments.setResults(null);
//
//        // Create the Assessments, which fails.
//
//        restAssessmentsMockMvc.perform(post("/api/assessments")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(assessments)))
//            .andExpect(status().isBadRequest());
//
//        List<Assessments> assessmentsList = assessmentsRepository.findAll();
//        assertThat(assessmentsList).hasSize(databaseSizeBeforeTest);
//    }

    @Test
    public void getAllAssessments() throws Exception {
        // Initialize the database
        assessmentsRepository.save(assessments);

        // Get all the assessmentsList
        restAssessmentsMockMvc.perform(get("/api/assessments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assessments.getId())))
            .andExpect(jsonPath("$.[*].results").value(hasItem(DEFAULT_RESULTS.toString())));
    }

    @Test
    public void getAssessments() throws Exception {
        // Initialize the database
        assessmentsRepository.save(assessments);

        // Get the assessments
        restAssessmentsMockMvc.perform(get("/api/assessments/{id}", assessments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(assessments.getId()))
            .andExpect(jsonPath("$.results").value(DEFAULT_RESULTS.toString()));
    }

    @Test
    public void getNonExistingAssessments() throws Exception {
        // Get the assessments
        restAssessmentsMockMvc.perform(get("/api/assessments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
//
//    @Test
//    public void updateAssessments() throws Exception {
//        // Initialize the database
//        assessmentsRepository.save(assessments);
//        int databaseSizeBeforeUpdate = assessmentsRepository.findAll().size();
//
//        // Update the assessments
//        Assessments updatedAssessments = assessmentsRepository.findOne(assessments.getId());
//        updatedAssessments
//            .results(UPDATED_RESULTS);
//
//        restAssessmentsMockMvc.perform(put("/api/assessments")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(updatedAssessments)))
//            .andExpect(status().isOk());
//
//        // Validate the Assessments in the database
//        List<Assessments> assessmentsList = assessmentsRepository.findAll();
//        assertThat(assessmentsList).hasSize(databaseSizeBeforeUpdate);
//        Assessments testAssessments = assessmentsList.get(assessmentsList.size() - 1);
//        assertThat(testAssessments.getResults()).isEqualTo(UPDATED_RESULTS);
//    }

    @Test
    public void updateNonExistingAssessments() throws Exception {
        int databaseSizeBeforeUpdate = assessmentsRepository.findAll().size();

        // Create the Assessments

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAssessmentsMockMvc.perform(put("/api/assessments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assessments)))
            .andExpect(status().isCreated());

        // Validate the Assessments in the database
        List<Assessments> assessmentsList = assessmentsRepository.findAll();
        assertThat(assessmentsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteAssessments() throws Exception {
        // Initialize the database
        assessmentsRepository.save(assessments);
        int databaseSizeBeforeDelete = assessmentsRepository.findAll().size();

        // Get the assessments
        restAssessmentsMockMvc.perform(delete("/api/assessments/{id}", assessments.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Assessments> assessmentsList = assessmentsRepository.findAll();
        assertThat(assessmentsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Assessments.class);
        Assessments assessments1 = new Assessments();
        assessments1.setId("id1");
        Assessments assessments2 = new Assessments();
        assessments2.setId(assessments1.getId());
        assertThat(assessments1).isEqualTo(assessments2);
        assessments2.setId("id2");
        assertThat(assessments1).isNotEqualTo(assessments2);
        assessments1.setId(null);
        assertThat(assessments1).isNotEqualTo(assessments2);
    }
}
