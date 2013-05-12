<%@ page contentType="text/html; charset=UTF-8" language="java" errorPage="" import="java.util.ArrayList, db.DBInterface, models.*, util.PhoneNumberExtractor"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
    String id = request.getParameter("id");
    Entertainment e = (Entertainment)session.getAttribute("entertainment");
    String phone = PhoneNumberExtractor.extract(e.address);
    if (phone != null) {
        e.address = e.address.replaceAll(phone, "").trim();
    }
    int posPercent = (Integer)session.getAttribute("pos_percent");
    ArrayList<Weibo> posWeibo = (ArrayList<Weibo>)session.getAttribute("pos");
    ArrayList<Weibo> negWeibo = (ArrayList<Weibo>)session.getAttribute("neg");
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script src="js/jquery-2.0.0.min.js"></script>
<script src="js/bootstrap.js"></script>
<link rel="stylesheet" href="css/bootstrap.css" />
<link rel="stylesheet" href="css/remark.css" />
<style type="text/css">
@font-face
    {
      font-family: 'Rafa';
      font-size: 20px;
      src: url(font/Rafa_bold.ttf);
    }
    .info-section {
        margin: 0 auto;
        width:710px;
        height:250px;
        position: relative;
        background-color: #fff;
        border: 1px solid #ddd;
    }
    .weibo-section {
        margin: 0 auto;
        width:710px;
        height:300px;
        position: relative;
        background-color: #fff;
    }
    .pos-weibo-section {
        width:310px;
        height:100%;
    }
    .pos-weibo-list {
        width:100%;
        height:100%;
        border: 2px solid #e11;
        border-top-left-radius:3px;
        border-bottom-left-radius:3px;
        overflow:auto
    }
    .neg-weibo-section {
        width:310px;
        height:100%;
        float:right;
        text-align:right;
    }
    .neg-weibo-list {
        width:100%;
        height:100%;
        text-align:left;
        border: 2px solid #000;
        border-top-right-radius:3px;
        border-bottom-right-radius:3px;
        overflow:auto
    }
    .shop-title {
        margin: 11px 0 20px;
        font-family: "Microsoft YaHei",黑体ed1\4f53,Tahoma,Arial,sans-serif;
        font-size: 20px;
        font-weight: bold;
        line-height:25px;
    }
    .sep {
        list-style:none;
        width:100%;
        height:1px;
        background:#ddd;
        overflow:hidden;
        line-height:1px;
        margin:0px;
        padding:0px;
        font-size:0px;
    }

    dd .average {
        color: #C00;
    }
</style>
<script type="text/javascript">
function initialize() {
    var map = new BMap.Map('map');
    var geocoder = new BMap.Geocoder();
    geocoder.getPoint("<%=e.name%>", function(point){
        if (point) {
            map.centerAndZoom(point, 15);
            map.addOverlay(new BMap.Marker(point));
        }
    }, "北京市");
}

function loadScript() {
    var script = document.createElement("script");
    script.src = "http://api.map.baidu.com/api?v=1.5&ak=D9afd1763d4820178898956607a1269e&callback=initialize";
    document.body.appendChild(script);
}

window.onload = loadScript;
</script>
<style type="text/css">
    html{height:100%}
    body{height:100%;margin:0px;padding:0px}
    #container{height:100%}
</style>
<title><%=e.name%> - Gonow</title>
</head>
<body>
<div class="navbar navbar-fixed-top" style="padding-left:10px;">
<a class="brand" href="index.jsp">Gonow</a>
<ul class="breadcrumb">
  <li><a href="index.jsp">主页</a> <span class="divider">/</span></li>
  <li><a href="index.jsp">景点</a> <span class="divider">/</span></li>
  <li class="active"><%= e.name %></li>
</ul>
</div>
<div align="center" style="font-family:Rafa; line-height: 100px; margin-top:30px">
<font size="100px" color="#aaa">Gonow</font>
</div>
<div style="width:100%;">
<div class="info-section">
<div id="map" style="width:300px;height:250px;float:right"></div>
<div style="width:400px;height:250px;">
<dl>
<dd>
    <ul class="detail" style="list-style:none;">
    <li class="shopName">
    <h1 class="shop-title" itemprop="name itemreviewed"><%=e.name %></h1>
    </li>
    <li>
    <span class="item-rank-rst irr-star<%= e.rate %>"></span>
    </li>
    <li>
    人均
    <strong class="average"><span class="Price">¥</span><%=e.price %></strong>
    </li>
    <li>
    <strong>地址:</strong>
    <%=e.address %>
    </li>
    <li>
    <%if (phone != null) {%>
    <strong>电话:</strong>
    <%=phone %>
    </li>
    <%} %>
    </ul>
</dd>
</dl>
</div>
</div>
<div class="weibo-section">
<div class="neg-weibo-section">
<%int negPercent = (100 - posPercent);%>
<%=negPercent%>%<strong>踩</strong>
<div class="weibo-list neg-weibo-list">
<ul style="margin-left:0px;width:100%;list-style:none;">
<% for (Weibo weibo : negWeibo) { %>
<li style="margin:10px;">
<p><%=weibo.time %></p>
<p><%=weibo.text %></p>
</li>
<li class="sep"></li>
<% } %>
</ul>
</div>
</div>
<div class="pos-weibo-section">
<strong>顶</strong><%=posPercent%>%
<div class="weibo-list pos-weibo-list">
<ul style="margin-left:0px;width:100%;list-style:none;">
<% for (Weibo weibo : posWeibo) { %>
<li style="margin:10px;">
<p><%=weibo.time %></p>
<p><%=weibo.text %></p>
</li>
<li class="sep"></li>
<% } %>
</ul>
</div>
</div>
</div>
</div>
</body>
</html>