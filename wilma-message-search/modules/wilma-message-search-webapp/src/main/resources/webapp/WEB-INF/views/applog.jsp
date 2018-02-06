<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Wilma Message Search</title>
<link href="./resources/css/bootstrap.min.css" rel="stylesheet" media="screen">
<link id="favicon" rel="shortcut icon" type="image/png" href="./resources/img/icon-wilma-message-search.png" />
</head>
<body>
  <div class="container">
    <%@include file="../include/navbar.html" %>
    <fieldset class="form-horizontal">
    <legend class="message-legend"d>Load information</legend>
    <div class="control-group">
    Queue size: <span id="span-queue-info" class="label"></span>
    </div>
    </fieldset>
    <fieldset class="form-horizontal">
    <legend>Log Files</legend>
    <div class="control-group">
      <span class="help-block">Click on a file to download its contents.</span>
    </div>
    <div class="control-group">
      <ol id="div-log-files"></ol>
    </div>
   </fieldset>
  </div>
  <script src="//code.jquery.com/jquery.js"></script>
  <script src="./resources/js/bootstrap.min.js"></script>
  <script src="./resources/js/applog.js"></script>
</body>
</html>
