package deu.cse.spring_webmail.control;

import deu.cse.spring_webmail.service.JoinValidation;
import deu.cse.spring_webmail.service.UserDeleteInfo;
import deu.cse.spring_webmail.model.Pop3Agent;
import deu.cse.spring_webmail.model.UserAdminAgent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import javax.imageio.ImageIO;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 초기 화면과 관리자 기능(사용자 추가, 삭제)에 대한 제어기
 *
 * @author skylo
 */
@Controller
@PropertySource("classpath:/system.properties")
@Slf4j
public class SystemController {

    final String JdbcDriver = "com.mysql.cj.jdbc.Driver";
    final String JdbcUrl = "jdbc:mysql://localhost:3306/mail?serverTimezone=Asia/Seoul&useUnicode=true&characterEncoding=utf8";
    final String User = "jdbctester";
    final String Password = "znqk0419";

    @Autowired
    private ServletContext ctx;
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;

    @Value("${root.id}")
    private String ROOT_ID;
    @Value("${root.password}")
    private String ROOT_PASSWORD;
    @Value("${admin.id}")
    private String ADMINISTRATOR;  //  = "admin";
    @Value("${james.control.port}")
    private Integer JAMES_CONTROL_PORT;
    @Value("${james.host}")
    private String JAMES_HOST;

    @GetMapping("/")
    public String index() {
        log.debug("index() called...");
        session.setAttribute("host", JAMES_HOST);
        session.setAttribute("debug", "false");

        return "/index";
    }
    
    //index 페이지 컨트롤러
    @GetMapping("/index")
    public String addIndex() {
        return "index";
    }

    @RequestMapping(value = "/login.do", method = {RequestMethod.GET, RequestMethod.POST})
    public String loginDo(@RequestParam Integer menu) {
        String url = "";
        log.debug("로그인 처리: menu = {}", menu);
        switch (menu) {
            case CommandType.LOGIN:
                String host = (String) request.getSession().getAttribute("host");
                String userid = request.getParameter("userid");
                String password = request.getParameter("passwd");

                // Check the login information is valid using <<model>>Pop3Agent.
                Pop3Agent pop3Agent = new Pop3Agent(host, userid, password);
                boolean isLoginSuccess = pop3Agent.validate();

                // Now call the correct page according to its validation result.
                if (isLoginSuccess) {
                    if (isAdmin(userid)) {
                        // HttpSession 객체에 userid를 등록해 둔다.
                        session.setAttribute("userid", userid);
                        // response.sendRedirect("admin_menu.jsp");
                        url = "redirect:/admin_menu";
                    } else {
                        // HttpSession 객체에 userid와 password를 등록해 둔다.
                        session.setAttribute("userid", userid);
                        session.setAttribute("password", password);
                        // response.sendRedirect("main_menu.jsp");
                        url = "redirect:/main_menu";  // URL이 http://localhost:8080/webmail/main_menu 이와 같이 됨.
                        // url = "/main_menu";  // URL이 http://localhost:8080/webmail/login.do?menu=91 이와 같이 되어 안 좋음
                    }
                } else {
                    // RequestDispatcher view = request.getRequestDispatcher("login_fail.jsp");
                    // view.forward(request, response);
                    url = "redirect:/login_fail";
                }
                break;
            case CommandType.LOGOUT:
                session.invalidate();
                url = "redirect:/";  // redirect: 반드시 넣어야만 컨텍스트 루트로 갈 수 있음
                break;
            default:
                break;
        }
        return url;
    }

    @GetMapping("/login_fail")
    public String loginFail() {
        return "login_fail";
    }

    //회원가입 페이지 컨트롤러
    @GetMapping("/join")
    public String addJoin() {
        return "join";
    }

    @PostMapping("/joinId.do")
    public String joinIdDo(@RequestParam String userid, Model model) {

        JoinValidation vali = new JoinValidation();

        if (vali.joinIdCheck(userid)) {
            //이미 해당 데이터가 있음
            //사용가능한 아이디라고 팝업
            model.addAttribute("popupid", "이미 사용하고 있는 ID 입니다!");
        } else {
            //중복 아이디 팝업
            model.addAttribute("popupid", "사용 가능한 ID 입니다!");
        }
        return "join";
    }

