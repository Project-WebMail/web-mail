package deu.cse.spring_webmail.model;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
public class UserDeleteInfo {

    private static final Logger log = LoggerFactory.getLogger(UserDeleteInfo.class);

    private final String JdbcUrl = "your_jdbc_url";
    private final String User = "your_db_user";
    private final String Password = "your_db_password";

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
        }
    }

    private void deleteSaveFile(Connection conn, String userid) throws SQLException {
        String deleteSaveFileSql = "DELETE FROM savefile WHERE userid = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteSaveFileSql)) {
            stmt.setString(1, userid);
            stmt.executeUpdate();
        }
    }

    private void deleteAddrBook(Connection conn, String userid) throws SQLException {
        String deleteAddrBookSql = "DELETE FROM addrbook WHERE send_name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteAddrBookSql)) {
            stmt.setString(1, userid);
            stmt.executeUpdate();
        }
    }

    private void deleteUser(Connection conn, String userid) throws SQLException {
        String deleteUsersSql = "DELETE FROM users WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteUsersSql)) {
            stmt.setString(1, userid);
            stmt.executeUpdate();
        }
    }
}
