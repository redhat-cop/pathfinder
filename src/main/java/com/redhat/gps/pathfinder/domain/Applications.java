package com.redhat.gps.pathfinder.domain;

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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Applications that = (Applications) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(Assessments, that.Assessments) &&
            Objects.equals(review, that.review);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, Assessments, review);
    }

    @Override
    public String toString() {
        return "Applications{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", Assessments=" + Assessments +
            ", review=" + review +
            '}';
    }
}
