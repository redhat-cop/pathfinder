package com.redhat.gps.pathfinder.web.api;

import com.redhat.gps.pathfinder.domain.Applications;
import com.redhat.gps.pathfinder.domain.Customer;
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
import java.util.List;

@RestController
@RequestMapping("/api/pathfinder")
public class CustomerAPIImpl  implements CustomersApi {

    private final Logger log = LoggerFactory.getLogger(CustomerAPIImpl.class);

    private final CustomerRepository custRepo;

    public CustomerAPIImpl(CustomerRepository custRepo) {
        this.custRepo = custRepo;
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


    public ResponseEntity<String> customersCustIdApplicationsAppIdAssessmentsPost(String custId, String appId, AssessmentType body) {
        log.debug("customersCustIdApplicationsAppIdAssessmentsPost....");
        return null;
    }

    public ResponseEntity<ApplicationType> customersCustIdApplicationsAppIdGet(String custId, String appId) {
        log.debug("customersCustIdApplicationsAppIdGet....");
        return null;
    }

    @Override
    public ResponseEntity<List<ApplicationType>> customersCustIdApplicationsGet(@ApiParam(value = "",required=true ) @PathVariable("custId") String custId) {
        log.info("customersCustIdApplicationsGet....["+custId+"]");
        List<Applications> resp = custRepo.findOne(custId).getApplications();
        for (Applications x:resp) {
            log.info(x.toString());

        }
//        return new ResponseEntity<>(resp, HttpStatus.FOUND);

        return null;
    }

    public ResponseEntity<String> customersCustIdApplicationsPost(String custId, ApplicationType body) {
        log.debug("customersCustIdApplicationsPost....");
        return null;
    }

    public ResponseEntity<CustomerType> customersCustIdGet(String custId) {
        log.debug("customersCustIdGet....");
        Customer myCust = custRepo.findOne(custId);
//        if (myCust == null){
//            return new ResponseEntity<CustomerType>(HttpStatus.BAD_REQUEST);
//        }else{
//            return new ResponseEntity<CustomerType>(myCust,HttpStatus.OK);
//        }
        return null;
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
