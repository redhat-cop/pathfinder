var defaultThemeColors = Survey
    .StylesManager
    .ThemeColors["default"];

defaultThemeColors["$main-color"] = "#a30000";
defaultThemeColors["$main-hover-color"] = "#820000";
defaultThemeColors["$text-color"] = "#4a4a4a";
defaultThemeColors["$header-color"] = "#ffffff";
defaultThemeColors["$header-background-color"] = "#820000";
defaultThemeColors["$body-container-background-color"] = "#f8f8f8";
defaultThemeColors["$error-color"]="#a30000";
defaultThemeColors["$border-color"]="#820000";

Survey
    .StylesManager
    .applyTheme();

Survey.ChoicesRestfull.onBeforeSendRequest = function(sender, options) {
        options.request.setRequestHeader("Content-Type", "application/json");
};

var json = {
    title: "Application Assessment",
    sendResultOnPageNext: "true",
    showProgressBar: "bottom",
    pages: [{
        "title": "Customer Details",
        "questions": [
            {
                type: "dropdown",
                "name": "CUSTNAME",
                title: "Select the Customer...",
                isRequired: true,
                choicesByUrl: {
                      url: "api/pathfinder/customers/",
                      valueName: "CustomerId",
                      titleName: "CustomerName"
                }
            }
        ]
    },
    {
        "title": "Application Details",
        "questions": [
            {
                type: "dropdown",
                "name": "ASSMENTNAME",
                title: "Select the application to be assessed....",
                isRequired: true,
                choicesByUrl: {
                        // Ignore the URL this will be replaced by the event handler
                        url: "api/pathfinder/",
                        valueName: "Id",
                        titleName: "Name"
                }
            },
            {
                type: "rating",
                "name": "BUSPRIORITY",
                title: "Whats the level of business criticality of this application?",
                rateMin: 1,
                rateMax: 10,
                rateStep: 1,
                minRateDescription: "End of Life",
                maxRateDescription: "Core Business Critical"
            },
            {
                "type": "radiogroup",
                "name": "OWNER",
                "title": "Level of Ownership",
                "comment": "Does the app team understand and actively develop the application ?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0|Unknown","1|Application developed by 3rd party or COTS application ", "2|In maintenance mode, no app SME knowledge, EOL imminent", "3|Maintenance mode, SME knowledge available", "4|Actively developed, SME knowledge available", "5|New Greenfield application"]
            },
            {
                "type": "radiogroup",
                "name": "COMPLIANCE",
                "title": "Compliance",
                "comment": "Does the application have any legal compliance requirements e.g. PCI, HIPPA etc Does the application have any licensing requirements e.g. per core licensing",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0|Unknown","1|High compliance requirements - both Legal and licensing", "2|Licensing compliance -  licensing servers", "3|Legal compliance - distinct hardware, isolated clusters, compliance certification","4| None"]
            }
        ]
    }, {
        "title": "Application Dependencies",
        questions: [
            {
                "type": "radiogroup",
                "name": "ARCHTYPE",
                "comment": "Does the app architecture suitable for containerisation e.g. would a VM be better approach",
                "title": "Architectural Suitability",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0|Unknown","1|Massive Monolith (high memory, high CPU) singleton deployment", "2|Massive Monolith (high memory, high CPU) , non singleton, difficult to scale", "3|Complex Monolith -  strict runtime dependency startup order, non resilient architecture", "4|Modern resilient monolith e.g. retries, circuit breaker etc", "5|Independently deployable microservices"]
            },
            {
                "type": "radiogroup",
                "name": "DEPSHW",
                "title": "Hardware",
                "comment": "Does the application require specific hardware features to run on",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0|Unknown","1|Non X86 CPU requirements", "2|Custom or legacy hardware required", "3|GPU, specific worker node hardware requirements", "4|X86 CPU architecture based"]
            },
            {
                "type": "radiogroup",
                "name": "DEPSOS",
                "title": "Operating System",
                "comment": "Does the application require specific hardware features to run on",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0|Unknown","1|Non-supported OS - OSX,  AIX, UNIX, SOLARIS", "2|Linux with custom kernel drivers or specific kernel version", "3|Linux with custom capabilities e.g. setcomp", "4|Standard Linux - root access required", "5|Standard Linux - no root access required"]
            },
            {
                "type": "radiogroup",
                "name": "DEPS3RD",
                "title": "3rd party vendor components/systems",
                "comment": "How dependent is the application on other systems and their failure modes",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0|Unknown","1|Not supported/recommended to run on containers or EOL", "2|Containers not supported by vendor", "3|Supported but with limited functionality", "4|Supported but relies on self built images", "5|Fully supported, certified images available"]
            },
            {
                "type": "radiogroup",
                "name": "DEPSIN",
                "title": "Dependencies - (Incoming/Northbound)",
                "comment": "How dependent are other systems on this application and how easy are they to change",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0|Unknown","1|Difficult to change dependent systems - legacy, 3rd party, external", "2|Many dependent systems, possible to change but expensive and time consuming", "3|Many dependent systems, possible to change as internally managed", "4|Internal dependencies only", "5|No dependent systems"]
            },
            {
                "type": "radiogroup",
                "name": "DEPSOUT",
                "title": "Dependencies - (Outgoing/Southbound)",
                "comment": "How dependent are other systems on this application and how easy are they to change",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0|Unknown","1|Availability only verified when processing traffic", "2|Complex strict startup order required", "3|Application not ready until dependencies are available ", "4|Limited processing available if dependencies are unavailable", "5|No dependencies"]
            },
            {
                "type": "tagbox",
                "isRequired": false,
                "choicesByUrl": {
                    // Ignore the URL this will be replaced by the event handler
                    "url": "api/pathfinder/customers/12345/applications/"
                },
                "name": "DEPSOUTLIST",
                "title": "Please add southbound dependencies..."
            }
        ]
    }, {
        "title": "Application Architecture",
        questions: [{
                "type": "radiogroup",
                "name": "RESILIENCY",
                "title": "Application resiliency",
                "comment": "How resilient is the application and how well does it recover from outages",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0|Unknown","1|Application errors when southbound dependencies unavailable and doesn't recover", "2|Application errors when dependency unavailable but recovers once dependency is available", "3|Application functionality limited when dependency is unavailable but recovers once dependency is available", "4|Application employs resilient architecture patterns e.g. circuit breaker, retries etc ", "5|Chaos Engineering principlals followed, application containers randomly terminated to test resiliency"]
            },
            {
                "type": "radiogroup",
                "name": "COMMS",
                "title": "Communication",
                "comment": "How does the app communicate with the external world",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0|Unknown","1|Non-IP protocols e.g. serial, IPX, AppleTalk", "2|IP based - hostname/ip encapsulated in payload", "3|Traffic with no host addressing e.g. SSH ", "4|Traffic with host addressing embedded in SSL SNI header ", "5|HTTP/HTTPS"]
            },
            {
                "type": "radiogroup",
                "name": "STATE",
                "title": "State Management",
                "comment": "Does the application have any state requirements ?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0|Unknown","1|Shared memory between applications", "2|Managed/Coordinated externally from application e.g. external Zookeeper", "3|State maintained in OSE based application requiring non shared, non ephemeral storage", "4|Shared disk between application instances ", "5|Stateless/Ephemeral container storage"]
            },
            {
                "type": "radiogroup",
                "name": "HA",
                "title": "Discovery",
                "comment": "Does the application have any unusual concerns around service discovery?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0|Unknown","1|Uses Discovery technologies that are not k8s suitable e.g. hardcoded ip addresses, custom cluster manager", "2|Application needs restart on cluster changes", "3|Service discovery layered ontop of k8s e.g. hashicorp consul, netflix eureka", "4|Standard k8s DNS name resolution, application resilient to cluster changes "]
            },
            {
                "type": "radiogroup",
                "name": "CLUSTER",
                "title": "Application Clustering",
                "comment": "How is the application clustered ?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0|Unknown","1|Application cluster mechanism tightly coupled to application design & implementation", "2|Application cluster provided by common runtime dependencies of application binaries", "3|Application clustering mostly provided by application runtime platform, with application integrating with these mechanisms through runtime dependencies", "4|Loosely coupled bespoke clustering solution", "5|Memberless and stateless runtimes"]
            },
            {
                "type": "radiogroup",
                "name": "PROFILE",
                "title": "Profile",
                "comment": "What does the runtime profile of the application look like ?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0|Unknown","1|High CPU,Storage,IO, deterministic real time execution requirements", "2|Latency sensitive applications e.g. voice", "3|High burstable memory/cpu needs", "4|Controlled burstable memory/cpu needs", "5|Deterministic production profile"]
            }
        ]
    }, {
        "title": "Application Observability",
        questions: [{
                "type": "radiogroup",
                "name": "LOGS",
                "title": "Application Logging",
                "comment": "How easy is it to determine how the application is performing and how to get information from it.",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0|Unknown","1|No logging available, internal only with no export mechanism", "2|Custom binary logs exposed using non-standard protocols", "3|Logs exposed via syslog", "4|Log entries written to filesystem", "5|Configurable logging can be sent to STDOUT "]
            },
            {
                "type": "radiogroup",
                "name": "METRICS",
                "title": "Application Metrics",
                "comment": "How easy is it to determine how the application is performing and how to get information from it.",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0|Unknown","1|No exposed metrics", "2|Internal metrics but not exposed", "3|Metrics exposed via binary protocols e.g. SNMP", "4|3rd party metrics solution e.g. dynatrace, app-dynamics etc", "5|Prometheus, native k8s metrics, integration with autoscalers"]
            },
            {
                "type": "radiogroup",
                "name": "HEALTH",
                "title": "Application Health",
                "comment": "How easy is it to determine how the application is performing and how to get information from it.",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0|Unknown","1|No health or readyiness probes available", "2|Custom watchdog process monitoring and managing the application", "3|Basic application health requires semi-complex scripting", "4|Scriptable liveness and readyiness probes", "5|Probes execute synthetic transactions to verify application health"]
            },
            {
                "type": "radiogroup",
                "name": "CONFIG",
                "title": "Application Configuration",
                "comment": "How is the application configured ?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0|Unknown","1|Compiled/Patched into application at installation time", "2|Externally stored and loaded using specific key e.g. hostname, ip address", "3|Configuration baked into application and enabled via system property at runtime", "4|Configuration files loaded from shared disk", "5|Configuration files loaded by application from mounted files, environment variables"]
            }
        ]
    }, {
        "title": "Application Cross-Cutting concerns",
        questions: [{
                "type": "radiogroup",
                "name": "TEST",
                "title": "Application Testing",
                "comment": "What kind of testing does the application undergo ?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0|Unknown","1|Manual testing only", "2|Minimal automated testing, UI focused only ", "3|Automated unit & regression testing, basic CI pipelines", "4|Highly repeatable automated testing - Unit, Integration, smoke tests before production deployment, modern test practices followed", "5|Chaos Engineering principlals followed. Testing in production e.g. A/B, experimentation"]
            },
            {
                "type": "radiogroup",
                "name": "SECURITY",
                "title": "Application Security",
                "comment": "How is the application secured ?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0|Unknown","1|HSM, hardware based encryption devices", "2|Certs, Keys bound to application IP addresses, generated at runtime per application instance", "3|Keys/Certs compiled into application", "4|Certificates/Keys loaded via shared disk", "5|Certificates/Keys loaded via files"]
            },
            {
                "type": "radiogroup",
                "name": "DEPLOY",
                "title": "Deployment Complexity",
                "comment": "Does the application have any unusual concerns around clustering or service discovery?",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0|Unknown","1|Manual documented steps", "2|Manual documented steps, some basic automation", "3|Simple automated deployment scripts", "4|Automated deployment, but manual, slow, promotion through stages", "5|Full CD Pipeline in place, promoting Applications through the stages;  B/G + Canary capable"]
            },
            {
                "type": "radiogroup",
                "name": "CONTAINERS",
                "title": "Containerisation Process",
                "comment": "Is the application already available as a container image? How well suited to cloud native is the existing containerisation.",
                "isRequired": true,
                "colCount": 1,
                "choices": ["0|Unknown","1|Desktop-led container implementation designed to support running app on a laptop. Container treated like a VM with multiple services", "2|Use of a init process within the container to manage multiple container processes that run independently but are tightly integrated", "3|Running process id agnostic", "4|High overhead health checks", "5|Fast graceful shutdown behaviour"]
            },
            {
                type: "comment",
                "name": "NOTES",
                title: "Additional notes or comments"
            }
        ]
    }
    ]
}

