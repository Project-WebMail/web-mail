package deu.cse.spring_webmail.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
public class UserDeleteInfo {

    private static final Logger log = LoggerFactory.getLogger(UserDeleteInfo.class);

   final String JdbcDriver = "com.mysql.cj.jdbc.Driver";
    final String JdbcUrl = "jdbc:mysql://192.168.200.166:3306/mail?serverTimezone=Asia/Seoul&useUnicode=true&characterEncoding=utf8";
    final String User = "jdbctester";
    final String Password = "znqk0419";

    public UserDeleteInfo() {
        
    }

public void deleteUserInfo(String userid) throws SQLException {
        try (Connection conn = DriverManager.getConnection(JdbcUrl, User, Password)) {
            deleteEmails(conn, userid);
            deleteSaveFile(conn, userid);
            deleteAddrBook(conn, userid);
            deleteUser(conn, userid);
        }
    }

    private void deleteEmails(Connection conn, String userid) throws SQLException {
        String deleteEmailsSql = "DELETE FROM inbox WHERE repository_name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteEmailsSql)) {
            stmt.setString(1, userid);
            stmt.executeUpdate();
            log.debug("이메일 삭제 성공");

        }
    }

    private void deleteSaveFile(Connection conn, String userid) throws SQLException {
        String deleteSaveFileSql = "DELETE FROM savefile WHERE userid = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteSaveFileSql)) {
            stmt.setString(1, userid);
            stmt.executeUpdate();
             log.debug("임시 저장 삭제 성공");
        }
    }

    private void deleteAddrBook(Connection conn, String userid) throws SQLException {
        String deleteAddrBookSql = "DELETE FROM addrbook WHERE send_name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteAddrBookSql)) {
            stmt.setString(1, userid);
            stmt.executeUpdate();
             log.debug("주소록 삭제 성공");
        }
    }

    private void deleteUser(Connection conn, String userid) throws SQLException {
        String deleteUsersSql = "DELETE FROM users WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteUsersSql)) {
            stmt.setString(1, userid);
            stmt.executeUpdate();
             log.debug("유 삭제 성공");
        }
    }
}
