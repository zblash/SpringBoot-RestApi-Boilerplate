package com.springboot.restboilerplate.demo.dtos.user;

import com.springboot.restboilerplate.demo.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WritableRegister implements Serializable {

    @NotBlank(message = "{validation.notBlank}")
    private String username;

    @NotBlank(message = "{validation.notBlank}")
    private String name;

    @NotBlank(message = "{validation.notBlank}")
    @Size(min = 5,max = 90, message = "{validation.size}")
    private String password;

    @Email(message = "{validation.email}")
    @NotBlank(message = "{validation.notBlank}")
    private String email;

    @NotNull(message = "{validation.notNull}")
    private RoleType roleType;

    private boolean status;

}
