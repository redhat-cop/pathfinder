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
        options.request.setRequestHeader("Content-Type", "application/javascript");
        //options.request.setRequestHeader("Authorization", "Bearer "+jwtToken);
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
                      "url": "SERVER_URL/api/pathfinder/customers/?_t=JWT_TOKEN",
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
                "name": "DEVOWNER",
                "title": "Development Level of Ownership",
                "comment": "Does the app team understand and actively develop the application to be containerised ?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|External 3rd party or COTS application ", "2-RED|In maintenance mode, no app SME knowledge, poor documentation", "3-AMBER|Maintenance mode, SME knowledge available", "4-GREEN|Actively developed, SME knowledge available", "5-GREEN|New Greenfield application"]
            },
            {
                "type": "radiogroup",
                "name": "OPSOWNER",
                "title": "Operations Level of Ownership",
                "comment": "How is the application to be containerised supported in Production?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Application production support outsourced to 3rd party support provider. No inhouse resources.", "2-RED|Application production support provided by separate internal team, little interaction with development team.", "3-AMBER|Multiple teams support the application using an established escalation model", "4-GREEN|SRE based approach with knowledgable and experienced operations team", "5-GREEN|Pure DevOps model, the team that builds it runs it in Production"]
            },
            {
                "type": "radiogroup",
                "name": "LEADTIME",
                "title": "Lead Time",
                "comment": "How long does it take from when code is committed to it being deployed to production?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|More than six months", "2-AMBER|Between one month and six months", "3-GREEN|Between one week and one month", "4-GREEN|Between one day and one week", "5-GREEN|Less than one day"]
            },
            {
                "type": "radiogroup",
                "name": "DEPLOYFREQ",
                "title": "Deployment frequency",
                "comment": "How often do you deploy software to production?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Greater than once every six months", "2-AMBER|Between once per month and once every six months", "3-GREEN|Weekly deployments", "4-GREEN|Daily deployments", "5-GREEN|On demand (multiple deploys per day)"]
            },
            {
                "type": "radiogroup",
                "name": "MTTR",
                "title": "Time to restore",
                "comment": "What is your Mean Time to Recover (MTTR) when a fault is found in production?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|One month or greater", "2-AMBER|Between one week and one month", "3-AMBER|Between one day and one week", "4-GREEN|Between one hour and one day", "5-GREEN|Immediately"]
            },
            {
                "type": "radiogroup",
                "name": "COMPLIANCE",
                "title": "Compliance",
                "comment": "Does the application have any legal compliance requirements e.g. PCI, HIPPA etc Does the application have any licensing requirements e.g. per core licensing",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|High compliance requirements - both Legal and licensing", "2-RED|Licensing compliance - licensing servers", "3-AMBER|Legal compliance - distinct hardware, isolated clusters, compliance certification","4-GREEN| None"]
            }
        ]
    }, {
        "title": "Application Dependencies",
        "questions": [
            {
                "type": "radiogroup",
                "name": "ARCHTYPE",
                "comment": "Is the application architecture suitable for containerisation e.g. would a VM be better approach",
                "title": "Architectural Suitability",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-AMBER|Massive Monolith (high memory, high CPU) singleton deployment", "2-AMBER|Massive Monolith (high memory, high CPU) , non singleton, difficult to scale", "3-AMBER|Complex Monolith -  strict runtime dependency startup order, non resilient architecture", "4-GREEN|Modern resilient monolith e.g. retries, circuit breaker etc", "5-GREEN|Independently deployable services"]
            },
            {
                "type": "radiogroup",
                "name": "DEPSHW",
                "title": "Hardware",
                "comment": "Does the application require specific hardware features to run",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Non X86 CPU requirements", "2-RED|Custom or legacy hardware required", "3-GREEN|GPU, specific worker node hardware requirements", "4-GREEN|X86 CPU architecture based"]
            },
            {
                "type": "radiogroup",
                "name": "DEPSOS",
                "title": "Operating System",
                "comment": "What operating system does the application run on",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Non-supported OS - OSX,  AIX, UNIX, SOLARIS", "2-RED|Linux with custom kernel drivers or specific kernel version", "3-AMBER|Linux with custom capabilities e.g. setcomp", "4-AMBER|Standard Linux - root access required", "5-GREEN|Standard Linux - no root access required"]
            },
            {
                "type": "radiogroup",
                "name": "DEPS3RD",
                "title": "3rd party vendor components",
                "comment": "Are 3rd party components supported in containers?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Not recommended to run component in containers", "2-RED|Component not supported by vendor when running in a container", "3-AMBER|Supported but with restricted functionality/untested", "4-AMBER|Supported but relies on self built images", "5-GREEN|Fully vendor supported, certified images available"]
            },
            {
                "type": "radiogroup",
                "name": "DEPSIN",
                "title": "Dependencies - (Incoming/Northbound)",
                "comment": "How dependent are other systems on this application and how easy are they to change",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Difficult/Expensive to change dependent systems - legacy, 3rd party, external", "2-AMBER|Many dependent systems, possible to change but expensive and time consuming", "3-GREEN|Many dependent systems, possible to change as they're internally managed", "4-GREEN|Internal dependencies only", "5-GREEN|No dependent systems"]
            },
            {
                "type": "radiogroup",
                "name": "DEPSOUT",
                "title": "Dependencies - (Outgoing/Southbound)",
                "comment": "How dependent is this application on other systems",
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
                "comment": "How resilient is the application and how well does it recover from outages/restarts",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Application cannot be restarted cleanly and requires manual intervention", "2-RED|Application errors when southbound dependencies are unavailable and doesn't recover automatically", "3-AMBER|Application functionality limited when dependency is unavailable but recovers once dependency is available", "4-GREEN|Application employs resilient architecture patterns e.g. circuit breaker, retries etc ", "5-GREEN|Chaos Engineering principals followed, application containers instances randomly terminated to test resiliency"]
            },
            {
                "type": "radiogroup",
                "name": "COMMS",
                "title": "Communication",
                "comment": "How does the app communicate with the external world",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Non-IP protocols e.g. serial, IPX, AppleTalk", "2-RED|IP based - hostname/ip encapsulated in payload", "3-AMBER|TCP/UDP Traffic without host addressing e.g. SSH ", "4-GREEN|TCP/UDP encapsulated in SSL with SNI header", "5-GREEN|Web traffic HTTP/HTTPS"]
            },
            {
                "type": "radiogroup",
                "name": "STATE",
                "title": "State Management",
                "comment": "Does the application have any state requirements ?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-AMBER|Shared memory between applications", "2-AMBER|Managed/Coordinated externally from application e.g. external Zookeeper, data grid etc", "3-AMBER|State maintained in non shared, non ephemeral storage", "4-GREEN|Shared disk between application instances ", "5-GREEN|Stateless/Ephemeral container storage"]
            },
            {
                "type": "radiogroup",
                "name": "HA",
                "title": "Discovery",
                "comment": "Does the application have any unusual concerns around service discovery?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Uses proprietary discovery technologies that are not k8s suitable e.g. hardcoded ip addresses, custom cluster manager", "2-RED|Application requires restart on cluster changes to pickup new service instances", "3-AMBER|Service discovery compatible with k8s e.g. hashicorp consul, netflix eureka", "4-GREEN|Standard k8s DNS name resolution, application resilient to cluster changes "]
            },
            {
                "type": "radiogroup",
                "name": "CLUSTER",
                "title": "Application Clustering",
                "comment": "How is the application clustered ?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Manually configured clustering mechanism e.g. static clusters", "2-AMBER|Application clustering mostly provided by external off-PAAS cluster manager", "3-GREEN|Application clustering mostly provided by application runtime platform using a k8s suitable mechanism", "5-GREEN| No application clustering not required"]
            },
            {
                "type": "radiogroup",
                "name": "PROFILE",
                "title": "Profile",
                "comment": "What does the runtime profile of the application look like ?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Deterministic predictable real time execution requirements", "2-AMBER|Latency sensitive applications e.g. voice, HFT", "3-AMBER|High burstable memory/cpu needs", "4-GREEN|Controlled burstable memory/cpu needs", "5-GREEN|Predictable production profile"]
            }
        ]
    }, {
        "title": "Application Observability",
        "questions": [{
                "type": "radiogroup",
                "name": "LOGS",
                "title": "Application Logging",
                "comment": "Does the application use logging?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|No logging available, internal only with no export mechanism", "2-RED|Custom binary logs exposed using non-standard protocols", "3-AMBER|Logs exposed via syslog", "4-GREEN|Log entries written to filesystem", "5-GREEN|Configurable logging e.g. can be sent to STDOUT"]
            },
            {
                "type": "radiogroup",
                "name": "METRICS",
                "title": "Application Metrics",
                "comment": "Does the application provide metrics ?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-AMBER|No metrics exposed", "2-AMBER|Internal metrics but not exposed", "3-AMBER|Metrics exposed via binary protocols e.g. SNMP", "4-GREEN|3rd party metrics solution e.g. dynatrace, app-dynamics etc", "5-GREEN|Prometheus support, native k8s metrics, integration with autoscalers"]
            },
            {
                "type": "radiogroup",
                "name": "HEALTH",
                "title": "Application Health",
                "comment": "How easy is it to determine the application health and if it's ready to handle traffic",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|No health or readyiness query functionality available", "2-RED|Custom watchdog process monitoring and managing the application", "3-AMBER|Basic application health requires semi-complex scripting", "4-GREEN|Scriptable liveness and readyiness probes", "5-GREEN|Probes execute synthetic transactions to verify application health"]
            },
            {
                "type": "radiogroup",
                "name": "CONFIG",
                "title": "Application Configuration",
                "comment": "How is the application configured ?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Configuration compiled/patched into the application at installation time, application configured via user interface", "2-RED|Externally stored and accessed using specific environment key e.g. hostname, ip address", "3-AMBER|Multiple configuration files in multiple filesystem locations", "4-GREEN|Configuration baked into application and enabled via system property at runtime", "5-GREEN|Configuration loaded by application from container mounted files, environment variables"]
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
                "choices": ["0-UNKNOWN|Unknown","1-AMBER|Manual testing only", "2-AMBER|None, Minimal automated testing, UI focused only ", "3-AMBER|Automated unit & regression testing, basic CI pipelines", "4-GREEN|Highly repeatable automated testing - Unit, Integration, smoke tests before production deployment, modern test practices followed", "5-GREEN|Chaos Engineering principals followed. Constant testing in production e.g. A/B, experimentation"]
            },
            {
                "type": "radiogroup",
                "name": "SECURITY",
                "title": "Application sensitive data handling",
                "comment": "How is the application provided with sensitive data e.g. certificates/passwords etc?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|HSM, hardware based encryption devices", "2-RED|Certs, Keys bound to application IP addresses, generated at runtime per application instance", "3-AMBER|Keys/Certs compiled into application", "4-GREEN|Certificates/Keys loaded via shared disk", "5-GREEN|Certificates/Keys loaded via files or vault integration","5-GREEN|None"]
            },
            {
                "type": "radiogroup",
                "name": "DEPLOY",
                "title": "Deployment Complexity",
                "comment": "Does the application have any unusual concerns around clustering or service discovery?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Manual documented steps, user interface driven interaction", "2-RED|Manual documented steps, some basic automation", "3-AMBER|Simple automated deployment scripts", "4-AMBER|Automated deployment, but manual, complex, promotion through stages", "5-GREEN|Full CD Pipeline in place, promoting Applications through the stages;  B/G + Canary capable"]
            },
            {
                "type": "radiogroup",
                "name": "CONTAINERS",
                "title": "Containerisation Process",
                "comment": "Is the application already available as a container image? How well suited to cloud native is the existing containerisation.",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Desktop-led container implementation designed to support running application on a laptop. Container treated like a VM with multiple services", "2-RED|Use of a init process within the container to manage multiple container processes that run independently but are tightly integrated", "3-AMBER|Naive container definition", "4-GREEN|Fast graceful shutdown behaviour", "5-GREEN|Containerisation not attempted as yet"]
            },
            {
                "type": "comment",
                "name": "NOTES",
                "title": "Additional notes or comments"
            }
        ]
    }],
    completedHtml: "<p><h4>Thank you for completing the Pathfinder Assessment.  Please click <a id='surveyCompleteLink' href='/pathfinder-ui/assessments-v2.jsp?customerId={CUSTID}'>Here</a> to return to the main page."
};

