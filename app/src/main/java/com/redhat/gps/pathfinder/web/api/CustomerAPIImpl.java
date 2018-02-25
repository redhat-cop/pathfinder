package com.redhat.gps.pathfinder.web.api;

import com.redhat.gps.pathfinder.domain.Applications;
import com.redhat.gps.pathfinder.domain.Assessments;
import com.redhat.gps.pathfinder.domain.Customer;
import com.redhat.gps.pathfinder.repository.ApplicationsRepository;
import com.redhat.gps.pathfinder.repository.AssessmentsRepository;
import com.redhat.gps.pathfinder.repository.CustomerRepository;
import com.redhat.gps.pathfinder.web.api.model.ApplicationType;
import com.redhat.gps.pathfinder.web.api.model.AssessmentType;
import com.redhat.gps.pathfinder.web.api.model.CustomerType;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Base64;

@RestController
@RequestMapping("/api/pathfinder")
public class CustomerAPIImpl  implements CustomersApi {

    private final Logger log = LoggerFactory.getLogger(CustomerAPIImpl.class);

    private final CustomerRepository custRepo;

    private final ApplicationsRepository appsRepo;

    private final AssessmentsRepository assmRepo;

    public CustomerAPIImpl(CustomerRepository custRepo, ApplicationsRepository appsRepo, AssessmentsRepository assmRepo) {
        this.custRepo = custRepo;
        this.appsRepo = appsRepo;
        this.assmRepo = assmRepo;
    }

    public ResponseEntity<AssessmentType> customersCustIdApplicationsAppIdAssessmentsAssessIdGet(String custId, String appId, String assessId) {
        log.debug("customersCustIdApplicationsAppIdAssessmentsAssessIdGet....");
        return null;
    }


    public ResponseEntity<Void> customersCustIdApplicationsAppIdAssessmentsAssessIdPost(String custId, String appId, String assessId, AssessmentType body) {
        log.debug("customersCustIdApplicationsAppIdAssessmentsAssessIdPost....");
        return null;
    }


    public ResponseEntity<List<String>> customersCustIdApplicationsAppIdAssessmentsGet(String custId, String appId) {
        log.debug("customersCustIdApplicationsAppIdAssessmentsGet....");
        return null;
    }


    public ResponseEntity<String> customersCustIdApplicationsAppIdAssessmentsPost(@ApiParam(value = "",required=true ) @PathVariable("custId") String custId,@ApiParam(value = "",required=true ) @PathVariable("appId") String appId,@ApiParam(value = "Application Assessment"  )  @Valid @RequestBody AssessmentType body) {
        log.debug("customersCustIdApplicationsAppIdAssessmentsPost...."+body);

        try {
            if (!custRepo.exists(custId)){
                log.error("customersCustIdApplicationsAppIdAssessmentsPost....app not found "+appId);
                return new ResponseEntity<String>("Customer Not found",HttpStatus.BAD_REQUEST);
            }

            Assessments newitem = new Assessments();
            newitem.setResults(body.getPayload());

            newitem = assmRepo.insert(newitem);

            Applications currApp = appsRepo.findOne(appId);

            if (currApp !=null ){
                List<Assessments> assmList = currApp.getAssessments();
                if (assmList == null){
                    assmList = new ArrayList<>();
                }
                assmList.add(newitem);
                currApp.setAssessments(assmList);
                currApp = appsRepo.save(currApp);
            }else{
                log.error("customersCustIdApplicationsAppIdAssessmentsPost....app not found "+appId);
                return new ResponseEntity<String>("Application not found",HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<String>(currApp.getId(),HttpStatus.OK);
        }catch(Exception ex){
            log.error("Unable to create applications for customer "+ex.toString());
        }
        return new ResponseEntity<String>("Unable to create assessment",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<ApplicationType> customersCustIdApplicationsAppIdGet(String custId, String appId) {
        log.debug("customersCustIdApplicationsAppIdGet....");
        return null;
    }

    @Override
    public ResponseEntity<List<ApplicationType>> customersCustIdApplicationsGet(@ApiParam(value = "",required=true ) @PathVariable("custId") String custId) {
        log.info("customersCustIdApplicationsGet....["+custId+"]");
        ArrayList<ApplicationType> response = new ArrayList<>();

        try {
            List<Applications> resp = custRepo.findOne(custId).getApplications();
            for (Applications x : resp) {
                ApplicationType lapp = new ApplicationType();
                lapp.setName(x.getName());
                lapp.setId(x.getId());
                response.add(lapp);
            }
        }catch(Exception ex){
            log.error("Unable to list applications for customer "+ex.toString());
            return new ResponseEntity<List<ApplicationType>>(response,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<List<ApplicationType>>(response,HttpStatus.OK);
    }

    public ResponseEntity<String> customersCustIdApplicationsPost(@ApiParam(value = "Customer Identifier",required=true ) @PathVariable("custId") String custId,@ApiParam(value = "Application Definition"  )  @Valid @RequestBody ApplicationType body) {
        log.debug("customersCustIdApplicationsPost....");
        Customer myCust = custRepo.findOne(custId);
        if (myCust == null){
            return new ResponseEntity<String>(custId,HttpStatus.BAD_REQUEST);
        }else{
            Applications app  = new Applications();
            app.setName(body.getName());
            app = appsRepo.save(app);
            List<Applications> appList = myCust.getApplications();
            if (appList==null){
                appList = new ArrayList<Applications>();
            }
            appList.add(app);
            myCust.setApplications(appList);
            custRepo.save(myCust);
            return new ResponseEntity<String>(app.getId(),HttpStatus.OK);
        }
    }

    public ResponseEntity<CustomerType> customersCustIdGet(@ApiParam(value = "Customer Identifier",required=true ) @PathVariable("custId") String custId) {
        log.debug("customersCustIdGet....");
        Customer myCust = custRepo.findOne(custId);
        if (myCust == null){
            return new ResponseEntity<CustomerType>(HttpStatus.BAD_REQUEST);
        }else{
            CustomerType resp = new CustomerType();
            resp.setCustomerName(myCust.getName());
            resp.setCustomerId(myCust.getId());
            return new ResponseEntity<CustomerType>(resp,HttpStatus.OK);
        }
    }

    public ResponseEntity<String> customersPost(@ApiParam(value = ""  ) @Valid @RequestBody CustomerType body) {
        log.debug("customersPost....");
        Customer myCust = new Customer();
        myCust.setName(body.getCustomerName());
        try {
            myCust = custRepo.insert(myCust);
        }
        catch (Exception ex){
            log.error("Unable to insert customer "+ex.toString());
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>(myCust.getId(),HttpStatus.OK);
    }
}
