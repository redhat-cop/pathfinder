package com.redhat.gps.pathfinder.web.api;

import com.codahale.metrics.annotation.Timed;
import com.redhat.gps.pathfinder.domain.Assessments;
import com.redhat.gps.pathfinder.domain.Customer;
import com.redhat.gps.pathfinder.domain.QuestionMetaData;
import com.redhat.gps.pathfinder.repository.AssessmentsRepository;
import com.redhat.gps.pathfinder.repository.CustomerRepository;
import com.redhat.gps.pathfinder.repository.QuestionMetaDataRepository;
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
 * REST controller for retrieving question metadata.
 */
@RestController
@RequestMapping("/api/static")
public class QuestionsMetaDataAPI {
    public final static String MIN_ASSESSMENT_VALUES="MIN_ASSESSMENT_VALUE";

    private final Logger log = LoggerFactory.getLogger(QuestionsMetaDataAPI.class);

    private static final String ENTITY_NAME = "customer";

    private final QuestionMetaDataRepository questionRepository;
    private final AssessmentsRepository assmRepository;

    public QuestionsMetaDataAPI(QuestionMetaDataRepository questionRepository,AssessmentsRepository assmRepo) {
        this.questionRepository = questionRepository;
        this.assmRepository = assmRepo;
    }


    /**
     * GET  /questions : get all the get question metadata.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of customers in body
     */
    @GetMapping(value = "/questions",produces = "application/json")
    @Timed
    public List<QuestionMetaData> getAllQuestionMetadata() {
        log.debug("REST request to get question metadata ");
        return questionRepository.findAll();
    }

    @GetMapping(value= "/minimum",produces = "application/json")
    public Assessments getMinValues(){
        log.debug("REST request to get minimum values ");
        return assmRepository.findOne(MIN_ASSESSMENT_VALUES);
    }

}
