package com.springboot.restboilerplate.demo.controllers;

import com.springboot.restboilerplate.demo.configs.constants.ApplicationContstants;
import com.springboot.restboilerplate.demo.configs.security.JWTAuthToken.JWTGenerator;
import com.springboot.restboilerplate.demo.dtos.user.*;
import com.springboot.restboilerplate.demo.errors.BadRequestException;
import com.springboot.restboilerplate.demo.errors.HttpMessage;
import com.springboot.restboilerplate.demo.models.User;
import com.springboot.restboilerplate.demo.services.user.UserService;
import com.springboot.restboilerplate.demo.utils.MailUtil;
import com.springboot.restboilerplate.demo.utils.RandomStringGenerator;
import com.springboot.restboilerplate.demo.utils.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
public class AuthController {

    private final UserService userService;

    private final MailUtil mailUtil;

    public AuthController(UserService userService, MailUtil mailUtil) {
        this.userService = userService;
        this.mailUtil = mailUtil;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody WritableLogin writableLogin, WebRequest request) {
        User userDetails = userService.findByUserName(writableLogin.getUsername());

        if (userService.loginControl(writableLogin.getUsername(), writableLogin.getPassword())) {
            Map<String, Object> body = new HashMap<>();
            body.put("role", userDetails.getRole().getName());
            body.put("userId", userDetails.getId());
            String jwt = JWTGenerator.generate(ApplicationContstants.JWT_SECRET, null, 86_400_000, body);

            ReadableLogin.LoginDTOBuilder loginDTOBuilder = new ReadableLogin.LoginDTOBuilder(jwt);
            loginDTOBuilder.email(userDetails.getEmail());
            loginDTOBuilder.name(userDetails.getName());
            loginDTOBuilder.userName(userDetails.getUsername());
            String role = userDetails.getRole().getName().split("_")[1];
            loginDTOBuilder.role(role);
            ReadableLogin readableLogin = loginDTOBuilder
                    .build();
            return ResponseEntity.ok(readableLogin);
        }

        HttpMessage httpMessage = new HttpMessage(HttpStatus.UNAUTHORIZED);
        httpMessage.setMessage("Given username or password incorrect");
        httpMessage.setPath(((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return new ResponseEntity<>(httpMessage, httpMessage.getStatus());

    }

    @PostMapping("/sign-up")
    public ResponseEntity<ReadableRegister> signUp(@Valid @RequestBody WritableRegister writableRegister, Locale locale) {
        User user = UserMapper.writableRegisterToUser(writableRegister);
        if (userService.canRegister(user)) {
            user.setStatus(false);
            user.setActivationToken(RandomStringGenerator.generateId());
            User createdUser = userService.create(user, writableRegister.getRoleType());
            mailUtil.sendActivationMail(user.getEmail(), user.getActivationToken(), locale);
            return ResponseEntity.ok(UserMapper.userToReadableRegister(createdUser));
        }
        throw new BadRequestException("Username or email already registered");
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<HttpMessage> forgottenPassword(@RequestBody WritableForgotPassword writableForgotPassword, WebRequest request, Locale locale) {
        User user;
        if (!writableForgotPassword.getEmail().isEmpty()) {
            user = userService.findByEmail(writableForgotPassword.getEmail());
        } else if (!writableForgotPassword.getUsername().isEmpty()) {
            user = userService.findByUserName(writableForgotPassword.getUsername());
        } else {
            throw new BadRequestException("One of username or email is required");
        }
        user.setPasswordResetToken(RandomStringGenerator.generateId());
        user.setResetTokenExpireTime(
                Date.from(LocalDateTime.now().plus(1, ChronoUnit.DAYS).atZone(ZoneId.systemDefault()).toInstant()));
        mailUtil.sendPasswordResetMail(user.getEmail(), user.getPasswordResetToken(), locale);
        userService.update(user.getId(), user);
        HttpMessage httpMessage = new HttpMessage(HttpStatus.OK);
        httpMessage.setMessage("Password reset token has been sent to to your email");
        httpMessage.setPath(((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return ResponseEntity.ok(httpMessage);
    }

    @PostMapping("/password-reset")
    public ResponseEntity<HttpMessage> passwordReset(@Valid @RequestBody WritablePasswordReset writablePasswordReset, WebRequest request) {
        User user = userService.findByResetToken(writablePasswordReset.getToken());
        Date now = new Date();
        if (now.compareTo(user.getResetTokenExpireTime()) < 0 &&
                writablePasswordReset.getPassword().equals(writablePasswordReset.getPasswordConfirmation())) {
            userService.changePassword(user, writablePasswordReset.getPassword());
            HttpMessage httpMessage = new HttpMessage(HttpStatus.OK);
            httpMessage.setMessage("Password changed");
            httpMessage.setPath(((ServletWebRequest) request).getRequest().getRequestURL().toString());
            return ResponseEntity.ok(httpMessage);
        }
        throw new BadRequestException("Fields not matching");
    }

    @PutMapping("/activation")
    public ResponseEntity<ReadableUserInfo> userActivation(@Valid @RequestBody WritableActivation writableActivation) {
        User user = userService.findByActivationToken(writableActivation.getActivationToken());
        user.setStatus(true);
        return ResponseEntity.ok(UserMapper.userToReadableUserInfo(userService.update(user.getId(), user)));
    }

    @PostMapping("/private/user/changePassword")
    public ResponseEntity<HttpMessage> changeUserPassword(@Valid @RequestBody WritablePasswordChange writablePasswordReset, WebRequest request) {
        User user = userService.getLoggedInUser();

        if (writablePasswordReset.getPassword().equals(writablePasswordReset.getPasswordConfirmation())) {
            userService.changePassword(user, writablePasswordReset.getPassword());
            HttpMessage httpMessage = new HttpMessage(HttpStatus.OK);
            httpMessage.setMessage("Password changed");
            httpMessage.setPath(((ServletWebRequest) request).getRequest().getRequestURL().toString());
            return ResponseEntity.ok(httpMessage);
        }
        throw new BadRequestException("Fields not matching");
    }


}