    @PostMapping("/join.do")
    public String addJoinDo(@RequestParam String username, @RequestParam String userid, @RequestParam String passwd, @RequestParam String repasswd,
            Model model, RedirectAttributes attrs) {

        if (!JoinValidation.isNotBlank(username) || !JoinValidation.isNotBlank(userid)
                || !JoinValidation.isNotBlank(passwd) || !JoinValidation.isNotBlank(repasswd)) {
            model.addAttribute("popupblank", "공백은 입력할 수 없습니다.");
            return "join";
        }

        try {
            String cwd = ctx.getRealPath(".");
            UserAdminAgent agent = new UserAdminAgent(JAMES_HOST, JAMES_CONTROL_PORT, cwd,
                    ROOT_ID, ROOT_PASSWORD, ADMINISTRATOR);

            JoinValidation vali = new JoinValidation();

            if (vali.joinIdCheck(userid)) {
                // 이미 해당 데이터가 있음
                log.debug("아이디 존재 IF");
                model.addAttribute("popupid", "이미 사용하고 있는 ID 입니다!");
                return "join";
            }

            // 사용 가능한 ID
            log.debug("아이디 중복 없음");

            if (!JoinValidation.isPasswordValid(passwd, repasswd)) {
                // 비밀번호 유효성 검사 실패
                log.debug("비밀번호 유효성 검사 실패");
                model.addAttribute("popupMessage", "비밀번호가 같지 않습니다.");
                return "join";
            }

            if (agent.addUser(userid, passwd)) {
                if (vali.updateUserNick(userid, username)) {
                    model.addAttribute("popupsuccess", String.format("사용자(%s)의 회원가입이 완료되었습니다!", userid));
                    return "index";
                }
            }

        } catch (Exception ex) {
            log.error("join.do: 시스템 접속에 실패했습니다. 예외 = {}", ex.getMessage());
        }

        return "redirect:/index";
    }

    protected boolean isAdmin(String userid) {
        boolean status = false;

        if (userid.equals(this.ADMINISTRATOR)) {
            status = true;
        }

        return status;
    }

    @GetMapping("/main_menu")
    public String mainMenu(Model model) {
        Pop3Agent pop3 = new Pop3Agent();
        pop3.setHost((String) session.getAttribute("host"));
        pop3.setUserid((String) session.getAttribute("userid"));
        pop3.setPassword((String) session.getAttribute("password"));

        String messageList = pop3.getMessageList();
        model.addAttribute("messageList", messageList);
        return "main_menu";
    }

    @GetMapping("/admin_menu")
    public String adminMenu(Model model) {
        log.debug("root.id = {}, root.password = {}, admin.id = {}",
                ROOT_ID, ROOT_PASSWORD, ADMINISTRATOR);

        model.addAttribute("userList", getUserList());
        return "admin/admin_menu";
    }

    @GetMapping("/add_user")
    public String addUser() {
        return "admin/add_user";
    }

    @PostMapping("/add_user.do")
    public String addUserDo(@RequestParam String id, @RequestParam String password,
            RedirectAttributes attrs) {
        log.debug("add_user.do: id = {}, password = {}, port = {}",
                id, password, JAMES_CONTROL_PORT);

        try {
            String cwd = ctx.getRealPath(".");
            UserAdminAgent agent = new UserAdminAgent(JAMES_HOST, JAMES_CONTROL_PORT, cwd,
                    ROOT_ID, ROOT_PASSWORD, ADMINISTRATOR);

            // if (addUser successful)  사용자 등록 성공 팦업창
            // else 사용자 등록 실패 팝업창
            if (agent.addUser(id, password)) {
                attrs.addFlashAttribute("msg", String.format("사용자(%s) 추가를 성공하였습니다.", id));
            } else {
                attrs.addFlashAttribute("msg", String.format("사용자(%s) 추가를 실패하였습니다.", id));
            }
        } catch (Exception ex) {
            log.error("add_user.do: 시스템 접속에 실패했습니다. 예외 = {}", ex.getMessage());
        }

        return "redirect:/admin_menu";
    }

    @GetMapping("/delete_user")
    public String deleteUser(Model model) {
        log.debug("delete_user called");
        model.addAttribute("userList", getUserList());
        return "admin/delete_user";
    }

