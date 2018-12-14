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
        "title": "Application Details",
        "questions": [
            {
                "type": "radiogroup",
                "name": "DEVOWNER",
                "title": "Does the application development team understand and actively develop the application?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|External 3rd party or COTS application ", "2-RED|In maintenance mode, no app SME knowledge, poor documentation", "3-AMBER|Maintenance mode, SME knowledge available", "4-GREEN|Actively developed, SME knowledge available", "5-GREEN|New Greenfield application"]
            },
            {
                "type": "radiogroup",
                "name": "OPSOWNER",
                "title": "How is the application supported in Production?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Application production support outsourced to 3rd party support provider. Ticket driven escalation process, no inhouse support resources.", "2-RED|Production support provided by separate internal team, little interaction with development team.", "3-AMBER|Multiple teams support the application using an established escalation model", "4-GREEN|SRE based approach with knowledgeable and experienced operations team", "5-GREEN|Pure DevOps model, the team that builds it is responsible for running it in Production"]
            },
            {
                "type": "radiogroup",
                "name": "LEADTIME",
                "title": "How long does it take from when code is committed to it being deployed to production?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|More than six months", "2-AMBER|Between one month and six months", "3-GREEN|Between one week and one month", "4-GREEN|Between one day and one week", "5-GREEN|Less than one day"]
            },
            {
                "type": "radiogroup",
                "name": "DEPLOYFREQ",
                "title": "Deployment frequency",
                "comment": "How often is the application deployed to production?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Greater than once every six months", "2-AMBER|Between once per month and once every six months", "3-GREEN|Weekly deployments", "4-GREEN|Daily deployments", "5-GREEN|On demand (multiple deploys per day)"]
            },
            {
                "type": "radiogroup",
                "name": "MTTR",
                "title": "What is the Mean Time to Recover (MTTR) when a fault is found with the application in production?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|One month or greater", "2-AMBER|Between one week and one month", "3-AMBER|Between one day and one week", "4-GREEN|Between one hour and one day", "5-GREEN|Immediately"]
            },
            {
                "type": "radiogroup",
                "name": "COMPLIANCE",
                "title": "Does the application have any legal compliance requirements e.g. PCI, HIPPA etc. Does the application have any licensing requirements e.g. per core licensing",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|High compliance requirements - both Legal and licensing", "2-RED|Licensing compliance - licensing servers", "3-AMBER|Legal compliance - distinct hardware, isolated clusters, compliance certification","4-GREEN| None"]
            },
            {
                "type": "radiogroup",
                "name": "ARCHTYPE",
                "title": "Is the application architecture suitable for containerisation?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-AMBER|Massive Monolith (high memory, high CPU) singleton deployment", "2-AMBER|Massive Monolith (high memory, high CPU) , non singleton, difficult to scale", "3-AMBER|Complex Monolith -  strict runtime dependency startup order, non resilient architecture", "4-GREEN|Modern resilient monolith e.g. retries, circuit breaker etc", "5-GREEN|Independently deployable services"]
            }
        ]
    }, {
        "title": "Application Dependencies",
        "questions": [
            {
                "type": "radiogroup",
                "name": "DEPSHW",
                "title": "Does the application require specific hardware features to run?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Non X86 CPU requirements", "2-RED|Custom or legacy hardware required", "3-GREEN|GPU, specific worker node hardware requirements", "4-GREEN|X86 CPU architecture based"]
            },
            {
                "type": "radiogroup",
                "name": "DEPSOS",
                "title": "What operating system does the application run on",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Non-supported OS - OSX,  AIX, UNIX, SOLARIS", "2-RED|Linux with custom kernel drivers or specific kernel version", "3-AMBER|Linux with custom capabilities e.g. setcomp", "4-AMBER|Standard Linux - root access required", "5-GREEN|Standard Linux - no root access required"]
            },
            {
                "type": "radiogroup",
                "name": "DEPS3RD",
                "title": "Are 3rd party components supported in containers?",
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
                "name": "DEPSINLIST",
                "type": "tagbox",
                "renderAs": "select2",
                "title": "Please add northbound dependencies...",
                "visibleIf": "{DEPSIN} notcontains '5'",
                "isRequired": false,
                "colCount": 3,
                "choicesByUrl": {
                    // Ignore the URL this will be replaced by the event handler
//                    "url": "http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com/api/pathfinder/customers/12345/applications/"
                }
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
                "type": "tagbox",
                "renderAs": "select2",
                "name": "DEPSOUTLIST",
                "title": "Please add southbound dependencies...",
                "visibleIf": "{DEPSOUT} notcontains '5'",
                "isRequired": false,
                "colCount": 3,
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
                "title": "How resilient is the application and how well does it recover from outages/restarts",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Application cannot be restarted cleanly and requires manual intervention", "2-RED|Application errors when southbound dependencies are unavailable and doesn't recover automatically", "3-AMBER|Application functionality limited when dependency is unavailable but recovers once dependency is available", "4-GREEN|Application employs resilient architecture patterns e.g. circuit breaker, retries etc ", "5-GREEN|Chaos Engineering principals followed, application containers instances randomly terminated to test resiliency"]
            },
            {
                "type": "radiogroup",
                "name": "COMMS",
                "title": "Communication",
                "comment": "How does the external world communicate with the application",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Non-IP protocols e.g. serial, IPX, AppleTalk", "2-RED|IP based - hostname/ip encapsulated in payload", "3-AMBER|TCP/UDP Traffic without host addressing e.g. SSH ", "4-GREEN|TCP/UDP encapsulated in SSL with SNI header", "5-GREEN|Web traffic HTTP/HTTPS"]
            },
            {
                "type": "radiogroup",
                "name": "STATE",
                "title": "How does the application handle internal state?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-AMBER|Shared memory between applications", "2-AMBER|Managed/Coordinated externally from application e.g. external Zookeeper, data grid etc", "3-AMBER|State maintained in non shared, non ephemeral storage", "4-GREEN|Shared disk between application instances ", "5-GREEN|Stateless/Ephemeral container storage"]
            },
            {
                "type": "radiogroup",
                "name": "HA",
                "title": "Does the application have any unusual concerns around service discovery?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Uses proprietary discovery technologies that are not k8s suitable e.g. hardcoded ip addresses, custom cluster manager", "2-RED|Application requires restart on cluster changes to pickup new service instances", "3-AMBER|Service discovery compatible with k8s e.g. hashicorp consul, netflix eureka", "4-GREEN|Standard k8s DNS name resolution, application resilient to cluster changes "]
            },
            {
                "type": "radiogroup",
                "name": "CLUSTER",
                "title": "How is the application clustered ?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Manually configured clustering mechanism e.g. static clusters", "2-AMBER|Application clustering mostly provided by external off-PAAS cluster manager", "3-GREEN|Application clustering mostly provided by application runtime platform using a k8s suitable mechanism", "5-GREEN| No application clustering not required"]
            }
        ]
    }, {
        "title": "Application Observability",
        "questions": [{
                "type": "radiogroup",
                "name": "LOGS",
                "title": "Does the application use logging?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|No logging available, internal only with no export mechanism", "2-RED|Custom binary logs exposed using non-standard protocols", "3-AMBER|Logs exposed via syslog", "4-GREEN|Log entries written to filesystem", "5-GREEN|Configurable logging e.g. can be sent to STDOUT"]
            },
            {
                "type": "radiogroup",
                "name": "METRICS",
                "title": "Does the application provide metrics ?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-AMBER|No metrics exposed", "2-AMBER|Internal metrics but not exposed", "3-AMBER|Metrics exposed via binary protocols e.g. SNMP", "4-GREEN|3rd party metrics solution e.g. dynatrace, app-dynamics etc", "5-GREEN|Prometheus support, native k8s metrics, integration with autoscalers"]
            },
            {
                "type": "radiogroup",
                "name": "HEALTH",
                "title": "How easy is it to determine the application health and if it's ready to handle traffic",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|No health or readyiness query functionality available", "2-RED|Custom watchdog process monitoring and managing the application", "3-AMBER|Basic application health requires semi-complex scripting", "4-GREEN|Scriptable liveness and readyiness probes", "5-GREEN|Probes execute synthetic transactions to verify application health"]
            },
            {
                "type": "radiogroup",
                "name": "PROFILE",
                "title": "What does the runtime profile of the application look like ?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Deterministic predictable real time execution requirements", "2-AMBER|Latency sensitive applications e.g. voice, HFT", "3-AMBER|High burstable memory/cpu needs", "4-GREEN|Controlled burstable memory/cpu needs", "5-GREEN|Predictable production profile"]
            }
        ]
    }, {
        "title": "Application Cross-Cutting concerns",
        "questions": [{
                "type": "radiogroup",
                "name": "TEST",
                "title": "What kind of testing does the application undergo?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|None, minimal manual testing only", "2-AMBER| Minimal automated testing, UI focused only ", "3-AMBER|Automated unit & regression testing, basic CI pipelines", "4-GREEN|Highly repeatable automated testing - Unit, Integration, smoke tests before production deployment, modern test practices followed", "5-GREEN|Chaos Engineering principals followed. Constant testing in production e.g. A/B, experimentation"]
            },
            {
                "type": "radiogroup",
                "name": "CONFIG",
                "title": "How is the application configured ?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Configuration compiled/patched into the application at installation time, application configured via user interface", "2-RED|Externally stored and accessed using specific environment key e.g. hostname, ip address", "3-AMBER|Multiple configuration files in multiple filesystem locations", "4-GREEN|Configuration baked into application and enabled via system property at runtime", "5-GREEN|Configuration loaded by application from container mounted files, environment variables"]
            },
            {
                "type": "radiogroup",
                "name": "SECURITY",
                "title": "How does the application acquire security credentials/certificates",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|HSM, hardware based encryption devices", "2-RED|Certs, Keys bound to application IP addresses, generated at runtime per application instance", "3-AMBER|Keys/Certs compiled into application", "4-GREEN|Certificates/Keys loaded via shared disk", "5-GREEN|Certificates/Keys loaded via files or vault integration","6-GREEN|None needed"]
            },
            {
                "type": "radiogroup",
                "name": "DEPLOY",
                "title": "How is the application currently deployed",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Manual documented steps, user interface driven interaction", "2-RED|Manual documented steps, some basic automation", "3-AMBER|Simple automated deployment scripts", "4-AMBER|Automated deployment, but manual, complex, promotion through stages", "5-GREEN|Full CD Pipeline in place, promoting Applications through the stages;  B/G + Canary capable"]
            },
            {
                "type": "radiogroup",
                "name": "CONTAINERS",
                "title": "How mature is the exisiting containerisation process, if any?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0-UNKNOWN|Unknown","1-RED|Desktop-led container implementation designed to support running application on a laptop. Container treated like a VM with multiple services", "2-RED|Use of a init process within the container to manage multiple container processes that run independently but are tightly integrated", "3-AMBER|Naive container definition", "4-GREEN| Proficient with containers", "5-GREEN| Application containerisation not attempted as yet"]
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

