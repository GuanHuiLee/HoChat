package com.zgg.hochat.bean;

import java.io.Serializable;

public class RegisterZggInput implements Serializable {
    private String account;
    private String passwd;

    public RegisterZggInput(String account, String passwd) {
        this.account = account;
        this.passwd = passwd;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
