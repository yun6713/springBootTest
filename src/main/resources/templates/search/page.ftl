<!DOCTYPE html>  
<html lang="en">  
<head>  
    <meta charset="UTF-8">
	<title>检索页面</title>
	<script src="../../static/search/page.js"></script>
	<script
	  src="https://code.jquery.com/jquery-2.2.4.min.js"
	  integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44="
	  crossorigin="anonymous"></script>
</head>  
<body> 
	<div>
	  <p>
		检索内容：<input type="text" id="search_info" placeholder="输入检索内容"/>
		<input type="button" id="submit_search" value="提交" /> 
		<input type="button" id="clear_doc" value="清空" />
	  </p>
	  <div id="doc_types"></div>
	</div> 
	<div id="doc_list"></div>
</body>  
</html>  