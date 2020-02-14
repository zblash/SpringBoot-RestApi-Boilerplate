package com.springboot.restboilerplate.demo.configs.security.JWTAuthentication;

import com.springboot.restboilerplate.demo.configs.constants.ApplicationContstants;
import com.springboot.restboilerplate.demo.configs.security.CustomPrincipal;
import com.springboot.restboilerplate.demo.configs.security.JWTAuthToken.JWTAuthToken;
import com.springboot.restboilerplate.demo.configs.security.JWTAuthToken.JWTGenerator;
import com.springboot.restboilerplate.demo.models.User;
import com.springboot.restboilerplate.demo.services.user.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JWTAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private UserService userService;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        JWTAuthToken jwtAuthenticationToken = (JWTAuthToken) authentication;
        String token = jwtAuthenticationToken.getToken();

        Claims body = (Claims) JWTGenerator.validate(token, ApplicationContstants.JWT_SECRET);
        User user = userService.findById(Long.parseLong(body.get("userId").toString()));
        if (user == null) {
            throw new AuthenticationCredentialsNotFoundException("JWT Token is incorrect");
        }

        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(user.getRole().getName());
        return new CustomPrincipal(user.getUsername(), user.getId(), token, grantedAuthorities,user);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (JWTAuthToken.class.isAssignableFrom(aClass));
    }

}