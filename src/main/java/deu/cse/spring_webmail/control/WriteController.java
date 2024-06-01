/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.control;

import deu.cse.spring_webmail.model.SaveModel;
import deu.cse.spring_webmail.model.SmtpAgent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 메일 쓰기를 위한 제어기
 *
 * @author Prof.Jong Min Lee
 */
@Controller
@PropertySource("classpath:/system.properties")
@Slf4j
public class WriteController {

    final String JdbcDriver = "com.mysql.cj.jdbc.Driver";
    final String JdbcUrl = "jdbc:mysql://localhost:3306/mail?serverTimezone=Asia/Seoul&useUnicode=true&characterEncoding=utf8";
    final String User = "jdbctester";
    final String Password = "znqk0419";

    @Value("${file.upload_folder}")
    private String UPLOAD_FOLDER;
    @Value("${file.max_size}")
    private String MAX_SIZE;

    @Autowired
    private ServletContext ctx;
    @Autowired
    private HttpSession session;

    @GetMapping("/write_mail")
    public String writeMail() {
        log.debug("write_mail called...");
        session.removeAttribute("sender");  // 220612 LJM - 메일 쓰기 시는 
        return "write_mail/write_mail";
    }

    @PostMapping("/write_mail.do")
    public String writeMailDo(@RequestParam String to, @RequestParam String cc,
            @RequestParam String subj, @RequestParam String body,
            @RequestParam(name = "file1") MultipartFile upFile,
            RedirectAttributes attrs) {
        log.debug("write_mail.do: to = {}, cc = {}, subj = {}, body = {}, file1 = {}",
                to, cc, subj, body, upFile.getOriginalFilename());
        // FormParser 클래스의 기능은 매개변수로 모두 넘어오므로 더이상 필요 없음.
        // 업로드한 파일이 있으면 해당 파일을 UPLOAD_FOLDER에 저장해 주면 됨.
        if (!"".equals(upFile.getOriginalFilename())) {
            String basePath = ctx.getRealPath(UPLOAD_FOLDER);
            log.debug("{} 파일을 {} 폴더에 저장...", upFile.getOriginalFilename(), basePath);
            File f = new File(basePath + File.separator + upFile.getOriginalFilename());
            try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f))) {
                bos.write(upFile.getBytes());
            } catch (IOException e) {
                log.error("upload.do: 오류 발생 - {}", e.getMessage());
            }
        }
        boolean sendSuccessful = sendMessage(to, cc, subj, body, upFile);
        if (sendSuccessful) {
            attrs.addFlashAttribute("msg", "메일 전송이 성공했습니다.");
        } else {
            attrs.addFlashAttribute("msg", "메일 전송이 실패했습니다.");
        }

        return "redirect:/main_menu";
    }

    @PostMapping("/savePoint.do")
    public String savePointDo(@RequestParam String to, @RequestParam String cc,
            @RequestParam String subj, @RequestParam String body, RedirectAttributes attrs) {

        log.debug("savePoint.do: to = {}, cc = {}, subj = {}, body = {}",
                to, cc, subj, body);

        boolean sendSuccessful = saveMessage(to, cc, subj, body);
        if (sendSuccessful) {
            attrs.addFlashAttribute("msg", "임시 저장에 성공했습니다.");
        } else {
            attrs.addFlashAttribute("msg", "임시 저장에 실패했습니다.");
        }

        return "redirect:/main_menu";

    }

    private boolean saveMessage(String to, String cc, String subject, String body) {
        boolean status = false;

        String userid = (String) session.getAttribute("userid");

        try {
            Class.forName(JdbcDriver);
            Connection conn = DriverManager.getConnection(JdbcUrl, User, Password);

            log.debug("saveMessage.do: to = {}, cc = {}, subj = {}, body = {}",
                    to, cc, subject, body);

            // 1. Get the last save number for the user
            String getLastSaveNumberSql = "SELECT MAX(save_number) FROM savefile WHERE userid = ?";
            try (PreparedStatement getLastSaveNumberStmt = conn.prepareStatement(getLastSaveNumberSql)) {
                getLastSaveNumberStmt.setString(1, userid);
                ResultSet rs = getLastSaveNumberStmt.executeQuery();

                long nextSaveNumber = 1; // default to 1
                if (rs.next()) {
                    long lastSaveNumber = rs.getLong(1);
                    nextSaveNumber = lastSaveNumber + 1;
                }

                String sql = "INSERT INTO savefile (userid, save_number, message_receiver, message_chamzo,"
                        + "message_title,message_body,save_time) VALUES (?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement pStmt = conn.prepareStatement(sql);

                // 3. Set the values
                pStmt.setString(1, userid);
                pStmt.setLong(2, nextSaveNumber);
                pStmt.setString(3, to); // Check for null and set accordingly
                pStmt.setString(4, cc); // Check for null and set accordingly
                pStmt.setString(5, subject); // Check for null and set accordingly
                pStmt.setString(6, body); // Check for null and set accordingly
                pStmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));

                // 4. Execute the insert
                pStmt.executeUpdate();
                status = true;

                // Close the resources
                rs.close();
                getLastSaveNumberStmt.close();
                pStmt.close();
                conn.close();
            }

        } catch (Exception ex) {
            log.error("saveMessage.do: 시스템 접속에 실패했습니다. 예외 = {}", ex.getMessage());
        }

        return status;
    }

    @PostMapping("/saveCall.do")
    public String saveCallDo(Model model) {
        return "redirect:/save_mail";
    }

    @GetMapping("/save_mail")
    public String loadSavedData(Model model) {
        String userid = (String) session.getAttribute("userid");
        List<SaveModel> savedData = new ArrayList<>();

        log.debug("loadSavedData 호출");

        try {
            Class.forName(JdbcDriver);
            Connection conn = DriverManager.getConnection(JdbcUrl, User, Password);

            String sql = "SELECT save_number, message_receiver, message_title, save_time FROM savefile WHERE userid = ?";
            try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
                pStmt.setString(1, userid);

                ResultSet rs = pStmt.executeQuery();

                while (rs.next()) {
                    SaveModel mailData = new SaveModel();
                    mailData.setSaveNumber(rs.getInt("save_number"));
                    mailData.setTo(rs.getString("message_receiver"));
                    mailData.setTimestamp(rs.getTimestamp("save_time"));

                    String messageTitle = rs.getString("message_title");
                    if ("".equals(messageTitle) || messageTitle.isEmpty()) {
                        mailData.setSubject("<제목없음>");
                    } else {
                        mailData.setSubject(messageTitle);
                    }

                    String messageReceiver = rs.getString("message_receiver");
                    if (messageReceiver == "" || messageReceiver.isEmpty()) {
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
            }

        } catch (Exception ex) {
            log.error("loadSavedData: 시스템 접속에 실패했습니다. 예외 = {}", ex.getMessage());
        }

        model.addAttribute("savedData", savedData);
        return "save_mail";
    }

    @GetMapping("/loadSavedMail")
    public String loadSavedMail(@RequestParam long save_number, Model model) {
        String userid = (String) session.getAttribute("userid");
        SaveModel mailData = new SaveModel();

        log.debug("loadSavedMail 호출");

        try {
            Class.forName(JdbcDriver);
            Connection conn = DriverManager.getConnection(JdbcUrl, User, Password);

            String sql = "SELECT message_receiver, message_chamzo, message_title, message_body FROM savefile WHERE userid = ? AND save_number = ?";
            try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
                pStmt.setString(1, userid);
                pStmt.setLong(2, save_number);

                ResultSet rs = pStmt.executeQuery();

                if (rs.next()) {
                    mailData.setTo(rs.getString("message_receiver"));
                    mailData.setCc(rs.getString("message_chamzo"));
                    mailData.setSubject(rs.getString("message_title"));
                    mailData.setBody(rs.getString("message_body"));

                    log.debug("savedData.do:  receiver = {},chamzo = {}, title = {}, message_body = {}",
                            rs.getString("message_receiver"), rs.getString("message_chamzo"), rs.getString("message_title"), rs.getString("message_body"));
                }
                rs.close();
                pStmt.close();
                conn.close();
            }
        } catch (Exception ex) {
            log.error("loadSavedMail: 시스템 접속에 실패했습니다. 예외 = {}", ex.getMessage());
        }

        model.addAttribute("mailData", mailData);

        return "write_mail/write_mail";
    }

    @PostMapping("/deleteSavedMail.do")
    public String deleteSavedMailDo(@RequestParam int save_number, RedirectAttributes attrs) {
        String userid = (String) session.getAttribute("userid");

        try {
            Class.forName(JdbcDriver);
            Connection conn = DriverManager.getConnection(JdbcUrl, User, Password);

            String sql = "DELETE FROM savefile WHERE userid = ? AND save_number = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, userid);
            pStmt.setInt(2, save_number);

            int rowsAffected = pStmt.executeUpdate();
            if (rowsAffected > 0) {
                attrs.addFlashAttribute("msg", "임시 저장된 메일이 삭제되었습니다.");
            } else {
                attrs.addFlashAttribute("msg", "임시 저장된 메일 삭제에 실패했습니다.");
            }

            pStmt.close();
            conn.close();
        } catch (Exception ex) {
            log.error("deleteSavedMail: 시스템 접속에 실패했습니다. 예외 = {}", ex.getMessage());
        }

        return "redirect:/save_mail";
    }

    /**
     * FormParser 클래스를 사용하지 않고 Spring Framework에서 이미 획득한 매개변수 정보를 사용하도록 기존
     * webmail 소스 코드를 수정함.
     *
     * @param to
     * @param cc
     * @param sub
     * @param body
     * @param upFile
     * @return
     */
    private boolean sendMessage(String to, String cc, String subject, String body, MultipartFile upFile) {
        boolean status = false;

        // 1. toAddress, ccAddress, subject, body, file1 정보를 파싱하여 추출
        // 2.  request 객체에서 HttpSession 객체 얻기
        // 3. HttpSession 객체에서 메일 서버, 메일 사용자 ID 정보 얻기
        String host = (String) session.getAttribute("host");
        String userid = (String) session.getAttribute("userid");

        // 4. SmtpAgent 객체에 메일 관련 정보 설정
        SmtpAgent agent = new SmtpAgent(host, userid);
        agent.setTo(to);
        agent.setCc(cc);
        agent.setSubj(subject);
        agent.setBody(body);
        String fileName = upFile.getOriginalFilename();

        if (fileName != null && !"".equals(fileName)) {
            log.debug("sendMessage: 파일({}) 첨부 필요", fileName);
            File f = new File(ctx.getRealPath(UPLOAD_FOLDER) + File.separator + fileName);
            agent.setFile1(f.getAbsolutePath());
        }

        // 5. 메일 전송 권한 위임
        if (agent.sendMessage()) {
            status = true;
        }
        return status;
    }  // sendMessage()
}