window.survey = new Survey.Model(json);

var loadData = function () {
    document.getElementById('username').innerText = keycloak.subject;
    console.log("In Load datafunction");

    var url = '/api/pathfinder/customers/';

    var req = new XMLHttpRequest();
    req.open('GET', url, true);
    req.setRequestHeader('Accept', 'application/json');
    req.setRequestHeader('Authorization', 'Bearer ' + keycloak.token);

    req.onreadystatechange = function () {
        if (req.readyState == 4) {
            if (req.status == 200) {
                alert('Success');
            } else if (req.status == 403) {
                alert('Forbidden');
            }
        }
    }

    req.send();
};

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
            xmlhttp.open("POST", "/api/pathfinder/customers/"+cust+"/applications/"+assm+"/assessments");
            xmlhttp.setRequestHeader("Content-Type", "application/json");
            myObj = { "payload": tmpResult,"deps":tmpDEPSOUTLIST, "datetime":new Date()};
            xmlhttp.send(JSON.stringify(myObj));
    });

survey
    .onPartialSend
    .add(function (result) {
        var q = survey.getQuestionByName('ASSMENTNAME');
        var tmp = result.data.CUSTNAME;
        q.choicesByUrl.url = "api/pathfinder/customers/"+tmp+"/applications/";
        q.choicesByUrl.valueName = "Id";
        q.choicesByUrl.titleName = "Name";
        q.choicesByUrl.run();

        var v = survey.getQuestionByName('DEPSOUTLIST');
        v.choicesByUrl.url = "api/pathfinder/customers/"+tmp+"/applications/";
        v.choicesByUrl.valueName = "Id";
        v.choicesByUrl.titleName = "Name";
        v.choicesByUrl.run();
    });


$("#surveyElement").Survey({
    model: survey
});
