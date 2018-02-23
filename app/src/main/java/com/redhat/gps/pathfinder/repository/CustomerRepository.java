package com.redhat.gps.pathfinder.repository;

import com.redhat.gps.pathfinder.domain.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the Customer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {

    @Override
    Customer insert(Customer entity);

    @Override
    List<Customer> findAll();

    @Override
    Customer findOne(String s);

    @Override
    boolean exists(String s);
}
