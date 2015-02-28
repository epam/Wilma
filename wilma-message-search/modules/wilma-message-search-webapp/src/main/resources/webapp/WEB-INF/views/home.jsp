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
      <span id="span-version" class="badge badge-info control-group"></span>
      <div class="control-group">
        <input id="searchedText" type="text" class="input span6" placeholder="Type text....">
        <button id="searchButton" type="button" class="btn btn-primary">Search</button>
          <a id="download-all-button" class="btn btn-primary" href="/downloadAll">Download All</a>
      </div>
      <div id="search-info-div" style="padding-top: 15px">
        <div id="search-info-div" class="alert" style="display: none">
          <button type="button" class="close">&times;</button>
          <span class="search-info-message"></span>
        </div>
      </div>
      <div class="control-group">
        <table class="table table-hover table-bordered">
          <thead>
            <tr>
              <th class="span1">#</th>
              <th>File name</th>
              <th>Request/Response Pair</th>
            </tr>
          </thead>
          <tbody id="searchResult">
            <tr>
              <td colspan="3">No files found</td>
            </tr>
          </tbody>
        </table>
        <div id="search-result-div" style="padding-top: 15px">
          <div id="search-result-div" class="alert" style="display: none">
           <button type="button" class="close">&times;</button>
           <span class="search-result-message"></span>
          </div>
        </div>
      </div>
    </fieldset>
  </div>
  <script src="http://code.jquery.com/jquery.js"></script>
  <script src="./resources/js/bootstrap.min.js"></script>
  <script src="./resources/js/message-search.js"></script>
</body>
</html>
