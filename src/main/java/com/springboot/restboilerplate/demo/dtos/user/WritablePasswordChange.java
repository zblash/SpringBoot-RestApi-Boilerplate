package com.springboot.restboilerplate.demo.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WritablePasswordChange implements Serializable {

    @NotBlank(message = "{validation.notBlank}")
    @Size(min = 5,max = 90, message = "{validation.size}")
    private String password;

    @NotBlank(message = "{validation.notBlank}")
    @Size(min = 5,max = 90, message = "{validation.size}")
    private String passwordConfirmation;

}
