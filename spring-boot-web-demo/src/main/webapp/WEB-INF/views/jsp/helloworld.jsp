<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<!-- staic resources path in spring-boot
    classpath:/META-INF/resources/
    classpath:/resources/
    classpath:/static/
    classpath:/public/
-->
<script src="<c:url value="/js/jquery-2.1.4.js" />" type="application/javascript" ></script>
</head>
<body><c:out value="${message}" default="No Message" />
</body>
</html>
