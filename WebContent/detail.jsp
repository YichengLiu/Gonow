<%@ page contentType="text/html; charset=UTF-8" language="java" errorPage="" import="db.DBInterface, models.Entertainment"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
    String id = request.getParameter("id");
    Entertainment e = DBInterface.getInstance().getEntertainmentById(id);
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
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
<title><%=id%> 的结果</title>
</head>
<body>
<div id="map" style="width:500px;height:320px"></div>
</body>
</html>