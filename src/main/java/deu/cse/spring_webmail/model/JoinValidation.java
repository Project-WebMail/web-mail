/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Hyeon
 */
@Slf4j
public class JoinValidation {

    final String JdbcDriver = "com.mysql.cj.jdbc.Driver";
    final String JdbcUrl = "jdbc:mysql://localhost:3306/mail?serverTimezone=Asia/Seoul&useUnicode=true&characterEncoding=utf8";
    final String User = "jdbctester";
    final String Password = "znqk0419";

    public boolean updateUserNick(String username, String nn) {
        boolean success = false;

        try {
            // 데이터베이스 커넥션 가져오기
            Connection conn = DriverManager.getConnection(JdbcUrl, User, Password);

            // SQL 쿼리 실행하여 사용자 정보 업데이트
            String sql = "UPDATE users SET nickname = ? WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nn);
            pstmt.setString(2, username);

            int rowsAffected = pstmt.executeUpdate();

            // 업데이트가 성공적으로 수행되었는지 확인
            if (rowsAffected > 0) {
                success = true;
                log.debug("updateUserEmail: 이름 업로드 성공");
            }
            // 리소스 닫기
            pstmt.close();
            conn.close();
        } catch (Exception ex) {
            log.error("updateUserEmail: 시스템 접속에 실패했습니다. 예외 = {}", ex.getMessage());
        }
        return success;
    }

    public boolean joinIdCheck(String userid) {
        boolean status = false;

        log.debug("joinIdCheck() called");

        try {

            Class.forName(JdbcDriver);
            Connection conn = DriverManager.getConnection(JdbcUrl, User, Password);

            String sql = "SELECT username from mail.users where username = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);

            pStmt.setString(1, userid);

            ResultSet rs = pStmt.executeQuery();

            if (rs.next()) {
                status = true;
            } else {
                status = false;
            }
        } catch (Exception ex) {
            log.error("joinIdCheck 예외: {}", ex.getMessage());
            status = false;
        }
        // 5: 상태 반환
          return status;
    }

    public static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isPasswordValid(String password, String confirmPassword) {
        return password != null && confirmPassword != null && password.equals(confirmPassword);
    }
}
