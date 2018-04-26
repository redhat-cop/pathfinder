package com.redhat.gps.pathfinder.repository;

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

import com.redhat.gps.pathfinder.domain.ApplicationAssessmentReview;
import com.redhat.gps.pathfinder.domain.Applications;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the Applications Assessment Review entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReviewsRepository extends MongoRepository<ApplicationAssessmentReview, String> {

    @Override
    List<ApplicationAssessmentReview> findAll();

    @Override
    ApplicationAssessmentReview insert(ApplicationAssessmentReview entity);

    @Override
    ApplicationAssessmentReview save(ApplicationAssessmentReview entity);

    @Override
    boolean exists(String s);

    @Override
    long count();
}
