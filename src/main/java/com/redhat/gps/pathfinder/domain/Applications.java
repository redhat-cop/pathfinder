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

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A Applications.
 */
@Document(collection = "applications")
public class Applications implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("name")
    private String name;

    @Field("Description")
    private String Description;

    @Field("stereotype")
    private String stereotype;

    @Override
    public String toString() {
        return "Applications{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", Description='" + Description + '\'' +
            ", stereotype='" + stereotype + '\'' +
            ", Assessments=" + Assessments +
            ", review=" + review +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Applications that = (Applications) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(Description, that.Description) &&
            Objects.equals(stereotype, that.stereotype) &&
            Objects.equals(Assessments, that.Assessments) &&
            Objects.equals(review, that.review);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, Description, stereotype, Assessments, review);
    }

    public String getStereotype() {

        return stereotype;
    }

    public void setStereotype(String stereotype) {
        this.stereotype = stereotype;
    }

    @DBRef
    private List<Assessments> Assessments;

    @DBRef
    private ApplicationAssessmentReview review;

    public List<Assessments> getAssessments() {
        return Assessments;
    }

    public void setAssessments(List<Assessments> Assessments) {
        this.Assessments = Assessments;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Applications name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ApplicationAssessmentReview getReview() {
        return review;
    }

    public void setReview(ApplicationAssessmentReview review) {
        this.review = review;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


}
