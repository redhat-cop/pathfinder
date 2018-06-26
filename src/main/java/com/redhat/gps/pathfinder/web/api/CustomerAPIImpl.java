package com.redhat.gps.pathfinder.web.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.redhat.gps.pathfinder.domain.ApplicationAssessmentReview;
import com.redhat.gps.pathfinder.domain.Applications;
import com.redhat.gps.pathfinder.domain.Assessments;
import com.redhat.gps.pathfinder.domain.Customer;
import com.redhat.gps.pathfinder.domain.QuestionMetaData;
import com.redhat.gps.pathfinder.domain.QuestionWeights;
import com.redhat.gps.pathfinder.repository.ApplicationsRepository;
import com.redhat.gps.pathfinder.repository.AssessmentsRepository;
import com.redhat.gps.pathfinder.repository.CustomerRepository;
import com.redhat.gps.pathfinder.repository.QuestionMetaDataRepository;
import com.redhat.gps.pathfinder.repository.ReviewsRepository;
import com.redhat.gps.pathfinder.web.api.model.ApplicationAssessmentProgressType;
import com.redhat.gps.pathfinder.web.api.model.ApplicationNames;
import com.redhat.gps.pathfinder.web.api.model.ApplicationSummaryType;
import com.redhat.gps.pathfinder.web.api.model.ApplicationType;
import com.redhat.gps.pathfinder.web.api.model.AssessmentProcessQuestionResultsType;
import com.redhat.gps.pathfinder.web.api.model.AssessmentProcessType;
import com.redhat.gps.pathfinder.web.api.model.AssessmentResponse;
import com.redhat.gps.pathfinder.web.api.model.AssessmentType;
import com.redhat.gps.pathfinder.web.api.model.CustomerType;
import com.redhat.gps.pathfinder.web.api.model.DependenciesListType;
import com.redhat.gps.pathfinder.web.api.model.DepsPairType;
import com.redhat.gps.pathfinder.web.api.model.ReviewType;

import io.swagger.annotations.ApiParam;

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
    
    
    // Non-Swagger api - returns the survey payload
    @RequestMapping(value="/survey", method=GET, produces={APPLICATION_JSON_VALUE})
    public String getSurvey() throws IOException {
      return getSurveyContent().replaceAll("\"SERVER_URL", "Utils.SERVER+\"");
    }
    
    private String getSurveyContent() throws IOException{
      return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("application-survey.js"), "UTF-8");
    }
    
    
    // Non-Swagger api - returns payload for the assessment summary page
    @RequestMapping(value="/customers/{customerId}/applications/{appId}/assessments/{assessmentId}/viewAssessmentSummary", method=GET, produces={APPLICATION_JSON_VALUE})
    public String viewAssessmentSummary(@PathVariable("customerId") String customerId, @PathVariable("appId") String appId, @PathVariable("assessmentId") String assessmentId) throws IOException{
      class ApplicationAssessmentSummary{
        public ApplicationAssessmentSummary(String q, String a, String r){
          this.question=q;
          this.answer=a;
          this.rating=r;
        }
        private String question; public String getQuestion() {return question;}
        private String answer;   public String getAnswer()   {return answer;}
        private String rating;   public String getRating()   {return rating;}
      }
      log.debug("viewAssessmentSummary....");
      
      // Get the survey json content (and fiddle with it so it's readable)
      String raw=getSurveyContent();
      int start=raw.indexOf("pages: [{")+7;
      int end=raw.indexOf("}],")+2;
      String x=raw.substring(start, end);
      
      mjson.Json surveyJson=mjson.Json.read(x);
      
      // Find the assessment in mongo
      Assessments assessment = assmRepo.findOne(assessmentId);
      if (null==assessment){
        log.error("Unable to find assessment: "+assessmentId);
        return null;
      }
      
      List<ApplicationAssessmentSummary> result=new ArrayList<ApplicationAssessmentSummary>();
      
      for(mjson.Json page:surveyJson.asJsonList()){
        for(mjson.Json question:page.at("questions").asJsonList()){
          
          if (question.at("type").asString().equals("radiogroup")){
            
            Map<String, String> answerRankingMap=new HashMap<String, String>();
            for(mjson.Json a:question.at("choices").asJsonList())
              answerRankingMap.put(a.asString().split("-")[0], a.asString().split("-")[1]); // answer id to ranking map
            
            try{
              String answerOrdinal=((String)assessment.getResults().get(question.at("name").asString())).split("-")[0]; // should return integer of the value chosen
              String answerRating=answerRankingMap.get(answerOrdinal).split("\\|")[0];
              String answerText=answerRankingMap.get(answerOrdinal).split("\\|")[1];
              String questionText=question.at("title").asString();
              
              log.debug("questionText="+questionText+", answerOrdinal="+answerOrdinal+", answerText="+answerText+", rating="+answerRating);
              
              result.add(new ApplicationAssessmentSummary(question.at("title").asString(), answerText, answerRating));
              
            }catch(Exception e){
              log.error(e.getMessage(), e);
              log.error("Error on: assessment.results="+assessment.getResults());
              log.error("Error on: question.name="+question.at("name").asString());
              log.error("Error on: assessment.results[question.name]="+assessment.getResults().get(question.at("name").asString()));
            }
            
          }else if (question.at("type").asString().equals("rating")){
            // leave this out since it's things like "Select the app..."
          }
        }
      }
      return Json.newObjectMapper(true).writeValueAsString(result);
    }
    
    ////// ###############
