package com.redhat.gps.pathfinder.config;

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

import com.redhat.gps.pathfinder.domain.QuestionMetaData;
import com.redhat.gps.pathfinder.domain.QuestionWeights;

import java.util.ArrayList;

public class PathfinderQuestionConfig {

//    public static QuestionMetaData QuestionARCHTYPE() {
//        QuestionMetaData currData = new QuestionMetaData();
//        currData.setId("ARCHTYPE");
//        currData.setAspect("Architectural Suitability");
//        currData.setMinimum(3);
//        ArrayList<QuestionWeights> qweights = new ArrayList<>();
//
//        QuestionWeights c1 = new QuestionWeights();
//        c1.setWeight(1);
//        c1.setRank(QuestionWeights.QuestionRank.UNKNOWN);
//        qweights.add(c1);
//
//        QuestionWeights c2 = new QuestionWeights();
//        c2.setWeight(1);
//        c2.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c2);
//
//        QuestionWeights c3 = new QuestionWeights();
//        c3.setWeight(1);
//        c3.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c3);
//
//        QuestionWeights c4 = new QuestionWeights();
//        c4.setWeight(1);
//        c4.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c4);
//
//        QuestionWeights c5 = new QuestionWeights();
//        c5.setWeight(1);
//        c5.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c5);
//
//        QuestionWeights c6 = new QuestionWeights();
//        c6.setWeight(1);
//        c6.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c6);
//
//        currData.setMetaData(qweights);
//        return currData;
//    }
//
//    public static QuestionMetaData QuestionDEPSHW() {
//        QuestionMetaData currData = new QuestionMetaData();
//        currData.setId("DEPSHW");
//        currData.setMinimum(3);
//        currData.setAspect("Dependencies - Hardware");
//        ArrayList<QuestionWeights> qweights = new ArrayList<>();
//
//        QuestionWeights c1 = new QuestionWeights();
//        c1.setWeight(1);
//        c1.setRank(QuestionWeights.QuestionRank.UNKNOWN);
//        qweights.add(c1);
//
//        QuestionWeights c2 = new QuestionWeights();
//        c2.setWeight(1);
//        c2.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c2);
//
//        QuestionWeights c3 = new QuestionWeights();
//        c3.setWeight(1);
//        c3.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c3);
//
//        QuestionWeights c4 = new QuestionWeights();
//        c4.setWeight(1);
//        c4.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c4);
//
//        QuestionWeights c5 = new QuestionWeights();
//        c5.setWeight(1);
//        c5.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c5);
//
//        QuestionWeights c6 = new QuestionWeights();
//        c6.setWeight(1);
//        c6.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c6);
//
//        currData.setMetaData(qweights);
//        return currData;
//    }
//
//    public static QuestionMetaData QuestionDEPSOS() {
//        QuestionMetaData currData = new QuestionMetaData();
//        currData.setId("DEPSOS");
//        currData.setMinimum(3);
//        currData.setAspect("Dependencies - Operating system");
//        ArrayList<QuestionWeights> qweights = new ArrayList<>();
//
//        QuestionWeights c1 = new QuestionWeights();
//        c1.setWeight(1);
//        c1.setRank(QuestionWeights.QuestionRank.UNKNOWN);
//        qweights.add(c1);
//
//        QuestionWeights c2 = new QuestionWeights();
//        c2.setWeight(1);
//        c2.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c2);
//
//        QuestionWeights c3 = new QuestionWeights();
//        c3.setWeight(1);
//        c3.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c3);
//
//        QuestionWeights c4 = new QuestionWeights();
//        c4.setWeight(1);
//        c4.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c4);
//
//        QuestionWeights c5 = new QuestionWeights();
//        c5.setWeight(1);
//        c5.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c5);
//
//        QuestionWeights c6 = new QuestionWeights();
//        c6.setWeight(1);
//        c6.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c6);
//
//        currData.setMetaData(qweights);
//        return currData;
//    }
//
//    public static QuestionMetaData QuestionDEPS3RD() {
//        QuestionMetaData currData = new QuestionMetaData();
//        currData.setId("DEPS3RD");
//        currData.setMinimum(4);
//        currData.setAspect("Dependencies - 3rd party vendor");
//        ArrayList<QuestionWeights> qweights = new ArrayList<>();
//
//        QuestionWeights c1 = new QuestionWeights();
//        c1.setWeight(1);
//        c1.setRank(QuestionWeights.QuestionRank.UNKNOWN);
//        qweights.add(c1);
//
//        QuestionWeights c2 = new QuestionWeights();
//        c2.setWeight(1);
//        c2.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c2);
//
//        QuestionWeights c3 = new QuestionWeights();
//        c3.setWeight(1);
//        c3.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c3);
//
//        QuestionWeights c4 = new QuestionWeights();
//        c4.setWeight(1);
//        c4.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c4);
//
//        QuestionWeights c5 = new QuestionWeights();
//        c5.setWeight(1);
//        c5.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c5);
//
//        QuestionWeights c6 = new QuestionWeights();
//        c6.setWeight(1);
//        c6.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c6);
//
//        currData.setMetaData(qweights);
//        return currData;
//    }
//
//
//    public static QuestionMetaData QuestionDEPSIN() {
//        QuestionMetaData currData = new QuestionMetaData();
//        currData.setId("DEPSIN");
//        currData.setMinimum(3);
//        currData.setAspect("Dependencies - (Incoming/Northbound)");
//        ArrayList<QuestionWeights> qweights = new ArrayList<>();
//
//        QuestionWeights c1 = new QuestionWeights();
//        c1.setWeight(1);
//        c1.setRank(QuestionWeights.QuestionRank.UNKNOWN);
//        qweights.add(c1);
//
//        QuestionWeights c2 = new QuestionWeights();
//        c2.setWeight(1);
//        c2.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c2);
//
//        QuestionWeights c3 = new QuestionWeights();
//        c3.setWeight(1);
//        c3.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c3);
//
//        QuestionWeights c4 = new QuestionWeights();
//        c4.setWeight(1);
//        c4.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c4);
//
//        QuestionWeights c5 = new QuestionWeights();
//        c5.setWeight(1);
//        c5.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c5);
//
//        QuestionWeights c6 = new QuestionWeights();
//        c6.setWeight(1);
//        c6.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c6);
//
//        currData.setMetaData(qweights);
//        return currData;
//    }
//
//
//    public static QuestionMetaData QuestionDEPSOUT() {
//        QuestionMetaData currData = new QuestionMetaData();
//        currData.setId("DEPSOUT");
//        currData.setMinimum(3);
//        currData.setAspect("Dependencies - (Outgoing/Southbound)");
//        ArrayList<QuestionWeights> qweights = new ArrayList<>();
//
//        QuestionWeights c1 = new QuestionWeights();
//        c1.setWeight(1);
//        c1.setRank(QuestionWeights.QuestionRank.UNKNOWN);
//        qweights.add(c1);
//
//        QuestionWeights c2 = new QuestionWeights();
//        c2.setWeight(1);
//        c2.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c2);
//
//        QuestionWeights c3 = new QuestionWeights();
//        c3.setWeight(1);
//        c3.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c3);
//
//        QuestionWeights c4 = new QuestionWeights();
//        c4.setWeight(1);
//        c4.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c4);
//
//        QuestionWeights c5 = new QuestionWeights();
//        c5.setWeight(1);
//        c5.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c5);
//
//        QuestionWeights c6 = new QuestionWeights();
//        c6.setWeight(1);
//        c6.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c6);
//
//        currData.setMetaData(qweights);
//        return currData;
//    }
//
//
//    public static QuestionMetaData QuestionRESILIENCY() {
//        QuestionMetaData currData = new QuestionMetaData();
//        currData.setId("RESILIENCY");
//        currData.setMinimum(2);
//        currData.setAspect("Application resiliency");
//        ArrayList<QuestionWeights> qweights = new ArrayList<>();
//
//        QuestionWeights c1 = new QuestionWeights();
//        c1.setWeight(1);
//        c1.setRank(QuestionWeights.QuestionRank.UNKNOWN);
//        qweights.add(c1);
//
//        QuestionWeights c2 = new QuestionWeights();
//        c2.setWeight(1);
//        c2.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c2);
//
//        QuestionWeights c3 = new QuestionWeights();
//        c3.setWeight(1);
//        c3.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c3);
//
//        QuestionWeights c4 = new QuestionWeights();
//        c4.setWeight(1);
//        c4.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c4);
//
//        QuestionWeights c5 = new QuestionWeights();
//        c5.setWeight(1);
//        c5.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c5);
//
//        QuestionWeights c6 = new QuestionWeights();
//        c6.setWeight(1);
//        c6.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c6);
//
//        currData.setMetaData(qweights);
//        return currData;
//    }
//
//    public static QuestionMetaData QuestionCOMMS() {
//        QuestionMetaData currData = new QuestionMetaData();
//        currData.setId("COMMS");
//        currData.setMinimum(3);
//        currData.setAspect("Communication");
//        ArrayList<QuestionWeights> qweights = new ArrayList<>();
//
//        QuestionWeights c1 = new QuestionWeights();
//        c1.setWeight(1);
//        c1.setRank(QuestionWeights.QuestionRank.UNKNOWN);
//        qweights.add(c1);
//
//        QuestionWeights c2 = new QuestionWeights();
//        c2.setWeight(1);
//        c2.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c2);
//
//        QuestionWeights c3 = new QuestionWeights();
//        c3.setWeight(1);
//        c3.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c3);
//
//        QuestionWeights c4 = new QuestionWeights();
//        c4.setWeight(1);
//        c4.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c4);
//
//        QuestionWeights c5 = new QuestionWeights();
//        c5.setWeight(1);
//        c5.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c5);
//
//        QuestionWeights c6 = new QuestionWeights();
//        c6.setWeight(1);
//        c6.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c6);
//
//        currData.setMetaData(qweights);
//        return currData;
//    }
//
//
//    public static QuestionMetaData QuestionCOMPLIANCE() {
//        QuestionMetaData currData = new QuestionMetaData();
//        currData.setId("COMPLIANCE");
//        currData.setMinimum(4);
//        currData.setAspect("Compliance");
//        ArrayList<QuestionWeights> qweights = new ArrayList<>();
//
//        QuestionWeights c1 = new QuestionWeights();
//        c1.setWeight(1);
//        c1.setRank(QuestionWeights.QuestionRank.UNKNOWN);
//        qweights.add(c1);
//
//        QuestionWeights c2 = new QuestionWeights();
//        c2.setWeight(1);
//        c2.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c2);
//
//        QuestionWeights c3 = new QuestionWeights();
//        c3.setWeight(1);
//        c3.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c3);
//
//        QuestionWeights c4 = new QuestionWeights();
//        c4.setWeight(1);
//        c4.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c4);
//
//        QuestionWeights c5 = new QuestionWeights();
//        c5.setWeight(1);
//        c5.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c5);
//
//        QuestionWeights c6 = new QuestionWeights();
//        c6.setWeight(1);
//        c6.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c6);
//
//        currData.setMetaData(qweights);
//        return currData;
//    }
//
//
//    public static QuestionMetaData QuestionSTATE() {
//        QuestionMetaData currData = new QuestionMetaData();
//        currData.setId("STATE");
//        currData.setMinimum(3);
//        currData.setAspect("State Management");
//        ArrayList<QuestionWeights> qweights = new ArrayList<>();
//
//        QuestionWeights c1 = new QuestionWeights();
//        c1.setWeight(1);
//        c1.setRank(QuestionWeights.QuestionRank.UNKNOWN);
//        qweights.add(c1);
//
//        QuestionWeights c2 = new QuestionWeights();
//        c2.setWeight(1);
//        c2.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c2);
//
//        QuestionWeights c3 = new QuestionWeights();
//        c3.setWeight(1);
//        c3.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c3);
//
//        QuestionWeights c4 = new QuestionWeights();
//        c4.setWeight(1);
//        c4.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c4);
//
//        QuestionWeights c5 = new QuestionWeights();
//        c5.setWeight(1);
//        c5.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c5);
//
//        QuestionWeights c6 = new QuestionWeights();
//        c6.setWeight(1);
//        c6.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c6);
//
//        currData.setMetaData(qweights);
//        return currData;
//    }
//
//
//    public static QuestionMetaData QuestionPROFILE() {
//        QuestionMetaData currData = new QuestionMetaData();
//        currData.setId("PROFILE");
//        currData.setMinimum(4);
//        currData.setAspect("Runtime profile");
//        ArrayList<QuestionWeights> qweights = new ArrayList<>();
//
//        QuestionWeights c1 = new QuestionWeights();
//        c1.setWeight(1);
//        c1.setRank(QuestionWeights.QuestionRank.UNKNOWN);
//        qweights.add(c1);
//
//        QuestionWeights c2 = new QuestionWeights();
//        c2.setWeight(1);
//        c2.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c2);
//
//        QuestionWeights c3 = new QuestionWeights();
//        c3.setWeight(1);
//        c3.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c3);
//
//        QuestionWeights c4 = new QuestionWeights();
//        c4.setWeight(1);
//        c4.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c4);
//
//        QuestionWeights c5 = new QuestionWeights();
//        c5.setWeight(1);
//        c5.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c5);
//
//        QuestionWeights c6 = new QuestionWeights();
//        c6.setWeight(1);
//        c6.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c6);
//
//        currData.setMetaData(qweights);
//        return currData;
//    }
//
//    public static QuestionMetaData QuestionLOGS() {
//        QuestionMetaData currData = new QuestionMetaData();
//        currData.setId("LOGS");
//        currData.setMinimum(4);
//        currData.setAspect("Observability - Application Logs");
//        ArrayList<QuestionWeights> qweights = new ArrayList<>();
//
//        QuestionWeights c1 = new QuestionWeights();
//        c1.setWeight(1);
//        c1.setRank(QuestionWeights.QuestionRank.UNKNOWN);
//        qweights.add(c1);
//
//        QuestionWeights c2 = new QuestionWeights();
//        c2.setWeight(1);
//        c2.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c2);
//
//        QuestionWeights c3 = new QuestionWeights();
//        c3.setWeight(1);
//        c3.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c3);
//
//        QuestionWeights c4 = new QuestionWeights();
//        c4.setWeight(1);
//        c4.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c4);
//
//        QuestionWeights c5 = new QuestionWeights();
//        c5.setWeight(1);
//        c5.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c5);
//
//        QuestionWeights c6 = new QuestionWeights();
//        c6.setWeight(1);
//        c6.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c6);
//
//        currData.setMetaData(qweights);
//        return currData;
//    }
//
//    public static QuestionMetaData QuestionMETRICS() {
//        QuestionMetaData currData = new QuestionMetaData();
//        currData.setId("METRICS");
//        currData.setMinimum(4);
//        currData.setAspect("Observability - Application Metrics");
//        ArrayList<QuestionWeights> qweights = new ArrayList<>();
//
//        QuestionWeights c1 = new QuestionWeights();
//        c1.setWeight(1);
//        c1.setRank(QuestionWeights.QuestionRank.UNKNOWN);
//        qweights.add(c1);
//
//        QuestionWeights c2 = new QuestionWeights();
//        c2.setWeight(1);
//        c2.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c2);
//
//        QuestionWeights c3 = new QuestionWeights();
//        c3.setWeight(1);
//        c3.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c3);
//
//        QuestionWeights c4 = new QuestionWeights();
//        c4.setWeight(1);
//        c4.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c4);
//
//        QuestionWeights c5 = new QuestionWeights();
//        c5.setWeight(1);
//        c5.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c5);
//
//        QuestionWeights c6 = new QuestionWeights();
//        c6.setWeight(1);
//        c6.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c6);
//
//        currData.setMetaData(qweights);
//        return currData;
//    }
//
//    public static QuestionMetaData QuestionHEALTH() {
//        QuestionMetaData currData = new QuestionMetaData();
//        currData.setId("HEALTH");
//        currData.setMinimum(2);
//        currData.setAspect("Observability - Application Health");
//        ArrayList<QuestionWeights> qweights = new ArrayList<>();
//
//        QuestionWeights c1 = new QuestionWeights();
//        c1.setWeight(1);
//        c1.setRank(QuestionWeights.QuestionRank.UNKNOWN);
//        qweights.add(c1);
//
//        QuestionWeights c2 = new QuestionWeights();
//        c2.setWeight(1);
//        c2.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c2);
//
//        QuestionWeights c3 = new QuestionWeights();
//        c3.setWeight(1);
//        c3.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c3);
//
//        QuestionWeights c4 = new QuestionWeights();
//        c4.setWeight(1);
//        c4.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c4);
//
//        QuestionWeights c5 = new QuestionWeights();
//        c5.setWeight(1);
//        c5.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c5);
//
//        QuestionWeights c6 = new QuestionWeights();
//        c6.setWeight(1);
//        c6.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c6);
//
//        currData.setMetaData(qweights);
//        return currData;
//    }
//
//
//    public static QuestionMetaData QuestionOWNER() {
//        QuestionMetaData currData = new QuestionMetaData();
//        currData.setId("OWNER");
//        currData.setMinimum(3);
//        currData.setAspect("Level of ownership");
//        ArrayList<QuestionWeights> qweights = new ArrayList<>();
//
//        QuestionWeights c1 = new QuestionWeights();
//        c1.setWeight(1);
//        c1.setRank(QuestionWeights.QuestionRank.UNKNOWN);
//        qweights.add(c1);
//
//        QuestionWeights c2 = new QuestionWeights();
//        c2.setWeight(1);
//        c2.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c2);
//
//        QuestionWeights c3 = new QuestionWeights();
//        c3.setWeight(1);
//        c3.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c3);
//
//        QuestionWeights c4 = new QuestionWeights();
//        c4.setWeight(1);
//        c4.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c4);
//
//        QuestionWeights c5 = new QuestionWeights();
//        c5.setWeight(1);
//        c5.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c5);
//
//        QuestionWeights c6 = new QuestionWeights();
//        c6.setWeight(1);
//        c6.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c6);
//
//        currData.setMetaData(qweights);
//        return currData;
//    }
//
//    public static QuestionMetaData QuestionHA() {
//        QuestionMetaData currData = new QuestionMetaData();
//        currData.setId("HA");
//        currData.setMinimum(3);
//        currData.setAspect("Discovery");
//        ArrayList<QuestionWeights> qweights = new ArrayList<>();
//
//        QuestionWeights c1 = new QuestionWeights();
//        c1.setWeight(1);
//        c1.setRank(QuestionWeights.QuestionRank.UNKNOWN);
//        qweights.add(c1);
//
//        QuestionWeights c2 = new QuestionWeights();
//        c2.setWeight(1);
//        c2.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c2);
//
//        QuestionWeights c3 = new QuestionWeights();
//        c3.setWeight(1);
//        c3.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c3);
//
//        QuestionWeights c4 = new QuestionWeights();
//        c4.setWeight(1);
//        c4.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c4);
//
//        QuestionWeights c5 = new QuestionWeights();
//        c5.setWeight(1);
//        c5.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c5);
//
//        QuestionWeights c6 = new QuestionWeights();
//        c6.setWeight(1);
//        c6.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c6);
//
//        currData.setMetaData(qweights);
//        return currData;
//    }
//
//
//    public static QuestionMetaData QuestionDEPLOY() {
//        QuestionMetaData currData = new QuestionMetaData();
//        currData.setId("DEPLOY");
//        currData.setMinimum(5);
//        currData.setAspect("Deployment Complexity");
//        ArrayList<QuestionWeights> qweights = new ArrayList<>();
//
//        QuestionWeights c1 = new QuestionWeights();
//        c1.setWeight(1);
//        c1.setRank(QuestionWeights.QuestionRank.UNKNOWN);
//        qweights.add(c1);
//
//        QuestionWeights c2 = new QuestionWeights();
//        c2.setWeight(1);
//        c2.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c2);
//
//        QuestionWeights c3 = new QuestionWeights();
//        c3.setWeight(1);
//        c3.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c3);
//
//        QuestionWeights c4 = new QuestionWeights();
//        c4.setWeight(1);
//        c4.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c4);
//
//        QuestionWeights c5 = new QuestionWeights();
//        c5.setWeight(1);
//        c5.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c5);
//
//        QuestionWeights c6 = new QuestionWeights();
//        c6.setWeight(1);
//        c6.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c6);
//
//        currData.setMetaData(qweights);
//        return currData;
//    }
//
//
//    public static QuestionMetaData QuestionTEST() {
//        QuestionMetaData currData = new QuestionMetaData();
//        currData.setId("TEST");
//        currData.setMinimum(3);
//        currData.setAspect("Application Testing");
//        ArrayList<QuestionWeights> qweights = new ArrayList<>();
//
//        QuestionWeights c1 = new QuestionWeights();
//        c1.setWeight(1);
//        c1.setRank(QuestionWeights.QuestionRank.UNKNOWN);
//        qweights.add(c1);
//
//        QuestionWeights c2 = new QuestionWeights();
//        c2.setWeight(1);
//        c2.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c2);
//
//        QuestionWeights c3 = new QuestionWeights();
//        c3.setWeight(1);
//        c3.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c3);
//
//        QuestionWeights c4 = new QuestionWeights();
//        c4.setWeight(1);
//        c4.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c4);
//
//        QuestionWeights c5 = new QuestionWeights();
//        c5.setWeight(1);
//        c5.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c5);
//
//        QuestionWeights c6 = new QuestionWeights();
//        c6.setWeight(1);
//        c6.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c6);
//
//        currData.setMetaData(qweights);
//        return currData;
//    }
//
//    public static QuestionMetaData QuestionSECURITY() {
//        QuestionMetaData currData = new QuestionMetaData();
//        currData.setId("SECURITY");
//        currData.setMinimum(3);
//        currData.setAspect("Application Security");
//        ArrayList<QuestionWeights> qweights = new ArrayList<>();
//
//        QuestionWeights c1 = new QuestionWeights();
//        c1.setWeight(1);
//        c1.setRank(QuestionWeights.QuestionRank.UNKNOWN);
//        qweights.add(c1);
//
//        QuestionWeights c2 = new QuestionWeights();
//        c2.setWeight(1);
//        c2.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c2);
//
//        QuestionWeights c3 = new QuestionWeights();
//        c3.setWeight(1);
//        c3.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c3);
//
//        QuestionWeights c4 = new QuestionWeights();
//        c4.setWeight(1);
//        c4.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c4);
//
//        QuestionWeights c5 = new QuestionWeights();
//        c5.setWeight(1);
//        c5.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c5);
//
//        QuestionWeights c6 = new QuestionWeights();
//        c6.setWeight(1);
//        c6.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c6);
//
//        currData.setMetaData(qweights);
//        return currData;
//    }
//
//    public static QuestionMetaData QuestionCONFIG() {
//        QuestionMetaData currData = new QuestionMetaData();
//        currData.setId("CONFIG");
//        currData.setMinimum(3);
//        currData.setAspect("Application Configuration");
//        ArrayList<QuestionWeights> qweights = new ArrayList<>();
//
//        QuestionWeights c1 = new QuestionWeights();
//        c1.setWeight(1);
//        c1.setRank(QuestionWeights.QuestionRank.UNKNOWN);
//        qweights.add(c1);
//
//        QuestionWeights c2 = new QuestionWeights();
//        c2.setWeight(1);
//        c2.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c2);
//
//        QuestionWeights c3 = new QuestionWeights();
//        c3.setWeight(1);
//        c3.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c3);
//
//        QuestionWeights c4 = new QuestionWeights();
//        c4.setWeight(1);
//        c4.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c4);
//
//        QuestionWeights c5 = new QuestionWeights();
//        c5.setWeight(1);
//        c5.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c5);
//
//        QuestionWeights c6 = new QuestionWeights();
//        c6.setWeight(1);
//        c6.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c6);
//
//        currData.setMetaData(qweights);
//        return currData;
//    }
//
//    public static QuestionMetaData QuestionCLUSTER() {
//        QuestionMetaData currData = new QuestionMetaData();
//        currData.setId("CLUSTER");
//        currData.setMinimum(3);
//        currData.setAspect("Clustering");
//        ArrayList<QuestionWeights> qweights = new ArrayList<>();
//
//        QuestionWeights c1 = new QuestionWeights();
//        c1.setWeight(1);
//        c1.setRank(QuestionWeights.QuestionRank.UNKNOWN);
//        qweights.add(c1);
//
//        QuestionWeights c2 = new QuestionWeights();
//        c2.setWeight(1);
//        c2.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c2);
//
//        QuestionWeights c3 = new QuestionWeights();
//        c3.setWeight(1);
//        c3.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c3);
//
//        QuestionWeights c4 = new QuestionWeights();
//        c4.setWeight(1);
//        c4.setRank(QuestionWeights.QuestionRank.AMBER);
//        qweights.add(c4);
//
//        QuestionWeights c5 = new QuestionWeights();
//        c5.setWeight(1);
//        c5.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c5);
//
//        QuestionWeights c6 = new QuestionWeights();
//        c6.setWeight(1);
//        c6.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c6);
//
//        currData.setMetaData(qweights);
//        return currData;
//    }
//
//    public static QuestionMetaData QuestionCONTAINERS() {
//        QuestionMetaData currData = new QuestionMetaData();
//        currData.setId("CONTAINERS");
//        currData.setMinimum(2);
//        currData.setAspect("Existing containerisation");
//        ArrayList<QuestionWeights> qweights = new ArrayList<>();
//
//        QuestionWeights c1 = new QuestionWeights();
//        c1.setWeight(1);
//        c1.setRank(QuestionWeights.QuestionRank.UNKNOWN);
//        qweights.add(c1);
//
//        QuestionWeights c2 = new QuestionWeights();
//        c2.setWeight(1);
//        c2.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c2);
//
//        QuestionWeights c3 = new QuestionWeights();
//        c3.setWeight(1);
//        c3.setRank(QuestionWeights.QuestionRank.RED);
//        qweights.add(c3);
//
//        QuestionWeights c4 = new QuestionWeights();
//        c4.setWeight(1);
//        c4.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c4);
//
//        QuestionWeights c5 = new QuestionWeights();
//        c5.setWeight(1);
//        c5.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c5);
//
//        QuestionWeights c6 = new QuestionWeights();
//        c6.setWeight(1);
//        c6.setRank(QuestionWeights.QuestionRank.GREEN);
//        qweights.add(c6);
//
//        currData.setMetaData(qweights);
//        return currData;
//    }

}
