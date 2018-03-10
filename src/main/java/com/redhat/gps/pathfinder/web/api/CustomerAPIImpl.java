package com.redhat.gps.pathfinder.web.api;

import com.redhat.gps.pathfinder.domain.Applications;
import com.redhat.gps.pathfinder.domain.Assessments;
import com.redhat.gps.pathfinder.domain.Customer;
import com.redhat.gps.pathfinder.domain.QuestionMetaData;
import com.redhat.gps.pathfinder.repository.ApplicationsRepository;
import com.redhat.gps.pathfinder.repository.AssessmentsRepository;
import com.redhat.gps.pathfinder.repository.CustomerRepository;
import com.redhat.gps.pathfinder.repository.QuestionMetaDataRepository;
import com.redhat.gps.pathfinder.web.api.model.*;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Base64;

@RestController
@RequestMapping("/api/pathfinder")
public class CustomerAPIImpl implements CustomersApi {

    private final Logger log = LoggerFactory.getLogger(CustomerAPIImpl.class);

    private final CustomerRepository custRepo;

    private final ApplicationsRepository appsRepo;

    private final AssessmentsRepository assmRepo;

    private final QuestionMetaDataRepository questionRepository;

    public CustomerAPIImpl(CustomerRepository custRepo, ApplicationsRepository appsRepo, AssessmentsRepository assmRepo, QuestionMetaDataRepository questionRepository) {
        this.custRepo = custRepo;
        this.appsRepo = appsRepo;
        this.assmRepo = assmRepo;
        this.questionRepository = questionRepository;
    }

