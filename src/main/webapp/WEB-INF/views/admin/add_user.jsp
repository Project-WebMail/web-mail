<%-- 
    Document   : add_user.jsp
    Author     : jongmin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="deu.cse.spring_webmail.control.CommandType" %>

<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>사용자 추가 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
        <link type="text/css" rel="stylesheet" href="css/adduser.css" />
    </head>
    <body>
        <%@ include file="../header.jspf" %>

        <div id="sidebar">
            <jsp:include page="sidebar_admin_previous_menu.jsp" />
        </div>

        <div id="main">
            
             <div class="container">
            <div class="row justify-content-center">
                <div class="center"> <!-- 가운데 정렬을 위한 추가 -->
                    <h2>사용자 추가</h2>
            <p>추가로 등록할 사용자 ID와 암호를 입력해 주시기 바랍니다.</p>

            <form name="AddUser" action="add_user.do" method="POST">
                <div class="form-group">
                    <label for="id">사용자 ID</label>
                    <input type="text" name="id" id="id" required />
                </div>
                <div class="form-group">
                    <label for="password">암호</label>
                    <input type="password" name="password" id="password" required />
                </div>
                <div class="form-actions">
                    <button type="submit" name="register">등록</button>
                    <button type="reset" name="reset">초기화</button>
                </div>
            </form>
                </div>
            </div>
        </div>

           
        </div>

        <%@include file="../footer.jspf" %>
    </body>
</html>
