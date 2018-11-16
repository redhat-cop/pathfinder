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

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.redhat.gps.pathfinder.domain.ApplicationAssessmentReview;
import com.redhat.gps.pathfinder.domain.Applications;
import com.redhat.gps.pathfinder.domain.Assessments;
import com.redhat.gps.pathfinder.domain.Customer;
import com.redhat.gps.pathfinder.repository.ApplicationsRepository;
import com.redhat.gps.pathfinder.repository.AssessmentsRepository;
import com.redhat.gps.pathfinder.repository.CustomerRepository;
import com.redhat.gps.pathfinder.repository.MembersRepository;
import com.redhat.gps.pathfinder.repository.ReviewsRepository;
import com.redhat.gps.pathfinder.service.util.Json;
import com.redhat.gps.pathfinder.service.util.MapBuilder;
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
import com.redhat.gps.pathfinder.web.api.model.IdentifierList;
import com.redhat.gps.pathfinder.web.api.model.MemberType;
import com.redhat.gps.pathfinder.web.api.model.ReviewType;

import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/pathfinder")
public class CustomerAPIImpl extends SecureAPIImpl implements CustomersApi{
    private final Logger log = LoggerFactory.getLogger(CustomerAPIImpl.class);
    private final CustomerRepository custRepo;
    private final ApplicationsRepository appsRepo;
    private final AssessmentsRepository assmRepo;
    private final ReviewsRepository reviewRepository;
    private final MembersRepository membersRepo;

    public CustomerAPIImpl(CustomerRepository custRepo, ApplicationsRepository appsRepo, AssessmentsRepository assmRepo, ReviewsRepository reviewRepository, MembersRepository membersRepository) {
      super(membersRepository);
      this.custRepo = custRepo;
      this.appsRepo = appsRepo;
      this.assmRepo = assmRepo;
      this.reviewRepository = reviewRepository;
      this.membersRepo=membersRepository;
    }

    
    // Another fail by Spring... cannot define multiple controllers with the same mapping when extending from an interface - going to have to have duplicate/delegate methods
    // Get Members
    // GET: /api/pathfinder/customers/{customerId}/member/
    public ResponseEntity<List<MemberType>> customersCustIdMembersGet(@ApiParam(value="", required=true) @PathVariable("custId") String custId){
    	return new MemberController(custRepo, membersRepo).getMembers(custId);
    }

    // Create Member
    // POST: /api/pathfinder/customers/{customerId}/members/
	  public ResponseEntity<String> customersCustIdMembersPost(@ApiParam(value = "Customer Identifier",required=true ) @PathVariable("custId") String custId,@ApiParam(value = "Member Details"  )  @Valid @RequestBody MemberType body) {
	  	return new MemberController(custRepo, membersRepo).createMember(custId, body);
	  }
	  
	  // Get Member
	  // GET: /api/pathfinder/customers/{customerId}/members/{memberId}
	  public ResponseEntity<MemberType> customersCustIdMembersMemberIdGet(@ApiParam(value = "Customer Identifier",required=true ) @PathVariable("custId") String custId,@ApiParam(value = "Member Identifier",required=true ) @PathVariable("memberId") String memberId) {
	  	return new MemberController(custRepo, membersRepo).getMember(custId, memberId);
	  }
	  
	  // Update Member
	  // POST: /api/pathfinder/customers/{customerId}/members/{memberId}
	  public ResponseEntity<String> customersCustIdMembersMemberIdPost(@ApiParam(value = "Customer Identifier",required=true ) @PathVariable("custId") String custId,@ApiParam(value = "Member Identifier",required=true ) @PathVariable("memberId") String memberId,@ApiParam(value = "Member Details"  )  @Valid @RequestBody MemberType body) {
	  	return new MemberController(custRepo, membersRepo).updateMember(custId, memberId, body);
	  }
	  
	  // Delete Member(s)
	  // POST: /customers/{custId}/members/
	  public ResponseEntity<String> customersCustIdMembersDelete(@ApiParam(value = "Customer Identifier",required=true ) @PathVariable("custId") String custId,@ApiParam(value = "Target member IDs"  )  @Valid @RequestBody IdentifierList body) {
	  	return new MemberController(custRepo, membersRepo).deleteMembers(custId, body);
	  }
    
