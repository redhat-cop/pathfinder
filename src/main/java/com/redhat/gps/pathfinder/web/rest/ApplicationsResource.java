package com.redhat.gps.pathfinder.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.redhat.gps.pathfinder.domain.Applications;

import com.redhat.gps.pathfinder.repository.ApplicationsRepository;
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
 * REST controller for managing Applications.
 */
//@RestController
//@RequestMapping("/api")
public class ApplicationsResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationsResource.class);

    private static final String ENTITY_NAME = "applications";

    private final ApplicationsRepository applicationsRepository;

    public ApplicationsResource(ApplicationsRepository applicationsRepository) {
        this.applicationsRepository = applicationsRepository;
    }

    /**
     * POST  /applications : Create a new applications.
     *
     * @param applications the applications to create
     * @return the ResponseEntity with status 201 (Created) and with body the new applications, or with status 400 (Bad Request) if the applications has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/applications")
    @Timed
    public ResponseEntity<Applications> createApplications(@Valid @RequestBody Applications applications) throws URISyntaxException {
        log.debug("REST request to save Applications : {}", applications);
        if (applications.getId() != null) {
            throw new BadRequestAlertException("A new applications cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Applications result = applicationsRepository.save(applications);
        return ResponseEntity.created(new URI("/api/applications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /applications : Updates an existing applications.
     *
     * @param applications the applications to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated applications,
     * or with status 400 (Bad Request) if the applications is not valid,
     * or with status 500 (Internal Server Error) if the applications couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/applications")
    @Timed
    public ResponseEntity<Applications> updateApplications(@Valid @RequestBody Applications applications) throws URISyntaxException {
        log.debug("REST request to update Applications : {}", applications);
        if (applications.getId() == null) {
            return createApplications(applications);
        }
        Applications result = applicationsRepository.save(applications);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, applications.getId().toString()))
            .body(result);
    }

    /**
     * GET  /applications : get all the applications.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of applications in body
     */
    @GetMapping("/applications")
    @Timed
    public List<Applications> getAllApplications() {
        log.debug("REST request to get all Applications");
        return applicationsRepository.findAll();
        }

    /**
     * GET  /applications/:id : get the "id" applications.
     *
     * @param id the id of the applications to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the applications, or with status 404 (Not Found)
     */
    @GetMapping("/applications/{id}")
    @Timed
    public ResponseEntity<Applications> getApplications(@PathVariable String id) {
        log.debug("REST request to get Applications : {}", id);
        Applications applications = applicationsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(applications));
    }

    /**
     * DELETE  /applications/:id : delete the "id" applications.
     *
     * @param id the id of the applications to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/applications/{id}")
    @Timed
    public ResponseEntity<Void> deleteApplications(@PathVariable String id) {
        log.debug("REST request to delete Applications : {}", id);
        applicationsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
