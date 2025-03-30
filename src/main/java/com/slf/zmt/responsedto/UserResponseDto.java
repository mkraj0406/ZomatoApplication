package com.slf.zmt.responsedto;

import com.slf.zmt.entity.User;
import lombok.Getter;
import lombok.Setter;

public class UserResponseDto {

        private Long userId;
        private String name;
        private String email;

        private User.Role role;

    public void setUserId(Long userId) {
                this.userId = userId;
        }

        public void setName(String name) {
                this.name = name;
        }

        public void setEmail(String email) {
                this.email = email;
        }



        public void setRole(User.Role role) {
                this.role = role;
        }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public User.Role getRole() {
        return role;
    }
}
