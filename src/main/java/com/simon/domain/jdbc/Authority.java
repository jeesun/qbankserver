package com.simon.domain.jdbc;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "authorities")
@Entity
public class Authority {
    private String usnername;
    private String authority;

    public String getUsnername() {
        return usnername;
    }

    public void setUsnername(String usnername) {
        this.usnername = usnername;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
