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

}
