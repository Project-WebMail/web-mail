<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>주소록 보기</title>
                <link type="text/css" rel="stylesheet" href="css/main_style.css" />
                                <link type="text/css" rel="stylesheet" href="css/addrbook.css" />
    </head>
    <body>
        
                <div id="sidebar">
            <jsp:include page="../sidebar_previous_menu.jsp" />
            
                    <!-- 주소록 추가 버튼 -->
        <p style="margin-top: 50px;"><a href="add_addr"><button>주소록 추가</button></a> </p>
         </div>
         
         
         <div id="main">
             <h1>주소록</h1>
        <hr />

        <table border="1">
            <thead>
                <tr>
                    <th>사용자 ID</th>
                    <th>저장명</th>
                </tr>
            </thead>
            <tbody>
                <%
                final String JdbcDriver = "com.mysql.cj.jdbc.Driver";
                final String JdbcUrl = "jdbc:mysql://192.168.200.166:3306/mail?serverTimezone=Asia/Seoul&useUnicode=true&characterEncoding=utf8";
                final String User = "jdbctester";
                final String Password = "znqk0419";
                String userid = (String) session.getAttribute("userid");

                
                try {
                    Class.forName(JdbcDriver);

                    Connection conn = DriverManager.getConnection(JdbcUrl, User, Password);

                    String sql = "SELECT receive_name, nick_name FROM addrbook WHERE send_name = ?"; 
                    PreparedStatement pStmt = conn.prepareStatement(sql);
                    pStmt.setString(1, userid);

                    ResultSet rs = pStmt.executeQuery();

                    while (rs.next()) {
%>
                <tr>
                    <td><a href="detail_addr?receive_name=<%=rs.getString("receive_name")%>&nick_name=<%=rs.getString("nick_name")%>"><%=rs.getString("receive_name")%></a></td>
                    <td><%=rs.getString("nick_name")%></td>

                </tr>
                <%
                        }
                        rs.close();
                        pStmt.close();
                        conn.close();
                    } catch (Exception ex){
                        out.println("오류가 발생했습니다. (발생 오류: " + ex.getMessage() + ")");
                    }
                %>
            </tbody>
        </table>
        <br><br>

         </div>

    </body>
</html>