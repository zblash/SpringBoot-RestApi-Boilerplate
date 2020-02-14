package com.springboot.restboilerplate.demo.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WritableForgotPassword implements Serializable {

    private String email;

    private String username;
}
