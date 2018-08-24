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

    @NotNull
    @Field("Minimum")
    private Integer Minimum;

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
