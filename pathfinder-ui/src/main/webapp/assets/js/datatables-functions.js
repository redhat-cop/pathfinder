function send(action, uri, data){
  var xhr = new XMLHttpRequest();
  var ctx = "${pageContext.request.contextPath}";
  //var url=ctx+"/api"+uri;
  var url=uri;
  
  xhr.open(action, addAuthToken(url), true);
  if (data != undefined){
    xhr.setRequestHeader("Content-type", "application/json");
    console.log("POSTing url="+url+", data="+JSON.stringify(data));
    xhr.send(JSON.stringify(data));
  }else{
    xhr.send();
  }
  xhr.onloadend = function () {
  	//var table1=$('#example').DataTable();
  	//var oSettings=table1.fnSettings();
  	//console.log("table="+JSON.stringify(table1));
  	//console.log("oSettings="+JSON.stringify(oSettings));
  	
  	if (this.status == 200){
	  	
	  	$('#example').DataTable().ajax.reload(
	  		function(json){
	  		  // ideally we'd just call fnInitComplete but sadly i can't find that function embedded in the datatable object yet
	  			if (undefined!=onDatatableRefresh) onDatatableRefresh(json);
	  			//table.fnSettings.fnInitComplete(null, json);
	  		}
	  	);
  		
  	}else if(xhr.status>=400){
  		
  		showNotification("error", xhr.responseText);
  		
  	}
  	
  	
    //$('#example').dataTable().ajax.reload();// new function(json){console.log("json="+json);}, true );
    //$('#example').dataTable().fnReloadAjax(null, onDatatableRefresh, null);
//    if (undefined!=postAction){
//    	postAction();
//    }
  };
}

function post(uri, data){
  return send("POST", uri, data);
}
function postWait(url, data, callback){
	var xhr = new XMLHttpRequest();
	xhr.open("POST", addAuthToken(url), true);
	xhr.setRequestHeader("Content-type", "application/json");
	xhr.send(JSON.stringify(data));
	xhr.onloadend = function () {
	  console.log("PostWait::status = "+xhr.status);
	  console.log("PostWait::status = "+xhr.responseText);
	  
	  if (xhr.status==200){
	    console.log("PostWait::calling callback");
	  	callback(xhr.responseText);
	  }
	};
}

function deleteItem(id){
	if (!confirm("Are you sure?")){
		return false;
	}else{
  	httpDelete(Utils.SERVER+"/api/pathfinder/customers/"+id);
	}
}

function httpDelete(uri, data){
  console.log("HTTP DELETE: "+uri+" ["+data+"]");
  return send("DELETE", uri, data);
}
function put(uri, data){
  return send("put", uri, data);
}
function editFormReset(){
    document.getElementById("edit-ok").innerHTML="Create";
    document.getElementById("exampleModalLabel").innerHTML=document.getElementById("exampleModalLabel").innerHTML.replace("Update", "New");
    document.getElementById(getIdFieldName()).disabled=false; // allow key fields to be changed when entities are new
    
    var form=document.getElementById("form");
    for (var i = 0, ii = form.length; i < ii; ++i) {
      var input = form[i];
      input.value="";
    }
    
    //if (null==document.getElementById(getIdFieldName())){
    //  console.log("Unable to find element named: "+getIdFieldName());
    //}
    //document.getElementById(getIdFieldName()).value="NEW";
}
function loadEntity(id){
  document.getElementById("edit-ok").innerHTML="Update";
  document.getElementById("exampleModalLabel").innerHTML=document.getElementById("exampleModalLabel").innerHTML.replace("New", "Update");
  document.getElementById(getIdFieldName()).disabled=true; // don't allow key fields to be changed
  
  var xhr = new XMLHttpRequest();
  var ctx = "${pageContext.request.contextPath}";
  
  xhr.open("GET", addAuthToken(entityManagementUrls["get"].replace("$ID", id)), true);
  //xhr.open("GET", addAuthToken(getLoadUrl(id)), true);
  
  xhr.send();
  xhr.onloadend = function () {
    var json=JSON.parse(xhr.responseText);
    var form=document.getElementById("form");
    for (var i = 0, ii = form.length; i < ii; ++i) {
      if (typeof json[form[i].name] == "undefined"){
        form[i].value="";
      }else{
        form[i].value=json[form[i].name];
      }
      //form[i].disabled=false;
    }
    if (formValidate!=undefined)
      formValidate();
  }
}
function save(sender, formId){
  var data = {};
  var op="";
  var form=document.getElementById(formId);
  for (var i = 0, ii = form.length; i < ii; ++i) {
    if (form[i].name) data[form[i].name]=form[i].value;
  }
  //console.log("button sender="+sender);
  var isUpdate=sender.innerHTML=="Update"; // don't like this, it's too fragile
  //var url=isUpdate?getUpdateUrl():getCreateUrl();
  
  //post(getSaveUrl(), data);
  
  var id=document.getElementById(getIdFieldName()).value;
  var url=!isUpdate?entityManagementUrls["create"]:entityManagementUrls["update"].replace("$ID", id);
  //console.log("Save url:"+url);
  post(url, data);
  
  editFormReset();
}

function httpGet(url, field, callback){
	var xhr = new XMLHttpRequest();
	xhr.open("GET", addAuthToken(url), true);
	xhr.send();
	xhr.onloadend = function () {
	  callback(JSON.parse(xhr.responseText)[field]);
	};
}

function httpGetObject(url, callback){
	var xhr = new XMLHttpRequest();
	xhr.open("GET", addAuthToken(url), true);
	//xhr.setRequestHeader("Accept", "application/json");
	xhr.send();
	xhr.onloadend = function () {
		if (this.status == 200){
		  callback(JSON.parse(xhr.responseText));
		}
	};
}


