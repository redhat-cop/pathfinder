

<!--#################-->
<!-- EDIT MODAL FORM -->
<!--#################-->
<script>
  var entityManagementUrls={
  	"get":			Utils.SERVER+"/api/pathfinder/customers/"+Utils.getParameterByName("customerId")+"/applications/$ID",
  	"create":		Utils.SERVER+"/api/pathfinder/customers/"+Utils.getParameterByName("customerId")+"/applications/",
  	"update":		Utils.SERVER+"/api/pathfinder/customers/"+Utils.getParameterByName("customerId")+"/applications/$ID"
  };
	function getIdFieldName(){
		return "Id";
	}
</script>
<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
  <div class="modal-dialog" role="document"> <!-- make wider by adding " modal-lg" to class -->
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title" id="exampleModalLabel">New Application</h4>
      </div>
      <div class="modal-body">
        <form id="form">
        	<div id="form-id" class="form-group" style="display:none">
            <label for="Id" class="control-label">Application ID</label>
            <input id="Id" name="Id" type="text" class="form-control"/>
          </div>
          <div class="form-group">
            <label for="Name" class="control-label">Application Name *</label>
            <input id="Name" name="Name" type="text" class="form-control mandatory">
          </div>
          <div class="form-group">
            <label for="Stereotype" class="control-label">Type *</label>
						<select name="Stereotype" id="Stereotype" onchange="formValidate();" class="mandatory">
							<option value="" selected disabled hidden>Choose...</option>
							<option value="TARGETAPP" selected>Assessable Application</option>
							<option value="DEPENDENCY">Dependency Only (ie. database or LDAP Server)</option>
							<!--option value="PROFILE">Profile</option-->
						</select>
          </div>
          <div class="form-group">
            <label for="Description" class="control-label">Description</label>
            <input id="Description" name="Description" type="text" class="form-control">
          </div>
          <div class="form-group">
            <label for="Owner" class="control-label">Owner/Contact</label>
            <input id="Owner" name="Owner" type="text" class="form-control">
          </div>
        </form>
      </div>
      <script>
      	
      	// dynamic validation by class name inclusion
	      function formValidate(){
      		var disabled=false;
	      	$("#exampleModal input[type=text]").each(function() {
	      		console.log("class=="+$(this).attr('class'));
	      		if ($(this).attr('class')!=undefined && $(this).attr('class').includes("mandatory")){
	      			disabled=disabled || isEmpty($(this).val());
	      		}
	      	});
	      	$("#exampleModal select").each(function() {
	      		if ($(this).attr('class')!=undefined && $(this).attr('class').includes("mandatory"))
	      			disabled=disabled || isEmpty($(this).val());
	      	});
	      	console.log("#edit-ok -> enabled="+!disabled);
	      	$('#edit-ok').attr('disabled', disabled);
		      //$('#edit-ok').attr('disabled', isEmpty($('#Stereotype').val()) || isEmpty($('#Name').val()));
	      }
	      
	      function isEmpty(val){
	      	return val==null || val=="";
	      }
      	
      	// attach validation onchange events
      	$(document).ready(function() {
	      	$("#exampleModal select").change(function(){
						formValidate();
					});
					$("#exampleModal input").keyup(function(){
						formValidate();
					});
				});
				
				// focus the first box
      	$('#exampleModal').on('shown.bs.modal', function() {
			    $("#Name").focus();
			    formValidate();
				})
      </script>
      
      <div class="modal-footer">
        <a type="button" data-dismiss="modal">Cancel</a>
        <button class="btn btn-primary" id="edit-ok" type="button" data-dismiss="modal" onclick="save(this, 'form'); return false;">Create</button>
      </div>
    </div>
  </div>
</div>