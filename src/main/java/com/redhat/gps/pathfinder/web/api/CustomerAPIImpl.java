package com.redhat.gps.pathfinder.web.api;

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
import com.redhat.gps.pathfinder.domain.*;
import com.redhat.gps.pathfinder.repository.*;
import com.redhat.gps.pathfinder.web.api.model.*;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/pathfinder")
public class CustomerAPIImpl implements CustomersApi {

    private final Logger log = LoggerFactory.getLogger(CustomerAPIImpl.class);

    private final CustomerRepository custRepo;

    private final ApplicationsRepository appsRepo;

    private final AssessmentsRepository assmRepo;

    private final QuestionMetaDataRepository questionRepository;

    private final ReviewsRepository reviewRepository;

    public CustomerAPIImpl(CustomerRepository custRepo, ApplicationsRepository appsRepo, AssessmentsRepository assmRepo, QuestionMetaDataRepository questionRepository, ReviewsRepository reviewRepository) {
        this.custRepo = custRepo;
        this.appsRepo = appsRepo;
        this.assmRepo = assmRepo;
        this.questionRepository = questionRepository;
        this.reviewRepository = reviewRepository;
    }

    @Timed
    public ResponseEntity<AssessmentType> customersCustIdApplicationsAppIdAssessmentsAssessIdGet(@ApiParam(value = "", required = true) @PathVariable("custId") String custId, @ApiParam(value = "", required = true) @PathVariable("appId") String appId, @ApiParam(value = "", required = true) @PathVariable("assessId") String assessId) {
        log.debug("customersCustIdApplicationsAppIdAssessmentsAssessIdGet....");

        AssessmentType resp = new AssessmentType();
        try {
            Assessments currAssm = assmRepo.findOne(assessId);
            if (currAssm != null) {
                resp.setDeps(currAssm.getDeps());
                AssessmentResponse tempPayload = new AssessmentResponse();

                for (Object o : currAssm.getResults().entrySet()) {
                    Map.Entry pair = (Map.Entry) o;
                    tempPayload.put((String) pair.getKey(), (String) pair.getValue());
                }
                resp.setPayload(tempPayload);
                return new ResponseEntity<AssessmentType>(resp, HttpStatus.OK);
            } else {
                return new ResponseEntity<AssessmentType>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.error("customersCustIdApplicationsAppIdAssessmentsAssessIdGet", ex.getMessage(), ex);
            return new ResponseEntity<AssessmentType>(HttpStatus.BAD_REQUEST);
        }
    }

    @Timed
    public ResponseEntity<List<String>> customersCustIdApplicationsAppIdAssessmentsGet(@ApiParam(value = "", required = true) @PathVariable("custId") String custId, @ApiParam(value = "", required = true) @PathVariable("appId") String appId) {
        log.debug("customersCustIdApplicationsAppIdAssessmentsGet....");
        ArrayList<String> results = new ArrayList<>();
        try {
            Applications currApp = appsRepo.findOne(appId);
            try {

                if (currApp != null) {

                    List<Assessments> res = currApp.getAssessments();
                    if ((res != null) && (!res.isEmpty())) {
                        for (Assessments x : res) {
                            results.add(x.getId());
                        }
                    }
                    return new ResponseEntity<List<String>>(results, HttpStatus.OK);

                }
            } catch (Exception ex) {
                log.error("Unable to get assessments for customer ", ex.getMessage(), ex);
            }
            return new ResponseEntity<List<String>>(results, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            log.error("customersCustIdApplicationsAppIdAssessmentsGet", ex.getMessage(), ex);
        }
        return new ResponseEntity<List<String>>(results, HttpStatus.BAD_REQUEST);
    }

    @Timed
    public ResponseEntity<String> customersCustIdApplicationsAppIdAssessmentsPost
        (@ApiParam(value = "", required = true) @PathVariable("custId") String
             custId, @ApiParam(value = "", required = true) @PathVariable("appId") String
             appId, @ApiParam(value = "Application Assessment") @Valid @RequestBody AssessmentType body) {
        log.debug("customersCustIdApplicationsAppIdAssessmentsPost....{} ", body.getPayload());

        try {
            if (!custRepo.exists(custId)) {
                log.error("customersCustIdApplicationsAppIdAssessmentsPost....app not found " + appId);
                return new ResponseEntity<String>("Customer Not found", HttpStatus.BAD_REQUEST);
            }

            Applications currApp = appsRepo.findOne(appId);

            if (currApp != null) {

                Assessments newitem = new Assessments();
                UUID uuid = UUID.randomUUID();
                newitem.setId(uuid.toString());
                newitem.setResults(body.getPayload());
                newitem.setDeps(body.getDeps());
                newitem.setDatetime(body.getDatetime());

                newitem = assmRepo.insert(newitem);

                List<Assessments> assmList = currApp.getAssessments();
                if (assmList == null) {
                    assmList = new ArrayList<>();
                }
                assmList.add(newitem);
                currApp.setAssessments(assmList);
                currApp = appsRepo.save(currApp);
                return new ResponseEntity<String>(newitem.getId(), HttpStatus.OK);
            } else {
                log.error("customersCustIdApplicationsAppIdAssessmentsPost....app not found " + appId);
                return new ResponseEntity<String>("Application not found", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.error("customersCustIdApplicationsAppIdAssessmentsPost Unable to create applications for customer ", ex.getMessage(), ex);
        }
        return new ResponseEntity<String>("Unable to create assessment", HttpStatus.BAD_REQUEST);
    }

    @Timed
    public ResponseEntity<ApplicationType> customersCustIdApplicationsAppIdGet(@ApiParam(value = "Customer Identifier", required = true) @PathVariable("custId") String custId, @ApiParam(value = "Application Identifier", required = true) @PathVariable("appId") String appId) {
        log.debug("customersCustIdApplicationsAppIdGet cid {} app {}", custId, appId);
        ApplicationType resp = new ApplicationType();
        //TODO : Check customer exists and owns application as well as application
        try {
            Applications details = appsRepo.findOne(appId);
            resp.setDescription(details.getDescription());
            if (details.getReview() != null)
                resp.setReview(details.getReview().getId());
            resp.setName(details.getName());
            resp.setId(appId);
        } catch (Exception ex) {
            log.error("Unable to get applications for customer ", ex.getMessage(), ex);
            return new ResponseEntity<ApplicationType>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<ApplicationType>(resp, HttpStatus.OK);
    }

    @Override
    @Timed
    public ResponseEntity<List<ApplicationType>> customersCustIdApplicationsGet
        (@ApiParam(value = "", required = true) @PathVariable("custId") String custId) {
        log.info("customersCustIdApplicationsGet....[" + custId + "]");
        ArrayList<ApplicationType> response = new ArrayList<>();

        try {
            Customer myCust = custRepo.findOne(custId);
            if (myCust == null) {
                log.error("customersCustIdApplicationsGet....[" + custId + "] customer not found");
                return new ResponseEntity<List<ApplicationType>>(HttpStatus.BAD_REQUEST);
            }
            List<Applications> resp = custRepo.findOne(custId).getApplications();
            if ((resp != null) && (!resp.isEmpty())) {
                for (Applications x : resp) {
                    ApplicationType lapp = new ApplicationType();
                    lapp.setName(x.getName());
                    lapp.setId(x.getId());
                    if (x.getReview() != null) {
                        lapp.setReview(x.getReview().getId());
                    }
                    lapp.setDescription(x.getDescription());
                    response.add(lapp);
                }
            }
        } catch (Exception ex) {
            log.error("Unable to list applications for customer ", ex.getMessage(), ex);
            return new ResponseEntity<List<ApplicationType>>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<List<ApplicationType>>(response, HttpStatus.OK);
    }

    @Timed
    public ResponseEntity<String> customersCustIdApplicationsPost
        (@ApiParam(value = "Customer Identifier", required = true) @PathVariable("custId") String
             custId, @ApiParam(value = "Application Definition") @Valid @RequestBody ApplicationType body) {
        log.debug("customersCustIdApplicationsPost....");
        Customer myCust = custRepo.findOne(custId);
        if (myCust == null) {
            return new ResponseEntity<String>(custId, HttpStatus.BAD_REQUEST);
        } else {
            Applications app = new Applications();
            UUID uuid = UUID.randomUUID();
            app.setId(uuid.toString());
            app.setName(body.getName());
            app.setDescription(body.getDescription());
            app = appsRepo.save(app);
            List<Applications> appList = myCust.getApplications();
            if (appList == null) {
                appList = new ArrayList<Applications>();
            }
            appList.add(app);
            myCust.setApplications(appList);
            custRepo.save(myCust);
            return new ResponseEntity<String>(app.getId(), HttpStatus.OK);
        }
    }

    @Timed
    public ResponseEntity<CustomerType> customersCustIdGet
        (@ApiParam(value = "Customer Identifier", required = true) @PathVariable("custId") String custId) {
        log.debug("customersCustIdGet....{}", custId);
        Customer myCust = custRepo.findOne(custId);
        if (myCust == null) {
            return new ResponseEntity<CustomerType>(HttpStatus.BAD_REQUEST);
        } else {
            CustomerType resp = new CustomerType();
            resp.setCustomerName(myCust.getName());
            resp.setCustomerId(myCust.getId());
            resp.setCustomerDescription(myCust.getId());
            return new ResponseEntity<CustomerType>(resp, HttpStatus.OK);
        }
    }

    @Timed
    public ResponseEntity<String> customersPost(@ApiParam(value = "") @Valid @RequestBody CustomerType body) {
        log.debug("customersPost....{}", body);
        Customer myCust = new Customer();
        UUID uuid = UUID.randomUUID();

        myCust.setId(uuid.toString());
        myCust.setName(body.getCustomerName());
        myCust.setSize(body.getCustomerVertical());
        myCust.setSize(body.getCustomerSize());
        try {
            myCust = custRepo.insert(myCust);
        } catch (Exception ex) {
            log.error("Unable to Create customer ", ex.getMessage(), ex);
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>(myCust.getId(), HttpStatus.OK);
    }

    @Timed
    public ResponseEntity<List<CustomerType>> customersGet() {
        log.debug("customersGet....");
        ArrayList<CustomerType> response = new ArrayList<>();
        List<Customer> myCust = custRepo.findAll();
        if (myCust == null) {
            return new ResponseEntity<List<CustomerType>>(response, HttpStatus.BAD_REQUEST);
        } else {
            for (Customer x : myCust) {
                CustomerType resp = new CustomerType();
                resp.setCustomerName(x.getName());
                resp.setCustomerId(x.getId());
                resp.setCustomerSize(x.getSize());
                resp.setCustomerVertical(x.getVertical());
                response.add(resp);
            }
            return new ResponseEntity<List<CustomerType>>(response, HttpStatus.OK);
        }
    }

    @Timed
    public ResponseEntity<AssessmentProcessType> customersCustIdApplicationsAppIdAssessmentsAssessIdProcessGet
        (@ApiParam(value = "", required = true) @PathVariable("custId") String
             custId, @ApiParam(value = "", required = true) @PathVariable("appId") String
             appId, @ApiParam(value = "", required = true) @PathVariable("assessId") String assessId) {
        log.debug("customersCustIdApplicationsAppIdAssessmentsAssessIdProcessGet....");

        AssessmentProcessType resp = new AssessmentProcessType();
        List<AssessmentProcessQuestionResultsType> assessResults = new ArrayList<>();

        try {

            Assessments currAssm = assmRepo.findOne(assessId);
            if (currAssm == null) {
                return new ResponseEntity<AssessmentProcessType>(resp, HttpStatus.BAD_REQUEST);
            }

            List<QuestionMetaData> questionData = questionRepository.findAll();


            for (QuestionMetaData currQuestion : questionData) {
                String res = (String) currAssm.getResults().get(currQuestion.getId());
                AssessmentProcessQuestionResultsType vals = new AssessmentProcessQuestionResultsType();
                vals.setQuestionTag(currQuestion.getId());

                QuestionWeights.QuestionRank answerRank = currQuestion.getMetaData().get(
                    Integer.parseInt(res)
                ).getRank();
                vals.setQuestionRank(answerRank.ordinal());
                assessResults.add(vals);

                log.debug(currQuestion.getId() + ": value=" + res + " RANK " + answerRank.toString());

            }
            resp.setAssessResults(assessResults);
            resp.setAssmentNotes(currAssm.getResults().get("NOTES"));
            resp.setDependencies(currAssm.getDeps());
            resp.setBusinessPriority(currAssm.getResults().get("BUSPRIORITY"));

        } catch (Exception ex) {
            log.error("Error while processing assessment", ex.getMessage(), ex);
            return new ResponseEntity<AssessmentProcessType>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<AssessmentProcessType>(resp, HttpStatus.OK);
    }

    @Timed
    public ResponseEntity<String> customersCustIdApplicationsAppIdReviewPost(@ApiParam(value = "", required = true) @PathVariable("custId") String custId, @ApiParam(value = "", required = true) @PathVariable("appId") String appId, @ApiParam(value = "Application Definition") @Valid @RequestBody ReviewType body) {
        log.debug("customersCustIdApplicationsAppIdReviewPost....");
        try {
            Applications app = appsRepo.findOne(appId);
            if (app == null) {
                log.error("Error while processing review - Unable to find application with id {}", appId);
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }

            Assessments assm = assmRepo.findOne(body.getAssessmentId());
            if (assm == null) {
                log.error("Error while processing review - Unable to find assessment with id {}", body.getAssessmentId());
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }

            ApplicationAssessmentReview reviewData = new ApplicationAssessmentReview(
                body.getReviewTimestamp(),
                assm,
                body.getReviewDecision(),
                body.getWorkEffort(),
                body.getReviewNotes(),
                body.getWorkPriority(),
                body.getBusinessPriority(),
                app);

            if (app.getReview() != null) {
                reviewData.setId(app.getReview().getId());
            } else {
                UUID uuid = UUID.randomUUID();
                reviewData.setId(uuid.toString());
            }


            reviewData = reviewRepository.save(reviewData);
            app.setReview(reviewData);
            appsRepo.save(app);

            return new ResponseEntity<String>(reviewData.getId(), HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Error while processing review", ex.getMessage(), ex);
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Timed
    public ResponseEntity<ReviewType> customersCustIdApplicationsAppIdReviewReviewIdGet(@ApiParam(value = "", required = true) @PathVariable("custId") String custId, @ApiParam(value = "", required = true) @PathVariable("appId") String appId, @ApiParam(value = "", required = true) @PathVariable("reviewId") String reviewId) {
        log.debug("customersCustIdApplicationsAppIdReviewReviewIdGet....");
        ReviewType resp = new ReviewType();
        try {
            Applications app = appsRepo.findOne(appId);
            if (app == null) {
                log.error("Error while retrieving review - Unable to find application with id {}", appId);
                return new ResponseEntity<ReviewType>(HttpStatus.BAD_REQUEST);
            }

            if (app.getReview() == null) {
                log.error("Error while retrieving review - no review associated with application {}", reviewId);
                return new ResponseEntity<ReviewType>(HttpStatus.BAD_REQUEST);
            }


            ApplicationAssessmentReview reviewData = reviewRepository.findOne(app.getReview().getId());

            if (reviewData == null) {
                log.error("Error while retrieving review - Unable to find review for application {}", appId);
                return new ResponseEntity<ReviewType>(HttpStatus.BAD_REQUEST);
            }

            resp.setAssessmentId(reviewData.getAssessments().getId());
            resp.setReviewDecision(reviewData.getReviewDecision());
            resp.setReviewNotes(reviewData.getReviewNotes());
            resp.setWorkEffort(reviewData.getReviewEstimate());
            resp.setReviewTimestamp(reviewData.getReviewDate());
            resp.setWorkPriority(reviewData.getWorkPriority());
            resp.setBusinessPriority(reviewData.getBusinessPriority());

            return new ResponseEntity<ReviewType>(resp, HttpStatus.OK);

        } catch (Exception ex) {
            log.error("Error while processing review", ex.getMessage(), ex);
            return new ResponseEntity<ReviewType>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //This is fugly and needs to be removed at a later stage
    @Timed
    public ResponseEntity<List<ReviewType>> customersCustIdReviewsGet(@ApiParam(value = "Customer Identifier", required = true) @PathVariable("custId") String custId) {
        log.debug("customersCustIdReviewsGet...." + custId);
        ArrayList<ReviewType> resp = new ArrayList<>();

        try {
            Customer currCust = custRepo.findOne(custId);
            if (currCust == null) {
                log.error("customersCustIdReviewsGet....customer not found {}", custId);
                return new ResponseEntity<List<ReviewType>>(HttpStatus.BAD_REQUEST);
            }

            List<Applications> appList = currCust.getApplications();
            if ((appList == null) || (appList.isEmpty())) {
                log.error("customersCustIdReviewsGet....no applications for customer {}", custId);
                return new ResponseEntity<List<ReviewType>>(HttpStatus.BAD_REQUEST);
            }

            ArrayList<ApplicationAssessmentReview> reviewList = new ArrayList<>();

            for (Applications x : appList) {
                ApplicationAssessmentReview tmpRev = x.getReview();
                if (tmpRev != null) {
                    reviewList.add(x.getReview());
                    ReviewType newRev = new ReviewType();
                    newRev.setBusinessPriority(tmpRev.getBusinessPriority());
                    newRev.setWorkPriority(tmpRev.getWorkPriority());
                    newRev.setReviewTimestamp(tmpRev.getReviewDate());
                    newRev.setWorkEffort(tmpRev.getReviewEstimate());
                    newRev.setReviewNotes(tmpRev.getReviewNotes());
                    newRev.setReviewDecision(tmpRev.getReviewDecision());
                    newRev.setAssessmentId(x.getName());
                    resp.add(newRev);
                }
            }

        } catch (Exception ex) {
            log.error("Error while processing review", ex.getMessage(), ex);
            return new ResponseEntity<List<ReviewType>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<List<ReviewType>>(resp, HttpStatus.OK);
    }

    @Timed
    public ResponseEntity<Void> customersCustIdApplicationsAppIdDelete(@ApiParam(value = "Customer Identifier", required = true) @PathVariable("custId") String custId, @ApiParam(value = "Application Identifier", required = true) @PathVariable("appId") String appId) {
        log.debug("customersCustIdApplicationsAppIdDelete {} {}", custId, appId);

        try {
            Customer currCust = custRepo.findOne(custId);
            if (currCust == null) {
                log.error("customersCustIdApplicationsAppIdDelete....customer not found {}", custId);
                return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
            }

            Applications delApp = appsRepo.findOne(appId);
            if (delApp == null) {
                log.error("customersCustIdApplicationsAppIdDelete....application not found {}", appId);
                return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
            }

            List<Applications> currApps = currCust.getApplications();

            List<Applications> newApps = new ArrayList<>();
            boolean appFound = false;

            for (Applications x : currApps) {
                if (x.getId().equalsIgnoreCase(appId)) {
                    appFound = true;
                } else {
                    newApps.add(x);
                }
            }

            if (!appFound) {
                log.error("customersCustIdApplicationsAppIdDelete....application not found {} in customer list {}", appId, custId);
                return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
            }

            currCust.setApplications(newApps);
            custRepo.save(currCust);

            if (delApp.getReview() != null)
                reviewRepository.delete(delApp.getReview());

            if (delApp.getAssessments() != null)
                assmRepo.delete(delApp.getAssessments());

            appsRepo.delete(appId);

        } catch (Exception ex) {
            log.error("Error while deleting application", ex.getMessage(), ex);
            return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Timed
    public ResponseEntity<Void> customersCustIdApplicationsAppIdReviewReviewIdDelete(@ApiParam(value = "", required = true) @PathVariable("custId") String custId, @ApiParam(value = "", required = true) @PathVariable("appId") String appId, @ApiParam(value = "", required = true) @PathVariable("reviewId") String reviewId) {
        log.debug("customersCustIdApplicationsAppIdReviewReviewIdDelete {} {} {}", custId, appId, reviewId);

        try {
            Customer currCust = custRepo.findOne(custId);
            if (currCust == null) {
                log.error("customersCustIdApplicationsAppIdReviewReviewIdDelete....customer not found {}", custId);
                return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
            }

            Applications currApp = appsRepo.findOne(appId);
            if (currApp == null) {
                log.error("customersCustIdApplicationsAppIdReviewReviewIdDelete....application not found {}", appId);
                return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
            }

            if (currApp.getReview().getId().equalsIgnoreCase(reviewId)) {
                currApp.setReview(null);
                appsRepo.save(currApp);
                reviewRepository.delete(reviewId);
            } else {
                log.error("customersCustIdApplicationsAppIdReviewReviewIdDelete....review {} not found for application", reviewId, appId);
                return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.error("Error while deleting review", ex.getMessage(), ex);
            return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Timed
    public ResponseEntity<Void> customersCustIdApplicationsAppIdAssessmentsAssessIdDelete(@ApiParam(value = "", required = true) @PathVariable("custId") String custId, @ApiParam(value = "", required = true) @PathVariable("appId") String appId, @ApiParam(value = "", required = true) @PathVariable("assessId") String assessId) {
        log.debug("customersCustIdApplicationsAppIdAssessmentsAssessIdDelete {} {} {}", custId, appId, assessId);

        try {
            Customer currCust = custRepo.findOne(custId);
            if (currCust == null) {
                log.error("customersCustIdApplicationsAppIdAssessmentsAssessIdDelete....customer not found {}", custId);
                return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
            }

            Applications currApp = appsRepo.findOne(appId);
            if (currApp == null) {
                log.error("customersCustIdApplicationsAppIdAssessmentsAssessIdDelete....application not found {}", appId);
                return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
            }

            List<Assessments> assmList = currApp.getAssessments();

            if ((assmList == null) || (assmList.isEmpty())) {
                log.error("customersCustIdApplicationsAppIdAssessmentsAssessIdDelete....assessment list is null for app {}", appId);
                return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
            }

            boolean assmFound = false;
            List<Assessments> newAssmLst = new ArrayList<>();

            for (Assessments x : assmList) {
                if (x.getId().equalsIgnoreCase(assessId)) {
                    assmFound = true;
                } else {
                    newAssmLst.add(x);
                }
            }

            if (assmFound) {
                assmRepo.delete(assessId);
                currApp.setAssessments(newAssmLst);
                appsRepo.save(currApp);
            } else {
                log.error("customersCustIdApplicationsAppIdAssessmentsAssessIdDelete....assessment not found for app {}", appId);
                return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.error("Error while deleting assessment", ex.getMessage(), ex);
            return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Timed
    public ResponseEntity<Void> customersCustIdDelete(@ApiParam(value = "Customer Identifier", required = true) @PathVariable("custId") String custId) {
        log.debug("customersCustIdDelete {}", custId);
        try {
            Customer currCust = custRepo.findOne(custId);
            if (currCust == null) {
                log.error("customersCustIdDelete....customer not found {}", custId);
                return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
            }
            if ((currCust.getApplications() != null) && (!currCust.getApplications().isEmpty())) {
                log.error("Customer {} has applications...not deleting", custId);
                return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            custRepo.delete(custId);
        } catch (Exception ex) {
            log.error("Error while deleting customer", ex.getMessage(), ex);
            return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Timed
    public ResponseEntity<List<ApplicationSummaryType>> customersCustIdApplicationAssessmentSummaryGet(@ApiParam(value = "Customer Identifier", required = true) @PathVariable("custId") String custId) {
        log.debug("customersCustIdApplicationAssessmentSummaryGet {}", custId);
        List<ApplicationSummaryType> resp = new ArrayList<>();
        try {
            Customer currCust = custRepo.findOne(custId);
            if (currCust == null) {
                log.error("customersCustIdApplicationAssessmentSummaryGet....customer not found {}", custId);
                return new ResponseEntity<List<ApplicationSummaryType>>(HttpStatus.BAD_REQUEST);
            }

            List<Applications> apps = currCust.getApplications();

            if ((apps == null) || (apps.isEmpty())) {
                log.error("Customer {} has no applications...", custId);
                return new ResponseEntity<List<ApplicationSummaryType>>(HttpStatus.OK);
            }

            for (Applications currApp : apps) {
                ApplicationSummaryType item = new ApplicationSummaryType();
                item.setId(currApp.getId());
                item.setName(currApp.getName());
                ApplicationAssessmentReview review = currApp.getReview();
                if (review != null) {
                    item.setReviewDate(review.getReviewDate());
                    item.setDecision(review.getReviewDecision());
                    item.setWorkEffort(review.getReviewEstimate());
                    item.setBusinessPriority(new Integer(review.getBusinessPriority()));
                }
                List<Assessments> assmList = currApp.getAssessments();
                if ((assmList != null) && (!assmList.isEmpty())) {
                    Assessments currAssm = assmList.get(assmList.size() - 1);
                    item.assessed(true);
                    item.setLatestAssessmentId(currAssm.getId());
                } else {
                    item.assessed(false);
                }
                resp.add(item);
            }
        } catch (Exception ex) {
            log.error("Error while processing customersCustIdApplicationAssessmentSummaryGet", ex.getMessage(), ex);
            return new ResponseEntity<List<ApplicationSummaryType>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<List<ApplicationSummaryType>>(resp, HttpStatus.OK);
    }

    @Timed
    public ResponseEntity<ApplicationAssessmentProgressType> customersCustIdApplicationAssessmentProgressGet(@ApiParam(value = "Customer Identifier", required = true) @PathVariable("custId") String custId) {

        log.debug("customersCustIdApplicationAssessmentProgressGet {}", custId);
        ApplicationAssessmentProgressType resp = new ApplicationAssessmentProgressType();
        int appCount = 0, assessedCount = 0, reviewedCount = 0;


        try {
            Customer currCust = custRepo.findOne(custId);
            if (currCust == null) {
                log.error("customersCustIdApplicationAssessmentProgressGet....customer not found {}", custId);
                return new ResponseEntity<ApplicationAssessmentProgressType>(HttpStatus.BAD_REQUEST);
            }

            List<Applications> apps = currCust.getApplications();

            if ((apps == null) || (apps.isEmpty())) {
                log.warn("Customer {} has no applications...", custId);
            } else {

                appCount = currCust.getApplications().size();

                for (Applications currApp : apps) {
                    ApplicationAssessmentReview review = currApp.getReview();
                    if (review != null) {
                        reviewedCount++;
                    }
                    List<Assessments> assmList = currApp.getAssessments();
                    if ((assmList != null) && (!assmList.isEmpty())) {
                        assessedCount++;
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Error while processing customersCustIdApplicationAssessmentProgressGet", ex.getMessage(), ex);
            return new ResponseEntity<ApplicationAssessmentProgressType>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        resp.setAppcount(appCount);
        resp.setAssessed(assessedCount);
        resp.setReviewed(reviewedCount);

        return new ResponseEntity<ApplicationAssessmentProgressType>(resp, HttpStatus.OK);
    }
}