    public ResponseEntity<AssessmentType> customersCustIdApplicationsAppIdAssessmentsAssessIdGet(@ApiParam(value = "", required = true) @PathVariable("custId") String custId, @ApiParam(value = "", required = true) @PathVariable("appId") String appId, @ApiParam(value = "", required = true) @PathVariable("assessId") String assessId) {
        log.debug("customersCustIdApplicationsAppIdAssessmentsAssessIdGet....");

        AssessmentType resp = new AssessmentType();
        try {
            Assessments currAssm = assmRepo.findOne(assessId);
            if (currAssm != null) {
                AssessmentVals newitem = new AssessmentVals();
                newitem.setARCHTYPE(currAssm.getARCHTYPE());
                newitem.setASSMENTNAME(currAssm.getASSMENTNAME());
                newitem.setBUSPRIORITY(currAssm.getBUSPRIORITY());
                newitem.setARCHTYPE(currAssm.getARCHTYPE());
                newitem.setCOMMS(currAssm.getCOMMS());
                newitem.setCOMPLIANCE(currAssm.getCOMPLIANCE());
                newitem.setCONFIG(currAssm.getCONFIG());
                newitem.setDEPLOY(currAssm.getDEPLOY());
                newitem.setDePS3RD(currAssm.getDEPS3RD());
                newitem.setDEPSHW(currAssm.getDEPSHW());
                newitem.setDEPSIN(currAssm.getDEPSIN());
                newitem.setDEPSOS(currAssm.getDEPSOS());
                newitem.setDEPSOUT(currAssm.getDEPSOUT());
                newitem.setCONTAINERS(currAssm.getCONTAINERS());
                newitem.setCLUSTER(currAssm.getCLUSTER());
                newitem.setHA(currAssm.getHA());
                newitem.setHEALTH(currAssm.getHEALTH());
                newitem.setLOGS(currAssm.getLOGS());
                newitem.setMETRICS(currAssm.getMETRICS());
                newitem.setNOTES(currAssm.getNOTES());
                newitem.setOWNER(currAssm.getOWNER());
                newitem.setPROFILE(currAssm.getPROFILE());
                newitem.setRESILIENCY(currAssm.getRESILIENCY());
                newitem.setSECURITY(currAssm.getSECURITY());
                newitem.setSTATE(currAssm.getSTATE());
                newitem.setTEST(currAssm.getTEST());
                newitem.setDEPSOUTLIST(currAssm.getDEPSOUTLIST());

                resp.setPayload(newitem);
                return new ResponseEntity<AssessmentType>(resp, HttpStatus.OK);
            } else {
                return new ResponseEntity<AssessmentType>(resp, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.error("customersCustIdApplicationsAppIdAssessmentsAssessIdGet", ex.getStackTrace());
            return new ResponseEntity<AssessmentType>(resp, HttpStatus.BAD_REQUEST);
        }
    }


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
                log.error("Unable to get assessments for customer ", ex.getStackTrace());
            }
            return new ResponseEntity<List<String>>(results, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            log.error("customersCustIdApplicationsAppIdAssessmentsGet", ex.getStackTrace());
        }
        return new ResponseEntity<List<String>>(results, HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<String> customersCustIdApplicationsAppIdAssessmentsPost
        (@ApiParam(value = "", required = true) @PathVariable("custId") String
             custId, @ApiParam(value = "", required = true) @PathVariable("appId") String
             appId, @ApiParam(value = "Application Assessment") @Valid @RequestBody AssessmentType body) {
        log.debug("customersCustIdApplicationsAppIdAssessmentsPost...." + body);

        try {
            if (!custRepo.exists(custId)) {
                log.error("customersCustIdApplicationsAppIdAssessmentsPost....app not found " + appId);
                return new ResponseEntity<String>("Customer Not found", HttpStatus.BAD_REQUEST);
            }

            Applications currApp = appsRepo.findOne(appId);

            if (currApp != null) {

                Assessments newitem = new Assessments();
                newitem.setARCHTYPE(body.getPayload().getARCHTYPE());
                newitem.setASSMENTNAME(body.getPayload().getASSMENTNAME());
                newitem.setBUSPRIORITY(body.getPayload().getBUSPRIORITY());
                newitem.setARCHTYPE(body.getPayload().getARCHTYPE());
                newitem.setCOMMS(body.getPayload().getCOMMS());
                newitem.setCOMPLIANCE(body.getPayload().getCOMPLIANCE());
                newitem.setCONFIG(body.getPayload().getCONFIG());
                newitem.setDEPLOY(body.getPayload().getDEPLOY());
                newitem.setDEPS3RD(body.getPayload().getDePS3RD());
                newitem.setDEPSHW(body.getPayload().getDEPSHW());
                newitem.setDEPSIN(body.getPayload().getDEPSIN());
                newitem.setDEPSOS(body.getPayload().getDEPSOS());
                newitem.setDEPSOUT(body.getPayload().getDEPSOUT());
                newitem.setCLUSTER(body.getPayload().getCLUSTER());
                newitem.setCONTAINERS(body.getPayload().getCONTAINERS());
                newitem.setHA(body.getPayload().getHA());
                newitem.setHEALTH(body.getPayload().getHEALTH());
                newitem.setLOGS(body.getPayload().getLOGS());
                newitem.setMETRICS(body.getPayload().getMETRICS());
                newitem.setNOTES(body.getPayload().getNOTES());
                newitem.setOWNER(body.getPayload().getOWNER());
                newitem.setPROFILE(body.getPayload().getPROFILE());
                newitem.setRESILIENCY(body.getPayload().getRESILIENCY());
                newitem.setSECURITY(body.getPayload().getSECURITY());
                newitem.setSTATE(body.getPayload().getSTATE());
                newitem.setTEST(body.getPayload().getTEST());
                newitem.setDEPSOUTLIST(body.getPayload().getDEPSOUTLIST());

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
            log.error("customersCustIdApplicationsAppIdAssessmentsPost Unable to create applications for customer ", ex.getStackTrace());
        }
        return new ResponseEntity<String>("Unable to create assessment", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<ApplicationType> customersCustIdApplicationsAppIdGet(String custId, String appId) {
        log.debug("customersCustIdApplicationsAppIdGet....");
        return null;
    }

    @Override
    public ResponseEntity<List<ApplicationType>> customersCustIdApplicationsGet
        (@ApiParam(value = "", required = true) @PathVariable("custId") String custId) {
        log.info("customersCustIdApplicationsGet....[" + custId + "]");
        ArrayList<ApplicationType> response = new ArrayList<>();

        try {
            List<Applications> resp = custRepo.findOne(custId).getApplications();
            if ((resp!=null)&&(!resp.isEmpty())) {
                for (Applications x : resp) {
                    ApplicationType lapp = new ApplicationType();
                    lapp.setName(x.getName());
                    lapp.setId(x.getId());
                    response.add(lapp);
                }
            }
        } catch (Exception ex) {
            log.error("Unable to list applications for customer " + ex.toString());
            return new ResponseEntity<List<ApplicationType>>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<List<ApplicationType>>(response, HttpStatus.OK);
    }

    public ResponseEntity<String> customersCustIdApplicationsPost
        (@ApiParam(value = "Customer Identifier", required = true) @PathVariable("custId") String
             custId, @ApiParam(value = "Application Definition") @Valid @RequestBody ApplicationType body) {
        log.debug("customersCustIdApplicationsPost....");
        Customer myCust = custRepo.findOne(custId);
        if (myCust == null) {
            return new ResponseEntity<String>(custId, HttpStatus.BAD_REQUEST);
        } else {
            Applications app = new Applications();
            app.setName(body.getName());
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

    public ResponseEntity<CustomerType> customersCustIdGet
        (@ApiParam(value = "Customer Identifier", required = true) @PathVariable("custId") String custId) {
        log.debug("customersCustIdGet....");
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

    public ResponseEntity<String> customersPost(@ApiParam(value = "") @Valid @RequestBody CustomerType body) {
        log.debug("customersPost....");
        Customer myCust = new Customer();
        myCust.setName(body.getCustomerName());
        try {
            myCust = custRepo.insert(myCust);
        } catch (Exception ex) {
            log.error("Unable to insert customer ", ex.getStackTrace());
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>(myCust.getId(), HttpStatus.OK);
    }


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
                response.add(resp);
            }
            return new ResponseEntity<List<CustomerType>>(response, HttpStatus.OK);
        }
    }


    public ResponseEntity<AssessmentProcessType> customersCustIdApplicationsAppIdAssessmentsAssessIdProcessGet
        (@ApiParam(value = "", required = true) @PathVariable("custId") String
             custId, @ApiParam(value = "", required = true) @PathVariable("appId") String
             appId, @ApiParam(value = "", required = true) @PathVariable("assessId") String assessId) {

        AssessmentProcessType resp = new AssessmentProcessType();
        AssessmentProcessQuestionResultsType vals = new AssessmentProcessQuestionResultsType();

        try {

            Assessments currAssm = assmRepo.findOne(assessId);
            if (currAssm == null) {
                return new ResponseEntity<AssessmentProcessType>(resp, HttpStatus.BAD_REQUEST);
            }
            resp.assmentNotes(currAssm.getNOTES());

            List<QuestionMetaData> questionData = questionRepository.findAll();


            for (QuestionMetaData currQuestion : questionData) {
                String key = currQuestion.getId();

                Method invokeByKey = Assessments.class.getMethod("get" + key);
                String res = (String) invokeByKey.invoke(null);
                vals.setQuestionTag(currQuestion.getId());
                vals.setQuestionRank(currQuestion.getMetaData().get(Integer.parseInt(res)).getRank());
            }
        } catch (Exception ex) {
            log.error("Error while processing assessment" + ex.toString());
            return new ResponseEntity<AssessmentProcessType>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return new ResponseEntity<AssessmentProcessType>(resp, HttpStatus.OK);
    }


}