//$('surveyCompleteLink').attr('href', '/pathfinder-ui/assessments-v2.jsp?customerId='+Utils.getParameterByName("customerId"));

window.survey = new Survey.Model(json);

//console.log(Utils.getParameterByName("customerId"));
//console.log(Utils.getParameterByName("applicationId"));

//survey.data={"CUSTNAME":Utils.getParameterByName("customerId")};

//survey.data.RETURN=Utils.getParameterByName("customerId");

// ### this pre-selects the customer
if (undefined!=Utils.getParameterByName("customerId") && undefined!=Utils.getParameterByName("applicationId")){
//  survey.data.CUSTNAME=Utils.getParameterByName("customerId");
//  survey.data.ASSMENTNAME=Utils.getParameterByName("applicationId");
  survey.data={"CUSTNAME":Utils.getParameterByName("customerId"), "ASSMENTNAME":Utils.getParameterByName("applicationId")};
}

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
            xmlhttp.open("POST", addAuthToken(Utils.SERVER+"/api/pathfinder/customers/"+cust+"/applications/"+assm+"/assessments"));
            xmlhttp.setRequestHeader("Content-Type", "application/json");
            myObj = { "payload": tmpResult,"deps":tmpDEPSOUTLIST, "datetime":new Date()};
            var payload=JSON.stringify(myObj);
            console.log("payload="+payload);
            xmlhttp.send(payload);
            
            console.log("CUST="+cust);
            console.log("link="+$('#surveyCompleteLink'));
            
            if (undefined!=$('#surveyCompleteLink')){
            	$('#surveyCompleteLink').attr('href', '/pathfinder-ui/assessments-v2.jsp?customerId='+Utils.getParameterByName("customerId"));
            }
    });

