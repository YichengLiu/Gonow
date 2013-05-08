<%@ page contentType="text/html; charset=UTF-8" language="java" errorPage="" %>
<!doctype html>

<html lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <title>Gonow</title>
  <script src="js/jquery-2.0.0.min.js"></script>
  <script src="js/bootstrap.js"></script>
  <link rel="stylesheet" href="css/bootstrap.css" />
  <link rel="stylesheet" href="css/gallery-checkbox.css" />
  <script src="js/d3.js"></script>
  <script src="js/d3.layout.cloud.js"></script>
  <style> 
    @font-face
    {
      font-family: 'Rafa';
      font-size: 20px;
      src: url(font/Rafa_bold.ttf);
    }
    html {
        height:100%;
        margin:auto;
        width:100%;
    }
    body {
      background: url(img/street.jpg) no-repeat;
      -webkit-background-size: cover;
  -   -background-size: cover;
      -o-background-size: cover;
      background-size: cover;
      width:100%;
      margin:auto;
      height:100%;
    }
    #tag_cloud_bkg {
      background-color:rgba(0, 0, 0, 0.5);
    }
  </style>
</head>
<body>
<div align="center" style="font-family:Rafa; line-height: 100px; margin-top:50px">
<font size="100px" color="#ccc">Gonow</font>
</div>
<div align="center">
<div>
<form action="search" method="post" class="form-search">
<div class="input-append">
  <input class="span6 search-query" name="searchWord" type="text" placeholder="请输入目的地或关键词" />
  <input class="btn" type="submit" value="Go!" />
</div>
</form>
</div>
<div>
<form action="hot">
<input class="btn btn-info" type="submit" value="随便看看" />
</form>
</div>
</div>

<script>
  var fill = d3.scale.category20();

  d3.layout.cloud().size([800, 300])
      .words([{text:"呵呵", size:50},{text:"呵呵", size:50},{text:"呵呵", size:50},{text:"呵呵", size:50},{text:"呵呵", size:50},{text:"呵呵", size:50},{text:"呵呵", size:50},{text:"呵呵", size:50},{text:"呵呵", size:50},{text:"Test", size:20},{text:"热闹", size:90},{text:"好玩", size:100}])
      .rotate(function() { return ~~(Math.random() * 2) * 90; })
      .font("Impact")
      .fontSize(function(d) { return d.size; })
      .on("end", draw)
      .start();

  function draw(words) {
    d3.select("body")
        .append("div")
        .attr("align", "center")
        .attr("style", "margin-top:100px")
        .append("svg")
        .attr("width", 800)
        .attr("height", 300)
        .attr("id", "tag_cloud_bkg")
        .append("g")
        .attr("transform", "translate(400,150)")
        .selectAll("text")
        .data(words)
        .enter().append("text")
        .style("font-size", function(d) { return d.size + "px"; })
        .style("font-family", "Impact")
        .style("fill", function(d, i) { return fill(i); })
        .attr("text-anchor", "middle")
        .attr("transform", function(d) {
          return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
        })
        .text(function(d) { return d.text; });
  }
</script>

</body>
</html>