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

    @Field("vertical")
    private String vertical;

    @Field("size")
    private String size;

    @DBRef
    private List<Applications> Applications;

    public List<Applications> getApplications() {
        return Applications;
    }

    public void setApplications(List<Applications> Applications) {
        this.Applications = Applications;
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

    public Customer name(String name) {
        this.name = name;
        return this;
    }



    public void setName(String name) {
        this.name = name;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(name, customer.name) &&
            Objects.equals(vertical, customer.vertical) &&
            Objects.equals(size, customer.size) &&
            Objects.equals(Applications, customer.Applications);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, vertical, size, Applications);
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", vertical='" + vertical + '\'' +
            ", size='" + size + '\'' +
            ", Applications=" + Applications +
            '}';
    }
}
