var defaultThemeColors = Survey
    .StylesManager
    .ThemeColors["default"];

defaultThemeColors["$main-color"] = "#a30000";
defaultThemeColors["$main-hover-color"] = "#820000";
defaultThemeColors["$text-color"] = "#4a4a4a";
defaultThemeColors["$header-color"] = "#ffffff";
defaultThemeColors["$header-background-color"] = "#cc0000";
defaultThemeColors["$body-container-background-color"] = "#f8f8f8";
defaultThemeColors["$error-color"]="#a30000";
defaultThemeColors["$border-color"]="#cc0000";

Survey
    .StylesManager
    .applyTheme();

Survey.requiredText = "AA";

Survey.ChoicesRestfull.onBeforeSendRequest = function(sender, options) {
        options.request.setRequestHeader("Content-Type", "application/json");
};

var json = {
    title: "Application Assessment",
    sendResultOnPageNext: "true",
     requiredText: "",
   showProgressBar: "bottom",
    pages: [{
        "title": "Customer Details",
        "questions": [
            {
                "type": "dropdown",
                "name": "CUSTNAME",
                "title": "Select the Customer...",
                "isRequired": true,
                "choicesByUrl": {
                      "url": "http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com/api/pathfinder/customers/",
                      //"url": "http://localhost:8080/api/pathfinder/customers/",
                      "valueName": "CustomerId",
                      "titleName": "CustomerName"
                }
            }
        ]
    },
    {
        "title": "Application Details",
        "questions": [
            {
                "type": "dropdown",
                "name": "ASSMENTNAME",
                "title": "Select the application to be assessed....",
                "isRequired": true,
                "choicesByUrl": {
                        // Ignore the URL this will be replaced by the event handler
                        //url: "http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com/api/pathfinder/",
                        //"url": "http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com/api/pathfinder/customers/"+survey.data.CUSTNAME+"/applications/",
                        "valueName": "Id",
                        "titleName": "Name"
                }
            },
            {
                "type": "rating",
                "name": "BUSPRIORITY",
                "title": "Whats the level of business criticality of this application?",
                "rateMin": 1,
                "rateMax": 10,
                "rateStep": 1,
                "minRateDescription": "End of Life",
                "maxRateDescription": "Core Business Critical"
            },
            {
                "type": "radiogroup",
                "name": "OWNER",
                "title": "Level of Ownership",
                "comment": "Does the app team understand and actively develop the application ?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-AMBER|Application developed by 3rd party or COTS application ", "2-AMBER|In maintenance mode, no app SME knowledge, EOL imminent", "3-AMBER|Maintenance mode, SME knowledge available", "4-GREEN|Actively developed, SME knowledge available", "5-GREEN|New Greenfield application"]
            },
            {
                "type": "radiogroup",
                "name": "COMPLIANCE",
                "title": "Compliance",
                "comment": "Does the application have any legal compliance requirements e.g. PCI, HIPPA etc Does the application have any licensing requirements e.g. per core licensing",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-AMBER|High compliance requirements - both Legal and licensing", "2-AMBER|Licensing compliance -  licensing servers", "3-AMBER|Legal compliance - distinct hardware, isolated clusters, compliance certification","4-GREEN| None"]
            }
        ]
    }, {
        "title": "Application Dependencies",
        "questions": [
            {
                "type": "radiogroup",
                "name": "ARCHTYPE",
                "comment": "Does the app architecture suitable for containerisation e.g. would a VM be better approach",
                "title": "Architectural Suitability",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-AMBER|Massive Monolith (high memory, high CPU) singleton deployment", "2-AMBER|Massive Monolith (high memory, high CPU) , non singleton, difficult to scale", "3-AMBER|Complex Monolith -  strict runtime dependency startup order, non resilient architecture", "4-GREEN|Modern resilient monolith e.g. retries, circuit breaker etc", "5-GREEN|Independently deployable microservices"]
            },
            {
                "type": "radiogroup",
                "name": "DEPSHW",
                "title": "Hardware",
                "comment": "Does the application require specific hardware features to run on",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Non X86 CPU requirements", "2-RED|Custom or legacy hardware required", "3-GREEN|GPU, specific worker node hardware requirements", "4-GREEN|X86 CPU architecture based"]
            },
            {
                "type": "radiogroup",
                "name": "DEPSOS",
                "title": "Operating System",
                "comment": "Does the application require specific hardware features to run on",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Non-supported OS - OSX,  AIX, UNIX, SOLARIS", "2-RED|Linux with custom kernel drivers or specific kernel version", "3-AMBER|Linux with custom capabilities e.g. setcomp", "4-AMBER|Standard Linux - root access required", "5-GREEN|Standard Linux - no root access required"]
            },
            {
                "type": "radiogroup",
                "name": "DEPS3RD",
                "title": "3rd party vendor components/systems",
                "comment": "How dependent is the application on other systems and their failure modes",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Not supported/recommended to run on containers or EOL", "2-RED|Containers not supported by vendor", "3-AMBER|Supported but with limited functionality", "4-AMBER|Supported but relies on self built images", "5-GREEN|Fully supported, certified images available"]
            },
            {
                "type": "radiogroup",
                "name": "DEPSIN",
                "title": "Dependencies - (Incoming/Northbound)",
                "comment": "How dependent are other systems on this application and how easy are they to change",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Difficult to change dependent systems - legacy, 3rd party, external", "2-AMBER|Many dependent systems, possible to change but expensive and time consuming", "3-AMBER|Many dependent systems, possible to change as internally managed", "4-GREEN|Internal dependencies only", "5-GREEN|No dependent systems"]
            },
            {
                "type": "radiogroup",
                "name": "DEPSOUT",
                "title": "Dependencies - (Outgoing/Southbound)",
                "comment": "How dependent are other systems on this application and how easy are they to change",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Availability only verified when processing traffic", "2-AMBER|Complex strict startup order required", "3-AMBER|Application not ready until dependencies are available ", "4-GREEN|Limited processing available if dependencies are unavailable", "5-GREEN|No dependencies"]
            },
            {
                "type": "checkbox",
                "name": "DEPSOUTLIST",
                "title": "Please add southbound dependencies...",
                "isRequired": false,
                "choicesByUrl": {
                    // Ignore the URL this will be replaced by the event handler
//                    "url": "http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com/api/pathfinder/customers/12345/applications/"
                }
            }
        ]
    }, {
        "title": "Application Architecture",
        "questions": [{
                "type": "radiogroup",
                "name": "RESILIENCY",
                "title": "Application resiliency",
                "comment": "How resilient is the application and how well does it recover from outages",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Application errors when southbound dependencies unavailable and doesn't recover", "2-AMBER|Application errors when dependency unavailable but recovers once dependency is available", "3-AMBER|Application functionality limited when dependency is unavailable but recovers once dependency is available", "4-GREEN|Application employs resilient architecture patterns e.g. circuit breaker, retries etc ", "5-GREEN|Chaos Engineering principlals followed, application containers randomly terminated to test resiliency"]
            },
            {
                "type": "radiogroup",
                "name": "COMMS",
                "title": "Communication",
                "comment": "How does the app communicate with the external world",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Non-IP protocols e.g. serial, IPX, AppleTalk", "2-RED|IP based - hostname/ip encapsulated in payload", "3-AMBER|Traffic with no host addressing e.g. SSH ", "4-GREEN|Traffic with host addressing embedded in SSL SNI header ", "5-GREEN|HTTP/HTTPS"]
            },
            {
                "type": "radiogroup",
                "name": "STATE",
                "title": "State Management",
                "comment": "Does the application have any state requirements ?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-AMBER|Shared memory between applications", "2-AMBER|Managed/Coordinated externally from application e.g. external Zookeeper", "3-AMBER|State maintained in OSE based application requiring non shared, non ephemeral storage", "4-GREEN|Shared disk between application instances ", "5-GREEN|Stateless/Ephemeral container storage"]
            },
            {
                "type": "radiogroup",
                "name": "HA",
                "title": "Discovery",
                "comment": "Does the application have any unusual concerns around service discovery?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Uses Discovery technologies that are not k8s suitable e.g. hardcoded ip addresses, custom cluster manager", "2-RED|Application needs restart on cluster changes", "3-AMBER|Service discovery layered ontop of k8s e.g. hashicorp consul, netflix eureka", "4-GREEN|Standard k8s DNS name resolution, application resilient to cluster changes "]
            },
            {
                "type": "radiogroup",
                "name": "CLUSTER",
                "title": "Application Clustering",
                "comment": "How is the application clustered ?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Application cluster mechanism tightly coupled to application design & implementation", "2-RED|Application cluster provided by common runtime dependencies of application binaries", "3-AMBER|Application clustering mostly provided by application runtime platform, with application integrating with these mechanisms through runtime dependencies", "4-GREEN|Loosely coupled bespoke clustering solution", "5-GREEN|Memberless and stateless runtimes"]
            },
            {
                "type": "radiogroup",
                "name": "PROFILE",
                "title": "Profile",
                "comment": "What does the runtime profile of the application look like ?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|High CPU,Storage,IO, deterministic real time execution requirements", "2-RED|Latency sensitive applications e.g. voice", "3-AMBER|High burstable memory/cpu needs", "4-GREEN|Controlled burstable memory/cpu needs", "5-GREEN|Deterministic production profile"]
            }
        ]
    }, {
        "title": "Application Observability",
        "questions": [{
                "type": "radiogroup",
                "name": "LOGS",
                "title": "Application Logging",
                "comment": "How easy is it to determine how the application is performing and how to get information from it.",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|No logging available, internal only with no export mechanism", "2-RED|Custom binary logs exposed using non-standard protocols", "3-AMBER|Logs exposed via syslog", "4-GREEN|Log entries written to filesystem", "5-GREEN|Configurable logging can be sent to STDOUT "]
            },
            {
                "type": "radiogroup",
                "name": "METRICS",
                "title": "Application Metrics",
                "comment": "How easy is it to determine how the application is performing and how to get information from it.",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-AMBER|No exposed metrics", "2-AMBER|Internal metrics but not exposed", "3-AMBER|Metrics exposed via binary protocols e.g. SNMP", "4-GREEN|3rd party metrics solution e.g. dynatrace, app-dynamics etc", "5-GREEN|Prometheus, native k8s metrics, integration with autoscalers"]
            },
            {
                "type": "radiogroup",
                "name": "HEALTH",
                "title": "Application Health",
                "comment": "How easy is it to determine how the application is performing and how to get information from it.",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|No health or readyiness probes available", "2-RED|Custom watchdog process monitoring and managing the application", "3-AMBER|Basic application health requires semi-complex scripting", "4-GREEN|Scriptable liveness and readyiness probes", "5-GREEN|Probes execute synthetic transactions to verify application health"]
            },
            {
                "type": "radiogroup",
                "name": "CONFIG",
                "title": "Application Configuration",
                "comment": "How is the application configured ?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Compiled/Patched into application at installation time", "2-RED|Externally stored and loaded using specific key e.g. hostname, ip address", "3-AMBER|Configuration baked into application and enabled via system property at runtime", "4-GREEN|Configuration files loaded from shared disk", "5-GREEN|Configuration files loaded by application from mounted files, environment variables"]
            }
        ]
    }, {
        "title": "Application Cross-Cutting concerns",
        "questions": [{
                "type": "radiogroup",
                "name": "TEST",
                "title": "Application Testing",
                "comment": "What kind of testing does the application undergo ?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-AMBER|Manual testing only", "2-AMBER|Minimal automated testing, UI focused only ", "3-AMBER|Automated unit & regression testing, basic CI pipelines", "4-GREEN|Highly repeatable automated testing - Unit, Integration, smoke tests before production deployment, modern test practices followed", "5-GREEN|Chaos Engineering principlals followed. Testing in production e.g. A/B, experimentation"]
            },
            {
                "type": "radiogroup",
                "name": "SECURITY",
                "title": "Application Security",
                "comment": "How is the application secured ?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|HSM, hardware based encryption devices", "2-RED|Certs, Keys bound to application IP addresses, generated at runtime per application instance", "3-AMBER|Keys/Certs compiled into application", "4-GREEN|Certificates/Keys loaded via shared disk", "5-GREEN|Certificates/Keys loaded via files"]
            },
            {
                "type": "radiogroup",
                "name": "DEPLOY",
                "title": "Deployment Complexity",
                "comment": "Does the application have any unusual concerns around clustering or service discovery?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Manual documented steps", "2-RED|Manual documented steps, some basic automation", "3-AMBER|Simple automated deployment scripts", "4-AMBER|Automated deployment, but manual, slow, promotion through stages", "5-GREEN|Full CD Pipeline in place, promoting Applications through the stages;  B/G + Canary capable"]
            },
            {
                "type": "radiogroup",
                "name": "CONTAINERS",
                "title": "Containerisation Process",
                "comment": "Is the application already available as a container image? How well suited to cloud native is the existing containerisation.",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Desktop-led container implementation designed to support running app on a laptop. Container treated like a VM with multiple services", "2-RED|Use of a init process within the container to manage multiple container processes that run independently but are tightly integrated", "3-GREEN|Running process id agnostic", "4-GREEN|High overhead health checks", "5-GREEN|Fast graceful shutdown behaviour"]
            },
            {
                "type": "comment",
                "name": "NOTES",
                "title": "Additional notes or comments"
            }
        ]
    }],
    completedHtml: "<p><h4>Thank you for completing the Pathfinder Assessment.  Please click <a id='returnlink' href='/pathfinder-ui/assessments.jsp?customerId='>Here</a> to return to the main page."
};

