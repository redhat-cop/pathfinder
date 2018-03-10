package com.redhat.gps.pathfinder.domain;


import lombok.Data;

@Data
public class QuestionWeights {
    public enum QuestionRank {
        UNKNOWN(0), RED(1), AMBER(2), GREEN(3);

        private final int levelCode;

        private QuestionRank(int levelCode) {
            this.levelCode = levelCode;
        }
    }

    private int weight;

    private QuestionRank rank;

    @Override
    public String toString() {
        return "QuestionWeights{" +
            "weight=" + weight +
            ", rank=" + rank +
            '}';
    }
}
