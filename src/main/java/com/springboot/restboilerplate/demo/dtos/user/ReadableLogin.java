package com.springboot.restboilerplate.demo.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadableLogin implements Serializable {

    private String username;

    private String name;

    private String email;

    private String role;

    private String token;

    private ReadableLogin(LoginDTOBuilder builder){
        this.username = builder.username;
        this.name = builder.name;
        this.email = builder.email;
        this.role = builder.role;
        this.token = builder.token;
    }

    public static class LoginDTOBuilder {

        private String username;

        private String name;

        private String email;

        private String role;

        private String token;

        public LoginDTOBuilder(String token){
            this.token = token;
        }

        public LoginDTOBuilder userName(String userName){
            this.username = userName;
            return this;
        }

        public LoginDTOBuilder name(String name){
            this.name = name;
            return this;
        }

        public LoginDTOBuilder email(String email){
            this.email = email;
            return this;
        }

        public LoginDTOBuilder role(String role){
            this.role = role;
            return this;
        }

        public ReadableLogin build() {
            return new ReadableLogin(this);
        }

    }
}

