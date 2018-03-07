package com.redhat.gps.pathfinder.repository;

import com.redhat.gps.pathfinder.domain.QuestionMetaData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface QuestionMetaDataRepository extends MongoRepository<QuestionMetaData, String> {
    @Override
    List<QuestionMetaData> findAll();

    @Override
    QuestionMetaData insert(QuestionMetaData entity);

    @Override
    QuestionMetaData save(QuestionMetaData entity);

    @Override
    boolean exists(String s);

    @Override
    long count();
}