if (undefined!=document.getElementById("returnlink")){
  document.getElementById("returnlink").href=document.getElementById("returnlink").href+Utils.getParameterByName("customerId");
}


window.survey = new Survey.Model(json);

//console.log(Utils.getParameterByName("customerId"));
//console.log(Utils.getParameterByName("applicationId"));

//survey.data={"CUSTNAME":Utils.getParameterByName("customerId")};

// ### this pre-selects the customer
if (undefined!=Utils.getParameterByName("customerId") && undefined!=Utils.getParameterByName("applicationId")){
	survey.data={"CUSTNAME":Utils.getParameterByName("customerId"), "ASSMENTNAME":Utils.getParameterByName("applicationId")};
}

//survey.data={"CUSTNAME":"Vodatel"};

survey
    .onComplete
    .add(function (result) {
            var xmlhttp = new XMLHttpRequest();
            tmpResult = result.data;
            var cust = tmpResult.CUSTNAME;
            var assm = tmpResult.ASSMENTNAME;
            //dependencies array needs special handling
            var tmpDEPSOUTLIST = tmpResult.DEPSOUTLIST;
            delete tmpResult.DEPSOUTLIST;
            xmlhttp.open("POST", Utils.SERVER+"/api/pathfinder/customers/"+cust+"/applications/"+assm+"/assessments");
            xmlhttp.setRequestHeader("Content-Type", "application/json");
            myObj = { "payload": tmpResult,"deps":tmpDEPSOUTLIST, "datetime":new Date()};
            xmlhttp.send(JSON.stringify(myObj));
    });

