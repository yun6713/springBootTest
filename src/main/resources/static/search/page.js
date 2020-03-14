window.onload=function(){
	initTypes();
	$("#submit_search").click(function(){
		clickSubmitSearch();
	});
	$("#clear_doc").click(function(){
		$("#doc_list").html("");
	});
};

function initTypes(){
	var doc_types=$("#doc_types");
	var xmlHttp=getxmlHttp();
	xmlHttp.onreadystatechange=function(){
		if (xmlHttp.readyState==4 && xmlHttp.status==200){
			var types=JSON.parse(xmlHttp.responseText);
			console.log(types);
//			模板字符串中引用变量${}格式
			types.forEach(type=>doc_types.append(
					`<input type="checkbox" checked name="types" value="${type}"/>${type} `));
		}
	}
	xmlHttp.open("GET","/search/getTypes",true);
	xmlHttp.send();
}

//获取xmlHttp；赋值属性onreadystatechange、open、send
function getxmlHttp(){
	var xmlHttp;
	if (window.XMLHttpRequest){
		//  IE7+, Firefox, Chrome, Opera, Safari 浏览器执行代码
		xmlHttp=new XMLHttpRequest();
	}else{
		// IE6, IE5 浏览器执行代码
		xmlHttp=new ActiveXObject("Microsoft.xmlHttp");
	}
	return xmlHttp;
}

function clickSubmitSearch(){
	var search_info=$("#search_info").val();
	var nodes=$("input[name='types']:checked");
	var types="";
	for(var i=0;i<nodes.length;i++){
		types=types.concat($(nodes[i]).val().trim(),",");
	}
	console.log(types);
	var xmlHttp=getxmlHttp();
	xmlHttp.onreadystatechange=function(){
		if (xmlHttp.readyState==4 && xmlHttp.status==200){
			var docs=JSON.parse(xmlHttp.responseText);
			console.log(docs);
			var doc_list=$("#doc_list");
			docs.forEach(doc=>doc_list.append(`<h3>${doc.title}</h3>
			<p>${doc.type}</p>
			<div>${doc.content}</div>`))
		}
	}
	var formData = new FormData();
	formData.append("content",search_info);
	formData.append("types",types.substr(0,types.length-1));
	xmlHttp.open("POST","/search/searchDoc",true);
	xmlHttp.send(formData);
}


















