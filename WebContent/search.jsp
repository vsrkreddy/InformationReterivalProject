<!DOCTYPE html>
<!-- saved from url=(0023)http://www.example.com/ -->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>search</title>
<meta name="viewport" content="width=device-width, initial-scale=1">

<style type="text/css">
.bold{
font-weight:bold;
text-decoration:underline;}
.textbox{
width: 300px;
}
#first{
height:30px;
width:300px;
}
#search{
width=300px;}


</style>
<script type="text/javascript">
// this is java script
var searchval = document.getElementById("first").value;



document.getElementById("search").onclick=function()
{

alert("empty");

}

</script>
</head>
<body style="background-color:lightgrey;">
<div>
	<center>
			<h1 class="bold"> Search</h1></hr>


	<form action="MainServlet">
		<input id="first" type="text" name="first" placeholder="search here.."  size="35"/>
		<br/> 
		<br/>
		
		
		<!--<select> <option>relevant</option>
                <option>relevant</option>
				<option>not-relevant</option>
				<option>none</option>
		</select>-->
		
	<!--checkbox
	<input type="checkbox" /> dog-->
				<br/>
				<br/>
				
		<input  type="submit"  value="search" onclick="alert"/> &nbsp &nbsp
		<input type="reset" value="clear"/>
	</form>
</div>
		


</body>
</html>