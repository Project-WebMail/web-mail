<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>주소록 추가</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/my_style.css">
    <style>
        .center-container {
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        .center-table {
            margin: 0 auto;
        }
        hr {
            width: 100%;
            border: none;
            height: 1px;
            background-color: #ccc; /* Adjust color as needed */
            margin: 20px 0; /* Add some space above and below the line */
        }

    </style>
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

<div class="center-container">
    <h1>주소록 추가</h1>

    <hr />
    <form action="addinsert.do" method="POST" onsubmit="return validateForm()">
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
                    <td><input type="text" name="addr_id" size="20" /></td>
                </tr>
                <tr>
                    <td>저장명</td>
                    <td><input type="text" name="addr_nick" size="20" /></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <center>
                            <input type="submit" value="추가" />
                            <button type="button" onclick="cancel()">취소</button>
                        </center>
                    </td>
                </tr>
            </tbody>
        </table>
    </form>
</div>

<br>
<br>
<br>
<%@include file="../footer.jspf"%>
</body>
</html>