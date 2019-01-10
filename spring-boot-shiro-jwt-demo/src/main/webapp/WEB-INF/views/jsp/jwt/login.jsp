<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>hello</title>
<script src="<c:url value="/common/js/jquery-3.3.1.js" />" type="application/javascript" ></script>
<script type="application/javascript">
;(function($){
    $(function(){
      $("#loginBtn").click(function(ev){
            $.ajax({
                "type" : "POST"
                ,"url" : $("#loginFrm").attr("action")
                ,"data" : $("#loginFrm").serializeArray()
                ,"success" : function(dat){
                               $("#msgp").text(JSON.stringify(dat));
                               $("#data").text(JSON.stringify(dat));
                            }
            });
      });
      $("#logoutBtn").click(function(ev){
          $.ajax({
              "type" : "POST"
              ,"url" : $("#logoutFrm").attr("action")
              ,"headers" : {"jwt" : JSON.parse($("#data").text()).jws}
              ,"success" : function(dat){
                              $("#msgp").text(JSON.stringify(dat));
                              $("#data").text("");
                          }
          });
    });
      $("#userhomeBtn").click(function(ev){
          $.ajax({
              "type" : "POST"
              ,"url" : $("#userhomeFrm").attr("action")
              ,"headers" : {"jwt" : JSON.parse($("#data").text()).jws}
              ,"success" : function(dat){
                            $("#msgp").html(dat);
                          }
          });
    });
    });
})(jQuery);
</script>
</head>
<body>
	<h1>login page</h1>
	<form action="<c:url value='/jwt/dologin'/>" method="POST" id="loginFrm" onSubmit="return false;">
		<label>User Name</label> <input type="text" name="username" maxLength="40" value="adm" />
		<label>Password</label> <input type="password" name="password" value="admpw" />
		<input type="button" id="loginBtn" value="login" />
	</form>
  
  <form action="<c:url value='/jwt/dologout'/>" method="POST" id="logoutFrm" onSubmit="return false;">
    <label>Logout</label> <input type="button" id="logoutBtn" value="logout" />
  </form>
  
  <form action="<c:url value='/jwt/userhome'/>" method="POST" id="userhomeFrm" onSubmit="return false;">
    <label>Post to the userhome</label> <input type="button" id="userhomeBtn" value="userhome" />
  </form>


  <div id="msgp" style="max-width:700px;word-wrap:break-word;">${message }</div>
  
  <div id="data" style="display:none;">
  </div>
</body>
</html>
