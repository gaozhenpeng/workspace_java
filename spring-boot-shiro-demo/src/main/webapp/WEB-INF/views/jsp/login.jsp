<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>hello</title>
<script src="<c:url value="/common/js/jquery-3.3.1.js" />" type="application/javascript" ></script>
</head>
<body>
	<h1>login page</h1>
	<form action="<c:url value='/shiro/dologin'/>" method="POST">
		<label>User Name</label> <input type="text" name="username" maxLength="40" />
		<label>Password</label> <input type="password" name="password" />
		<input type="submit" value="login" />
	</form>

	<P>${message }</P>

</body>
</html>