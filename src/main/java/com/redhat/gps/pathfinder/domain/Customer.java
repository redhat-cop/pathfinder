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
import java.util.Set;

/**
 * A Customer.
 */
@Document(collection = "customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("name")
    private String name;

    @Field("vertical")
    private String vertical;

    @Field("size")
    private String size;

    @Field("description")
    private String description;

    @Field("assessor")
    private String assessor;

    @Field("rtilink")
    private String rtilink;

    @DBRef
    private List<Applications> Applications;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVertical() {
        return vertical;
    }

    public void setVertical(String vertical) {
        this.vertical = vertical;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssessor() {
        return assessor;
    }

    public void setAssessor(String assessor) {
        this.assessor = assessor;
    }

    public String getRtilink() {
        return rtilink;
    }

    public void setRtilink(String rtilink) {
        this.rtilink = rtilink;
    }

    public List<com.redhat.gps.pathfinder.domain.Applications> getApplications() {
        return Applications;
    }

    public void setApplications(List<com.redhat.gps.pathfinder.domain.Applications> applications) {
        Applications = applications;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) &&
            Objects.equals(name, customer.name) &&
            Objects.equals(vertical, customer.vertical) &&
            Objects.equals(size, customer.size) &&
            Objects.equals(description, customer.description) &&
            Objects.equals(assessor, customer.assessor) &&
            Objects.equals(rtilink, customer.rtilink) &&
            Objects.equals(Applications, customer.Applications);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, vertical, size, description, assessor, rtilink, Applications);
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", vertical='" + vertical + '\'' +
            ", size='" + size + '\'' +
            ", description='" + description + '\'' +
            ", assessor='" + assessor + '\'' +
            ", rtilink='" + rtilink + '\'' +
            ", Applications=" + Applications +
            '}';
    }
}
