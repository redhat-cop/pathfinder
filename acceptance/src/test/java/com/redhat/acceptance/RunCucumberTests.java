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

package com.redhat.acceptance;

import org.junit.Test;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
//@Cucumber.Options(format = {/*"html:target/cucumber-html-report", */"json-pretty:target/cucumber-json-report.json"})
//@Cucumber.Options(features={"src/test/resources/cucumber"}, glue={"cucumber.com.zzz.yyy.steps"}, format={"html:target/test-reports"}, )
//format = {"pretty", /*"html:target/cucumber",*/ "json:target/cucumber.json"}//, "rerun:target/rerun.txt"}

@CucumberOptions(
    monochrome = false, 
    format = {"pretty", "html:target/cucumber", "json:target/cucumber.json"}//, "rerun:target/rerun.txt"}
    )
public class RunCucumberTests {
  @Test
  public void runAcceptanceTests(){}
}

