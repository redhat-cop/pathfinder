// Get the status of the customer apps
httpGetObject(Utils.SERVER+'/api/pathfinder/customers/'+customerId+"/applicationAssessmentProgress", function(progress){
	  progress.Appcount=progress.Appcount.toString();
	  progress.Assessed=progress.Assessed.toString();
	  progress.Reviewed=progress.Reviewed.toString();
	  if (progress.Appcount == progress.Assessed && progress.Appcount == progress.Reviewed) {
//			console.log("App Count is the same as assessed");			
			return "<img src=images/ok-48.png>";		  
		  } else {
			  return "";
		  }                   
});