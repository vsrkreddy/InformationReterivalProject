<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
<script type="text/javascript" > 
	$(document).ready(function(){
	
		//$("#fb_button").click(function(){
		$(document).on('click', '#fb_button', function(){ 
			var irrelavant = [];
			var relavant = [];
			$(":checkbox").each(function(){
				if(this.checked){
					relavant.push(this.id);
				}else{
					irrelavant.push(this.id);
				}
			
			}
			
			);
			$("#results_form").text("");
			$.each(relavant,function(){
				var content = '<input id="'+this+'" class="class1" type="checkbox" name>&nbsp&nbsp<a href="file:///C:/Users/vsrkreddy/Downloads/docsnew/'+this+'"><label class="class1" id="label">'+this+'</label></a></br>'

	
				
				
				$("#results_form").append(content);
			});
			$.each(irrelavant,function(){
				var content = '<input id="'+this+'" class="class1" type="checkbox" name>&nbsp&nbsp<a href="file:///C:/Users/vsrkreddy/Downloads/docsnew/'+this+'"><label class="class1" id="label">'+this+'</label></a></br>'

	
				
				
				$("#results_form").append(content);
			});
			$("#results_form").append('<input type="button" id="fb_button" value="OK!">');
		});
	});
	
	
	</script>
</head>
<body style="background-color:lightgrey;">
<h1> <b>RESULTS AFTER INCLUDING TERM PROXIMITY</b></h1>

<%List finalResults= (List)session.getAttribute("finalResults");
	Iterator i = finalResults.iterator();
	while(i.hasNext()){
		String doc = (String)i.next();
		
	%><div id="division">
	<form id="results_form">
	<input id="<%=doc%>" class="class1" type="checkbox" name>
		&nbsp&nbsp<a href="file:///C:/Users/vsrkreddy/Downloads/docsnew/<%=doc%>"><label class="class1" id="label"><%=doc%></label></a></br>
	<%} %>
	<input type="button" id="fb_button" value="OK!">
	
	</form></div>
	
</body>
</html>