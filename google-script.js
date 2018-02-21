function onFormSubmit(e) {

    var sheet = SpreadsheetApp.getActiveSpreadsheet().getSheetByName("numeric");

    try {
        var rng = e.range;
        var ss = SpreadsheetApp.getActiveSpreadsheet();

        var fUrl = ss.getFormUrl();
        var f = FormApp.openByUrl(fUrl);

        var rs = f.getResponses();
        var r = rs[rs.length - 1]; // Get last response made

        var itemResponses = r.getItemResponses();
        for (var j = 1; j < itemResponses.length; j++) {
            var itemResponse = itemResponses[j].getResponse()
            var res = itemResponse.substr(0, 1);
            var cell = sheet.getRange(rs.length, j).setValue(res)
        }
    } catch (e) {
        sheet.getRange(3, 2).setValue(e.message);
    }
}


function CreateForm() {
    var assessment = FormApp.create("Application Assessment")
        .setDescription("Red Hat Application Container Suitability Assessment")
        .setProgressBar(true)
        .setPublishingSummary(true);
    var currsheet = SpreadsheetApp.getActiveSpreadsheet();

    assessment.setDestination(FormApp.DestinationType.SPREADSHEET, currsheet.getId());
    ScriptApp.newTrigger('onFormSubmit').forSpreadsheet(currsheet).onFormSubmit().create();

    var textItem = assessment.addTextItem().setTitle('Application Name').setRequired(true);
    var textValidation = FormApp.createTextValidation()
        .requireTextMatchesPattern("[a-zA-Z0-9]+")
        .build();
    textItem.setValidation(textValidation);

    var sec1 = assessment.addSectionHeaderItem();
    sec1.setTitle('Business Concerns');

    var businessitem = assessment.addScaleItem();
    businessitem.setTitle('Business priority of the application').setLabels("Low Business Priority", "High Business Priority")
        .setBounds(1, 10).setRequired(true);

    var owneritem = assessment.addMultipleChoiceItem();
    owneritem.setTitle('Level of Ownership?')
        .setRequired(true).setHelpText("Does the application team understand and actively develop the application?")
        .setRequired(true)
        .setChoices([
            owneritem.createChoice('1. Don\'t Know'),
            owneritem.createChoice('2. Application developed by 3rd party or COTS application'),
            owneritem.createChoice('3. In maintenance mode, no app SME knowledge, EOL imminent'),
            owneritem.createChoice('4. Maintenance mode, SME knowledge available'),
            owneritem.createChoice('5. Actively developed, SME knowledge available'),
            owneritem.createChoice('6. New Greenfield application')
        ])
        .showOtherOption(false);


    var comp = assessment.addMultipleChoiceItem();
    comp.setTitle('Compliance ?')
        .setRequired(true).setHelpText("Does the application have any legal compliance requirements e.g. PCI, HIPPA etc Does the application have any licensing requirements e.g. per core licensing ?")
        .setRequired(true)
        .setChoices([
            comp.createChoice('1. Don\'t Know'),
            comp.createChoice('2. High compliance requirements - both Legal and licensing'),
            comp.createChoice('3. Licensing compliance -  licensing servers'),
            comp.createChoice('4. Legal compliance - distinct hardware, isolated clusters, compliance certification'),
            comp.createChoice('5. None')
        ])
        .showOtherOption(false);



    var pageTwo = assessment.addPageBreakItem().setTitle('Page Two');



    var sec2 = assessment.addSectionHeaderItem();
    sec2.setTitle('Dependencies');

    var hwitem = assessment.addMultipleChoiceItem();
    hwitem.setTitle('Hardware Dependencies?')
        .setRequired(true).setHelpText("Does the application require specific hardware features to run on?")
        .setRequired(true)
        .setChoices([
            hwitem.createChoice('1. Don\'t Know'),
            hwitem.createChoice('2. Non X86 CPU requirements'),
            hwitem.createChoice('3. Custom or legacy hardware required'),
            hwitem.createChoice('4. X86 CPU architecture based'),
            hwitem.createChoice('5. GPU, specific worker node hardware requirements')
        ])
        .showOtherOption(false);


    var ositem = assessment.addMultipleChoiceItem();
    ositem.setTitle('Operating System Dependencies?')
        .setRequired(true).setHelpText("How dependent is the application on the underlying operating system ?")
        .setRequired(true)
        .setChoices([
            ositem.createChoice('1. Don\'t Know'),
            ositem.createChoice('2. Non-supported OS - OSX, AIX, UNIX, SOLARIS'),
            ositem.createChoice('3. Linux with custom kernel drivers or specific kernel version'),
            ositem.createChoice('4. Linux with custom capabilities e.g. setcomp'),
            ositem.createChoice('5. Standard Linux - root access required'),
            ositem.createChoice('6. Standard Linux - no root access required')
        ])
        .showOtherOption(false);

    var tpartydep = assessment.addMultipleChoiceItem();
    tpartydep.setTitle('3rd party vendor components/systems ?')
        .setRequired(true).setHelpText("How dependent is the application on other systems and their failure modes ?")
        .setRequired(true)
        .setChoices([
            tpartydep.createChoice('1. Don\'t Know'),
            tpartydep.createChoice('2. Not supported/recommended to run on containers or EOL'),
            tpartydep.createChoice('3. Containers not supported by vendor'),
            tpartydep.createChoice('4. Supported but with limited functionality'),
            tpartydep.createChoice('5. Supported but relies on self built images'),
            tpartydep.createChoice('6. Fully supported, certified images available')
        ])
        .showOtherOption(false);

    var depin = assessment.addMultipleChoiceItem();
    depin.setTitle('Dependencies - (Incoming/Northbound) ?')
        .setRequired(true).setHelpText("How dependent are other systems on this application and how easy are they to change ?")
        .setRequired(true)
        .setChoices([
            depin.createChoice('1. Don\'t Know'),
            depin.createChoice('2. Difficult to change dependent systems - legacy, 3rd party, external'),
            depin.createChoice('3. Many dependent systems, possible to change but expensive and time consuming'),
            depin.createChoice('4. Many dependent systems, possible to change as internally managed'),
            depin.createChoice('5. Internal dependencies only'),
            depin.createChoice('6. No dependent systems')
        ])
        .showOtherOption(false);



    var depout = assessment.addMultipleChoiceItem();
    depout.setTitle('Dependencies - (Outgoing/Southbound) ?')
        .setRequired(true).setHelpText("How is this application dependent on other systems/application  ?")
        .setRequired(true)
        .setChoices([
            depout.createChoice('1. Don\'t Know'),
            depout.createChoice('2. Availability only verified when processing traffic'),
            depout.createChoice('3. Complex strict startup order required.'),
            depout.createChoice('4. Application not ready until dependencies are available'),
            depout.createChoice('5. Limited processing available if dependencies are unavailable'),
            depout.createChoice('6. No dependencies')
        ])
        .showOtherOption(false);

    var pageThree = assessment.addPageBreakItem().setTitle('Page Three');

    var sec3 = assessment.addSectionHeaderItem();
    sec3.setTitle('Architectural Concerns');


    var architem = assessment.addMultipleChoiceItem();
    architem.setTitle('Architectural Suitability?')
        .setRequired(true).setHelpText("Is the application architecture suitable for containerisation e.g. would virtualisation be better approach?")
        .setRequired(true)
        .setChoices([
            architem.createChoice('1. Don\'t Know'),
            architem.createChoice('2. Massive Monolith (high memory, high CPU) singleton deployment'),
            architem.createChoice('3. Massive Monolith (high memory, high CPU) , non singleton, difficult to scale'),
            architem.createChoice('4. Complex Monolith - strict runtime dependency startup order, non resilient architecture'),
            architem.createChoice('5. Modern resilient monolith e.g. retries, circuit breaker etc'),
            architem.createChoice('6. Independently deployable microservices')
        ])
        .showOtherOption(false);



    var resil = assessment.addMultipleChoiceItem();
    resil.setTitle('Application resiliency ?')
        .setRequired(true).setHelpText("How resilient is the application and how well does it recover from outages ?")
        .setRequired(true)
        .setChoices([
            resil.createChoice('1. Don\'t Know'),
            resil.createChoice('2. Application errors when southbound dependencies unavailable and doesn\'t recover'),
            resil.createChoice('3. Application errors when dependency unavailable but recovers once dependency is available'),
            resil.createChoice('4. Application functionality limited when dependency is unavailable but recovers once dependency is available'),
            resil.createChoice('5. Application employs resilient architecture patterns e.g. circuit breaker, retries etc '),
            resil.createChoice('6. Chaos Engineering principlals followed, application containers randomly terminated to test resiliency')
        ])
        .showOtherOption(false);



    var comms = assessment.addMultipleChoiceItem();
    comms.setTitle('Application communication ?')
        .setRequired(true).setHelpText("How does the application communicate with the external world ?")
        .setRequired(true)
        .setChoices([
            comms.createChoice('1. Don\'t Know'),
            comms.createChoice('2. Non-IP protocols e.g. serial, IPX, AppleTalk'),
            comms.createChoice('3. IP based - hostname/ip encapsulated in payload'),
            comms.createChoice('4. Traffic with no host addressing e.g. SSH'),
            comms.createChoice('5. Traffic with host addressing embedded in SSL SNI header'),
            comms.createChoice('6. HTTP/HTTPS')
        ])
        .showOtherOption(false);



    var state = assessment.addMultipleChoiceItem();
    state.setTitle('State Management ?')
        .setRequired(true).setHelpText("How does the application handle state ?")
        .setRequired(true)
        .setChoices([
            state.createChoice('1. Don\'t Know'),
            state.createChoice('2. Shared memory between applications'),
            state.createChoice('3. Managed/Coordinated externally from application e.g. external Zookeeper'),
            state.createChoice('4. State maintained in OSE based application requiring non shared, non ephemeral storage'),
            state.createChoice('5. Shared disk between application instances'),
            state.createChoice('6. Stateless/Ephemeral container storage')
        ])
        .showOtherOption(false);

    var ha = assessment.addMultipleChoiceItem();
    ha.setTitle('High Availability/Discovery')
        .setRequired(true).setHelpText("Does the application have any unusual concerns around clustering or service discovery?")
        .setRequired(true)
        .setChoices([
            ha.createChoice('1. Don\'t Know'),
            ha.createChoice('2. Uses Clustering/Discovery technologies that are not k8s suitable e.g. hardcoded ip addresses, custom cluster manager'),
            ha.createChoice('3. Application needs restart on cluster changes'),
            ha.createChoice('4. Service discovery layered ontop of k8s e.g. hashicorp consul, netflix eureka'),
            ha.createChoice('5. Standard k8s DNS name resolution, application resilient to cluster changes '),
            ha.createChoice('6. None')
        ])
        .showOtherOption(false);



    var pageFour = assessment.addPageBreakItem().setTitle('Page Four');

    var sec4 = assessment.addSectionHeaderItem();
    sec4.setTitle('Application Observability');


    var rpro = assessment.addMultipleChoiceItem();
    rpro.setTitle('Application runtime profile ?')
        .setRequired(true).setHelpText("What does the runtime profile of the application look like ?")
        .setRequired(true)
        .setChoices([
            rpro.createChoice('1. Don\'t Know'),
            rpro.createChoice('2. High CPU,Storage,IO, deterministic real time execution requirements'),
            rpro.createChoice('3. Latency sensitive applications e.g. voice'),
            rpro.createChoice('4. High burstable memory/cpu needs'),
            rpro.createChoice('5. Controlled burstable memory/cpu needs'),
            rpro.createChoice('6. Deterministic production profile')
        ])
        .showOtherOption(false);

    var logs = assessment.addMultipleChoiceItem();
    logs.setTitle('Application communication ?')
        .setRequired(true).setHelpText("How easy is it to get the application logs?")
        .setRequired(true)
        .setChoices([
            logs.createChoice('1. Don\'t Know'),
            logs.createChoice('2. No logging available, internal only with no export mechanism'),
            logs.createChoice('3. Custom binary logs exposed using non-standard protocols'),
            logs.createChoice('4. Logs exposed via syslog'),
            logs.createChoice('5. Log entries written to filesystem'),
            logs.createChoice('6. Configurable logging can be sent to STDOUT ')
        ])
        .showOtherOption(false);


    var metrics = assessment.addMultipleChoiceItem();
    metrics.setTitle('Application metrics ?')
        .setRequired(true).setHelpText("How easy is it to get the application metrics?")
        .setRequired(true)
        .setChoices([
            metrics.createChoice('1. Don\'t Know'),
            metrics.createChoice('2. No exposed metrics'),
            metrics.createChoice('3. Internal metrics but not exposed'),
            metrics.createChoice('4. Metrics exposed via binary protocols e.g. SNMP'),
            metrics.createChoice('5. 3rd party metrics solution e.g. dynatrace, app-dynamics etc'),
            metrics.createChoice('6. Prometheus, native OSE metrics, integration with autoscalers')
        ])
        .showOtherOption(false);

    var health = assessment.addMultipleChoiceItem();
    health.setTitle('Application health ?')
        .setRequired(true).setHelpText("How easy is it to get the application health ?")
        .setRequired(true)
        .setChoices([
            health.createChoice('1. Don\'t Know'),
            health.createChoice('2. No health or readyiness probes available'),
            health.createChoice('3. Custom watchdog process monitoring and managing the application'),
            health.createChoice('4. Basic application health requires semi-complex scripting'),
            health.createChoice('5. Scriptable liveness and readyiness probes'),
            health.createChoice('6. Probes execute synthetic transactions to verify application health')
        ])
        .showOtherOption(false);


    var deploy = assessment.addMultipleChoiceItem();
    deploy.setTitle('Deployment Complexity')
        .setRequired(true).setHelpText("How easy is it to deploy the application ?")
        .setRequired(true)
        .setChoices([
            deploy.createChoice('1. Don\'t Know'),
            deploy.createChoice('2. Manual documented steps'),
            deploy.createChoice('3. Manual documented steps, some basic automation'),
            deploy.createChoice('4. Simple automated deployment scripts'),
            deploy.createChoice('5. Automated deployment, but manual, slow, promotion through stages'),
            deploy.createChoice('6. Full CD Pipeline in place, promoting Applications through the stages;  B/G + Canary capable')
        ])
        .showOtherOption(false);


    var testing = assessment.addMultipleChoiceItem();
    testing.setTitle('Application Testing')
        .setRequired(true).setHelpText("What kind of testing does the application undergo ?")
        .setRequired(true)
        .setChoices([
            testing.createChoice('1. Don\'t Know'),
            testing.createChoice('2. Manual testing only'),
            testing.createChoice('3. Minimal automated testing, UI focused only'),
            testing.createChoice('4. Automated unit & regression testing, basic CI pipelines'),
            testing.createChoice('5. Highly repeatable automated testing - Unit, Integration, smoke tests before production deployment, modern test practices followed'),
            testing.createChoice('6. Chaos Engineering principlals followed. Testing in production e.g. A/B, experimentation')
        ])
        .showOtherOption(false);


    var security = assessment.addMultipleChoiceItem();
    security.setTitle('Application Security')
        .setRequired(true).setHelpText("What kind of testing does the application undergo ?")
        .setRequired(true)
        .setChoices([
            security.createChoice('1. Don\'t Know'),
            security.createChoice('2. HSM, hardware based encryption devices'),
            security.createChoice('3. Certs, Keys bound to application IP addresses, generated at runtime per application instance'),
            security.createChoice('4. Keys/Certs compiled into application'),
            security.createChoice('5. Certificates/Keys loaded via shared disk'),
            security.createChoice('6. Certificates/Keys loaded via files')
        ])
        .showOtherOption(false);



    var config = assessment.addMultipleChoiceItem();
    config.setTitle('Application Configuration')
        .setRequired(true).setHelpText("How is the application configured ?")
        .setRequired(true)
        .setChoices([
            config.createChoice('1. Don\'t Know'),
            config.createChoice('2. Compiled/Patched into application at installation time'),
            config.createChoice('3. Externally stored and loaded using specific key e.g. hostname, ip address'),
            config.createChoice('4. Configuration baked into container image and enabled via system property at runtime'),
            config.createChoice('5. Configuration files loaded from shared disk'),
            config.createChoice('6. Configuration files loaded by application, environment variables, configmaps')
        ])
        .showOtherOption(false);

    var pageFive = assessment.addPageBreakItem().setTitle('Page Five');


    var item4 = assessment.addSectionHeaderItem();
    item4.setTitle('Estimates');

    var estimate = assessment.addListItem();
    estimate.setTitle('Level of effort estimate?').setHelpText("T-shirt level of work estimate")
        .setChoices([
            estimate.createChoice('1. S'),
            estimate.createChoice('2. M'),
            estimate.createChoice('3. L'),
            estimate.createChoice('4. XL'),
            estimate.createChoice('4. XXL')
        ]);

}