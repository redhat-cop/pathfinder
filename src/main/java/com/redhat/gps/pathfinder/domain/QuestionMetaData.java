package com.redhat.gps.pathfinder.domain;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

//@Document(collection = "questionmetadata")
//@Data
public class QuestionMetaData {

    @Id
    private int id;

//    private Array<int,int>

}
