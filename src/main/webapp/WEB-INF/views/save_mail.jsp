<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
    <head>
        <title>임시 저장된 데이터</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
                <link type="text/css" rel="stylesheet" href="css/savemail.css" />
    </head>
    <body>
        <%@include file="header.jspf" %>
        
        
        <div id="main">
            <h2>임시저장 보관함</h2>
            <table border="1">
                <thead>
                    <tr>
                        <th>저장 번호</th>
                        <th>보낼 사람</th>
                        <th>제목</th>
                        <th>시간</th>
                        <th>삭제</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="data" items="${savedData}">
                    <tr>
                        <td>${data.saveNumber}</td>
                        <td>${data.to}</td>
                        <td><a href="loadSavedMail?save_number=${data.saveNumber}">${data.subject}</a></td>
                        <td>${data.timestamp}</td>
                        <td>
                            <form action="deleteSavedMail.do" method="post" style="display:inline;">
                                <input type="hidden" name="save_number" value="${data.saveNumber}" />
                                <button type="submit">삭제</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <%@ include file="footer.jspf" %>
    </body>
</html>
