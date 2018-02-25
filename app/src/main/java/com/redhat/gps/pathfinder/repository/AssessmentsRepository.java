package com.redhat.gps.pathfinder.repository;

import com.redhat.gps.pathfinder.domain.Assessments;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Assessments entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssessmentsRepository extends MongoRepository<Assessments, String> {

    @Override
    Assessments insert(Assessments Assessments);

    @Override
    Assessments save(Assessments entity);

    @Override
    Assessments findOne(String s);

    @Override
    boolean exists(String s);

    @Override
    long count();
}
