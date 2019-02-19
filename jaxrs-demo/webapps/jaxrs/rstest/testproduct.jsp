<%@ page language="java" contentType="text/html; charset=ISO-8859-1" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<title>jaxrs trigger form</title>
<script type="text/javascript" src="<%= request.getContextPath() %>/common/js/jquery-3.3.1.js" ></script>
<script type="text/javascript" >
    ;(function($){
        $(function(){
            $("#frm_prd input[type=button]").click(function(){
              var xml_dat = "<product><id>12</id><name>my xml name</name><description>a xml product</description><price>1299</price></product>";
              $.ajax({
                  url : $("#frm_prd").attr("action"),
                  type : 'POST',
                  data : xml_dat,
                  contentType: "application/xml",
                  dataType: "text",
                  success : function(data){console.log("xml success function");},
                  error : function (xhr, ajaxOptions, thrownError){  
                      console.log(xhr.status);          
                      console.log(thrownError);
                  }
              });
              json_dat = '{ "id" : "34", "name" : "my json name", "description" : "a json product", "price" : "3499" }';
              $.ajax({
                  url : $("#frm_prd").attr("action"),
                  type : 'POST',
                  data : json_dat,
                  contentType: "application/json",
                  dataType: "text",
                  success : function(data){console.log("json success function");},
                  error : function (xhr, ajaxOptions, thrownError){  
                      console.log(xhr.status);          
                      console.log(thrownError);
                  }
              });
            });
        });
    })(jQuery);
</script>
</head>
<body>

<div id="container">
   <div id="content" align="center">
    <form id="frm_prd" action="<%= request.getContextPath() %>/rest/product/create" method="post" onsubmit="return false;">
        <input type="button" name="submit" value="submit" />
    </form>
   </div>
</div>
</body>
</html>
