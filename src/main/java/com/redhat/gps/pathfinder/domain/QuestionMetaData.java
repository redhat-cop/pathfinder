package com.redhat.gps.pathfinder.domain;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Document(collection = "questionmetadata")
@Data
public class QuestionMetaData {

    private static final long serialVersionUID = 1734879348L;

    @Id
    private String id;

    @NotNull
    @Field("weights")
    private List<QuestionWeights> MetaData;

    @NotNull
    @Field("Aspect")
    private String Aspect;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        QuestionMetaData that = (QuestionMetaData) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(MetaData, that.MetaData) &&
            Objects.equals(Aspect, that.Aspect);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), id, MetaData, Aspect);
    }

    @Override
    public String toString() {
        return "QuestionMetaData{" +
            "id='" + id + '\'' +
            ", MetaData=" + MetaData +
            ", Aspect='" + Aspect + '\'' +
            '}';
    }
}
