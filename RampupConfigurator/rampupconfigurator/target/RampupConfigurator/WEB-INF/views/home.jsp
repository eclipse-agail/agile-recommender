<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>

<head>

<title>Home</title>

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<style>
* {
    box-sizing: border-box;
}
.row::after {
    content: "";
    clear: both;
    display: block;
}
[class*="col-"] {
    float: left;
    padding: 15px;
}
.col-1 {width: 8.33%;}
.col-2 {width: 16.66%;}
.col-3 {width: 25%;}
.col-4 {width: 33.33%;}
.col-5 {width: 41.66%;}
.col-6 {width: 50%;}
.col-7 {width: 58.33%;}
.col-8 {width: 66.66%;}
.col-9 {width: 75%;}
.col-10 {width: 83.33%;}
.col-11 {width: 91.66%;}
.col-12 {width: 100%;}
html {
    font-family: "Lucida Sans", sans-serif;
}
.header {
    background-color: #9933cc;
    color: #ffffff;
    padding: 15px;
}
.menu ul {
    list-style-type: none;
    margin: 0;
    padding: 0;
}
.menu li {
    padding: 4px;
    margin-bottom: 3px;
    background-color :#33b5e5;
    color: #ffffff;
    box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24);
}
.menu li:hover {
    background-color: #0099cc;
}


.button {
    background-color: white;
    border: gray;
    color: #33b5e5;
    padding: 5px 15px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 20px;
    width: 100%;
    margin: 2px 2px;
    cursor: pointer;
}
.astext {
    background:none;
    border:none;
    margin:0;
    padding:0;
}s

</style>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script>
$(document).ready(
	    function() {
	    	var k = '${configurator}';
	    	$("input[value="+k+"]").click();
	    });
</script>

</head>
<body>

<div class="header">
<h1>AGILE Ramp-Up Configurator</h1>
</div>

<div class="row">


<div class="col-6 menu">
<ul>
<h1>Ramp-up Configurator</h1>
<li>
<form id="selectConfigurator" action="/RampupConfigurator/selectConfigurator" method="post">
Select a Ramp-up Configurator:<br><input type="radio" name="conf" id="conf" value="SmartHome"> SmartHome<br>
	  				  <input type="radio" name="conf" id="conf" value="Pilot-C" > Pilot-C<br>
	 				  <script type="text/javascript">
						   
							var k = '${configurator}';
							 $("input[value="+k+"]").click();
						  
					  </script>
					  <input type="hidden" name="kbUpdate" id="kbUpdate" value=""} /> 
					  
					  
  	   <input type="hidden" name="fd" id="fd" value= ""/>
    				  
	   <input class="button" type="submit" onclick="getInputs()" value="Select Ramp-up Configurator"> <br>
</form>


<b>${configurator}:</b>
<textarea id="myTextarea" rows="10" cols="87">
${fileData}
</textarea><br>
<form id="getConfiguration" action="/RampupConfigurator/getConfiguration" method="post">
	<input type="hidden" name="conf" id="conf" value=""/> 
	<input type="hidden" name="fd" id="fd" value=""/>
	<input class="button" type="submit" onclick="getInputs()" value="Get Configuration"> 
</form>


<li>
</ul>
</div>

<div class="col-6 menu">
<h1>Configuration Results</h1>
<p>${results}</p>

</div>
</div>

<script type="text/javascript">
function refreshPage(){
    window.location.href = "/RampupConfigurator/";
} 

function getInputs() {
	document.getElementById("selectConfigurator").elements.namedItem("conf").value= getFormValue();
	document.getElementById("getConfiguration").elements.namedItem("fd").value = getTextArea();
	document.getElementById("getConfiguration").elements.namedItem("conf").value = getFormValue();
}

function getTextArea() {
	return document.getElementById("myTextarea").value;
}

function getFormValue() {
	var val;
    // get list of radio buttons with specified name
    var radios = document.getElementById("selectConfigurator").elements["conf"];
    // loop through list of radio buttons
    for (var i=0, len=radios.length; i<len; i++) {
        if ( radios[i].checked ) { // radio checked?
            val = radios[i].value; // if so, hold its value in val
            break; // and break out of for loop
        }
    }
    return val; // return value of checked radio or undefined if none checked
}
</script>
 
<script type="text/javascript">
    window.onload = function () { 
	    var k = '${configurator}';
	    $("input[value="+k+"]").click();
    }
</script>

</body>
</html>
