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

import com.codahale.metrics.annotation.Timed;
import com.redhat.gps.pathfinder.domain.Assessments;

import com.redhat.gps.pathfinder.repository.AssessmentsRepository;
import com.redhat.gps.pathfinder.web.rest.errors.BadRequestAlertException;
import com.redhat.gps.pathfinder.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Assessments.
 */
//@RestController
//@RequestMapping("/api")
public class AssessmentsResource {

    private final Logger log = LoggerFactory.getLogger(AssessmentsResource.class);

    private static final String ENTITY_NAME = "assessments";

    private final AssessmentsRepository assessmentsRepository;

    public AssessmentsResource(AssessmentsRepository assessmentsRepository) {
        this.assessmentsRepository = assessmentsRepository;
    }

    /**
     * POST  /assessments : Create a new assessments.
     *
     * @param assessments the assessments to create
     * @return the ResponseEntity with status 201 (Created) and with body the new assessments, or with status 400 (Bad Request) if the assessments has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/assessments")
    @Timed
    public ResponseEntity<Assessments> createAssessments(@Valid @RequestBody Assessments assessments) throws URISyntaxException {
        log.debug("REST request to save Assessments : {}", assessments);
        if (assessments.getId() != null) {
            throw new BadRequestAlertException("A new assessments cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Assessments result = assessmentsRepository.save(assessments);
        return ResponseEntity.created(new URI("/api/assessments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /assessments : Updates an existing assessments.
     *
     * @param assessments the assessments to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated assessments,
     * or with status 400 (Bad Request) if the assessments is not valid,
     * or with status 500 (Internal Server Error) if the assessments couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/assessments")
    @Timed
    public ResponseEntity<Assessments> updateAssessments(@Valid @RequestBody Assessments assessments) throws URISyntaxException {
        log.debug("REST request to update Assessments : {}", assessments);
        if (assessments.getId() == null) {
            return createAssessments(assessments);
        }
        Assessments result = assessmentsRepository.save(assessments);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, assessments.getId().toString()))
            .body(result);
    }

    /**
     * GET  /assessments : get all the assessments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of assessments in body
     */
    @GetMapping("/assessments")
    @Timed
    public List<Assessments> getAllAssessments() {
        log.debug("REST request to get all Assessments");
        return assessmentsRepository.findAll();
        }

    /**
     * GET  /assessments/:id : get the "id" assessments.
     *
     * @param id the id of the assessments to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the assessments, or with status 404 (Not Found)
     */
    @GetMapping("/assessments/{id}")
    @Timed
    public ResponseEntity<Assessments> getAssessments(@PathVariable String id) {
        log.debug("REST request to get Assessments : {}", id);
        Assessments assessments = assessmentsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(assessments));
    }

    /**
     * DELETE  /assessments/:id : delete the "id" assessments.
     *
     * @param id the id of the assessments to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/assessments/{id}")
    @Timed
    public ResponseEntity<Void> deleteAssessments(@PathVariable String id) {
        log.debug("REST request to delete Assessments : {}", id);
        assessmentsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
