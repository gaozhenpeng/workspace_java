<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome aboard</title>
<script src="<c:url value="/common/js/jquery-2.1.4.js" />" type="application/javascript" ></script>
</head>
<body>

<shiro:hasPermission name="user:create"><%-- permission check can be defined in spring 'shiroFilter'--%>
<p>欢迎${user.username}登录</p>
<p>
	You're able to create user.
</p> 
</shiro:hasPermission>


<p><a href="<c:url value='/api/shiro/dologout'/>"><button>退出登录</button></a>
</p>
</body>
</html>
