/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.control;

/**
 *
 * @author skylo
 */
public final class CommandType {
    // private 생성자 추가하여 인스턴스화 방지
    private CommandType() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    public static final int READ_MENU = 1;
    public static final int WRITE_MENU = 2;

    public static final int ADD_USER_MENU = 3;
    public static final int DELETE_USER_MENU = 4;

    public static final int SEND_MAIL_COMMAND = 21;
    public static final int DELETE_MAIL_COMMAND = 41;
    public static final int DOWNLOAD_COMMAND = 51;
    
    public static final int ADD_USER_COMMAND = 61;
    public static final int DELETE_USER_COMMAND = 62;

    public static final int LOGIN = 91;
    public static final int LOGOUT = 92;
}
