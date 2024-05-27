<%-- 
    Document   : write_mail.jsp
    Author     : jongmin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<%-- @taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core" --%>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>메일 쓰기 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <body>
        <%@include file="../header.jspf"%>

        <div id="sidebar">
            <jsp:include page="../sidebar_previous_menu.jsp" />
        </div>

        <div id="main">
            <%-- <jsp:include page="mail_send_form.jsp" /> --%>
            <form id="mailForm" enctype="multipart/form-data" method="POST" action="write_mail.do" >
                <table>
                    <tr>
                        <td> 수신 </td>
                        <td> <input type="text" name="to" size="80" value="${mailData.to}"> </td>
                    </tr>
                    <tr>
                        <td>참조</td>
                        <td> <input type="text" name="cc" size="80" value="${mailData.cc}">  </td>
                    </tr>
                    <tr>
                        <td> 메일 제목 </td>
                        <td> <input type="text" name="subj" size="80" value="${mailData.subject}">  </td>
                    </tr>
                    <tr>
                        <td colspan="2">본  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 문</td>
                    </tr>
                    <tr>  <%-- TextArea    --%>
                        <td colspan="2">
                            <textarea rows="15" name="body" cols="80">${mailData.body}</textarea> 
                        </td>
                    </tr>
                    <tr>
                        <td>첨부 파일</td>
                        <td> <input type="file" name="file1"  size="80">  </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <input type="submit" value="메일 보내기">
                            <input type="reset" value="다시 입력">
                        </td>
                    </tr>
                </table>
            </form>
            <form id="saveForm" enctype="multipart/form-data" method="POST" action="savePoint.do" >
                <button class="btn btn-dark" style="width: 50%; margin-top: 10px; margin-left: 25%" type="button" onclick="submitSaveForm()">임시저장하기</button><br>
                <!-- 여기서 hidden input 요소를 사용하여 맨 위의 form의 데이터를 받아올 수 있습니다. -->
                <input type="hidden" name="to" />
                <input type="hidden" name="cc" />
                <input type="hidden" name="subj" />
                <input type="hidden" name="body" />
                <input type="hidden" name="file1" />
            </form>
            <form enctype="multipart/form-data" method="POST" action="saveCall.do" >
                <button class="btn btn-dark" style="width: 50%; margin-top: 10px; margin-left: 25%">임시저장 불러오기</button><br>
            </form>
        </div>
        <%@include file="../footer.jspf"%>

        <script>
            function submitSaveForm() {
                // 맨 위의 form에서 데이터를 가져와 두 번째 form에 설정합니다.
                var mailForm = document.getElementById("mailForm");
                var saveForm = document.getElementById("saveForm");
                saveForm.elements["to"].value = mailForm.elements["to"].value;
                saveForm.elements["cc"].value = mailForm.elements["cc"].value;
                saveForm.elements["subj"].value = mailForm.elements["subj"].value;
                saveForm.elements["body"].value = mailForm.elements["body"].value;
                saveForm.elements["file1"].value = mailForm.elements["file1"].value;
                // 두 번째 form을 서버로 제출합니다.
                saveForm.submit();
            }
        </script>

    </body>
</html>
