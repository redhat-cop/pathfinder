package com.redhat.gps.pathfinder.domain;

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

import com.redhat.gps.pathfinder.web.api.SurveyPayload;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * A Assessments.
 */
@Document(collection = "assessments")
@Data
@Component
public class Assessments implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    //Results store the answer selected
    @NotNull
    @Field("results")
    private HashMap<String, String> results = new HashMap<>();

    //qanswers stores the text value of the selected answer i.e. what text did the customer see
    @NotNull
    @Field("qanswers")
    private HashMap<String, String> qanswers = new HashMap<>();

    @Field("dependenciesIN")
    private List<String> depsIN = new ArrayList<String>();

    @Field("dependenciesOUT")
    private List<String> depsOUT = new ArrayList<String>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assessments that = (Assessments) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(results, that.results) &&
            Objects.equals(depsIN, that.depsIN) &&
            Objects.equals(depsOUT, that.depsOUT) &&
            Objects.equals(datetime, that.datetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, results, depsIN, depsOUT, datetime);
    }

    @Override
    public String toString() {
        return "Assessments{" +
            "id='" + id + '\'' +
            ", results=" + results +
            ", depsIN=" + depsIN +
            ", depsOUT=" + depsOUT +
            ", datetime='" + datetime + '\'' +
            '}';
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datatime) {
        this.datetime = datatime;
    }

    @Field("AssessmentDateTime")
    private String datetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String, String> getResults() {
        return results;
    }

    public void setResults(HashMap<String, String> resultsIn) {
        this.results = resultsIn;
    }

    public List<String> getDepsIN() {
        return depsIN;
    }

    public void setDepsIN(List<String> depsIN) {
        this.depsIN = depsIN;
    }

    public List<String> getDepsOUT() {
        return depsOUT;
    }

    public void setDepsOUT(List<String> depsOUT) {
        this.depsOUT = depsOUT;
    }

    public HashMap<String, String> getQanswers() {
        return qanswers;
    }

    public void setQanswers(HashMap<String, String> qanswers) {
        this.qanswers = qanswers;
    }
}
