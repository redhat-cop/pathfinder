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

Survey
    .JsonObject
    .metaData
    .addProperty("questionbase", "tooltip");

Survey.requiredText = "AA";


Survey.ChoicesRestfull.onBeforeSendRequest = function(sender, options) {
        options.request.setRequestHeader("Content-Type", "application/javascript");
        //options.request.setRequestHeader("Authorization", "Bearer "+jwtToken);
};

var json = $$QUESTIONS_JSON$$;

window.survey = new Survey.Model(json);

survey
    .onAfterRenderQuestion
    .add(function (survey, options) {
        //Return if there is no description to show in popup
        if (!options.question.tooltip)
            return;

        var header = options
            .htmlElement
            .querySelector("h5");
        header.title = options.question.tooltip;

        var span = document.createElement("span");
        span.style.display='inline-block';
        span.style.color='white';
        span.style.background-color='#1ab394';
        span.style.border-radius='50%';
        span.style.padding='0 7px'';
        span.style.cursor='help';
        span.innerText = "?";
        span.className = "survey-tooltip";
        header.appendChild(span);
    });

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
            	$('#surveyCompleteLink').attr('href', '/assessments.jsp?customerId='+Utils.getParameterByName("customerId"));
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
	//Force the user to answer the dependencies question in the case of re-editing the survey
    var d11 = survey.getQuestionByName('DEPSIN');
    d11.value="";
    var d21 = survey.getQuestionByName('DEPSOUT');
    d21.value="";
}

$("#surveyElement").Survey({
    model: survey
});


