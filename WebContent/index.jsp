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
  </style>
</head>
<body>
<div style="font-family:Rafa; line-height: 100px; margin-top:50px">
<p class="text-center"><font size="100px">Gonow</font></p>
</div>
<div class="text-center">
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

<div id="tag_cloud">
<script>
  var fill = d3.scale.category20();

  d3.layout.cloud().size([800, 400])
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
        .append("svg")
        .attr("width", 800)
        .attr("height", 400)
        .append("g")
        .attr("transform", "translate(400,200)")
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
</div>

</body>
</html>