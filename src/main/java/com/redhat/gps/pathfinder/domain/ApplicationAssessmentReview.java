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
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Application Review
 */
@Document(collection = "reviews")
public class ApplicationAssessmentReview implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("Date")
    private String ReviewDate;

    @DBRef
    private Assessments Assessments;

    @NotNull
    @Field("Decision")
    private String ReviewDecision;

    @NotNull
    @Field("Estimate")
    private String ReviewEstimate;

    @NotNull
    @Field("Notes")
    private String ReviewNotes;

    @NotNull
    @Field("WorkPriority")
    private String WorkPriority;

    @NotNull
    @Field("BusinessPriority")
    private String BusinessPriority;

    @DBRef
    private Applications Application;

    public ApplicationAssessmentReview(String ReviewDate, com.redhat.gps.pathfinder.domain.Assessments Assessments, String ReviewDecision, String ReviewEstimate, String ReviewNotes, String WorkPriority, String BusinessPriority, Applications Application) {
        this.ReviewDate = ReviewDate;
        this.Assessments = Assessments;
        this.ReviewDecision = ReviewDecision;
        this.ReviewEstimate = ReviewEstimate;
        this.ReviewNotes = ReviewNotes;
        this.WorkPriority = WorkPriority;
        this.BusinessPriority = BusinessPriority;
        this.Application = Application;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReviewDate() {
        return ReviewDate;
    }

    public void setReviewDate(String reviewDate) {
        ReviewDate = reviewDate;
    }

    public com.redhat.gps.pathfinder.domain.Assessments getAssessments() {
        return Assessments;
    }

    public void setAssessments(com.redhat.gps.pathfinder.domain.Assessments assessments) {
        Assessments = assessments;
    }

    public String getReviewDecision() {
        return ReviewDecision;
    }

    public void setReviewDecision(String reviewDecision) {
        ReviewDecision = reviewDecision;
    }

    public String getReviewEstimate() {
        return ReviewEstimate;
    }

    public void setReviewEstimate(String reviewEstimate) {
        ReviewEstimate = reviewEstimate;
    }

    public String getReviewNotes() {
        return ReviewNotes;
    }

    public void setReviewNotes(String reviewNotes) {
        ReviewNotes = reviewNotes;
    }

    public String getWorkPriority() {
        return WorkPriority;
    }

    public void setWorkPriority(String workPriority) {
        WorkPriority = workPriority;
    }

    public String getBusinessPriority() {
        return BusinessPriority;
    }

    public void setBusinessPriority(String businessPriority) {
        BusinessPriority = businessPriority;
    }

    public Applications getApplication() {
        return Application;
    }

    public void setApplication(Applications application) {
        Application = application;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationAssessmentReview that = (ApplicationAssessmentReview) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(ReviewDate, that.ReviewDate) &&
            Objects.equals(Assessments, that.Assessments) &&
            Objects.equals(ReviewDecision, that.ReviewDecision) &&
            Objects.equals(ReviewEstimate, that.ReviewEstimate) &&
            Objects.equals(ReviewNotes, that.ReviewNotes) &&
            Objects.equals(WorkPriority, that.WorkPriority) &&
            Objects.equals(BusinessPriority, that.BusinessPriority) &&
            Objects.equals(Application, that.Application);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, ReviewDate, Assessments, ReviewDecision, ReviewEstimate, ReviewNotes, WorkPriority, BusinessPriority, Application);
    }

    @Override
    public String toString() {
        return "ApplicationAssessmentReview{" +
            "id='" + id + '\'' +
            ", ReviewDate='" + ReviewDate + '\'' +
            ", Assessments=" + Assessments +
            ", ReviewDecision=" + ReviewDecision +
            ", ReviewEstimate=" + ReviewEstimate +
            ", ReviewNotes='" + ReviewNotes + '\'' +
            ", WorkPriority='" + WorkPriority + '\'' +
            ", BusinessPriority='" + BusinessPriority + '\'' +
            ", Application=" + Application +
            '}';
    }
}
