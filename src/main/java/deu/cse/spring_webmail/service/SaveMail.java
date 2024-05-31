package deu.cse.spring_webmail.service;

import deu.cse.spring_webmail.model.SaveModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaveMail {
    private static final Logger log = LoggerFactory.getLogger(SaveMail.class);

    final String JdbcDriver = "com.mysql.cj.jdbc.Driver";
    final String JdbcUrl = "jdbc:mysql://192.168.200.166:3306/mail?serverTimezone=Asia/Seoul&useUnicode=true&characterEncoding=utf8";
    final String User = "jdbctester";
    final String Password = "znqk0419";
    
    public void SaveMail(){
        
    }

    public List<SaveModel> loadSavedData(String userid) {
        List<SaveModel> savedData = new ArrayList<>();

        log.debug("loadSavedData 호출");

        try {
            Class.forName(JdbcDriver);
            Connection conn = DriverManager.getConnection(JdbcUrl, User, Password);

            String sql = "SELECT save_number, message_receiver, message_title, save_time FROM savefile WHERE userid = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, userid);

            ResultSet rs = pStmt.executeQuery();

            while (rs.next()) {
                SaveModel mailData = new SaveModel();
                mailData.setSaveNumber(rs.getInt("save_number"));
                mailData.setTo(rs.getString("message_receiver"));
                mailData.setTimestamp(rs.getTimestamp("save_time"));

                String messageTitle = rs.getString("message_title");
                if (messageTitle == null || messageTitle.isEmpty()) {
                    mailData.setSubject("<제목없음>");
                } else {
                    mailData.setSubject(messageTitle);
                }

                String messageReceiver = rs.getString("message_receiver");
                if (messageReceiver == null || messageReceiver.isEmpty()) {
                    mailData.setTo("<수신자없음>");
                } else {
                    mailData.setTo(messageReceiver);
                }

                savedData.add(mailData);

                log.debug("mailData: save_number = {}, receiver = {}, title = {}",
                        mailData.getSaveNumber(), mailData.getTo(), mailData.getSubject());

                log.debug("savedData.do: save_number = {}, receiver = {}, title = {}, save_time = {}",
                        rs.getLong("save_number"), rs.getString("message_receiver"), rs.getString("message_title"), rs.getTimestamp("save_time"));
            }

            rs.close();
            pStmt.close();
            conn.close();
        } catch (Exception ex) {
            log.error("loadSavedData: 시스템 접속에 실패했습니다. 예외 = {}", ex.getMessage());
        }

        return savedData;
    }

    public SaveModel loadSavedMail(String userid, long saveNumber) {
        SaveModel mailData = new SaveModel();

        log.debug("loadSavedMail 호출");

        try {
            Class.forName(JdbcDriver);
            Connection conn = DriverManager.getConnection(JdbcUrl, User, Password);

            String sql = "SELECT message_receiver, message_chamzo, message_title, message_body FROM savefile WHERE userid = ? AND save_number = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, userid);
            pStmt.setLong(2, saveNumber);

            ResultSet rs = pStmt.executeQuery();

            if (rs.next()) {
                mailData.setTo(rs.getString("message_receiver"));
                mailData.setCc(rs.getString("message_chamzo"));
                mailData.setSubject(rs.getString("message_title"));
                mailData.setBody(rs.getString("message_body"));

                log.debug("savedData.do:  receiver = {}, chamzo = {}, title = {}, message_body = {}",
                        rs.getString("message_receiver"), rs.getString("message_chamzo"), rs.getString("message_title"), rs.getString("message_body"));
            }

            rs.close();
            pStmt.close();
            conn.close();
        } catch (Exception ex) {
            log.error("loadSavedMail: 시스템 접속에 실패했습니다. 예외 = {}", ex.getMessage());
        }

        return mailData;
    }

    public boolean deleteSavedMail(String userid, int saveNumber) {
        try {
            Class.forName(JdbcDriver);
            Connection conn = DriverManager.getConnection(JdbcUrl, User, Password);

            String sql = "DELETE FROM savefile WHERE userid = ? AND save_number = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, userid);
            pStmt.setInt(2, saveNumber);

            int rowsAffected = pStmt.executeUpdate();

            pStmt.close();
            conn.close();

            return rowsAffected > 0;
        } catch (Exception ex) {
            log.error("deleteSavedMail: 시스템 접속에 실패했습니다. 예외 = {}", ex.getMessage());
            return false;
        }
    }
}