    /**
     *
     * @param selectedUsers <input type=checkbox> 필드의 선택된 이메일 ID. 자료형: String[]
     * @param attrs
     * @return
     */
    @PostMapping("delete_user.do") //수정
    public String deleteUserDo(@RequestParam String[] selectedUsers, RedirectAttributes attrs) {
        log.debug("delete_user.do: selectedUser = {}", List.of(selectedUsers));

        try {
            String cwd = ctx.getRealPath(".");
            UserAdminAgent agent = new UserAdminAgent(JAMES_HOST, JAMES_CONTROL_PORT, cwd,
                    ROOT_ID, ROOT_PASSWORD, ADMINISTRATOR);
            agent.deleteUsers(selectedUsers);  // 수정!!!
        } catch (Exception ex) {
            log.error("delete_user.do : 예외 = {}", ex);
        }

        return "redirect:/admin_menu";
    }

   @PostMapping("delete_info.do")
    public String deleteInfoDo(RedirectAttributes attrs, Model model) {
        String userid = (String) session.getAttribute("userid");

        log.debug("delete_info.do: user = {}", userid);

        try {
            UserDeleteInfo del = new UserDeleteInfo();
            del.deleteUserInfo(userid);
            model.addAttribute("popupexit", 
                    String.format("사용자(%s)의 정보를 정상적으로 삭제하고, 탈퇴하였습니다.", userid));
        } catch (SQLException e) {
            log.error("Error deleting user data", e);
            attrs.addFlashAttribute("msg", "사용자의 데이터 삭제 중 오류가 발생했습니다.");
        }

        return "index";
    }
    

    private List<String> getUserList() {
        String cwd = ctx.getRealPath(".");
        UserAdminAgent agent = new UserAdminAgent(JAMES_HOST, JAMES_CONTROL_PORT, cwd,
                ROOT_ID, ROOT_PASSWORD, ADMINISTRATOR);
        List<String> userList = agent.getUserList();
        log.debug("userList = {}", userList);

        //(주의) root.id와 같이 '.'을 넣으면 안 됨.
        userList.sort((e1, e2) -> e1.compareTo(e2));
        return userList;
    }

    @GetMapping("/img_test")
    public String imgTest() {
        return "img_test/img_test";
    }

    //주소록 보여주기
    @GetMapping("/addrbook")
    public String showAddrBook() {
        return "addr/addrbook";
    }

    //주소록 추가
    @GetMapping("/add_addr")
    public String addAddr() {
        return "addr/add_addr";
    }