//    public void dummy(){
//      Assessments currAssm = assmRepo.findOne(assessId);
//      if (currAssm == null) {
//          return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
//      }
//
//      List<QuestionMetaData> questionData = questionRepository.findAll();
//
//
//      for (QuestionMetaData currQuestion : questionData) {
//          String res = (String) currAssm.getResults().get(currQuestion.getId());
//          AssessmentProcessQuestionResultsType vals = new AssessmentProcessQuestionResultsType();
//          vals.setQuestionTag(currQuestion.getId());
//
//          QuestionWeights.QuestionRank answerRank = currQuestion.getMetaData().get(Integer.parseInt(res)).getRank();
//          vals.setQuestionRank(answerRank.ordinal());
//          assessResults.add(vals);
//
//          log.debug(currQuestion.getId() + ": value=" + res + " RANK " + answerRank.toString());
//
//      }
//      resp.setAssessResults(assessResults);
//      resp.setAssmentNotes(currAssm.getResults().get("NOTES"));
//      resp.setDependencies(currAssm.getDeps());
//      resp.setBusinessPriority(currAssm.getResults().get("BUSPRIORITY"));
//    }
    ///// ###################
    
    @Timed
    public ResponseEntity<ApplicationNames> customersCustIdApplicationsAppIdCopyPost(@ApiParam(value = "", required = true) @PathVariable("custId") String custId, @ApiParam(value = "", required = true) @PathVariable("appId") String appId, @ApiParam(value = "Target Application Names") @Valid @RequestBody ApplicationNames body) {
        log.debug("customersCustIdApplicationsAppIdCopyPost....{} ", body.toString());
        ApplicationNames appIDS = new ApplicationNames();

        if (body.isEmpty()) {
            log.error("customersCustIdApplicationsAppIdAssessmentsPost....Empty list of target application names");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            Customer currCust = custRepo.findOne(custId);

            if (currCust == null) {
                log.error("customersCustIdApplicationsAppIdAssessmentsPost....customer not found " + custId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Applications currApp = appsRepo.findOne(appId);

            if (currApp == null) {
                log.error("customersCustIdApplicationsAppIdAssessmentsPost....app not found " + appId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            ApplicationAssessmentReview currReview = currApp.getReview();

            if (currReview == null) {
                log.error("customersCustIdApplicationsAppIdAssessmentsPost....now reviews for app " + appId);
                return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
            }

            List<Assessments> currAssessments = currApp.getAssessments();

            if ((currAssessments == null) || (currAssessments.isEmpty())) {
                log.error("customersCustIdApplicationsAppIdAssessmentsPost....now assessments for app " + appId);
                return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
            }
            Assessments latestAssessment = currAssessments.get(currAssessments.size() - 1);

            List<Applications> listApps = currCust.getApplications();

            body.forEach((appName)-> {
                log.debug("Creating application "+appName);
                //Copy Assessment (latest only)
                Assessments newAssessment = new Assessments();
                newAssessment.setId(UUID.randomUUID().toString());
                newAssessment.setDatetime(latestAssessment.getDatetime());
                if (!latestAssessment.getDeps().isEmpty())
                    newAssessment.setDeps(latestAssessment.getDeps());
                newAssessment.setResults(latestAssessment.getResults());
                newAssessment = assmRepo.save(newAssessment);

                //Copy review
                ApplicationAssessmentReview newReview = new ApplicationAssessmentReview(
                    currReview.getReviewDate(),
                    newAssessment,
                    currReview.getReviewDecision(),
                    currReview.getReviewEstimate(),
                    currReview.getReviewNotes(),
                    currReview.getWorkPriority(),
                    currReview.getBusinessPriority());
                newReview.setId(UUID.randomUUID().toString());
                newReview = reviewRepository.insert(newReview);

                //Create application
                List<Assessments> assArray = new ArrayList<>();
                assArray.add(newAssessment);
                Applications newApp = new Applications();
                newApp.setDescription(currApp.getDescription());
                newApp.setStereotype(currApp.getStereotype());
                newApp.setAssessments(assArray);
                newApp.setReview(newReview);
                newApp.setName(appName);
                Applications app = new Applications();
                newApp.setId(UUID.randomUUID().toString());
                newApp = appsRepo.insert(newApp);

                listApps.add(newApp);
                appIDS.add(newApp.getId());
            });

            //Update customer
            currCust.setApplications(listApps);
            custRepo.save(currCust);

        } catch (Exception ex) {
            log.error("customersCustIdApplicationsAppIdCopyPost...Unable to copy applications for customer ", ex.getMessage(), ex);
        }
        return new ResponseEntity<>(appIDS, HttpStatus.OK);
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
                return new ResponseEntity<>(resp, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.error("customersCustIdApplicationsAppIdAssessmentsAssessIdGet", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
                    return new ResponseEntity<>(results, HttpStatus.OK);
                }
            } catch (Exception ex) {
                log.error("Unable to get assessments for customer ", ex.getMessage(), ex);
            }
            return new ResponseEntity<>(results, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            log.error("customersCustIdApplicationsAppIdAssessmentsGet", ex.getMessage(), ex);
        }
        return new ResponseEntity<>(results, HttpStatus.BAD_REQUEST);
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
                return new ResponseEntity<>("Customer Not found", HttpStatus.BAD_REQUEST);
            }

            Applications currApp = appsRepo.findOne(appId);

            if (currApp != null) {

                Assessments newitem = new Assessments();
                newitem.setId(UUID.randomUUID().toString());
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
                appsRepo.save(currApp);
                return new ResponseEntity<>(newitem.getId(), HttpStatus.OK);
            } else {
                log.error("customersCustIdApplicationsAppIdAssessmentsPost....app not found " + appId);
                return new ResponseEntity<>("Application not found", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.error("customersCustIdApplicationsAppIdAssessmentsPost Unable to create applications for customer ", ex.getMessage(), ex);
        }
        return new ResponseEntity<>("Unable to create assessment", HttpStatus.BAD_REQUEST);
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
            if ((details.getStereotype() != null) && (!details.getStereotype().isEmpty())) {
                resp.setStereotype(ApplicationType.StereotypeEnum.fromValue(details.getStereotype()));
            }
        } catch (Exception ex) {
            log.error("Unable to get applications for customer ", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @Override
    @Timed
    public ResponseEntity<List<ApplicationType>> customersCustIdApplicationsGet(@ApiParam(value = "", required = true) @PathVariable("custId") String custId, @ApiParam(value = "TARGETS,DEPENDENCIES,PROFILES") @RequestParam(value = "apptype", required = false) String apptype) {
        log.info("customersCustIdApplicationsGet....[" + custId + "]");
        ArrayList<ApplicationType> response = new ArrayList<>();
        try {
            Customer myCust = custRepo.findOne(custId);
            if (myCust == null) {
                log.error("customersCustIdApplicationsGet....[" + custId + "] customer not found");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
                    if (x.getStereotype() != null) {
                        if (apptype != null) {
                            switch (apptype) {
                                case "TARGETS":  //Explicit targets
                                    if (x.getStereotype().equals(ApplicationType.StereotypeEnum.TARGETAPP.toString())) {
                                        lapp.setStereotype(ApplicationType.StereotypeEnum.fromValue(x.getStereotype()));
                                        response.add(lapp);
                                    }
                                    break;
                                case "DEPENDENCIES": //Dependencies - Everything but Profiles
                                    if (!x.getStereotype().equals(ApplicationType.StereotypeEnum.PROFILE.toString())) {
                                        lapp.setStereotype(ApplicationType.StereotypeEnum.fromValue(x.getStereotype()));
                                        response.add(lapp);
                                    }
                                    break;
                                case "PROFILES": //Explicit Profiles
                                    if (x.getStereotype().equals(ApplicationType.StereotypeEnum.PROFILE.toString())) {
                                        lapp.setStereotype(ApplicationType.StereotypeEnum.fromValue(x.getStereotype()));
                                        response.add(lapp);
                                    }
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            //maintain backward compatibility with initial api when no apptype is passed - All Dependencies
                            if (!x.getStereotype().equals(ApplicationType.StereotypeEnum.PROFILE.toString())) {
                                lapp.setStereotype(ApplicationType.StereotypeEnum.fromValue(x.getStereotype()));
                                response.add(lapp);
                            }
                        }
                    } else {
                        response.add(lapp);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Unable to list applications for customer ", ex.getMessage(), ex);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Timed
    public ResponseEntity<String> customersCustIdApplicationsPost
        (@ApiParam(value = "Customer Identifier", required = true) @PathVariable("custId") String
             custId, @ApiParam(value = "Application Definition") @Valid @RequestBody ApplicationType body) {
        log.debug("customersCustIdApplicationsPost....");
        Customer myCust = custRepo.findOne(custId);
        if (myCust == null) {
            return new ResponseEntity<>(custId, HttpStatus.BAD_REQUEST);
        } else {
            Applications app = new Applications();
            app.setId(UUID.randomUUID().toString());
            app.setName(body.getName());
            app.setDescription(body.getDescription());
            if (body.getStereotype() == null) {
                log.warn("customersCustIdApplicationsPost....application stereotype missing ");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                app.setStereotype(body.getStereotype().toString());
            }
            app = appsRepo.save(app);
            List<Applications> appList = myCust.getApplications();
            if (appList == null) {
                appList = new ArrayList<Applications>();
            }
            appList.add(app);
            myCust.setApplications(appList);
            custRepo.save(myCust);
            return new ResponseEntity<>(app.getId(), HttpStatus.OK);
        }
    }

    @Timed
    public ResponseEntity<CustomerType> customersCustIdGet
        (@ApiParam(value = "Customer Identifier", required = true) @PathVariable("custId") String custId) {
        log.debug("customersCustIdGet....{}", custId);
        Customer myCust = custRepo.findOne(custId);
        if (myCust == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            CustomerType resp = new CustomerType();
            resp.setCustomerName(myCust.getName());
            resp.setCustomerId(myCust.getId());
            resp.setCustomerDescription(myCust.getDescription());
            resp.setCustomerSize(myCust.getSize());
            resp.setCustomerVertical(myCust.getVertical());
            resp.setCustomerRTILink(myCust.getRtilink());
            resp.setCustomerAssessor(myCust.getAssessor());
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }
    }

    @Timed
    public ResponseEntity<String> customersPost(@ApiParam(value = "") @Valid @RequestBody CustomerType body) {
        log.debug("customersPost....{}", body);
        Customer myCust = new Customer();

        myCust.setId(UUID.randomUUID().toString());
        myCust.setName(body.getCustomerName());
        myCust.setDescription(body.getCustomerDescription());
        myCust.setVertical(body.getCustomerVertical());
        myCust.setSize(body.getCustomerSize());
        myCust.setRtilink(body.getCustomerRTILink());
        myCust.setVertical(body.getCustomerVertical());
        myCust.setAssessor(body.getCustomerAssessor());
        try {
            myCust = custRepo.insert(myCust);
        } catch (Exception ex) {
            log.error("Unable to Create customer ", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(myCust.getId(), HttpStatus.OK);
    }

    @Timed
    public ResponseEntity<List<CustomerType>> customersGet() {
        log.debug("customersGet....");
        ArrayList<CustomerType> response = new ArrayList<>();
        List<Customer> myCust = custRepo.findAll();
        if (myCust == null) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            for (Customer x : myCust) {
                CustomerType resp = new CustomerType();
                resp.setCustomerName(x.getName());
                resp.setCustomerId(x.getId());
                resp.setCustomerSize(x.getSize());
                resp.setCustomerVertical(x.getVertical());

                int total = x.getApplications() != null ? x.getApplications().size() : 0;
                resp.setCustomerAssessor(x.getAssessor());
                resp.setCustomerRTILink(x.getRtilink());

                int assessedCount = 0;
                int reviewedCount = 0;
                if (total > 0) {
                    for (Applications app : x.getApplications()) {
                        ApplicationAssessmentReview review = app.getReview();
                        // if review is null, then it's not been reviewed
                        reviewedCount += (review != null ? 1 : 0);

                        List<Assessments> assmList = app.getAssessments();
                        if ((assmList != null) && (!assmList.isEmpty())) {
                            assessedCount += 1;
                        }
                    }
                    // reviewedCount + assessedCount / potential total (ie. total * 2)
                    BigDecimal percentageComplete = new BigDecimal(100 * (double) (assessedCount + reviewedCount) / (double) (total * 2));
                    percentageComplete.setScale(0, BigDecimal.ROUND_DOWN);
                    resp.setCustomerPercentageComplete(percentageComplete.intValue());// a merge of assessed & reviewed
                } else {
                    resp.setCustomerPercentageComplete(0);
                }

                response.add(resp);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
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
                return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
            }

            List<QuestionMetaData> questionData = questionRepository.findAll();


            for (QuestionMetaData currQuestion : questionData) {
                String res = (String) currAssm.getResults().get(currQuestion.getId());
                AssessmentProcessQuestionResultsType vals = new AssessmentProcessQuestionResultsType();
                vals.setQuestionTag(currQuestion.getId());

                QuestionWeights.QuestionRank answerRank = currQuestion.getMetaData().get(Integer.parseInt(res)).getRank();
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
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @Timed
    public ResponseEntity<String> customersCustIdApplicationsAppIdReviewPost(@ApiParam(value = "", required = true) @PathVariable("custId") String custId, @ApiParam(value = "", required = true) @PathVariable("appId") String appId, @ApiParam(value = "Application Definition") @Valid @RequestBody ReviewType body) {
        log.debug("customersCustIdApplicationsAppIdReviewPost....");
        try {
            Applications app = appsRepo.findOne(appId);
            if (app == null) {
                log.error("Error while processing review - Unable to find application with id {}", appId);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Assessments assm = assmRepo.findOne(body.getAssessmentId());
            if (assm == null) {
                log.error("Error while processing review - Unable to find assessment with id {}", body.getAssessmentId());
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            ApplicationAssessmentReview reviewData = new ApplicationAssessmentReview(
                Long.toString(System.currentTimeMillis()),
                assm,
                body.getReviewDecision().toString(),
                body.getWorkEffort().toString(),
                body.getReviewNotes(),
                body.getWorkPriority(),
                body.getBusinessPriority());

            if (app.getReview() != null) {
                reviewData.setId(app.getReview().getId());
            } else {
                reviewData.setId(UUID.randomUUID().toString());
            }


            reviewData = reviewRepository.save(reviewData);
            app.setReview(reviewData);
            appsRepo.save(app);

            return new ResponseEntity<>(reviewData.getId(), HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Error while processing review", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (app.getReview() == null) {
                log.error("Error while retrieving review - no review associated with application {}", reviewId);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            ApplicationAssessmentReview reviewData = reviewRepository.findOne(app.getReview().getId());

            if (reviewData == null) {
                log.error("Error while retrieving review - Unable to find review for application {}", appId);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            resp.setAssessmentId(reviewData.getAssessments().getId());
            resp.setReviewDecision(ReviewType.ReviewDecisionEnum.fromValue(reviewData.getReviewDecision()));
            resp.setReviewNotes(reviewData.getReviewNotes());
            resp.setWorkEffort(ReviewType.WorkEffortEnum.fromValue(reviewData.getReviewEstimate()));
            resp.setReviewTimestamp(reviewData.getReviewDate());
            resp.setWorkPriority(reviewData.getWorkPriority());
            resp.setBusinessPriority(reviewData.getBusinessPriority());

            return new ResponseEntity<>(resp, HttpStatus.OK);

        } catch (Exception ex) {
            log.error("Error while processing review", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Timed
    public ResponseEntity<List<ReviewType>> customersCustIdReviewsGet(@ApiParam(value = "Customer Identifier", required = true) @PathVariable("custId") String custId) {
        log.debug("customersCustIdReviewsGet...." + custId);
        ArrayList<ReviewType> resp = new ArrayList<>();

        try {
            Customer currCust = custRepo.findOne(custId);
            if (currCust == null) {
                log.error("customersCustIdReviewsGet....customer not found {}", custId);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            List<Applications> appList = currCust.getApplications();
            if ((appList == null) || (appList.isEmpty())) {
                log.error("customersCustIdReviewsGet....no applications for customer {}", custId);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            for (Applications x : appList) {
                ApplicationAssessmentReview tmpRev = x.getReview();
                if (tmpRev != null) {
                    ReviewType newRev = new ReviewType();
                    newRev.setBusinessPriority(tmpRev.getBusinessPriority());
                    newRev.setWorkPriority(tmpRev.getWorkPriority());
                    newRev.setReviewTimestamp(tmpRev.getReviewDate());
                    newRev.setWorkEffort(ReviewType.WorkEffortEnum.fromValue(tmpRev.getReviewEstimate()));
                    newRev.setReviewNotes(tmpRev.getReviewNotes());
                    newRev.setReviewDecision(ReviewType.ReviewDecisionEnum.fromValue(tmpRev.getReviewDecision()));
                    newRev.setAssessmentId(x.getName());
                    resp.add(newRev);
                }
            }

        } catch (Exception ex) {
            log.error("Error while processing review", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @Timed
    public ResponseEntity<Void> customersCustIdApplicationsAppIdDelete(@ApiParam(value = "Customer Identifier", required = true) @PathVariable("custId") String custId, @ApiParam(value = "Application Identifier", required = true) @PathVariable("appId") String appId) {
        log.debug("customersCustIdApplicationsAppIdDelete {} {}", custId, appId);

        try {
            Customer currCust = custRepo.findOne(custId);
            if (currCust == null) {
                log.error("customersCustIdApplicationsAppIdDelete....customer not found {}", custId);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Applications delApp = appsRepo.findOne(appId);
            if (delApp == null) {
                log.error("customersCustIdApplicationsAppIdDelete....application not found {}", appId);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Timed
    public ResponseEntity<Void> customersCustIdApplicationsAppIdReviewReviewIdDelete(@ApiParam(value = "", required = true) @PathVariable("custId") String custId, @ApiParam(value = "", required = true) @PathVariable("appId") String appId, @ApiParam(value = "", required = true) @PathVariable("reviewId") String reviewId) {
        log.debug("customersCustIdApplicationsAppIdReviewReviewIdDelete {} {} {}", custId, appId, reviewId);

        try {
            Customer currCust = custRepo.findOne(custId);
            if (currCust == null) {
                log.error("customersCustIdApplicationsAppIdReviewReviewIdDelete....customer not found {}", custId);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Applications currApp = appsRepo.findOne(appId);
            if (currApp == null) {
                log.error("customersCustIdApplicationsAppIdReviewReviewIdDelete....application not found {}", appId);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (currApp.getReview().getId().equalsIgnoreCase(reviewId)) {
                currApp.setReview(null);
                appsRepo.save(currApp);
                reviewRepository.delete(reviewId);
            } else {
                log.error("customersCustIdApplicationsAppIdReviewReviewIdDelete....review {} not found for application", reviewId, appId);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.error("Error while deleting review", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Timed
    public ResponseEntity<Void> customersCustIdApplicationsAppIdAssessmentsAssessIdDelete(@ApiParam(value = "", required = true) @PathVariable("custId") String custId, @ApiParam(value = "", required = true) @PathVariable("appId") String appId, @ApiParam(value = "", required = true) @PathVariable("assessId") String assessId) {
        log.debug("customersCustIdApplicationsAppIdAssessmentsAssessIdDelete {} {} {}", custId, appId, assessId);

        try {
            Customer currCust = custRepo.findOne(custId);
            if (currCust == null) {
                log.error("customersCustIdApplicationsAppIdAssessmentsAssessIdDelete....customer not found {}", custId);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Applications currApp = appsRepo.findOne(appId);
            if (currApp == null) {
                log.error("customersCustIdApplicationsAppIdAssessmentsAssessIdDelete....application not found {}", appId);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            List<Assessments> assmList = currApp.getAssessments();

            if ((assmList == null) || (assmList.isEmpty())) {
                log.error("customersCustIdApplicationsAppIdAssessmentsAssessIdDelete....assessment list is null for app {}", appId);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.error("Error while deleting assessment", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Timed
    public ResponseEntity<Void> customersCustIdDelete(@ApiParam(value = "Customer Identifier", required = true) @PathVariable("custId") String custId) {
        log.debug("customersCustIdDelete {}", custId);
        try {
            Customer currCust = custRepo.findOne(custId);
            if (currCust == null) {
                log.error("customersCustIdDelete....customer not found {}", custId);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if ((currCust.getApplications() != null) && (!currCust.getApplications().isEmpty())) {
                log.error("Customer {} has applications...not deleting", custId);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            custRepo.delete(custId);
        } catch (Exception ex) {
            log.error("Error while deleting customer", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Timed
    public ResponseEntity<List<ApplicationSummaryType>> customersCustIdApplicationAssessmentSummaryGet(@ApiParam(value = "Customer Identifier", required = true) @PathVariable("custId") String custId) {
        log.debug("customersCustIdApplicationAssessmentSummaryGet {}", custId);
        List<ApplicationSummaryType> resp = new ArrayList<>();
        try {
            Customer currCust = custRepo.findOne(custId);
            if (currCust == null) {
                log.error("customersCustIdApplicationAssessmentSummaryGet....customer not found {}", custId);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            List<Applications> apps = currCust.getApplications();

            if ((apps == null) || (apps.isEmpty())) {
                log.info("customersCustIdApplicationAssessmentSummaryGet Customer {} has no applications...", custId);

            } else {

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
            }
        } catch (Exception ex) {
            log.error("Error while processing customersCustIdApplicationAssessmentSummaryGet", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(resp, HttpStatus.OK);
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
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        resp.setAppcount(appCount);
        resp.setAssessed(assessedCount);
        resp.setReviewed(reviewedCount);

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }


    public ResponseEntity<DependenciesListType> customersCustIdDependencyTreeGet(@ApiParam(value = "Customer Identifier", required = true) @PathVariable("custId") String custId) {
        log.debug("customersCustIdDependencyTreeGet {}", custId);
        DependenciesListType respDeps = new DependenciesListType();

        try {
            Customer currCust = custRepo.findOne(custId);
            if (currCust == null) {
                log.error("customersCustIdDependencyTreeGet....customer not found {}", custId);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            List<Applications> apps = currCust.getApplications();

            if ((apps == null) || (apps.isEmpty())) {
                log.warn("Customer {} has no applications...", custId);
            } else {
                for (Applications currApp : apps) {
                    List<Assessments> currAssmList = currApp.getAssessments();

                    if (currAssmList.size() == 0) {
                        log.info("Application {} has no assessments...", currApp.getId());
                    } else {

                        String assmID = currAssmList.get(currAssmList.size() - 1).getId();
                        Assessments currAssm = assmRepo.findOne(assmID);

                        List<String> depList = currAssm.getDeps();

                        if (depList.size() == 0) {
                            log.info("Application {} has no dependencies...", currApp.getId());
                        } else {
                            for (String currDep : depList) {
                                DepsPairType dep = new DepsPairType();
                                dep.from(currApp.getId());
                                dep.to(currDep);
                                respDeps.addDepsListItem(dep);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Error while processing customersCustIdDependencyTreeGet", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respDeps, HttpStatus.OK);
    }
}
