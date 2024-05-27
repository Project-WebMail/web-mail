/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.model;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Hyeon
 */
public class SaveModel {
    @Getter @Setter protected int saveNumber;
    @Getter @Setter protected String to;
    @Getter @Setter protected String cc;
    @Getter @Setter protected String subject;
    @Getter @Setter protected String body;
    @Getter @Setter protected Timestamp timestamp;
    
    public SaveModel(){
    
    }

    public SaveModel(int saveNumber, String to, String cc, String subject, String body, Timestamp timestamp) {
        this.saveNumber = saveNumber;
        this.to = to;
        this.cc = cc;
        this.subject = subject;
        this.body = body;
        this.timestamp = timestamp;
    }
}