survey
//    .onPartialSend
		.onAfterRenderPage
//		.onLoadChoicesFromServer
    .add(function (result) {
    		console.log("FIRED!");
    		
    		//console.log("result="+JSON.stringify(result));
    		
//        var c = survey.getQuestionByName('CUSTNAME');
//        c.choicesByUrl.url = Utils.SERVER+"/api/pathfinder/customers/";
//        c.choicesByUrl.valueName = "CustomerId";
//        c.choicesByUrl.titleName = "CustomerName";
//        c.choicesByUrl.run();
//        
//        //var q = survey.getQuestionByName('CUSTNAME');
//        var tmp = result.data.CUSTNAME;
        //c.value="b49ab128-261e-4621-81ad-f7429de45ea7";
        //result.data.CUSTNAME="b49ab128-261e-4621-81ad-f7429de45ea7";
        
        if (result.data.CUSTNAME!=undefined){
	        var q = survey.getQuestionByName('ASSMENTNAME');
	        q.choicesByUrl.url = Utils.SERVER+"/api/pathfinder/customers/"+result.data.CUSTNAME+"/applications/";
	        q.choicesByUrl.valueName = "Id";
	        q.choicesByUrl.titleName = "Name";
	        q.choicesByUrl.run();
	        // ### this pre-selects the application
	        if (undefined!=Utils.getParameterByName("customerId") && undefined!=Utils.getParameterByName("applicationId")){
	        	survey.data={"CUSTNAME":Utils.getParameterByName("customerId"), "ASSMENTNAME":Utils.getParameterByName("applicationId")};
	        }
	        q.disabled=true;
				}
				
        if (result.data.CUSTNAME!=undefined){
		      var v = survey.getQuestionByName('DEPSOUTLIST');
	        v.choicesByUrl.url = Utils.SERVER+"/api/pathfinder/customers/"+result.data.CUSTNAME+"/applications/";
	        v.choicesByUrl.valueName = "Id";
	        v.choicesByUrl.titleName = "Name";
	        v.choicesByUrl.run();
	      }
    });


//survey.data = {
//    "CUSTNAME" : "5aa6c58e23911200015454d0"
////    "CUSTNAME" : "StaticTel"
//};

$("#surveyElement").Survey({
    model: survey
});
