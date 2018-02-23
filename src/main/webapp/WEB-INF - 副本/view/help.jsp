<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">
    <title>根据城市名称设置中心点</title>
    <link rel="stylesheet" href="http://cache.amap.com/lbs/static/main1119.css"/>
    <style>
        input[type="text"] {
            height: 25px;
            outline: none;
            border: 0;
            border: 1px solid #CCCCCC;
            padding: 0 4px;
        }
    </style>
    <script src="http://webapi.amap.com/maps?v=1.4.2&key=02e2c94af4a58f3d10a3d1bc67b37fe4"></script>
    <script type="text/javascript" src="http://cache.amap.com/lbs/static/addToolbar.js"></script>
</head>
<body>
<div id="container"></div>
<div class="button-group">
    <input id="cityName" class="inputtext" placeholder="请输入城市的名称" type="text"/>
    <input id="query" class="button" value="到指定的城市" type="button"/>
</div>
<script>
    var map = new AMap.Map('container', {
        resizeEnable: true
    });
    //设置城市
    AMap.event.addDomListener(document.getElementById('query'), 'click', function() {
        var cityName = document.getElementById('cityName').value;
        if (!cityName) {
            cityName = '岳麓区';
        }
        map.setCity(cityName);
    });
</script>
</body>
</html>


