
<!--#################-->
<!-- EDIT MODAL FORM -->
<!--#################-->

<script>
  var entityManagementUrls={
  	"get":			Utils.SERVER+"/api/pathfinder/customers/$ID/",
  	"create":		Utils.SERVER+"/api/pathfinder/customers/",
  	"update":		Utils.SERVER+"/api/pathfinder/customers/$ID"
  };
	function getIdFieldName(){
		return "CustomerId";
	}
</script>

<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
  <div class="modal-dialog" role="document"> <!-- make wider by adding " modal-lg" to class -->
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title" id="exampleModalLabel">New Customer</h4>
      </div>
      <div class="modal-body">
        <form id="form">
        	<!-- ### Hidden ID field -->
        	<div id="form-id" class="form-group" style="display:none">
            <label for="CustomerId" class="control-label">Customer Name</label>
            <input id="CustomerId" name="CustomerId" type="text" class="form-control"/>
          </div>
          
          <div id="form-id" class="form-group">
            <label for="CustomerName" class="control-label">Customer Name *</label>
            <input id="CustomerName" name="CustomerName" type="text" class="form-control mandatory"/>
          </div>
          <div class="form-group">
            <label for="CustomerDescription" class="control-label">Customer Description</label>
            <input id="CustomerDescription" name="CustomerDescription" type="text" class="form-control">
          </div>
          <div class="form-group">
            <label for="CustomerVertical" class="control-label">Customer Vertical</label>
            <select name="CustomerVertical" id="CustomerVertical">
							<option value="Agriculture" selected="selected">Agriculture</option>
							<option value="Business Services">Business Services</option>
							<option value="Construction & Real Estate">Construction & Real Estate</option>
							<option value="Education">Education</option>
							<option value="Energy, Raw Materials & Utilities">Energy, Raw Materials & Utilities</option>
							<option value="Finance">Finance</option>
							<option value="Government">Government</option>
							<option value="Healthcare">Healthcare</option>
							<option value="IT">IT</option>
							<option value="Leisure & Hospitality">Leisure & Hospitality</option>
							<option value="Libraries">Libraries</option>
							<option value="Manufacturing">Manufacturing</option>
							<option value="Media & Internet">Media & Internet</option>
							<option value="Non-Profit & Professional Orgs.">Non-Profit & Professional Orgs.</option>
							<option value="Retail">Retail</option>
							<option value="Software">Software</option>
							<option value="Telecommunications">Telecommunications</option>
							<option value="Transportation">Transportation</option>
						</select>
          </div>
          <div class="form-group">
            <label for="CustomerAssessor" class="control-label">Customer Assessor</label>
            <input id="CustomerAssessor" name="CustomerAssessor" type="text" class="form-control">
          </div>
        </form>
      </div>
      <script>
	      function formValidate(){
		      //$('#edit-ok').attr('disabled', isEmpty($('#CustomerName').val()) || isEmpty($('#CustomerAssessor').val()));
		      $('#edit-ok').attr('disabled', isEmpty($('#CustomerName').val()) );
	      }
	      function isEmpty(val){
	      	return val==null || val==undefined || val=="";
	      }
      	
      	$('#exampleModal').on('shown.bs.modal', function() {
			    $("#CustomerName").focus();
				});
				
      	$(document).ready(function() {
	      	$("#exampleModal select").change(function(){
						formValidate();
					});
					$("#exampleModal input").keyup(function(){
						formValidate();
					});
					formValidate();
				});
      </script>
      <div class="modal-footer">
        <a data-dismiss="modal">Cancel</a>
        <button id="edit-ok" type="button" class="btn btn-primary" data-dismiss="modal" onclick="save(this, 'form'); return false;">Create</button>
      </div>
    </div>
  </div>
</div>