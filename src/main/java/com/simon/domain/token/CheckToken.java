package com.simon.domain.token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by simon on 2017/2/22.
 */

public class CheckToken {
    private Long exp;
    private String user_name;
    private ArrayList<String> authorities;
    private String client_id;
    private ArrayList<String> scope;

    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public ArrayList<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(ArrayList<String> authorities) {
        this.authorities = authorities;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public List<String> getScope() {
        return scope;
    }

    public void setScope(ArrayList<String> scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "CheckToken{" +
                "exp=" + exp +
                ", user_name='" + user_name + '\'' +
                ", authorities=" + authorities +
                ", client_id='" + client_id + '\'' +
                ", scope=" + scope +
                '}';
    }

    public boolean isNull(){
        return (null==exp)
                ||(null==user_name)||"".equals(user_name)||"null".equals(user_name)
                ||(null==authorities)||(authorities.isEmpty())
                ||(null==client_id)||(client_id.isEmpty())||("null".equals(client_id))
                ||(null==scope)||(scope.isEmpty());
    }

    public boolean isNotNull(){
        return (null!=exp)
                &&(null!=user_name)&&!"".equals(user_name)&&!"null".equals(user_name)
                &&(null!=authorities)&&!(authorities.isEmpty())
                &&(null!=client_id)&&!(client_id.isEmpty())&&!("null".equals(client_id))
                &&(null!=scope)&&!(scope.isEmpty());
    }
}
