package com.redhat.gps.pathfinder.config;

import com.redhat.gps.pathfinder.domain.QuestionMetaData;
import com.redhat.gps.pathfinder.domain.QuestionWeights;

import java.util.ArrayList;

public class PathfinderQuestionConfig {

    public static QuestionMetaData QuestionARCHTYPE() {
        QuestionMetaData currData = new QuestionMetaData();
        currData.setId("ARCHTYPE");
        currData.setAspect("Architectural Suitability");
        ArrayList<QuestionWeights> qweights = new ArrayList<>();

        QuestionWeights c1 = new QuestionWeights();
        c1.setWeight(1);
        c1.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c1);

        QuestionWeights c2 = new QuestionWeights();
        c2.setWeight(1);
        c2.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c2);

        QuestionWeights c3 = new QuestionWeights();
        c3.setWeight(1);
        c3.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c3);

        QuestionWeights c4 = new QuestionWeights();
        c4.setWeight(1);
        c4.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c4);

        QuestionWeights c5 = new QuestionWeights();
        c5.setWeight(1);
        c5.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c5);

        QuestionWeights c6 = new QuestionWeights();
        c6.setWeight(1);
        c6.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c6);

        currData.setMetaData(qweights);
        return currData;
    }

    public static QuestionMetaData QuestionDEPSHW() {
        QuestionMetaData currData = new QuestionMetaData();
        currData.setId("DEPSHW");
        currData.setAspect("Dependencies - Hardware");
        ArrayList<QuestionWeights> qweights = new ArrayList<>();

        QuestionWeights c1 = new QuestionWeights();
        c1.setWeight(1);
        c1.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c1);

        QuestionWeights c2 = new QuestionWeights();
        c2.setWeight(1);
        c2.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c2);

        QuestionWeights c3 = new QuestionWeights();
        c3.setWeight(1);
        c3.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c3);

        QuestionWeights c4 = new QuestionWeights();
        c4.setWeight(1);
        c4.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c4);

        QuestionWeights c5 = new QuestionWeights();
        c5.setWeight(1);
        c5.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c5);

        QuestionWeights c6 = new QuestionWeights();
        c6.setWeight(1);
        c6.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c6);

        currData.setMetaData(qweights);
        return currData;
    }

    public static QuestionMetaData QuestionDEPSOS() {
        QuestionMetaData currData = new QuestionMetaData();
        currData.setId("DEPSOS");
        currData.setAspect("Dependencies - Operating system");
        ArrayList<QuestionWeights> qweights = new ArrayList<>();

        QuestionWeights c1 = new QuestionWeights();
        c1.setWeight(1);
        c1.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c1);

        QuestionWeights c2 = new QuestionWeights();
        c2.setWeight(1);
        c2.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c2);

        QuestionWeights c3 = new QuestionWeights();
        c3.setWeight(1);
        c3.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c3);

        QuestionWeights c4 = new QuestionWeights();
        c4.setWeight(1);
        c4.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c4);

        QuestionWeights c5 = new QuestionWeights();
        c5.setWeight(1);
        c5.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c5);

        QuestionWeights c6 = new QuestionWeights();
        c6.setWeight(1);
        c6.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c6);

        currData.setMetaData(qweights);
        return currData;
    }

    public static QuestionMetaData QuestionDEPS3RD() {
        QuestionMetaData currData = new QuestionMetaData();
        currData.setId("DEPS3RD");
        currData.setAspect("Dependencies - 3rd party vendor");
        ArrayList<QuestionWeights> qweights = new ArrayList<>();

        QuestionWeights c1 = new QuestionWeights();
        c1.setWeight(1);
        c1.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c1);

        QuestionWeights c2 = new QuestionWeights();
        c2.setWeight(1);
        c2.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c2);

        QuestionWeights c3 = new QuestionWeights();
        c3.setWeight(1);
        c3.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c3);

        QuestionWeights c4 = new QuestionWeights();
        c4.setWeight(1);
        c4.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c4);

        QuestionWeights c5 = new QuestionWeights();
        c5.setWeight(1);
        c5.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c5);

        QuestionWeights c6 = new QuestionWeights();
        c6.setWeight(1);
        c6.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c6);

        currData.setMetaData(qweights);
        return currData;
    }


    public static QuestionMetaData QuestionDEPSIN() {
        QuestionMetaData currData = new QuestionMetaData();
        currData.setId("DEPSIN");
        currData.setAspect("Dependencies - (Incoming/Northbound)");
        ArrayList<QuestionWeights> qweights = new ArrayList<>();

        QuestionWeights c1 = new QuestionWeights();
        c1.setWeight(1);
        c1.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c1);

        QuestionWeights c2 = new QuestionWeights();
        c2.setWeight(1);
        c2.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c2);

        QuestionWeights c3 = new QuestionWeights();
        c3.setWeight(1);
        c3.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c3);

        QuestionWeights c4 = new QuestionWeights();
        c4.setWeight(1);
        c4.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c4);

        QuestionWeights c5 = new QuestionWeights();
        c5.setWeight(1);
        c5.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c5);

        QuestionWeights c6 = new QuestionWeights();
        c6.setWeight(1);
        c6.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c6);

        currData.setMetaData(qweights);
        return currData;
    }


    public static QuestionMetaData QuestionDEPSOUT() {
        QuestionMetaData currData = new QuestionMetaData();
        currData.setId("DEPSOUT");
        currData.setAspect("Dependencies - (Outgoing/Southbound)");
        ArrayList<QuestionWeights> qweights = new ArrayList<>();

        QuestionWeights c1 = new QuestionWeights();
        c1.setWeight(1);
        c1.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c1);

        QuestionWeights c2 = new QuestionWeights();
        c2.setWeight(1);
        c2.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c2);

        QuestionWeights c3 = new QuestionWeights();
        c3.setWeight(1);
        c3.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c3);

        QuestionWeights c4 = new QuestionWeights();
        c4.setWeight(1);
        c4.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c4);

        QuestionWeights c5 = new QuestionWeights();
        c5.setWeight(1);
        c5.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c5);

        QuestionWeights c6 = new QuestionWeights();
        c6.setWeight(1);
        c6.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c6);

        currData.setMetaData(qweights);
        return currData;
    }


    public static QuestionMetaData QuestionRESILIENCY() {
        QuestionMetaData currData = new QuestionMetaData();
        currData.setId("RESILIENCY");
        currData.setAspect("Application resiliency");
        ArrayList<QuestionWeights> qweights = new ArrayList<>();

        QuestionWeights c1 = new QuestionWeights();
        c1.setWeight(1);
        c1.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c1);

        QuestionWeights c2 = new QuestionWeights();
        c2.setWeight(1);
        c2.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c2);

        QuestionWeights c3 = new QuestionWeights();
        c3.setWeight(1);
        c3.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c3);

        QuestionWeights c4 = new QuestionWeights();
        c4.setWeight(1);
        c4.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c4);

        QuestionWeights c5 = new QuestionWeights();
        c5.setWeight(1);
        c5.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c5);

        QuestionWeights c6 = new QuestionWeights();
        c6.setWeight(1);
        c6.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c6);

        currData.setMetaData(qweights);
        return currData;
    }

    public static QuestionMetaData QuestionCOMMS() {
        QuestionMetaData currData = new QuestionMetaData();
        currData.setId("COMMS");
        currData.setAspect("Communication");
        ArrayList<QuestionWeights> qweights = new ArrayList<>();

        QuestionWeights c1 = new QuestionWeights();
        c1.setWeight(1);
        c1.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c1);

        QuestionWeights c2 = new QuestionWeights();
        c2.setWeight(1);
        c2.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c2);

        QuestionWeights c3 = new QuestionWeights();
        c3.setWeight(1);
        c3.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c3);

        QuestionWeights c4 = new QuestionWeights();
        c4.setWeight(1);
        c4.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c4);

        QuestionWeights c5 = new QuestionWeights();
        c5.setWeight(1);
        c5.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c5);

        QuestionWeights c6 = new QuestionWeights();
        c6.setWeight(1);
        c6.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c6);

        currData.setMetaData(qweights);
        return currData;
    }


    public static QuestionMetaData QuestionCOMPLIANCE() {
        QuestionMetaData currData = new QuestionMetaData();
        currData.setId("COMPLIANCE");
        currData.setAspect("Compliance");
        ArrayList<QuestionWeights> qweights = new ArrayList<>();

        QuestionWeights c1 = new QuestionWeights();
        c1.setWeight(1);
        c1.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c1);

        QuestionWeights c2 = new QuestionWeights();
        c2.setWeight(1);
        c2.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c2);

        QuestionWeights c3 = new QuestionWeights();
        c3.setWeight(1);
        c3.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c3);

        QuestionWeights c4 = new QuestionWeights();
        c4.setWeight(1);
        c4.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c4);

        QuestionWeights c5 = new QuestionWeights();
        c5.setWeight(1);
        c5.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c5);

        QuestionWeights c6 = new QuestionWeights();
        c6.setWeight(1);
        c6.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c6);

        currData.setMetaData(qweights);
        return currData;
    }


    public static QuestionMetaData QuestionSTATE() {
        QuestionMetaData currData = new QuestionMetaData();
        currData.setId("STATE");
        currData.setAspect("State Management");
        ArrayList<QuestionWeights> qweights = new ArrayList<>();

        QuestionWeights c1 = new QuestionWeights();
        c1.setWeight(1);
        c1.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c1);

        QuestionWeights c2 = new QuestionWeights();
        c2.setWeight(1);
        c2.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c2);

        QuestionWeights c3 = new QuestionWeights();
        c3.setWeight(1);
        c3.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c3);

        QuestionWeights c4 = new QuestionWeights();
        c4.setWeight(1);
        c4.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c4);

        QuestionWeights c5 = new QuestionWeights();
        c5.setWeight(1);
        c5.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c5);

        QuestionWeights c6 = new QuestionWeights();
        c6.setWeight(1);
        c6.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c6);

        currData.setMetaData(qweights);
        return currData;
    }


    public static QuestionMetaData QuestionPROFILE() {
        QuestionMetaData currData = new QuestionMetaData();
        currData.setId("PROFILE");
        currData.setAspect("Runtime profile");
        ArrayList<QuestionWeights> qweights = new ArrayList<>();

        QuestionWeights c1 = new QuestionWeights();
        c1.setWeight(1);
        c1.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c1);

        QuestionWeights c2 = new QuestionWeights();
        c2.setWeight(1);
        c2.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c2);

        QuestionWeights c3 = new QuestionWeights();
        c3.setWeight(1);
        c3.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c3);

        QuestionWeights c4 = new QuestionWeights();
        c4.setWeight(1);
        c4.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c4);

        QuestionWeights c5 = new QuestionWeights();
        c5.setWeight(1);
        c5.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c5);

        QuestionWeights c6 = new QuestionWeights();
        c6.setWeight(1);
        c6.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c6);

        currData.setMetaData(qweights);
        return currData;
    }

    public static QuestionMetaData QuestionLOGS() {
        QuestionMetaData currData = new QuestionMetaData();
        currData.setId("LOGS");
        currData.setAspect("Observability - Application Logs");
        ArrayList<QuestionWeights> qweights = new ArrayList<>();

        QuestionWeights c1 = new QuestionWeights();
        c1.setWeight(1);
        c1.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c1);

        QuestionWeights c2 = new QuestionWeights();
        c2.setWeight(1);
        c2.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c2);

        QuestionWeights c3 = new QuestionWeights();
        c3.setWeight(1);
        c3.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c3);

        QuestionWeights c4 = new QuestionWeights();
        c4.setWeight(1);
        c4.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c4);

        QuestionWeights c5 = new QuestionWeights();
        c5.setWeight(1);
        c5.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c5);

        QuestionWeights c6 = new QuestionWeights();
        c6.setWeight(1);
        c6.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c6);

        currData.setMetaData(qweights);
        return currData;
    }

    public static QuestionMetaData QuestionMETRICS() {
        QuestionMetaData currData = new QuestionMetaData();
        currData.setId("METRICS");
        currData.setAspect("Observability - Application Metrics");
        ArrayList<QuestionWeights> qweights = new ArrayList<>();

        QuestionWeights c1 = new QuestionWeights();
        c1.setWeight(1);
        c1.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c1);

        QuestionWeights c2 = new QuestionWeights();
        c2.setWeight(1);
        c2.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c2);

        QuestionWeights c3 = new QuestionWeights();
        c3.setWeight(1);
        c3.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c3);

        QuestionWeights c4 = new QuestionWeights();
        c4.setWeight(1);
        c4.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c4);

        QuestionWeights c5 = new QuestionWeights();
        c5.setWeight(1);
        c5.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c5);

        QuestionWeights c6 = new QuestionWeights();
        c6.setWeight(1);
        c6.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c6);

        currData.setMetaData(qweights);
        return currData;
    }

    public static QuestionMetaData QuestionHEALTH() {
        QuestionMetaData currData = new QuestionMetaData();
        currData.setId("HEALTH");
        currData.setAspect("Observability - Application Health");
        ArrayList<QuestionWeights> qweights = new ArrayList<>();

        QuestionWeights c1 = new QuestionWeights();
        c1.setWeight(1);
        c1.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c1);

        QuestionWeights c2 = new QuestionWeights();
        c2.setWeight(1);
        c2.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c2);

        QuestionWeights c3 = new QuestionWeights();
        c3.setWeight(1);
        c3.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c3);

        QuestionWeights c4 = new QuestionWeights();
        c4.setWeight(1);
        c4.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c4);

        QuestionWeights c5 = new QuestionWeights();
        c5.setWeight(1);
        c5.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c5);

        QuestionWeights c6 = new QuestionWeights();
        c6.setWeight(1);
        c6.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c6);

        currData.setMetaData(qweights);
        return currData;
    }


    public static QuestionMetaData QuestionOWNER() {
        QuestionMetaData currData = new QuestionMetaData();
        currData.setId("OWNER");
        currData.setAspect("Level of ownership");
        ArrayList<QuestionWeights> qweights = new ArrayList<>();

        QuestionWeights c1 = new QuestionWeights();
        c1.setWeight(1);
        c1.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c1);

        QuestionWeights c2 = new QuestionWeights();
        c2.setWeight(1);
        c2.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c2);

        QuestionWeights c3 = new QuestionWeights();
        c3.setWeight(1);
        c3.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c3);

        QuestionWeights c4 = new QuestionWeights();
        c4.setWeight(1);
        c4.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c4);

        QuestionWeights c5 = new QuestionWeights();
        c5.setWeight(1);
        c5.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c5);

        QuestionWeights c6 = new QuestionWeights();
        c6.setWeight(1);
        c6.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c6);

        currData.setMetaData(qweights);
        return currData;
    }

    public static QuestionMetaData QuestionHA() {
        QuestionMetaData currData = new QuestionMetaData();
        currData.setId("HA");
        currData.setAspect("Discovery");
        ArrayList<QuestionWeights> qweights = new ArrayList<>();

        QuestionWeights c1 = new QuestionWeights();
        c1.setWeight(1);
        c1.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c1);

        QuestionWeights c2 = new QuestionWeights();
        c2.setWeight(1);
        c2.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c2);

        QuestionWeights c3 = new QuestionWeights();
        c3.setWeight(1);
        c3.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c3);

        QuestionWeights c4 = new QuestionWeights();
        c4.setWeight(1);
        c4.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c4);

        QuestionWeights c5 = new QuestionWeights();
        c5.setWeight(1);
        c5.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c5);

        QuestionWeights c6 = new QuestionWeights();
        c6.setWeight(1);
        c6.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c6);

        currData.setMetaData(qweights);
        return currData;
    }


    public static QuestionMetaData QuestionDEPLOY() {
        QuestionMetaData currData = new QuestionMetaData();
        currData.setId("DEPLOY");
        currData.setAspect("Deployment Complexity");
        ArrayList<QuestionWeights> qweights = new ArrayList<>();

        QuestionWeights c1 = new QuestionWeights();
        c1.setWeight(1);
        c1.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c1);

        QuestionWeights c2 = new QuestionWeights();
        c2.setWeight(1);
        c2.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c2);

        QuestionWeights c3 = new QuestionWeights();
        c3.setWeight(1);
        c3.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c3);

        QuestionWeights c4 = new QuestionWeights();
        c4.setWeight(1);
        c4.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c4);

        QuestionWeights c5 = new QuestionWeights();
        c5.setWeight(1);
        c5.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c5);

        QuestionWeights c6 = new QuestionWeights();
        c6.setWeight(1);
        c6.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c6);

        currData.setMetaData(qweights);
        return currData;
    }


    public static QuestionMetaData QuestionTEST() {
        QuestionMetaData currData = new QuestionMetaData();
        currData.setId("TEST");
        currData.setAspect("Application Testing");
        ArrayList<QuestionWeights> qweights = new ArrayList<>();

        QuestionWeights c1 = new QuestionWeights();
        c1.setWeight(1);
        c1.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c1);

        QuestionWeights c2 = new QuestionWeights();
        c2.setWeight(1);
        c2.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c2);

        QuestionWeights c3 = new QuestionWeights();
        c3.setWeight(1);
        c3.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c3);

        QuestionWeights c4 = new QuestionWeights();
        c4.setWeight(1);
        c4.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c4);

        QuestionWeights c5 = new QuestionWeights();
        c5.setWeight(1);
        c5.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c5);

        QuestionWeights c6 = new QuestionWeights();
        c6.setWeight(1);
        c6.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c6);

        currData.setMetaData(qweights);
        return currData;
    }

    public static QuestionMetaData QuestionSECURITY() {
        QuestionMetaData currData = new QuestionMetaData();
        currData.setId("SECURITY");
        currData.setAspect("Application Security");
        ArrayList<QuestionWeights> qweights = new ArrayList<>();

        QuestionWeights c1 = new QuestionWeights();
        c1.setWeight(1);
        c1.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c1);

        QuestionWeights c2 = new QuestionWeights();
        c2.setWeight(1);
        c2.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c2);

        QuestionWeights c3 = new QuestionWeights();
        c3.setWeight(1);
        c3.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c3);

        QuestionWeights c4 = new QuestionWeights();
        c4.setWeight(1);
        c4.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c4);

        QuestionWeights c5 = new QuestionWeights();
        c5.setWeight(1);
        c5.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c5);

        QuestionWeights c6 = new QuestionWeights();
        c6.setWeight(1);
        c6.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c6);

        currData.setMetaData(qweights);
        return currData;
    }

    public static QuestionMetaData QuestionCONFIG() {
        QuestionMetaData currData = new QuestionMetaData();
        currData.setId("CONFIG");
        currData.setAspect("Application Configuration");
        ArrayList<QuestionWeights> qweights = new ArrayList<>();

        QuestionWeights c1 = new QuestionWeights();
        c1.setWeight(1);
        c1.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c1);

        QuestionWeights c2 = new QuestionWeights();
        c2.setWeight(1);
        c2.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c2);

        QuestionWeights c3 = new QuestionWeights();
        c3.setWeight(1);
        c3.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c3);

        QuestionWeights c4 = new QuestionWeights();
        c4.setWeight(1);
        c4.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c4);

        QuestionWeights c5 = new QuestionWeights();
        c5.setWeight(1);
        c5.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c5);

        QuestionWeights c6 = new QuestionWeights();
        c6.setWeight(1);
        c6.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c6);

        currData.setMetaData(qweights);
        return currData;
    }

    public static QuestionMetaData QuestionCLUSTER() {
        QuestionMetaData currData = new QuestionMetaData();
        currData.setId("CLUSTER");
        currData.setAspect("Clustering");
        ArrayList<QuestionWeights> qweights = new ArrayList<>();

        QuestionWeights c1 = new QuestionWeights();
        c1.setWeight(1);
        c1.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c1);

        QuestionWeights c2 = new QuestionWeights();
        c2.setWeight(1);
        c2.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c2);

        QuestionWeights c3 = new QuestionWeights();
        c3.setWeight(1);
        c3.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c3);

        QuestionWeights c4 = new QuestionWeights();
        c4.setWeight(1);
        c4.setRank(QuestionWeights.QuestionRank.AMBER.ordinal());
        qweights.add(c4);

        QuestionWeights c5 = new QuestionWeights();
        c5.setWeight(1);
        c5.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c5);

        QuestionWeights c6 = new QuestionWeights();
        c6.setWeight(1);
        c6.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c6);

        currData.setMetaData(qweights);
        return currData;
    }

    public static QuestionMetaData QuestionCONTAINERS() {
        QuestionMetaData currData = new QuestionMetaData();
        currData.setId("CONTAINERS");
        currData.setAspect("Existing containerisation");
        ArrayList<QuestionWeights> qweights = new ArrayList<>();

        QuestionWeights c1 = new QuestionWeights();
        c1.setWeight(1);
        c1.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c1);

        QuestionWeights c2 = new QuestionWeights();
        c2.setWeight(1);
        c2.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c2);

        QuestionWeights c3 = new QuestionWeights();
        c3.setWeight(1);
        c3.setRank(QuestionWeights.QuestionRank.RED.ordinal());
        qweights.add(c3);

        QuestionWeights c4 = new QuestionWeights();
        c4.setWeight(1);
        c4.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c4);

        QuestionWeights c5 = new QuestionWeights();
        c5.setWeight(1);
        c5.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c5);

        QuestionWeights c6 = new QuestionWeights();
        c6.setWeight(1);
        c6.setRank(QuestionWeights.QuestionRank.GREEN.ordinal());
        qweights.add(c6);

        currData.setMetaData(qweights);
        return currData;
    }

}
