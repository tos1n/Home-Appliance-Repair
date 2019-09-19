package com.example.ffive.seg2105ffiveproject;

public abstract class Account {
    private String _username;
    private String _password;
    private String _role;

    public Account(String username, String password, String role) {
        _username = username;
        _password = password;
        _role = role;
    }

    public void setUsername(String username) {
        _username = username;
    }
    public String getUsername() {
        return _username;
    }
    public void setPassword(String password) {
        _password = password;
    }
    public String getPassword() {
        return _password;
    }
    public void setRole(String role){ _role = role; }
    public String getRole(){ return _role; }
}
