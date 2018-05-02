<html>
<head>
  <title>index</title>
</head>
<style>
body {
	height: 3000px;
}
</style>
<script>
	window.onload = function () {
		const semesterValue = document.querySelector(".semestr");
		const sheduleInner = document.querySelector(".shedule_inner");
		
		document.querySelector(".sem_action").onclick = function () {
			const value = semesterValue.value;
			if (!isNaN(value)) {
				$AJAX("GET", "/getShedule/"+value,null, function(response) {
					const list = JSON.parse(response);
					sheduleInner.innerHTML = "";
					for (var i = 0; i < list.length; i++) {
					 	const textArea = document.createElement("div");
					 	textArea.innerHTML = list[i].replace(/(?:\r\n|\r|\n)/g, '<br />');
						sheduleInner.append(textArea);
					}
				}); 
			}
		}
		
		function $AJAX(method, url, json, callback) {
			const request = new XMLHttpRequest();
			request.open(method, url, true);
			if (method == "PUT" || method == "POST") {
				request.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
			}
				
			var timeout = 10000;
    	        var timer = window.setTimeout(function () { request.abort(); console.log("request is aborted");},timeout);
				
    	        request.onreadystatechange = function () {
	                if (request.readyState == 4 && request.status == 200) {
	                    window.clearTimeout(timer);
	                    callback(request.responseText);
					}else if(request.readyState == 4 && !request.status == 200){
						console.log("request is not OK(200)");
					}
	         };
	         if (json) {
	            	request.send(json);
	         } else {
	            	request.send();
	         }
	     }
	}
</script>
<body>
  <h1>Все группы:</h1>
  <#if error??><h2>${error}</h2></#if>
	
<#if groups??>
  <ul>
    <#list groups as group>
      <li>
      	<h3>${group.name} ${group.plan.title}<h3>
      	<#list group.plan.subjectList as plan>
	      	<ul>
	      		<li>семестр ${plan.semestr}</li>
				<li>предмет ${plan.subjects.title}</li>
				<li>пар в семестр ${plan.numberOfClasses}</li>
			</ul>
			<br>
      	</#list>
      </li>
    </#list>
  </ul>
  	<div>
  		<span>Расписание для семестра</span>
		<input type='textfield' class='semestr'/>
		<button class='sem_action'>Получить</button>
	</div>
</#if>
<ul style='display: inline-flex;' class='shedule_inner'></ul>
</body>
</html>