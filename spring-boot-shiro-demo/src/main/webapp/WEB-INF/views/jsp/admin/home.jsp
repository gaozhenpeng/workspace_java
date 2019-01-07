<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome aboard</title>
<script src="<c:url value="/common/js/jquery-3.3.1.js" />" type="application/javascript" ></script>
</head>
<body>

<p>Welcome ${user.username}!
</p>

<p>
<a href="<c:url value='/shiro/dologout'/>"><button>logout</button></a> <br />
<a href="<c:url value='/shiro/admin/withperm'/>"><button>withperm</button></a> <br />
<a href="<c:url value='/shiro/admin/withrole'/>"><button>withrole</button></a> <br />
</p>

</body>
</html>
