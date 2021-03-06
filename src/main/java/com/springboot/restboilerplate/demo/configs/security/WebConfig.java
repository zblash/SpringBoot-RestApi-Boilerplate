package com.springboot.restboilerplate.demo.configs.security;

import com.springboot.restboilerplate.demo.configs.StringToEnumConverterFactory;
import com.springboot.restboilerplate.demo.configs.security.JWTAuthentication.JWTAuthEntryPoint;
import com.springboot.restboilerplate.demo.configs.security.JWTAuthentication.JWTAuthorizationFilter;
import com.springboot.restboilerplate.demo.configs.security.JWTAuthentication.JWTAuthenticationProvider;
import com.springboot.restboilerplate.demo.configs.security.JWTAuthentication.JWTSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

@EnableGlobalMethodSecurity(prePostEnabled=true)
@Configuration
@EnableWebSecurity
public class WebConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final JWTAuthenticationProvider autheticationProvider;

    private final JWTAuthEntryPoint authEntryPoint;

    public WebConfig(JWTAuthenticationProvider autheticationProvider, JWTAuthEntryPoint authEntryPoint) {
        this.autheticationProvider = autheticationProvider;
        this.authEntryPoint = authEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(autheticationProvider));
    }

    @Bean
    public JWTAuthorizationFilter authTokenFilter() {

        JWTAuthorizationFilter filter =new JWTAuthorizationFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new JWTSuccessHandler());
        return filter;

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests().antMatchers("api/**").authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(authEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        http.cors();
        http.headers().cacheControl();
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        AntPathMatcher matcher = new AntPathMatcher();
        matcher.setCaseSensitive(false);
        configurer.setPathMatcher(matcher);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new StringToEnumConverterFactory());
    }
}
