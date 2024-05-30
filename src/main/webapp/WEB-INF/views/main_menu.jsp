<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>주메뉴 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
                <link type="text/css" rel="stylesheet" href="css/mainmenu.css" />
        <script>
            function confirmDelete(msgId) {
                if (confirm("정말로 이 메일을 삭제하시겠습니까?")) {
                    window.location.href = "delete_mail.do?msgid=" + msgId;
                }
            }
            
            <c:if test="${!empty msg}">
                alert("${msg}");
            </c:if>
        </script>
    </head>
    <body>
        <%@include file="header.jspf"%>

        <div id="sidebar">
            <jsp:include page="sidebar_menu.jsp" />
        </div>

        <div id="main">
            ${messageList}
        </div>

        <%@include file="footer.jspf"%>
    </body>
</html>
