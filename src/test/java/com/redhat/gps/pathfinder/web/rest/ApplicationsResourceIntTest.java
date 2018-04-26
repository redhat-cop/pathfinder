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

import com.redhat.gps.pathfinder.domain.Applications;
import com.redhat.gps.pathfinder.repository.ApplicationsRepository;
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
 * Test class for the ApplicationsResource REST controller.
 *
 * @see ApplicationsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PathfinderApp.class)
public class ApplicationsResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ApplicationsRepository applicationsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restApplicationsMockMvc;

    private Applications applications;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ApplicationsResource applicationsResource = new ApplicationsResource(applicationsRepository);
        this.restApplicationsMockMvc = MockMvcBuilders.standaloneSetup(applicationsResource)
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
    public static Applications createEntity() {
        Applications applications = new Applications()
            .name(DEFAULT_NAME);
        return applications;
    }

    @Before
    public void initTest() {
        applicationsRepository.deleteAll();
        applications = createEntity();
    }

    @Test
    public void createApplications() throws Exception {
        int databaseSizeBeforeCreate = applicationsRepository.findAll().size();

        // Create the Applications
        restApplicationsMockMvc.perform(post("/api/applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applications)))
            .andExpect(status().isCreated());

        // Validate the Applications in the database
        List<Applications> applicationsList = applicationsRepository.findAll();
        assertThat(applicationsList).hasSize(databaseSizeBeforeCreate + 1);
        Applications testApplications = applicationsList.get(applicationsList.size() - 1);
        assertThat(testApplications.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void createApplicationsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = applicationsRepository.findAll().size();

        // Create the Applications with an existing ID
        applications.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationsMockMvc.perform(post("/api/applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applications)))
            .andExpect(status().isBadRequest());

        // Validate the Applications in the database
        List<Applications> applicationsList = applicationsRepository.findAll();
        assertThat(applicationsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationsRepository.findAll().size();
        // set the field null
        applications.setName(null);

        // Create the Applications, which fails.

        restApplicationsMockMvc.perform(post("/api/applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applications)))
            .andExpect(status().isBadRequest());

        List<Applications> applicationsList = applicationsRepository.findAll();
        assertThat(applicationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllApplications() throws Exception {
        // Initialize the database
        applicationsRepository.save(applications);

        // Get all the applicationsList
        restApplicationsMockMvc.perform(get("/api/applications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applications.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    public void getApplications() throws Exception {
        // Initialize the database
        applicationsRepository.save(applications);

        // Get the applications
        restApplicationsMockMvc.perform(get("/api/applications/{id}", applications.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(applications.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    public void getNonExistingApplications() throws Exception {
        // Get the applications
        restApplicationsMockMvc.perform(get("/api/applications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateApplications() throws Exception {
        // Initialize the database
        applicationsRepository.save(applications);
        int databaseSizeBeforeUpdate = applicationsRepository.findAll().size();

        // Update the applications
        Applications updatedApplications = applicationsRepository.findOne(applications.getId());
        updatedApplications
            .name(UPDATED_NAME);

        restApplicationsMockMvc.perform(put("/api/applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedApplications)))
            .andExpect(status().isOk());

        // Validate the Applications in the database
        List<Applications> applicationsList = applicationsRepository.findAll();
        assertThat(applicationsList).hasSize(databaseSizeBeforeUpdate);
        Applications testApplications = applicationsList.get(applicationsList.size() - 1);
        assertThat(testApplications.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    public void updateNonExistingApplications() throws Exception {
        int databaseSizeBeforeUpdate = applicationsRepository.findAll().size();

        // Create the Applications

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restApplicationsMockMvc.perform(put("/api/applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applications)))
            .andExpect(status().isCreated());

        // Validate the Applications in the database
        List<Applications> applicationsList = applicationsRepository.findAll();
        assertThat(applicationsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteApplications() throws Exception {
        // Initialize the database
        applicationsRepository.save(applications);
        int databaseSizeBeforeDelete = applicationsRepository.findAll().size();

        // Get the applications
        restApplicationsMockMvc.perform(delete("/api/applications/{id}", applications.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Applications> applicationsList = applicationsRepository.findAll();
        assertThat(applicationsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Applications.class);
        Applications applications1 = new Applications();
        applications1.setId("id1");
        Applications applications2 = new Applications();
        applications2.setId(applications1.getId());
        assertThat(applications1).isEqualTo(applications2);
        applications2.setId("id2");
        assertThat(applications1).isNotEqualTo(applications2);
        applications1.setId(null);
        assertThat(applications1).isNotEqualTo(applications2);
    }
}