survey
//    .onPartialSend
		.onAfterRenderPage
//		.onLoadChoicesFromServer
    .add(function (result) {
    		//console.log("FIRED!");
    		
    		//if (undefined!=document.getElementById("returnlink")){
				//  document.getElementById("returnlink").href=document.getElementById("returnlink").href+Utils.getParameterByName("customerId");
				//}
//if (undefined!=Utils.getParameterByName("customerId") && undefined!=Utils.getParameterByName("applicationId")){
//  survey.data.CUSTNAME=Utils.getParameterByName("customerId");
//  survey.data.ASSMENTNAME=Utils.getParameterByName("applicationId");
//}    		
    		console.log("result="+JSON.stringify(survey.data));
    		
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
	        q.choicesByUrl.url = addAuthToken(Utils.SERVER+"/api/pathfinder/customers/"+result.data.CUSTNAME+"/applications/");
	        q.choicesByUrl.valueName = "Id";
	        q.choicesByUrl.titleName = "Name";
	        q.choicesByUrl.run();
	        // ### this pre-selects the application
	        if (undefined!=Utils.getParameterByName("customerId") && undefined!=Utils.getParameterByName("applicationId")){
	        	if (undefined==survey.data.ASSMENTNAME){
	        		survey.data={"CUSTNAME":Utils.getParameterByName("customerId"), "ASSMENTNAME":Utils.getParameterByName("applicationId")};
	        	}
	        	//survey.data.CUSTNAME=Utils.getParameterByName("customerId");
	        	//survey.data.ASSMENTNAME=Utils.getParameterByName("applicationId");
	        	//survey.data={"CUSTNAME":Utils.getParameterByName("customerId"), "ASSMENTNAME":Utils.getParameterByName("applicationId")};
	        }
	        q.disabled=true;
        }
				
        if (result.data.CUSTNAME!=undefined){
          result.data.CUSTID=result.data.CUSTNAME;
		      var v = survey.getQuestionByName('DEPSOUTLIST');
	        v.choicesByUrl.url = addAuthToken(Utils.SERVER+"/api/pathfinder/customers/"+result.data.CUSTNAME+"/applications/");
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
