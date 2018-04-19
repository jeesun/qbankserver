package com.simon.domain;

import javax.persistence.*;

@Table
@Entity
public class QrCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//扫码id
    private String username;//扫码人
    private String token;//扫码人token
    private String sid;//扫码唯一标识
    private Boolean isOk;//是否已经扫码

    public QrCode() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Boolean getOk() {
        return isOk;
    }

    public void setOk(Boolean ok) {
        isOk = ok;
    }

    @Override
    public String toString() {
        return "QrCode{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", token='" + token + '\'' +
                ", sid='" + sid + '\'' +
                ", isOk=" + isOk +
                '}';
    }
}
