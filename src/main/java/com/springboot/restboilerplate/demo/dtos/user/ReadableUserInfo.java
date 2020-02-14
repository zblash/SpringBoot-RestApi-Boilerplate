package com.springboot.restboilerplate.demo.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadableUserInfo implements Serializable {

    private String id;

    private String username;

    private String name;

    private String email;

    private String role;


    private ReadableUserInfo(Builder builder){
        this.id = builder.id;
        this.username = builder.username;
        this.name = builder.name;
        this.email = builder.email;
        this.role = builder.role;
    }

    public static class Builder {

        private String id;

        private String username;

        private String name;

        private String email;

        private String role;

        public Builder(String username){
            this.username = username;
        }

        public Builder id(String id){
            this.id = id;
            return this;
        }

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder email(String email){
            this.email = email;
            return this;
        }

        public Builder role(String role){
            this.role = role;
            return this;
        }

        public ReadableUserInfo build() {
            return new ReadableUserInfo(this);
        }

    }
}