    // Non-Swagger api - report page content
    @RequestMapping(value="/customers/{custId}/report", method=GET, produces=MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public String getReport(@PathVariable("custId") String custId) throws IOException {
      log.debug("getReport()...");
      
      class Risk{
        public Risk(String q, String a, String apps){
          this.q=q;this.a=a;this.apps=apps;
        }
        String q; public String getQuestion(){return q;}
        String a; public String getAnswer(){return a;}
        String apps; public String getOffendingApps(){return apps;}
      }
      class Report{
        Map<String,Double> s;
        public Map<String,Double> getAssessmentSummary() {
          if (null==s) s=new HashMap<String,Double>(); return s;
        }
        List<Risk> risks;
        public List<Risk> getRisks() {
          if (null==risks) risks=new ArrayList<Risk>(); return risks;
        }
      }  
      
      Report result=new Report();
      
      Customer customer=custRepo.findOne(custId);
      
      Map<String,Integer> overallStatusCount=new HashMap<>();
      overallStatusCount.put("GREEN",0);
      overallStatusCount.put("AMBER",0);
      overallStatusCount.put("RED",0);
      int assessmentTotal=0;
      Map<String, Risk> risks2=new HashMap<>();
      
      if (null!=customer.getApplications()){
        for(Applications app:customer.getApplications()){
          if (null==app.getAssessments()) continue;
          Assessments assessment=app.getAssessments().get(app.getAssessments().size()-1);
          
          Map<String,Map<String,String>> questionKeyToText=new QuestionReader<Map<String,Map<String,String>>>().read(new HashMap<String,Map<String,String>>(), getSurveyContent(), assessment, new QuestionParser<Map<String,Map<String,String>>>(){
            @Override
            public void parse(Map<String,Map<String,String>> result, String name, String answerOrdinal, String answerRating, String answerText, String questionText){
              result.put(name, new MapBuilder<String, String>()
                  .put("questionText", questionText)
                  .put("answerText", answerText)
                  .build());
            }
          });
          
          
          String assessmentOverallStatus="GREEN";
          int mediumCount=0;
          for(Entry<String, String> e:assessment.getResults().entrySet()){
  //          System.out.println(e.getKey() +"="+ e.getValue());
            
            // If ANY answers were RED, then the status is RED
            if (e.getValue().contains("-RED")){
              assessmentOverallStatus="RED";
              
              // add the RED item to the risk list and add the app name against the risk
              String riskQuestionAnswerKey=e.getKey()+e.getValue();
              if (!risks2.containsKey(riskQuestionAnswerKey)){
                String question=questionKeyToText.get(e.getKey()).get("questionText");
                String answer=questionKeyToText.get(e.getKey()).get("answerText");
                risks2.put(riskQuestionAnswerKey, new Risk(question, answer, app.getName()));
              }else{
                risks2.get(riskQuestionAnswerKey).apps=Joiner.on(",").join(risks2.get(riskQuestionAnswerKey).getOffendingApps().split(","));
              }
              
            }
            
            if (e.getValue().contains("-AMBER"))
              mediumCount=mediumCount+1;
            
            // If more than 30% of answers were AMBER, then overall rating is AMBER
            double percentageOfAmbers=(double)mediumCount/(double)assessment.getResults().size();
            double threshold=0.3;
            if ("GREEN".equals(assessmentOverallStatus) && percentageOfAmbers>threshold){
              log.debug("getReport():: amber answer percentage is {} which is over the {}% threshold, therefore downgrading to AMBER rating", (percentageOfAmbers*100), (threshold*100));
              assessmentOverallStatus="AMBER";
            }
          }
          
          assessmentTotal=assessmentTotal+1;
          overallStatusCount.put(assessmentOverallStatus, overallStatusCount.get(assessmentOverallStatus)+1);
          
        }
      }
      
      result.risks=Lists.newArrayList(risks2.values());
      
      result.getAssessmentSummary().put("Easy",   (double)overallStatusCount.get("GREEN"));
      result.getAssessmentSummary().put("Medium", (double)overallStatusCount.get("AMBER"));
      result.getAssessmentSummary().put("Hard",   (double)overallStatusCount.get("RED"));
      result.getAssessmentSummary().put("Total",  (double)assessmentTotal);
      
      return Json.newObjectMapper(true).writeValueAsString(result);
    }

    
    // Non-Swagger api - returns the swagger docs
    @RequestMapping(value="/docs", method=GET, produces={"application/javascript"})
    public String getDocs() throws IOException {
    	return Json.yamlToJson(IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("swagger/api.yml"), "UTF-8"));
    }

    // Non-Swagger api - returns the survey payload
    @RequestMapping(value="/survey", method=GET, produces={"application/javascript"})
    public String getSurvey() throws IOException {
      return getSurveyContent().replaceAll("\"SERVER_URL", "Utils.SERVER+\"").replaceAll("JWT_TOKEN", "\"+jwtToken+\"") ;
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
      
      // Find the assessment in mongo
      Assessments assessment = assmRepo.findOne(assessmentId);
      if (null==assessment){
        log.error("Unable to find assessment: "+assessmentId);
        return null;
      }
      
//      // Get the survey json content (and fiddle with it so it's readable)
      List<ApplicationAssessmentSummary> result=new QuestionReader<List<ApplicationAssessmentSummary>>().read(new ArrayList<ApplicationAssessmentSummary>(), getSurveyContent(), assessment, new QuestionParser<List<ApplicationAssessmentSummary>>(){
        @Override
        public void parse(List<ApplicationAssessmentSummary> result, String name, String answerOrdinal, String answerRating, String answerText, String questionText){
          result.add(new ApplicationAssessmentSummary(questionText, answerText, answerRating));
        }
      });
      
      return Json.newObjectMapper(true).writeValueAsString(result);
    }
    
    /* Uh-oh, Uncle Noel is going to murder me for this one... yes, yes I will convert to Swagger later on, chalk it up on the technical debt board! */
    @RequestMapping(value="/assessmentResults", method=GET, produces={"application/javascript"})
    public String getAssessmentResults(HttpServletRequest request) throws IOException {
      log.debug("getAssessmentResults...");
      String assessmentId=request.getParameter("assessmentId");
      Assessments assessment=assmRepo.findOne(assessmentId);
      
      Map<String, Object> result=new HashMap<>();
      result.putAll(assessment.getResults());
      result.put("DEPSINLIST", assessment.getDepsIN());
      result.put("DEPSOUTLIST", assessment.getDepsOUT());
      
      return Json.newObjectMapper(true).writeValueAsString(result);
    }
    
