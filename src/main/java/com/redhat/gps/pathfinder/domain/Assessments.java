package com.redhat.gps.pathfinder.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A Assessments.
 */
@Document(collection = "assessments")
@Data
public class Assessments implements Serializable {

    private static final long serialVersionUID = 1L;

    private String CONTAINERS = null;

    private String CLUSTER = null;

    private String ASSMENTNAME = null;

    private String BUSPRIORITY = null;

    private String OWNER = null;

    private String ARCHTYPE = null;

    private String DEPSHW = null;

    private String DEPSOS = null;

    private String DEPS3RD = null;

    private String DEPSIN = null;

    private String DEPSOUT = null;

    private String RESILIENCY = null;

    private String COMMS = null;

    private String STATE = null;

    private String HA = null;

    private String PROFILE = null;

    private String LOGS = null;

    private String METRICS = null;

    private String HEALTH = null;

    private String CONFIG = null;

    private String DEPLOY = null;

    private String TEST = null;

    private String COMPLIANCE = null;

    private String SECURITY = null;

    private String NOTES = null;

    private List<String> DEPSOUTLIST = null;

    @Id
    private String id;

//    @NotNull
    @Field("results")
    private String results;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResults() {
        return results;
    }

    public Assessments results(String results) {
        this.results = results;
        return this;
    }

    public void setResults(String results) {
        this.results = results;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Assessments assessments = (Assessments) o;
        if (assessments.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), assessments.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Assessments{" +
            "id=" + getId() +
            ", results='" + getResults() + "'" +
            "}";
    }
}
