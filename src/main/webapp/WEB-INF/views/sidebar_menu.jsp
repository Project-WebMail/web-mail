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
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link type="text/css" rel="stylesheet" href="css/sidebar_admin_menu.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.7.2/animate.min.css"/>
        <script>
            function confirmExit() {
                return confirm("정말로 탈퇴하시겠습니까? 탈퇴하시면 모든 데이터가 삭제됩니다.");
            }
        </script>
    </head>
    <body>
        <br> <br>
        

              <span style="color: indigo"> <strong>사용자: <%= session.getAttribute("userid") %> </strong> </span> <br>
        <p style="margin-bottom: 30px;"> <a href="write_mail"><button> 메일 쓰기 </button></a> </p>
        <p style="margin-bottom: 30px;"> <a href="addrbook"><button> 주소록 편집 </button></a> </p>
        <p style="margin-bottom: 20px;"><a href="login.do?menu=<%= CommandType.LOGOUT %>"><button> 로그아웃 </button></a></p>
        <form action="delete_info.do" method="post" onsubmit="return confirmExit();">
            <input type="submit" value="회원탈퇴">
        </form>
    </body>
</html>