    public interface QuestionParser<T>{
      public void parse(T result, String name, String answerOrdinal, String answerRating, String answerText, String questionText);
    }
    public class QuestionReader<T>{
      public T read(T result, String survey, Assessments assessment, QuestionParser<T> parser){
        String raw=survey;
        int start=raw.indexOf("pages: [{")+7;
        int end=raw.indexOf("}],", start)+2;
        String x=raw.substring(start, end);
        mjson.Json surveyJson=mjson.Json.read(x);
        for(mjson.Json page:surveyJson.asJsonList()){
          for(mjson.Json question:page.at("questions").asJsonList()){
            
            if (question.at("type").asString().equals("radiogroup")){
              
              Map<String, String> answerRankingMap=new HashMap<String, String>();
              for(mjson.Json a:question.at("choices").asJsonList()){
                String answer=a.asString();
                answerRankingMap.put(answer.substring(0, answer.indexOf("-")), answer.substring(answer.indexOf("-")+1)); // answer id to ranking map
              }
              
              try{
                if (assessment.getResults().containsKey(question.at("name").asString())){
                  
                  String name=question.at("name").asString();
                  String answerOrdinal=((String)assessment.getResults().get(question.at("name").asString())).split("-")[0]; // should return integer of the value chosen
                  String answerRating=answerRankingMap.get(answerOrdinal).split("\\|")[0];
                  String answerText=answerRankingMap.get(answerOrdinal).split("\\|")[1];
                  String questionText=question.at("title").asString();
                  
                  parser.parse(result, name, answerOrdinal, answerRating, answerText, questionText);
//                  d.callback(name, answerOrdinal, answerRating, answerText, questionText);
//                  result.add(new ApplicationAssessmentSummary(questionText, answerText, answerRating));
                }
                
              }catch(Exception e){
                log.error(e.getMessage(), e);
                log.error("Error on: assessment.results="+assessment.getResults());
                log.error("Error on: assessment.results.containsKey("+question.at("name").asString()+")="+assessment.getResults().containsKey(question.at("name").asString()));
                log.error("Error on: question.name="+question.at("name").asString());
                log.error("Error on: assessment.results["+question.at("name").asString()+"]="+assessment.getResults().get(question.at("name").asString()));
              }
              
            }else if (question.at("type").asString().equals("rating")){
              // leave this out since it's things like "Select the app..."
            }
          }
        }
        return result;
      }
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
                log.warn("customersCustIdApplicationsAppIdAssessmentsPost....no reviews for app " + appId);
//                return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
            }

            List<Assessments> currAssessments = currApp.getAssessments();

            if ((currAssessments == null) || (currAssessments.isEmpty())) {
                log.warn("customersCustIdApplicationsAppIdAssessmentsPost....now assessments for app " + appId);
//                return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
            }
            Assessments latestAssessment = currAssessments==null?null:currAssessments.get(currAssessments.size() - 1);
 
//            List<Applications> listApps = currCust.getApplications();

            body.forEach((appName)-> {
                log.debug("Creating application "+appName);
                
                //Create application
                Applications newApp = new Applications();
                newApp.setId(UUID.randomUUID().toString());
                newApp.setName(appName);
                newApp.setDescription(currApp.getDescription());
                newApp.setStereotype(currApp.getStereotype());
                
                //Copy Assessment (latest only)
                if (latestAssessment!=null){
                  Assessments newAssessment = new Assessments();
                  newAssessment.setId(UUID.randomUUID().toString());
                  newAssessment.setDatetime(latestAssessment.getDatetime());
                  if (!latestAssessment.getDepsIN().isEmpty())
                    newAssessment.setDepsIN(latestAssessment.getDepsIN());
                if (!latestAssessment.getDepsOUT().isEmpty())
                    newAssessment.setDepsOUT(latestAssessment.getDepsOUT());
                  newAssessment.setResults(latestAssessment.getResults());
                  newAssessment = assmRepo.save(newAssessment);
                  if (newApp.getAssessments()==null) newApp.setAssessments(new ArrayList<>());
                  newApp.getAssessments().add(newAssessment);
                  
                  //Copy review
                  if (currReview!=null) {
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
                    newApp.setReview(newReview);
                  }
                  
                }
                
                newApp = appsRepo.insert(newApp);
                
                currCust.getApplications().add(newApp);
                
//                listApps.add(newApp);
                appIDS.add(newApp.getId());
            });

            //Update customer
//            currCust.setApplications(listApps);
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
                resp.setDepsIN(currAssm.getDepsIN());
                resp.setDepsOUT(currAssm.getDepsOUT());
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
                newitem.setDepsIN(body.getDepsIN());
                newitem.setDepsOUT(body.getDepsOUT());
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

