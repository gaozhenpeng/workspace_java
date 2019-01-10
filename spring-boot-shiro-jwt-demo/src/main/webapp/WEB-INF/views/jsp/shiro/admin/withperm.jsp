<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome aboard</title>
<script src="<c:url value="/common/js/jquery-3.3.1.js" />" type="application/javascript" ></script>
</head>
<body>

<shiro:hasPermission name="user:create"><%-- permission check can be defined in spring 'shiroFilter'--%>
<p>Welcome ${user.username}!</p>
<p>
	You're able to create user.
</p> 
</shiro:hasPermission>


<p><a href="<c:url value='/shiro/dologout'/>"><button>logout</button></a>
</p>
</body>
</html>
