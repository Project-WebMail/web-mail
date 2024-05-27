<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>상세 주소록 정보</title>
    
     <script>
        function showAlert() {
            alert("수정되었습니다!");
        }
        
        function confirmDelete() {
            return confirm("정말로 삭제하시겠습니까?");
        }
        
        // 삭제 성공 메시지가 서버에서 전달되었는지 확인하고 알림 창
    <% if(request.getAttribute("msg") != null) { %>
        alert("<%= request.getAttribute("msg") %>");
    <% } %>
        
    </script>
    
</head>
<body>
    <div class="center-container">
        <h1>상세 주소록</h1>
        <hr />
        <form action="update_addr.do" method="POST">
            <table border="0" class="center-table">
                <thead>
                    <tr>
                        <th></th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>사용자 ID</td>
                        <td><input type="text" name="receive_name" size="20" value="${receive_name}" readonly/></td>
                    </tr>
                    <tr>
                        <td>저장명</td>
                        <td><input type="text" name="nick_name" size="20" value="${nick_name}"  /></td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <center><input type="submit" value="수정" onclick="showAlert()" /></center>
                            <center><button onclick="return confirmDelete();" formaction="delete_addr.do" formmethod="POST">삭제</button></center>
                        </td>
                    </tr>
                </tbody>
            </table>
        </form>
    </div>
</body>
</html>