    private AssessmentResponse populateCustomAssessmentFields(String custom, List<Assessments> assList, AssessmentResponse r){
      if (r==null) r=new AssessmentResponse();
      
      if (assList!=null && assList.size()<=1){
        Assessments a=assList.get(assList.size()-1);
        for(String f:custom.split(",")){
          if (a.getResults().containsKey(f)){
            r.put(f, a.getResults().get(f));
          }
        }
      }
      return r;
    }
    private AssessmentResponse populateCustomCustomerFields(String custom, Customer c, AssessmentResponse r){
      if (r==null) r=new AssessmentResponse();
      for(String f:custom.split(",")){
        if (f.contains("customer.")){
          if (f.equalsIgnoreCase("customer.name")){
            r.put("customer.name", c.getName());
          }else if (f.equalsIgnoreCase("customer.id")){
            r.put("customer.id", c.getId());
          }else
            log.error("Skipping/Unable to find custom field: "+f);
        }
      }
      return r;
    }
    
//    private AssessmentResponse populateCustomerFields(AssessmentResponse map, Customer customer, String field){
//      try{
//        
//        }
//        
//        Map<String, Object> getters=Arrays.asList(
//            Introspector.getBeanInfo(customer.getClass(), Object.class)
//                             .getPropertyDescriptors()
//        )
//        .stream()
//        .filter(pd ->Objects.nonNull(pd.getReadMethod()))
//        .collect(Collectors.toMap(
//                PropertyDescriptor::getName,
//                pd -> {
//                    try { 
//                        return pd.getReadMethod().invoke(customer);
//                    } catch (Exception e) {
//                       return null;
//                    }
//                }));
//        
//        
//        log.debug("GETTING "+"get"+StringUtils.capitalize(field));
//        log.debug("FOUND {} CUSTOMER GETTERS "+getters.size());
//        for(Entry<String, Object> e:getters.entrySet()){
//          log.debug("CUSTOMER PROPERTIES: {} = {}", e.getKey(), e.getValue());
//        }
//        
//        log.debug("ADDING CUSTOMER FIELD {}={}", field, getters.get(field));
//        
//        map.put(field, (String)getters.get("get"+StringUtils.capitalize(field)));
//      }catch (IntrospectionException e){
//        e.printStackTrace();
//      }
//      return map;
//    }
    
    
    // Get Application
    // GET: /customers/{customerId}/applications/{applicationId}
    //
    @Timed
    public ResponseEntity<ApplicationType> customersCustIdApplicationsAppIdGet(@ApiParam(value = "Customer Identifier", required = true) @PathVariable("custId") String custId, @ApiParam(value = "Application Identifier", required = true) @PathVariable("appId") String appId, @RequestParam(value = "custom", required = false) String custom) {
        log.debug("customersCustIdApplicationsAppIdGet cid {} app {}", custId, appId);
        ApplicationType response = new ApplicationType();
        //TODO : Check customer exists and owns application as well as application
        
        Customer customer=custRepo.findOne(custId);
        if (customer==null) {
            log.error("customersCustIdApplicationsAppIdGet....customer not found " + custId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        try {
            Applications application = appsRepo.findOne(appId);
            response.setDescription(application.getDescription());
            if (application.getReview() != null)
                response.setReview(application.getReview().getId());
            response.setName(application.getName());
            response.setOwner(application.getOwner());
            response.setId(appId);
            if ((application.getStereotype() != null) && (!application.getStereotype().isEmpty())) {
                response.setStereotype(ApplicationType.StereotypeEnum.fromValue(application.getStereotype()));
            }
            
            // custom fields parsing/injection
            if (null!=custom){
            	AssessmentResponse customFieldMap=new AssessmentResponse();
            	for(String f:custom.split(",")){
            		if (f.indexOf(".")<=0) continue;
            		String entity=f.substring(0, f.indexOf("."));
            		String field=f.substring(f.indexOf(".")+1);
            		if (entity.equals("customer")){
            			for (PropertyDescriptor pd:Introspector.getBeanInfo(Customer.class).getPropertyDescriptors()){
            				if (pd.getReadMethod() != null && pd.getReadMethod().getName().equals("get"+StringUtils.capitalize(field)) && !"class".equals(pd.getName())){
            					Object value=pd.getReadMethod().invoke(customer);
            					if (value instanceof String){
            						log.debug("Adding custom customer field:: {}={}", field, (String)pd.getReadMethod().invoke(customer));
            						customFieldMap.put(entity+"."+field, (String)value);
            						
            					}
            				}
            			}
            			
            		}else if (entity.equals("assessment")){
            			if (application.getAssessments()!=null && application.getAssessments().size()>0){
            				Assessments latestAssessment=application.getAssessments().get(application.getAssessments().size()-1);
            				log.debug("Adding customer assessment field:: {}={}", field, latestAssessment.getResults().get(field));
            				customFieldMap.put(entity+"."+field, latestAssessment.getResults().get(field));
            			}
            			
            		}
            	}
            	
            	response.setCustomFields(customFieldMap);
            }
            
            
            
        } catch (Exception ex) {
            log.error("Unable to get applications for customer ", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    
    @Override
    @Timed
    public ResponseEntity<String> customersCustIdApplicationsDelete(@ApiParam(value = "", required = true) @PathVariable("custId") String custId, @ApiParam(value = "Target Application Names") @Valid @RequestBody ApplicationNames body) {
      log.info("customersCustIdApplicationsDelete....[" + custId + "]");
      
      try {
        Customer currCust = custRepo.findOne(custId);
        if (currCust == null) {
            log.error("customersCustIdApplicationsDelete....customer not found {}", custId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        for(String appId: body){
          
          try{
            
            Applications delApp = appsRepo.findOne(appId);
            if (delApp == null) {
              log.error("customersCustIdApplicationsDelete....application not found {}", appId);
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
            log.error("Error while deleting application ["+appId+"]", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
          }
          
        }

      } catch (Exception ex) {
          log.error("Error with customer ["+custId+"] while deleting application(s)", ex.getMessage(), ex);
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<>(HttpStatus.OK);
    }
    
    

    // Get Applications
    // GET: /api/pathfinder/customers/{customerId}/applications/
    @Override
    @Timed
    public ResponseEntity<List<ApplicationType>> customersCustIdApplicationsGet(@ApiParam(value = "",required=true ) @PathVariable("custId") String custId,@ApiParam(value = "TARGETS,DEPENDENCIES,PROFILES") @RequestParam(value = "apptype", required = false) String apptype,@ApiParam(value = "app id to exclude") @RequestParam(value = "exclude", required = false) String exclude) {
        log.info("customersCustIdApplicationsGet....[" + custId + "]");
        ArrayList<ApplicationType> response = new ArrayList<>();
        try {
            Customer customer = custRepo.findOne(custId);
            if (customer == null) {
                log.error("customersCustIdApplicationsGet....[" + custId + "] customer not found");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            List<Applications> resp = customer.getApplications();
            if (isAuthorizedFor(customer)){
                if ((resp != null) && (!resp.isEmpty())) {
                    for (Applications x : resp) {
                    		
                    		if (null!=exclude && x.getId().equals(exclude)) continue;
                    		
                        ApplicationType app = new ApplicationType();
                        app.setName(x.getName());
                        app.setId(x.getId());
                        if (x.getReview() != null) {
                            app.setReview(x.getReview().getId());
                        }
                        app.setDescription(x.getDescription());
                        app.setOwner(x.getOwner());
                        
                        if (x.getStereotype() != null) {
                            if (apptype != null) {
                                switch (apptype) {
                                    case "TARGETS":  //Explicit targets
                                        if (x.getStereotype().equals(ApplicationType.StereotypeEnum.TARGETAPP.toString())) {
                                            app.setStereotype(ApplicationType.StereotypeEnum.fromValue(x.getStereotype()));
                                            response.add(app);
                                        }
                                        break;
                                    case "DEPENDENCIES": //Dependencies - Everything but Profiles
                                        if (!x.getStereotype().equals(ApplicationType.StereotypeEnum.DEPENDENCY.toString())) {
                                            app.setStereotype(ApplicationType.StereotypeEnum.fromValue(x.getStereotype()));
                                            response.add(app);
                                        }
                                        break;
                                    case "PROFILES": //Explicit Profiles
                                        if (x.getStereotype().equals(ApplicationType.StereotypeEnum.PROFILE.toString())) {
                                            app.setStereotype(ApplicationType.StereotypeEnum.fromValue(x.getStereotype()));
                                            response.add(app);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                //maintain backward compatibility with initial api when no apptype is passed - All Dependencies
                                if (!x.getStereotype().equals(ApplicationType.StereotypeEnum.PROFILE.toString())) {
                                    app.setStereotype(ApplicationType.StereotypeEnum.fromValue(x.getStereotype()));
                                    response.add(app);
                                }
                            }
                        } else {
                            response.add(app);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Unable to list applications for customer ", ex.getMessage(), ex);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Create Application
    // POST: /api/pathfinder/customers/{customerId}/applications/
    @Timed
    public ResponseEntity<String> customersCustIdApplicationsPost(@ApiParam(value = "Customer Identifier", required = true) @PathVariable("custId") String custId, @ApiParam(value = "Application Definition") @Valid @RequestBody ApplicationType body) {
        log.debug("customersCustIdApplicationsPost....");
        return createOrUpdateApplication(custId, null, body);
    }
    
    // Update application
    // POST: /api/pathfinder/customers/{customerId}/applications/{applicationId}
    public ResponseEntity<String> customersCustIdApplicationsAppIdPost(@ApiParam(value = "Customer Identifier",required=true ) @PathVariable("custId") String custId,@ApiParam(value = "Application Identifier",required=true ) @PathVariable("appId") String appId,@ApiParam(value = "Application Definition"  )  @Valid @RequestBody ApplicationType body) {
      log.debug("customersCustIdApplicationsAppIdPost....");
      return createOrUpdateApplication(custId, appId, body);
    }
    
    public ResponseEntity<String> createOrUpdateApplication(String custId, String appId, ApplicationType body){
      Customer myCust = custRepo.findOne(custId);
      if (myCust == null) {
          return new ResponseEntity<>(custId, HttpStatus.BAD_REQUEST);
      } else {
        
        Applications app;
        if (appId==null){
          app=new Applications();
          app.setId(UUID.randomUUID().toString());
        }else{
          app=appsRepo.findOne(appId);
        }
        
        app.setName(body.getName());
        app.setDescription(body.getDescription());
        app.setOwner(body.getOwner());
        
        if (body.getStereotype() == null) {
            log.warn("createOrUpdateApplication....application stereotype missing ");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            app.setStereotype(body.getStereotype().toString());
        }
        app = appsRepo.save(app);
        
        if (appId==null){
          List<Applications> appList = myCust.getApplications();
          if (appList == null) {
            appList = new ArrayList<Applications>();
          }
          appList.add(app);
          myCust.setApplications(appList);
          custRepo.save(myCust);
        }
        return new ResponseEntity<>(app.getId(), HttpStatus.OK);
      }
    }
    
    
    // Get Customer
    // GET: /api/pathfinder/customers/{customerId}
    @Timed
    public ResponseEntity<CustomerType> customersCustIdGet(@ApiParam(value = "Customer Identifier", required = true) @PathVariable("custId") String custId) {
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

    // Create Customer
    // POST: /api/pathfinder/customers/
    @Timed
    public ResponseEntity<String> customersPost(@ApiParam(value = "") @Valid @RequestBody CustomerType body) {
        log.debug("customersPost....{}", body);
        return createOrUpdateCustomer(null, body);
    }
    
    // Update Customer
    // POST: /api/pathfinder/customers/{custId}
    public ResponseEntity<String> customersCustIdPost(@ApiParam(value = "Customer Identifier",required=true ) @PathVariable("custId") String custId,@ApiParam(value = ""  )  @Valid @RequestBody CustomerType body) {
        log.debug("customersCustIdPost....{}", body);
        return createOrUpdateCustomer(custId, body);
    }
    
    public ResponseEntity<String> createOrUpdateCustomer(String custId, CustomerType body){
      Customer myCust;
      if (custId==null){
        myCust=new Customer();
        myCust.setId(UUID.randomUUID().toString());
        
        // check customer doesnt already exist with the same name
        Customer example=new Customer();
        example.setName(body.getCustomerName());
        long count=custRepo.count(Example.of(example));
        if (count>0){
          log.error("Customer already exists with name {}", body.getCustomerName());
          return new ResponseEntity<>("Customer already exists with name "+body.getCustomerName(), HttpStatus.BAD_REQUEST);
        }
        
      }else{
        myCust=custRepo.findOne(custId);
      }
      
      myCust.setName(body.getCustomerName());
      myCust.setDescription(body.getCustomerDescription());
      myCust.setVertical(body.getCustomerVertical());
      myCust.setSize(body.getCustomerSize());
      myCust.setRtilink(body.getCustomerRTILink());
      myCust.setVertical(body.getCustomerVertical());
      myCust.setAssessor(body.getCustomerAssessor());
      try {
          myCust=custRepo.save(myCust);
      } catch (Exception ex) {
          log.error("Unable to Create customer ", ex.getMessage(), ex);
          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
      return new ResponseEntity<>(myCust.getId(), HttpStatus.OK);
    }
    
    // Get Customers
    // GET: /api/pathfinder/customers/
    @Timed
    public ResponseEntity<List<CustomerType>> customersGet() {
        log.debug("customersGet....");
        ArrayList<CustomerType> response = new ArrayList<>();
        
        List<Customer> customers=custRepo.findAll();
        if (customers == null) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            for (Customer customer:customers) {
                if (isAuthorizedFor(customer)){
                  // then add the customer to the response
                  CustomerType resp = new CustomerType();
                  resp.setCustomerId(customer.getId());
                  resp.setCustomerName(customer.getName());
                  resp.setCustomerDescription(customer.getDescription());
                  resp.setCustomerSize(customer.getSize());
                  resp.setCustomerVertical(customer.getVertical());
                  
                  int total = customer.getApplications() != null ? Collections2.filter(customer.getApplications(), Predicates.assessableApplications).size() : 0;
                  resp.setCustomerAssessor(customer.getAssessor());
                  resp.setCustomerRTILink(customer.getRtilink());
  
                  int assessedCount = 0;
                  int reviewedCount = 0;
                  if (total > 0) {
                      for (Applications app : customer.getApplications()) {
                      	if (app==null) continue;  // TODO: Remove this MAT!!!!
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
                  
                  resp.setCustomerAppCount(customer.getApplications()==null?0:customer.getApplications().size());
                  resp.setCustomerMemberCount(customer.getMembers()==null?0:customer.getMembers().size());
  
                  response.add(resp);
                }
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @Timed
    public ResponseEntity<Void> customersDelete(@ApiParam(value = "Target Customer Names") @Valid @RequestBody ApplicationNames body) {
        log.debug("customersDelete....");
        
        for(String customerId: body){
          log.debug("customersDelete: deleting customer [{}]", customerId);
          try{
            
            Customer c=custRepo.findOne(customerId);
            if (c==null){
              log.error("customersDelete....customer not found {}", customerId);
              return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            log.debug("customersDelete: customer [{}] had "+(c.getApplications()!=null?c.getApplications().size():0)+" apps", customerId);
            if (null!=c.getApplications()){
              for (Applications app:c.getApplications()){
              	if (app==null) continue;  // TODO: Remove this MAT!!!!
                log.debug("customersDelete: deleting customer [{}] application [{}]", customerId, app.getId());
                
                if (null!=app.getAssessments()){
                  for(Assessments ass:app.getAssessments()){
                    log.debug("customersDelete: deleting customer [{}] application [{}] assessment [{}]", customerId, app.getId(), ass.getId());
                    
                    assmRepo.delete(ass.getId());
                  }
                }
                
                if (null!=app.getReview()){
                  reviewRepository.delete(app.getReview().getId());
                }
                
                appsRepo.delete(app.getId());
              }
            }
            
            custRepo.delete(customerId);
            
          }catch(Exception e){
            log.error("Error deleting customer ["+customerId+"] ", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
          }
        }
        return new ResponseEntity<>(HttpStatus.OK);
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

//            List<QuestionMetaData> questionData = questionRepository.findAll();

//            for (QuestionMetaData currQuestion : questionData) {
//                String res = (String) currAssm.getResults().get(currQuestion.getId());
//                AssessmentProcessQuestionResultsType vals = new AssessmentProcessQuestionResultsType();
//                vals.setQuestionTag(currQuestion.getId());
//
//                QuestionWeights.QuestionRank answerRank = currQuestion.getMetaData().get(Integer.parseInt(res)).getRank();
//                vals.setQuestionRank(answerRank.ordinal());
//                assessResults.add(vals);
//
//                log.debug(currQuestion.getId() + ": value=" + res + " RANK " + answerRank.toString());
//
//            }
            resp.setAssessResults(assessResults);
            resp.setAssmentNotes(currAssm.getResults().get("NOTES"));
            resp.setDependenciesIN(currAssm.getDepsIN());
            resp.setDependenciesOUT(currAssm.getDepsOUT());
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
//            return ResponseEntity.status(302).location(new URI(""));
            
        } catch (Exception ex) {
            log.error("Error while processing review", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Timed
    public ResponseEntity<ReviewType> customersCustIdApplicationsAppIdReviewReviewIdGet(@ApiParam(value = "", required = true) @PathVariable("custId") String custId, @ApiParam(value = "", required = true) @PathVariable("appId") String appId, @ApiParam(value = "", required = true) @PathVariable("reviewId") String reviewId) {
        log.debug("customersCustIdApplicationsAppIdReviewReviewIdGet....");
        try {
          
            Customer customer = custRepo.findOne(custId);
            if (customer == null) {
                log.error("Error while retrieving review....customer not found {}", custId);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Applications app = appsRepo.findOne(appId);
            if (app == null) {
                log.error("Error while retrieving review - Unable to find application with id {}", appId);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (app.getReview() == null) {
                log.error("Error while retrieving review - no review associated with application {}", reviewId);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            ApplicationAssessmentReview review = reviewRepository.findOne(app.getReview().getId());

            if (review == null || !review.getId().equals(reviewId)) {
                log.error("Error while retrieving review - Unable to find review for application {}", appId);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            ReviewType result = new ReviewType();
            result.setAssessmentId(review.getAssessments().getId());
            result.setReviewDecision(ReviewType.ReviewDecisionEnum.fromValue(review.getReviewDecision()));
            result.setReviewNotes(review.getReviewNotes());
            result.setWorkEffort(ReviewType.WorkEffortEnum.fromValue(review.getReviewEstimate()));
            result.setReviewTimestamp(review.getReviewDate());
            result.setWorkPriority(review.getWorkPriority());
            result.setBusinessPriority(review.getBusinessPriority());

            return new ResponseEntity<>(result, HttpStatus.OK);

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

    // THIS METHOD IS NOT USED - please use customersCustIdApplicationsAppIdReviewReviewIdGet
//    public ResponseEntity<ReviewType> customersCustIdApplicationsAppIdReviewGet(@ApiParam(value = "",required=true ) @PathVariable("custId") String custId,@ApiParam(value = "",required=true ) @PathVariable("appId") String appId) {
//      log.debug("customersCustIdApplicationsAppIdReviewGet... {} {}", custId, appId);
//      try {
//        Customer currCust = custRepo.findOne(custId);
//        if (currCust == null) {
//            log.error("customersCustIdApplicationsAppIdReviewGet....customer not found {}", custId);
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        
//        Applications app=appsRepo.findOne(appId);
//        if (null==app){
//          log.error("customersCustIdApplicationsAppIdReviewGet....app not found {}", appId);
//          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        
//        ApplicationAssessmentReview review=app.getReview();
//        if (null==review){
//          log.error("customersCustIdApplicationsAppIdReviewGet....review not found for app {}", appId);
//          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        
//        ReviewType result = new ReviewType();
//        result.setBusinessPriority(review.getBusinessPriority());
//        result.setWorkPriority(review.getWorkPriority());
//        result.setReviewTimestamp(review.getReviewDate());
//        result.setWorkEffort(ReviewType.WorkEffortEnum.fromValue(review.getReviewEstimate()));
//        result.setReviewNotes(review.getReviewNotes());
//        result.setReviewDecision(ReviewType.ReviewDecisionEnum.fromValue(review.getReviewDecision()));
//        result.setAssessmentId(app.getAssessments().get(app.getAssessments().size()-1).getId());
//        
//        return new ResponseEntity<ReviewType>(result, HttpStatus.OK);
//      } catch (Exception ex) {
//        log.error("Error while getting review", ex.getMessage(), ex);
//        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//      }
//    }

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
    
    
    private Integer calculateConfidence(Assessments assessment, ApplicationAssessmentReview review) throws IOException{
      double confidence=0;
      
      Map<String,Integer> weightMap=new MapBuilder<String,Integer>()
          .put("RED", 1)
          .put("UNKNOWN", 700)
          .put("AMBER", 800)
          .put("GREEN", 1000)
      .build();
      
      
      // Get the questions, answers, ratings etc...
      Map<String,Map<String,String>> questionInfo=new QuestionReader<Map<String,Map<String,String>>>().read(new HashMap<String,Map<String,String>>(), getSurveyContent(), assessment, new QuestionParser<Map<String,Map<String,String>>>(){
        @Override
        public void parse(Map<String,Map<String,String>> result, String name, String answerOrdinal, String answerRating, String answerText, String questionText){
          result.put(name, new MapBuilder<String, String>()
//                      .put("questionText", questionText)
//                      .put("answerText", answerText)
              .put("answerRating", answerRating)
              .build());
        }
      });
      
      List<String> ratings=new ArrayList<>();
      for(Entry<String, String> qa:assessment.getResults().entrySet()){
        if (null!=questionInfo.get(qa.getKey())){ //ie, an answered question with a missing question definition
          String rating=questionInfo.get(qa.getKey()).get("answerRating");
          ratings.add(rating);
        }
      }
      Collections.sort(ratings, 
        new Comparator<String>(){
          @Override public int compare(String o1, String o2){
            return "RED".equals(o1)?-1:0;
          }
        }
      );
      
      int redCount=Collections.frequency(ratings, "RED");
      int amberCount=Collections.frequency(ratings, "AMBER");
      
      double adjuster=1;
      
      if (redCount>0) adjuster=adjuster * Math.pow(0.5, redCount);
      if (amberCount>0) adjuster=adjuster * Math.pow(0.98, amberCount);
      
      for(String rating:ratings){
        if ("RED".equals(rating)) confidence=confidence*0.6;
        if ("AMBER".equals(rating)) confidence=confidence*0.95;
        
        
        int questionWeight=1; //not implemented yet
//        if ("RED".equals(rating)) adjuster=adjuster/2;
        confidence+=weightMap.get(rating) * adjuster * questionWeight;
      }
      int answerCount=ratings.size();
      
//      System.out.println("["+assessment.getResults().get("CUSTNAME")+"] confidence = "+confidence);
      
      int maxConfidence=weightMap.get("GREEN")*answerCount;
      
      BigDecimal result = new BigDecimal(((double)confidence/(double)maxConfidence)*100);
      result.setScale(0, BigDecimal.ROUND_DOWN);
      return result.intValue();
    }
    
    // Get assessment summary data for UI's assessment summary screen
    // GET: /api/pathfinder/customers/{customerId}/applicationAssessmentSummary
    @Timed
    public ResponseEntity<List<ApplicationSummaryType>> customersCustIdApplicationAssessmentSummaryGet(@ApiParam(value = "Customer Identifier",required=true ) @PathVariable("custId") String custId) {
        log.debug("customersCustIdApplicationAssessmentSummaryGet {}", custId);
        List<ApplicationSummaryType> resp = new ArrayList<>();
        try {
            Customer currCust = custRepo.findOne(custId);
            if (currCust == null) {
                log.error("customersCustIdApplicationAssessmentSummaryGet....customer not found {}", custId);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            List<Applications> applications = currCust.getApplications();

            if ((applications == null) || (applications.isEmpty())) {
                log.info("customersCustIdApplicationAssessmentSummaryGet Customer {} has no applications...", custId);

            } else {

                for (Applications app : applications) {
                	if (app==null) continue; //TODO Mat fix this!!!
                    if (app.getStereotype().equals(ApplicationType.StereotypeEnum.TARGETAPP.toString())){
                        ApplicationSummaryType item = new ApplicationSummaryType();
                        item.setId(app.getId());
                        item.setName(app.getName());
                        ApplicationAssessmentReview review = app.getReview();
                        List<Assessments> assmList = app.getAssessments();
                        Assessments assessment=null;
                        if ((assmList != null) && (!assmList.isEmpty())) assessment = assmList.get(assmList.size() - 1);
                        item.assessed(assessment!=null);
                        if (item.getAssessed()){
                            item.setLatestAssessmentId(assessment.getId());
                            item.setIncompleteAnswersCount(Collections.frequency(assessment.getResults().values(), "0-UNKNOWN"));
                            item.setCompleteAnswersCount(assessment.getResults().size()-item.getIncompleteAnswersCount());
                            item.setOutboundDeps(assessment.getDepsOUT());
                        }
                        if (review != null) {
                          item.setReviewDate(review.getReviewDate());
                          item.setDecision(review.getReviewDecision());
                          item.setWorkEffort(review.getReviewEstimate());
                          item.setWorkPriority(Integer.parseInt(null==review.getWorkPriority()?"0":review.getWorkPriority()));
                          item.setBusinessPriority(Integer.parseInt(null==review.getBusinessPriority()?"0":review.getBusinessPriority()));
                          
                        }
                        if (item.getAssessed() && review!=null){
                          item.setConfidence(!item.getAssessed()?0:calculateConfidence(assessment, review));
                        }
                        resp.add(item);
                    }
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
                    reviewedCount=reviewedCount+(currApp.getReview()!=null?1:0);
                    assessedCount=assessedCount+((currApp.getAssessments()!=null) && (!currApp.getAssessments().isEmpty())?1:0);
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


//    public ResponseEntity<DependenciesListType> customersCustIdDependencyTreeGet(@ApiParam(value = "Customer Identifier", required = true) @PathVariable("custId") String custId) {

    public ResponseEntity<DependenciesListType> customersCustIdDependencyTreeGet(@ApiParam(value = "Customer Identifier", required = true) @PathVariable("custId") String custId, @NotNull @ApiParam(value = "Specify the depedency direction from the applications persepctive NORTHBOUND = incoming to, SOUTHBOUND = outgoing from", required = true, allowableValues = "NORTHBOUND, SOUTHBOUND") @RequestParam(value = "direction", required = true) String direction) {

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

                    if (currAssmList == null || currAssmList.size() == 0) {
                        log.info("Application {} has no assessments...", currApp.getId());
                    } else {

                        String assmID = currAssmList.get(currAssmList.size() - 1).getId();
                        Assessments currAssm = assmRepo.findOne(assmID);
                        List<String> depList;

                        if (direction.equals("NORTHBOUND")) {
                            depList = currAssm.getDepsIN();
                            if (depList.size() == 0) {
                                log.info("Application {} has no NorthBound dependencies...", currApp.getId());
                            } else {
                                for (String currDep : depList) {
                                    DepsPairType dep = new DepsPairType();
                                    dep.to(currApp.getId());
                                    dep.from(currDep);
                                    respDeps.addDepsListItem(dep);
                                }
                            }
                        } else {
                            depList = currAssm.getDepsOUT();
                            if (depList.size() == 0) {
                                log.info("Application {} has no SouthBound dependencies...", currApp.getId());
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
            }
        } catch (Exception ex) {
            log.error("Error while processing customersCustIdDependencyTreeGet", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respDeps, HttpStatus.OK);
    }
}
