package com.springboot.restboilerplate.demo.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadableRegister implements Serializable {

    private String id;

    private String username;

    private String name;

    private String email;

    private boolean status;
}
