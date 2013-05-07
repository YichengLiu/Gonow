<%@ page contentType="text/html; charset=UTF-8" language="java" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%
    String id = request.getParameter("id");
%>
<title><%=id%> 的结果</title>
</head>
<body>
<div id="container" class="container_l"> 
<div id="content_left">
<%=id%>的细节
</div>
</div>
</body>
</html>