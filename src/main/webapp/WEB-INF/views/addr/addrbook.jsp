<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>주소록 보기</title>
    </head>
    <body>
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
        final String JdbcUrl = "jdbc:mysql://localhost:3306/mail?serverTimezone=Asia/Seoul&useUnicode=true&characterEncoding=utf8";
        final String User = "jdbctester";
        final String Password = "znqk0419";
                
                    try{
                        Class.forName(JdbcDriver);
                    
                        Connection conn = DriverManager.getConnection(JdbcUrl,User,Password);
                    
                        Statement stmt= conn.createStatement();
                    
                        String sql = "SELECT receive_name, nick_name FROM addrbook"; 
  
                        ResultSet rs = stmt.executeQuery(sql);
                        
                        
                        
                        while (rs.next()){
                %>
                <tr>
                    <td><a href="detail_addr?receive_name=<%=rs.getString("receive_name")%>&nick_name=<%=rs.getString("nick_name")%>"><%=rs.getString("receive_name")%></a></td>
                    <td><%=rs.getString("nick_name")%></td>

                </tr>
                <%
                        }
                        rs.close();
                        stmt.close();
                        conn.close();
                    } catch (Exception ex){
                        out.println("오류가 발생했습니다. (발생 오류: " + ex.getMessage() + ")");
                    }
                %>
            </tbody>
        </table>
        <br><br>
        <!-- 주소록 추가 버튼 -->
        <p style="margin-top: 10px;"><a href="add_addr"><button>주소록 추가</button></a> </p>





    </body>
</html>