window.survey = new Survey.Model(json);

survey
    .onComplete
    .add(function (result) {
            var xmlhttp = new XMLHttpRequest();
            tmpResult = result.data;
   		    var custID = Utils.getParameterByName("customerId");
   		    var appID  = Utils.getParameterByName("applicationId");

            //dependencies array needs special handling
            var tmpDEPSOUTLIST = tmpResult.DEPSOUTLIST;
            var tmpDEPSINLIST = tmpResult.DEPSINLIST;
            delete tmpResult.DEPSOUTLIST;
            delete tmpResult.DEPSINLIST;
            xmlhttp.open("POST", addAuthToken(Utils.SERVER+"/api/pathfinder/customers/"+custID+"/applications/"+appID+"/assessments"));
            xmlhttp.setRequestHeader("Content-Type", "application/json");
            myObj = { "payload": tmpResult,"depsOUT":tmpDEPSOUTLIST, "depsIN":tmpDEPSINLIST,"datetime":new Date()};
            var payload=JSON.stringify(myObj);
            console.log("payload="+payload);
            xmlhttp.send(payload);

            if (undefined!=$('#surveyCompleteLink')){
            	$('#surveyCompleteLink').attr('href', '/pathfinder-ui/assessments-v2.jsp?customerId='+Utils.getParameterByName("customerId"));
            }
    });


