package com.cuiyn.bookpush.model;

import com.cuiyn.bookpush.tools.BCryptPasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String userName;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String pushEmail;
    @Column(nullable = false)
    private int pushLimit;
    @Column(nullable = false)
    private Date checkinDate = new Date();

    private String encryptPassword(String password) {
        String encrypted = new BCryptPasswordEncoder().encode(password);
        return encrypted;
    }

    public User() {
        super();
    }

    public User(String userName, String password, String pushEmail) {
        this.userName = userName;
        this.password = encryptPassword(password);
        this.pushEmail = pushEmail;
        this.pushLimit = 5;
    }

    public User(String userName, String password, String pushEmail, Integer pushLimit) {
        this.userName = userName;
        this.password = encryptPassword(password);
        this.pushEmail = pushEmail;
        this.pushLimit = pushLimit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPushEmail() {
        return pushEmail;
    }

    public void setPushEmail(String pushEmail) {
        this.pushEmail = pushEmail;
    }

    public int getPushLimit() {
        return pushLimit;
    }

    public void setPushLimit(int pushLimit) {
        this.pushLimit = pushLimit;
    }

    public Date getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(Date checkinDate) {
        this.checkinDate = checkinDate;
    }
}
