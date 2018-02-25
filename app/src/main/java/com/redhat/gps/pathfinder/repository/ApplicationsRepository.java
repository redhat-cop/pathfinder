package com.redhat.gps.pathfinder.repository;

import com.redhat.gps.pathfinder.domain.Applications;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the Applications entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationsRepository extends MongoRepository<Applications, String> {

    @Override
    List<Applications> findAll();

    @Override
    Applications  insert(Applications entity);

    @Override
    Applications save(Applications entity);

    @Override
    boolean exists(String s);

    @Override
    long count();
}
