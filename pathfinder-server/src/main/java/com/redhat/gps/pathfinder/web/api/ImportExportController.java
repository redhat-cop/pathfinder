package com.redhat.gps.pathfinder.web.api;

/*-
 * #%L
 * com.redhat.gps.pathfinder.pathfinder-server
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2018 RedHat Inc
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
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.redhat.gps.pathfinder.domain.Applications;
import com.redhat.gps.pathfinder.domain.Assessments;
import com.redhat.gps.pathfinder.domain.Customer;
import com.redhat.gps.pathfinder.domain.Member;
import com.redhat.gps.pathfinder.repository.ApplicationsRepository;
import com.redhat.gps.pathfinder.repository.AssessmentsRepository;
import com.redhat.gps.pathfinder.repository.CustomerRepository;
import com.redhat.gps.pathfinder.repository.MembersRepository;
import com.redhat.gps.pathfinder.repository.QuestionMetaDataRepository;
import com.redhat.gps.pathfinder.repository.ReviewsRepository;
import com.redhat.gps.pathfinder.service.util.Json;

@RestController
@RequestMapping("/api/pathfinder")
public class ImportExportController extends SecureAPIImpl {
    private final Logger log = LoggerFactory.getLogger(ImportExportController.class);
    private final CustomerRepository custRepo;
    private final ApplicationsRepository appsRepo;
    private final AssessmentsRepository assmRepo;
    private final ReviewsRepository reviewRepository;
    private final MembersRepository membersRepo;

    public ImportExportController(CustomerRepository custRepo, ApplicationsRepository appsRepo, AssessmentsRepository assmRepo, ReviewsRepository reviewRepository, MembersRepository membersRepository) {
        super(membersRepository);
        this.custRepo = custRepo;
        this.appsRepo = appsRepo;
        this.assmRepo = assmRepo;
        this.reviewRepository = reviewRepository;
        this.membersRepo = membersRepository;
    }

    private Customer newExampleCustomer(String name) {
        Customer example = new Customer();
        example.setName(name);
        return example;
    }

    // Non-Swagger api - import/export
    @RequestMapping(value = "/customers/import", method = POST)
//, headers={"Content-Disposition: attachment; filename=myfile.json"})
    public ResponseEntity<?> importCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("importCustomer()...");

        String payload = IOUtils.toString(request.getInputStream(), "UTF-8");

        try {
            List<Customer> customers = Json.newObjectMapper(true).readValue(payload, new TypeReference<List<Customer>>() {
            });

            for (Customer customer : customers) {
                log.debug("importCustomer():: importing customer = " + customer.getName());

                // Check Customer entity
                if (null != custRepo.findOne(customer.getId())) {
                    // CustomerID in use, generate a new one
                    customer.setId(UUID.randomUUID().toString());

                    Customer example = new Customer();
                    example.setName(customer.getName());
                    if (custRepo.count(Example.of(example)) > 0) {
                        int i = 1, max = 20;
                        while (custRepo.count(Example.of(newExampleCustomer(customer.getName() + "_" + i))) > 0) {
//          		log.debug("theres already a customer: "+(customer.getName()+"_"+i));
                            i = i + 1;
                            if (i >= max) break;
                        }
//          	log.debug("setting customer name to : "+ (customer.getName()+"_"+i));
                        customer.setName(customer.getName() + "_" + i); // give the customer a unique name if we can

                        if (i >= max) {
                            log.error("Customer already exists with name {}", customer.getName());
                            return new ResponseEntity<>("Customer already exists with name " + customer.getName(), HttpStatus.BAD_REQUEST);
                        }
                    }
                }

                // Check Applications entities
                for (Applications app : customer.getApplications()) {

                    // Check Application
                    app.setId(null != appsRepo.findOne(app.getId()) ? UUID.randomUUID().toString() : app.getId()); // generate a new ID if it's in use

                    // Check Assessment entity
                    Assessments ass = app.getAssessments() != null && app.getAssessments().size() > 0 ? app.getAssessments().get(app.getAssessments().size() - 1) : null;
                    if (ass != null) {
                        log.debug("ass id before = " + ass.getId());
                        ass.setId(null != assmRepo.findOne(ass.getId()) ? UUID.randomUUID().toString() : ass.getId()); // generate a new ID if it's in use
                        log.debug("ass id after = " + ass.getId());
                        app.setAssessments(Lists.newArrayList(ass));
                        assmRepo.save(ass);
                    }

                    // Check Review entity
                    if (null != app.getReview()) {
                        app.getReview().setId(app.getReview() != null && null != reviewRepository.findOne(app.getReview().getId()) ? UUID.randomUUID().toString() : app.getReview().getId());
                        reviewRepository.save(app.getReview());
                    }

                    appsRepo.save(app);
                }

                // Add Members
                for (Member m : customer.getMembers()) {
                    if (null == membersRepo.findOne(m.getUsername())) {
                        m.setCustomerId(customer.getId());
                    } else {
                        log.error("Unable to add user to customer because the username already exists [" + m.getUsername() + "]");
                    }
                    membersRepo.save(m);
                }

                custRepo.save(customer);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

//  	return ResponseEntity.status(200).header("Access-Control-Allow-Origin", "*/*").build();
        return ResponseEntity.status(200).build();
    }


    // Non-Swagger api - import/export
    @RequestMapping(value = "/customers/export", method = GET)
//, headers={"Content-Disposition: attachment; filename=myfile.json"})
//  @CrossOrigin
    public ResponseEntity<?> exportCustomer(@RequestParam("ids") String custIds, HttpServletResponse response) throws IOException {

        List<Customer> result = new ArrayList<Customer>();

        log.debug("custIds = " + custIds);
        String[] custIdss = custIds.split(",");
        log.debug("# of customers = " + custIdss.length);

        HttpHeaders h = new HttpHeaders();

        String filename = null;
        for (String custId : custIdss) {
            Customer c = custRepo.findOne(custId);
            filename = c.getName();
            log.debug("Adding customer: " + c.getName());

            // distill the customer a bit
            for (Applications app : c.getApplications()) {
                if (app.getAssessments() != null && app.getAssessments().size() > 0) {
                    app.getAssessments().removeIf(new Predicate<Assessments>() {
                        @Override
                        public boolean apply(Assessments input) {
                            return !input.getId().equals(app.getAssessments().get(app.getAssessments().size() - 1).getId());
                        }
                    });
                }
            }

//  		Optional<Stream<String>> d = c.getApplications().stream()
//				.map(app->app.getAssessments().get(0))
//				.map(assm ->assm.getResults().entrySet().stream().map(res->res.setValue("testing"))).findFirst();

//            c.getApplications()
//                    .forEach(app -> {
//                        log.info("app  {}", app);
//                        if (app.getAssessments() != null) {
//                            Assessments assm = app.getAssessments().get(0);
//                            if (assm != null) {
//                                HashMap<String, String> newAnswers = new HashMap<>();
//                                assm.getResults().entrySet().forEach(e -> {
//                                    newAnswers.put(e.getKey(), "test");
//                                });
//                                assm.setResults(newAnswers);
//                                app.setAssessments(assm);
//                            }
//                        }
//                    });

//		);

            result.add(c);
        }

        if (custIdss.length == 1) {
            h.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename.replaceAll(" ", "-") + "_export.json");
        } else {
            h.add(HttpHeaders.CONTENT_DISPOSITION, "attachment");
        }

        log.debug("returning: " + result.size() + " customer(s)");

        return ResponseEntity.ok()
                .headers(h)
                .body(result);
    }


}
