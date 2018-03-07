package com.redhat.gps.pathfinder.domain;


import lombok.Data;

@Data
public class QuestionWeights {
    public enum QuestionRank {
        RED, AMBER, GREEN
    }

    private int weight;

    private int rank;

    @Override
    public String toString() {
        return "QuestionWeights{" +
            "weight=" + weight +
            ", rank=" + rank +
            '}';
    }
}
