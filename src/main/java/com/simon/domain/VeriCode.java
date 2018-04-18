package com.simon.domain;

import javax.persistence.*;

/**
 * Created by simon on 2017/2/28.
 */

@Table
@Entity
public class VeriCode {
    @Id
    private Long id;

    private String phone;

    private Integer code;

    private Long createTime;

    private Integer expires;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getExpires() {
        return expires;
    }

    public void setExpires(Integer expires) {
        this.expires = expires;
    }
}