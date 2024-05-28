<%-- 
    Document   : sidebar_menu
    Created on : 2022. 6. 10., 오후 3:25:30
    Author     : skylo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="deu.cse.spring_webmail.control.CommandType"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>웹메일 시스템 메뉴</title>
        <script>
            function confirmDelete() {
                return confirm("정말로 탈퇴하시겠습니까? 탈퇴하시면 모든 데이터가 삭제됩니다.");
            }
        </script>
    </head>
    <body>
        <br> <br>
        
        <span style="color: indigo"> <strong>사용자: <%= session.getAttribute("userid") %> </strong> </span> <br>

        <p> <a href="main_menu"> 메일 읽기 </a> </p>
        <p> <a href="write_mail"> 메일 쓰기 </a> </p>
        <p> <a href="addrbook"> 주소록 편집 </a> </p>
        <p><a href="login.do?menu=<%= CommandType.LOGOUT %>">로그아웃</a></p>
        <p> <a href="delete_info.do" > 주소록 편집 </a> </p>
        <form action="delete_info.do" method="post" onsubmit="return confirmDelete();">
            <input type="submit" value="회원탈퇴">
        </form>
    </body>
</html>
