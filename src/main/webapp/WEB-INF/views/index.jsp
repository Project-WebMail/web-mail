<%-- 
    Document   : index
    Created on : 2022. 6. 10., 오후 2:19:43
    Author     : skylo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="deu.cse.spring_webmail.control.CommandType"%>

<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>로그인 화면</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link type="text/css" rel="stylesheet" href="css/index.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.7.2/animate.min.css"/>
    </head>
    <body>
        <%-- 
        <%@include file="header.jspf"%>
--%>

     <div id="login_form">
    <form method="POST" action="login.do?menu=<%= CommandType.LOGIN %>">
        <label for="userid" class="form-label" style="color: #F2F2F2;">사용자</label>
        <input type="text" id="userid" name="userid" class="form-control" aria-describedby="passwordHelpBlock">
        <br>
        <label for="passwd" class="form-label" style="color: #F2F2F2;">암호</label>
        <input type="password" id="passwd" name="passwd" class="form-control">
        <button type="submit" class="btn btn-dark" name="B1" style="width: 100%; margin-top: 10px;">로그인</button><br>
        <a href="join"><button type="button" class="btn btn-light" style="width: 100%; margin-top: 10px;">회원가입</button></a>
        
    </form>
</div>
        
    <script> var popupsuccess = "${popupsuccess}";
                if (popupsuccess.trim() !== "") {
                    alert(popupsuccess);
                    }
                            
                var popupexit= "${popupexit}";
                if (popupexit.trim() !== "") {
                    alert(popupexit);
                    }
            </script>        

               

                <%-- 
                <%@include file="footer.jspf"%>
                --%>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
        
    </body>
</html>
