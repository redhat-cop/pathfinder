  <%-- <link href="assets/css/jquery.dataTables-1.10.16.css" rel="stylesheet"> --%>
  <%-- <link href="https://cdn.datatables.net/v/bs/dt-1.10.18/datatables.min.css" rel="stylesheet"/> --%>

  <script src="assets/js/bootstrap-3.3.7.min.js"></script>
  <%-- <script src="assets/js/jquery.dataTables-1.10.16.js"></script> --%>
  <script src="https://cdn.datatables.net/v/bs/dt-1.10.18/datatables.min.js"></script>
  <script src="assets/js/datatables-functions.js?v14"></script>
	<script src="assets/js/dateFormat.min.js"></script>
	
	<script src="utils.jsp"></script>
	
	<style>
		#example_filter label input{
			height: 25px;
		}
	</style>




<script>
$(document).ready (function(){
  $("#successNotification").hide();
  $("#errorNotification").hide();
});

function showNotification(type, message){
  $('#'+type+'NotificationMessage').html(message);
  $("#"+type+"Notification").fadeTo(3000, 500).slideUp(500, function(){
		$("#"+type+"Notification").slideUp(500);
	});
};
</script>

<div class="alert alert-success" style="display:none;float:right;width:30%" id="successNotification">
    <button type="button" class="close" data-dismiss="alert">x</button>
    <strong>Success:</strong> <span id="successNotificationMessage">...</span>
</div>
<div class="alert alert-danger" style="display:none;float:right;width:30%" id="errorNotification">
    <button type="button" class="close" data-dismiss="alert">x</button>
    <strong>Error:</strong> <span id="errorNotificationMessage">...</span>
</div>
	
	
 

