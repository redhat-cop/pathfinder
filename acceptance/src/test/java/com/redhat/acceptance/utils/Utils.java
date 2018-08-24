/*
* JBoss, Home of Professional Open Source
* Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
* contributors by the @authors tag. See the copyright.txt in the
* distribution for a full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.redhat.acceptance.utils;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class Utils {
  private static final Logger log=LoggerFactory.getLogger(Utils.class);
  
  public static boolean beforeScenarios(){
//    boolean successfulRuleDeployment=Wait.For(180, new ToHappen() {
//      public boolean hasHappened() {
//        String username=System.getProperty("bpms.username")!=null?System.getProperty("bpms.username"):"admin";
//        String password=System.getProperty("bpms.password")!=null?System.getProperty("bpms.password"):"admin";
//        String serverUrl=System.getProperty("bpms.url")!=null?System.getProperty("bpms.url"):"http://localhost:16080/business-central";
//        Preconditions.checkArgument(username!=null, "bpms.username cannot be null");
//        Preconditions.checkArgument(password!=null, "bpms.password cannot be null");
//        Preconditions.checkArgument(serverUrl!=null, "bpms.base.url cannot be null");
//        String response=given().when().auth().preemptive().basic(username, password).get(serverUrl+"/rest/deployment").asString();
//        boolean result=response.contains("business-rules");
//        if (result)
//          log.debug("Deployment found ["+response+"]");
//        return result;
//      }
//    }, "Unable to find deployed rules");
//    assertEquals("Rules were not deployed", true, successfulRuleDeployment);
    return true;
  }
}
