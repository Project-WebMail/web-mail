<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>주소록 추가</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/my_style.css">
    
                    <link type="text/css" rel="stylesheet" href="css/main_style.css" />
                                <link type="text/css" rel="stylesheet" href="css/addaddr.css" />
                                
    <script>
        function validateForm() {
            var ad_id = document.getElementsByName("addr_id")[0].value.trim();
            var ad_nick = document.getElementsByName("addr_nick")[0].value.trim();
            
            if (ad_id === '' || ad_nick === '') {
                alert('빈칸을 채워주세요.');
                return false;
            }
            alert('주소록에 추가되었습니다!');
            return true;
        }
        
         function cancel() {
            window.location.href = "addrbook";
        }
        
    </script>

</head>
<body>

<%@include file="../header.jspf"%>


  <div id="sidebar">
            <jsp:include page="../sidebar_previous_menu.jsp" />
            

         </div>
            
            
            <div id="main">
                
                <h1>주소록 추가</h1>
        <form action="addinsert.do" method="POST" onsubmit="return validateForm()">
            <label for="addr_id">사용자 ID</label>
            <input type="text" id="addr_id" name="addr_id" size="20" />

            <label for="addr_nick">저장명</label>
            <input type="text" id="addr_nick" name="addr_nick" size="20" />

            <div class="button-group">
                <input type="submit" value="추가" />
                <button id="btn" type="button" onclick="cancel()">취소</button>
            </div>
        </form>
            </div>

<br>
<br>
<br>
<%@include file="../footer.jspf"%>
</body>
</html>