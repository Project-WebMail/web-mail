<%-- 
    Document   : join
    Created on : 2022. 6. 10., 오후 2:19:43
    Author     : skylo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="deu.cse.spring_webmail.control.CommandType"%>

<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>회원가입 화면</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link type="text/css" rel="stylesheet" href="css/join.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.7.2/animate.min.css"/>
    </head>
    <body>
        <%-- 
        <%@include file="header.jspf"%>
        --%>

        <div id="join_form">
            <p id="FormTitle">회원가입</p>
            <form name="JoinUser" action="join.do" method="POST">
                <label for="username" class="form-label" style="color: #F2F2F2;">이름</label>
                <input type="text" id="userid" name="username" class="form-control" aria-describedby="passwordHelpBlock">
                <br>

                <label for="userid" class="form-label" style="color: #F2F2F2;">아이디</label>
                <input type="text" id="userid" name="userid" class="form-control" aria-describedby="passwordHelpBlock">
                <br>

                <label for="passwd" class="form-label" style="color: #F2F2F2;">암호</label>
                <input type="password" id="passwd" name="passwd" class="form-control">
                <br>
                <label for="repasswd" class="form-label" style="color: #F2F2F2;">암호 재확인</label>
                <input type="password" id="passwd" name="repasswd" class="form-control">
                <button class="btn btn-dark" style="width: 100%; margin-top: 10px;">회원가입 완료</button><br>
            </form>

            <script>
                var popupMessage = "${popupMessage}";
                if (popupMessage.trim() !== "") {
                    alert(popupMessage);
                }
                var popupid = "${popupid}";
                if (popupid.trim() !== "") {
                    alert(popupid);
                }
            </script>


            <a href="index"><button class="btn btn-warning" style="width: 100%; margin-top: 50px;">돌아가기</button><br></a>
        </div>



        <%-- 
        <%@include file="footer.jspf"%>
        --%>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>

    </body>
</html>
