<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="js/angular.min.js"></script>
<script type="text/javascript" src="js/app.js"></script>
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Test File Create</title>
</head>
<body ng-app>
  <div class="container" ng-controller="login">
     <%@ include file="jspf/login.jspf"%>
     <form action="op/file/create" method="post" ng-show="result.uuid != null">
        <input type="hidden" id="uuid" name="uuid" value="{{ result.uuid }}"><input type="hidden" id="service" name="service" value="{{ service }}">
        <label for="project">Project:</label>  
        <input type="text" name="project" />
        <br />
        <label for="parents">Parents:</label>
        <input type="text" name="parents" />
        <br />
        <label for="file">File:</label>
        <input type="text" name="file" />
        <br />
        <label for="directory">Directory:</label>    
        <input type="checkbox" name="directory" value="true">
        <br />
        <input type="submit" value="Enviar" />		
    </form>
  </div>
</body>
</html>