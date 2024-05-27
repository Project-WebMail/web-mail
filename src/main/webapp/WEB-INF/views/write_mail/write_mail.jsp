<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.Connection, java.sql.DriverManager, java.sql.PreparedStatement, java.sql.ResultSet, java.sql.SQLException" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>메일 쓰기 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
        
        <script type="text/javascript">
            function setRecipient() {
                var dropdown = document.getElementById("nameDropdown");
                var recipientField = document.getElementsByName("to")[0];
                recipientField.value = dropdown.value;
            }
        </script>
    </head>
    <body>
        <%@ include file="../header.jspf" %>

        <div id="sidebar">
            <jsp:include page="../sidebar_previous_menu.jsp" />
            <p>✏️주소록</p>
            <select id="nameDropdown" onchange="setRecipient()">
                <option value="" disabled selected>선택하세요</option>
                <%
                    final String JdbcDriver = "com.mysql.cj.jdbc.Driver";
                    final String JdbcUrl = "jdbc:mysql://localhost:3306/mail?serverTimezone=Asia/Seoul&useUnicode=true&characterEncoding=utf8";
                    final String User = "jdbctester";
                    final String Password = "znqk0419";

                    Connection conn = null;
                    PreparedStatement pStmt = null;
                    ResultSet rs = null;
                    
                    String userid = (String) session.getAttribute("userid");

                    try {
                        Class.forName(JdbcDriver);
                        conn = DriverManager.getConnection(JdbcUrl, User, Password);
                        
                        String sql = "SELECT receive_name, nick_name FROM addrbook WHERE send_name = ?";
                        
                        pStmt = conn.prepareStatement(sql);
                        pStmt.setString(1, userid);
                        
                        rs = pStmt.executeQuery();

                        while (rs.next()) {
                            String name = rs.getString("receive_name");
                            String nick = rs.getString("nick_name");
                            out.println("<option value=\"" + name + "\">" + name + " (" + nick + ")</option>");
                        }

                    } catch (Exception ex) {
                        out.println("오류가 발생했습니다. (발생 오류: " + ex.getMessage() + ")");
                    } finally {
                        if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
                        if (pStmt != null) try { pStmt.close(); } catch (SQLException ignore) {}
                        if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
                    }
                %>
            </select>
        </div>

        <div id="main">
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
                    <tr>
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
        <%@ include file="../footer.jspf" %>

        <script>
            function submitSaveForm() {
                var mailForm = document.getElementById("mailForm");
                var saveForm = document.getElementById("saveForm");
                saveForm.elements["to"].value = mailForm.elements["to"].value;
                saveForm.elements["cc"].value = mailForm.elements["cc"].value;
                saveForm.elements["subj"].value = mailForm.elements["subj"].value;
                saveForm.elements["body"].value = mailForm.elements["body"].value;
                saveForm.elements["file1"].value = mailForm.elements["file1"].value;
                saveForm.submit();
            }
        </script>
    </body>
</html>
