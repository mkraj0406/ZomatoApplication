package com.slf.zmt.requestdto;

import com.slf.zmt.entity.User;



public class UserRequestDto {

    private String name;
    private String email;
    private String password;
    private User.Role role;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public User.Role getRole() {
        return role;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(User.Role role) {
        this.role = role;
    }

}
