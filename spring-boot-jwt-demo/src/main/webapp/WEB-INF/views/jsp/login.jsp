<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>hello</title>
<script src="<c:url value="/common/js/jquery-2.1.4.js" />" type="application/javascript" ></script>
<script type="application/javascript">
;(function($){
    $(function(){
      $("#loginBtn").click(function(ev){
            $.ajax({
                "type" : "POST"
                ,"url" : $("#frm").attr("action")
                ,"data" : $("#frm").serializeArray()
                ,"success" : function(dat){
                              $("#msgp").text(JSON.stringify(dat));
                            }
            });
      });
    });
})(jQuery);
</script>
</head>
<body>
	<h1>login page</h1>
	<form action="<c:url value='/jwt/dologin'/>" method="POST" id="frm" onSubmit="return false;">
		<label>User Name</label> <input type="text" name="username" maxLength="40" />
		<label>Password</label> <input type="password" name="password" />
		<input type="button" id="loginBtn" value="login" />
	</form>

	<P id="msgp">${message }</P>

</body>
</html>