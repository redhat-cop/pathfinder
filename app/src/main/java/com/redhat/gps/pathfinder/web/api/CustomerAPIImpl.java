package com.redhat.gps.pathfinder.web.api;

import com.redhat.gps.pathfinder.web.api.model.ApplicationType;
import com.redhat.gps.pathfinder.web.api.model.AssessmentType;
import com.redhat.gps.pathfinder.web.api.model.CustomerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pathfinder")
public class CustomerAPIImpl  implements CustomersApi {

    private final Logger log = LoggerFactory.getLogger(CustomerAPIImpl.class);

    @Override
    public ResponseEntity<AssessmentType> customersCustIdApplicationsAppIdAssessmentsAssessIdGet(String custId, String appId, String assessId) {
        log.debug("customersCustIdApplicationsAppIdAssessmentsAssessIdGet....");
        return null;
    }

    @Override
    public ResponseEntity<Void> customersCustIdApplicationsAppIdAssessmentsAssessIdPost(String custId, String appId, String assessId, AssessmentType body) {
        log.debug("customersCustIdApplicationsAppIdAssessmentsAssessIdPost....");
        return null;
    }

    @Override
    public ResponseEntity<List<String>> customersCustIdApplicationsAppIdAssessmentsGet(String custId, String appId) {
        log.debug("customersCustIdApplicationsAppIdAssessmentsGet....");
        return null;
    }

    @Override
    public ResponseEntity<String> customersCustIdApplicationsAppIdAssessmentsPost(String custId, String appId, AssessmentType body) {
        log.debug("customersCustIdApplicationsAppIdAssessmentsPost....");
        return null;
    }

    @Override
    public ResponseEntity<ApplicationType> customersCustIdApplicationsAppIdGet(String custId, String appId) {
        log.debug("customersCustIdApplicationsAppIdGet....");
        return null;
    }

    @Override
    public ResponseEntity<List<ApplicationType>> customersCustIdApplicationsGet(String custId) {
        log.debug("customersCustIdApplicationsGet....");
        return null;
    }

    @Override
    public ResponseEntity<String> customersCustIdApplicationsPost(String custId, ApplicationType body) {
        log.debug("customersCustIdApplicationsPost....");
        return null;
    }

    @Override
    public ResponseEntity<CustomerType> customersCustIdGet(String custId) {
        log.debug("customersCustIdGet....");
        return null;
    }

    @Override
    public ResponseEntity<String> customersPost(CustomerType body) {
        log.debug("customersPost....");
        return null;
    }
}
