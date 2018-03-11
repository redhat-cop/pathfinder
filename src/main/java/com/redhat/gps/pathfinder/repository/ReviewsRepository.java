package com.redhat.gps.pathfinder.repository;

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
