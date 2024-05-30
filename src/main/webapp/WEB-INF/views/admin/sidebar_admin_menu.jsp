<%-- 
    Document   : sidebar_admin_menu.jsp
    Author     : jongmin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="deu.cse.spring_webmail.control.CommandType" %>

<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>웹메일 시스템 메뉴</title>
                        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link type="text/css" rel="stylesheet" href="css/sidebar_admin_menu.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.7.2/animate.min.css"/>
    </head>
    <body>
        <br> <br>
        
        <span style="color: indigo"> <strong>사용자: <%= session.getAttribute("userid") %> </strong> </span> <br>
        <%--
        <p><a href="UserAdmin.do?select=<%= CommandType.ADD_USER_MENU %>">사용자 추가</a></p>
        <p><a href="UserAdmin.do?select=<%= CommandType.DELETE_USER_MENU %>">사용자 제거</a></p>
        --%>
		<p style="margin-top: 10px;"><a href="add_user"><button>사용자 추가</button></a> </p>
        <p style="margin-top: 10px;"><a href="delete_user"><button>사용자 제거</button></a> </p>
        <p style="margin-top: 30px;"><a href="login.do?menu=<%= CommandType.LOGOUT %>"><button>로그아웃</button></a></p>
    </body>
</html>
