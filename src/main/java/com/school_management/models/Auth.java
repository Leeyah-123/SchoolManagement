package com.school_management.models;

public class Auth {
    private String email, password;
    private String selection;

    public Auth() {
    }

    public Auth(String email, String password, String selection) {
        this.email = email;
        this.password = password;
        this.selection = selection;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSelection() {
        return selection;
    }

    public void setSelection() {
        this.selection = selection;
    }
}