    //주소록 추가
    @PostMapping("/addinsert.do")
    public String addAddrDo(@RequestParam String addr_id, @RequestParam String addr_nick, RedirectAttributes attrs) {

        String userid = (String) session.getAttribute("userid");

        log.debug("addinsert.do: send_name = {}, addr_id = {}, addr_nick = {}", userid, addr_id, addr_nick);

        Connection conn = null;
        PreparedStatement pStmt = null;

        try {
            Class.forName(JdbcDriver);
            conn = DriverManager.getConnection(JdbcUrl, User, Password);

            String sql = "INSERT INTO addrbook (send_name, receive_name, nick_name) VALUES (?, ?, ?)";
            pStmt = conn.prepareStatement(sql);

            pStmt.setString(1, userid);
            pStmt.setString(2, addr_id);
            pStmt.setString(3, addr_nick);

            int rowsAffected = pStmt.executeUpdate();

            if (rowsAffected > 0) {
                attrs.addFlashAttribute("msg", String.format("주소록에 사용자(%s) 추가에 성공하였습니다.", addr_id));
            } else {
                attrs.addFlashAttribute("msg", "주소록 사용자 추가에 실패했습니다.");
            }

        } catch (Exception ex) {
            log.error("addAddrDo: 시스템 접속에 실패했습니다. 예외 = {}", ex.getMessage());
            attrs.addFlashAttribute("msg", "주소록 사용자 추가에 실패했습니다.");
        } finally {
            try {
                if (pStmt != null) {
                    pStmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                log.error("addAddrDo: 리소스를 닫는 중에 오류가 발생했습니다. 예외 = {}", e.getMessage());
            }
        }

        return "redirect:/addrbook";
    }

    //주소록 상세 보여주기
    @GetMapping("/detail_addr")
    public String detailAddr(@RequestParam String receive_name, @RequestParam String nick_name, Model model) {
        model.addAttribute("receive_name", receive_name);
        model.addAttribute("nick_name", nick_name);
        return "addr/detail_addr";
    }

    // 주소록 업데이트
    @PostMapping("/update_addr.do")
    public String updateAddrDo(@RequestParam String receive_name, @RequestParam String nick_name, RedirectAttributes attrs) {
        log.debug("update_addr.do: addr_id = {}, addr_nick = {}", receive_name, nick_name);

        Connection conn = null;
        PreparedStatement pStmt = null;

        String userid = (String) session.getAttribute("userid");

        try {
            Class.forName(JdbcDriver);
            conn = DriverManager.getConnection(JdbcUrl, User, Password);

            String sql = "UPDATE addrbook SET nick_name = ? WHERE send_name = ? AND receive_name = ?";
            pStmt = conn.prepareStatement(sql);

            pStmt.setString(1, nick_name);
            pStmt.setString(2, userid);
            pStmt.setString(3, receive_name);

            int rowsAffected = pStmt.executeUpdate();

            if (rowsAffected > 0) {
                attrs.addFlashAttribute("msg", String.format("주소록 사용자(%s) 정보 수정에 성공하였습니다.", receive_name));
            } else {
                attrs.addFlashAttribute("msg", "주소록 사용자 정보 수정에 실패했습니다.");
            }

        } catch (Exception ex) {
            log.error("updateAddrDo: 시스템 접속에 실패했습니다. 예외 = {}", ex.getMessage());
            attrs.addFlashAttribute("msg", "주소록 사용자 정보 수정에 실패했습니다.");
        } finally {
            try {
                if (pStmt != null) {
                    pStmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                log.error("updateAddrDo: 리소스를 닫는 중에 오류가 발생했습니다. 예외 = {}", e.getMessage());
            }
        }

        return "redirect:/addrbook";
    }

    // 주소록 삭제
    @PostMapping("/delete_addr.do")
    public String deleteAddrDo(@RequestParam String receive_name, RedirectAttributes attrs) {
        log.debug("delete_addr.do: email = {}", receive_name);

        Connection conn = null;
        PreparedStatement pStmt = null;

        String userid = (String) session.getAttribute("userid");

        try {
            Class.forName(JdbcDriver);
            conn = DriverManager.getConnection(JdbcUrl, User, Password);

            String sql = "DELETE FROM addrbook WHERE send_name = ? AND receive_name = ?";
            pStmt = conn.prepareStatement(sql);

            pStmt.setString(1, userid);
            pStmt.setString(2, receive_name);

            int rowsAffected = pStmt.executeUpdate();

            if (rowsAffected > 0) {
                attrs.addFlashAttribute("msg", "주소록 사용자 정보 삭제에 성공했습니다.");
            } else {
                attrs.addFlashAttribute("msg", "주소록 사용자 정보 삭제에 실패했습니다.");
            }

        } catch (Exception ex) {
            log.error("deleteAddrDo: 시스템 접속에 실패했습니다. 예외 = {}", ex.getMessage());
            attrs.addFlashAttribute("msg", "주소록 사용자 정보 삭제에 실패했습니다.");
        } finally {
            try {
                if (pStmt != null) {
                    pStmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                log.error("deleteAddrDo: 리소스를 닫는 중에 오류가 발생했습니다. 예외 = {}", e.getMessage());
            }
        }

        return "redirect:/addrbook";
    }

    /**
     * https://34codefactory.wordpress.com/2019/06/16/how-to-display-image-in-jsp-using-spring-code-factory/
     *
     * @param imageName
     * @return
     */
    @RequestMapping(value = "/get_image/{imageName}")
    @ResponseBody
    public byte[] getImage(@PathVariable String imageName) {
        try {
            String folderPath = ctx.getRealPath("/WEB-INF/views/img_test/img");
            return getImageBytes(folderPath, imageName);
        } catch (Exception e) {
            log.error("/get_image 예외: {}", e.getMessage());
        }
        return new byte[0];
    }

    private byte[] getImageBytes(String folderPath, String imageName) {
        ByteArrayOutputStream byteArrayOutputStream;
        BufferedImage bufferedImage;
        byte[] imageInByte;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            bufferedImage = ImageIO.read(new File(folderPath + File.separator + imageName));
            String format = imageName.substring(imageName.lastIndexOf(".") + 1);
            ImageIO.write(bufferedImage, format, byteArrayOutputStream);
            byteArrayOutputStream.flush();
            imageInByte = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return imageInByte;
        } catch (FileNotFoundException e) {
            log.error("getImageBytes 예외: {}", e.getMessage());
        } catch (Exception e) {
            log.error("getImageBytes 예외: {}", e.getMessage());
        }
        return null;
    }

}