survey
    .onAfterRenderPage
    .add(function (result) {
      console.log("result="+JSON.stringify(survey.data));
      
      // this adds the question weighting to the css class so we can add a visual clue to where the thresholds are 
      $('input', $(".iradio_square-blue")).each(function(){
        var valueSplit=this.value.split('-');
        if (valueSplit.length>1){
          var color=valueSplit[1];
          $(this).parent().addClass("radio-weighting");
          $(this).parent().addClass("radio-weighting-"+color.toLowerCase());
          //console.log("Adding question weighting to: "+$(this).name);
        }
      });
      
      var custID = Utils.getParameterByName("customerId");
      var appID  = Utils.getParameterByName("applicationId");
        
      if (survey.currentPageNo === 1){
        
	    result.data.CUSTID=result.data.CUSTNAME;
	    var d1 = survey.getQuestionByName('DEPSOUTLIST');
	    d1.choicesByUrl.url = addAuthToken(Utils.SERVER+"/api/pathfinder/customers/"+custID+"/applications/?exclude="+appID);
	    d1.choicesByUrl.valueName = "Id";
	    d1.choicesByUrl.titleName = "Name";
	    d1.choicesByUrl.run();

        var d2 = survey.getQuestionByName('DEPSINLIST');
        d2.choicesByUrl.url = addAuthToken(Utils.SERVER+"/api/pathfinder/customers/"+custID+"/applications/?exclude="+appID);
        d2.choicesByUrl.valueName = "Id";
        d2.choicesByUrl.titleName = "Name";
        d2.choicesByUrl.run();
	  }
    });

if (null!=results){
	survey.data=results;
}

$("#surveyElement").Survey({
    model: survey
});


