<%@ page contentType="text/html; charset=UTF-8" language="java" errorPage="" import="java.util.ArrayList, models.Entertainment, java.util.Collections, java.util.Comparator" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%
    String query = (String)session.getAttribute("query");
    String sort = (String)request.getParameter("sort");
    ArrayList<Entertainment> list = (ArrayList<Entertainment>)session.getAttribute("list");
%>
<title><%=query%> 的结果</title>
<script src="js/jquery-2.0.0.min.js"></script>
<script src="js/bootstrap.js"></script>
<link rel="stylesheet" href="css/bootstrap.css" />
<link rel="stylesheet" href="css/remark.css" />
<style>
    @font-face
    {
      font-family: 'Rafa';
      font-size: 20px;
      src: url(font/Rafa_bold.ttf);
    }
    .detail strong {
        margin-right: 10px;
        font-weight: normal;
        color: #666;
    }
    .shopName {
        color:#66c;
        font-size: 20px;
        font-weight: bold;
        margin-right: 5px;
        text-decoration: none;
    }
    .shopName a {
        text-decoration: none;
    }
    dd .average {
        color: #C00;
    }
    .section-list {
        margin: 0 auto;
        width:800px;
        border:solid #e4e4e4;
        border-width: 1px 1px 1px 1px;
    }
</style>
</head>
<body>
<div class="navbar navbar-fixed-top" style="padding-left:10px;">
<a class="brand" href="index.jsp">Gonow</a>
<ul class="breadcrumb">
  <li><a href="index.jsp">主页</a> <span class="divider">/</span></li>
  <li><a href="index.jsp">搜索</a> <span class="divider">/</span></li>
  <li class="active"><%= query %></li>
</ul>
</div>
<div align="center" style="font-family:Rafa; line-height: 100px; margin-top:30px">
<font size="100px" color="#aaa">Gonow</font>
</div>
<div style="width:100%;">
<div class="section-list">
<div style="background-color:#f5f5f5; padding-right:60px;" align="right">
<div class="btn-group">
  <a class="btn btn-info dropdown-toggle" data-toggle="dropdown" href="#">
    <i class="icon-th-list icon-white"></i> 排序方式
    <span class="caret"></span>
  </a>
  <ul class="dropdown-menu"  align="left">
    <li><a href="list.jsp?sort=1"><i class="icon-arrow-up"></i> 按价格从低到高</a></li>
    <li><a href="list.jsp?sort=2"><i class="icon-arrow-down"></i> 按价格从高到低</a></li>
    <li><a href="list.jsp?sort=3"><i class="icon-heart"></i> 按评分从高到低</a></li>
  </ul>
</div>
</div>
<dl>
<%
if (sort != null) {
    Comparator<Entertainment> c = Entertainment.getComparator(sort);
    if (c != null) {
        Collections.sort(list, Entertainment.getComparator(sort));
    }
}
int length = list.size();
for (int i = 0; i < length; i++) {
    Entertainment e = list.get(i);
%>
    <dd>
    <ul class="remark" style="float:right;list-style:none;width:150px;text-align:left">
    <li>
    <span class="item-rank-rst irr-star<%= e.rate %>"></span>
    </li>
    <li>
    <strong class="average"><span class="Price">¥</span><%=e.price %></strong>
    </li>
    </ul>
    <ul class="detail" style="list-style:none;">
    <li class="shopName">
    <a href="/Gonow/detail.jsp?id=<%=e.id%>"><%=e.name%></a><br/>
    </li>
    <li>
    <strong>地址:</strong>
    <%=e.address %>
    </li>
    <li>
    <strong>关键词:</strong>
    <%= e.getKeywordList() %>
    </li>
    <li>ehe</li>
    <li>ehe</li>
    </ul>
    </dd>
<%
    if (i != length - 1) {
%>
    <hr/>
<%
    }
}
%>
</dl>
</div>
</div>
</body>
</html>