package com.springboot.restboilerplate.demo.services.user;

import com.springboot.restboilerplate.demo.configs.constants.MessagesConstants;
import com.springboot.restboilerplate.demo.configs.security.CustomPrincipal;
import com.springboot.restboilerplate.demo.enums.RoleType;
import com.springboot.restboilerplate.demo.errors.ResourceNotFoundException;
import com.springboot.restboilerplate.demo.models.Role;
import com.springboot.restboilerplate.demo.models.User;
import com.springboot.restboilerplate.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleServiceImpl roleService;


    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUsername(userName).orElseThrow(() -> new ResourceNotFoundException(MessagesConstants.RESOURCES_NOT_FOUND+"user.name",userName));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(MessagesConstants.RESOURCES_NOT_FOUND+"user.name", email));
    }

    @Override
    public User findByResetToken(String token) {
        return userRepository.findByPasswordResetToken(token).orElseThrow(() -> new ResourceNotFoundException(MessagesConstants.RESOURCES_NOT_FOUND+"user.name", token));
    }

    @Override
    public boolean checkUserByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean canRegister(User user){
        return !userRepository.findByUsernameOrEmailOrName(user.getUsername(), user.getEmail(), user.getName()).isPresent();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findAllByRole(RoleType roleType) {
        Role role = roleService.findByName("ROLE_"+roleType.toString());
        return userRepository.findAllByRole(role);
    }

    @Override
    public List<User> findAllByRoleAndStatus(RoleType roleType,boolean status) {
        Role role = roleService.findByName("ROLE_"+roleType.toString());
        return userRepository.findAllByRoleAndStatus(role,status);
    }

    @Override
    public List<User> findAllByStatus(boolean status) {
        return userRepository.findAllByStatus(status);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MessagesConstants.RESOURCES_NOT_FOUND+"user",""));
    }

    @Override
    public User findByUUID(String uuid) {
        return userRepository.findByUuid(UUID.fromString(uuid)).orElseThrow(() -> new ResourceNotFoundException(MessagesConstants.RESOURCES_NOT_FOUND+"user", uuid));
    }

    @Override
    public User create(User user, RoleType roleType) {
        Role role = roleService.createOrFind("ROLE_"+roleType.toString());
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User update(Long id, User updatedUser) {
        User user = findById(id);
        user.setUsername(updatedUser.getUsername());
        user.setName(updatedUser.getName());
        user.setPassword(updatedUser.getPassword());
        user.setEmail(updatedUser.getEmail());
        user.setStatus(updatedUser.isStatus());
        user.setRole(updatedUser.getRole());
        user.setPasswordResetToken(updatedUser.getPasswordResetToken());
        if (updatedUser.getResetTokenExpireTime() != null) {
            user.setResetTokenExpireTime(updatedUser.getResetTokenExpireTime());
        }
        return userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public User getLoggedInUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ((CustomPrincipal) auth.getPrincipal()).getUser();
    }

    @Override
    public User changePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        return update(user.getId(),user);
    }

    @Override
    public boolean loginControl(String username, String password) {
        User user = findByUserName(username);
        return passwordEncoder.matches(password, user.getPassword()) && user.isStatus();
    }

    @Override
    public User findByActivationToken(String activationToken) {
        return userRepository.findByActivationToken(activationToken).orElseThrow(() -> new ResourceNotFoundException(MessagesConstants.RESOURCES_NOT_FOUND+"user", activationToken));
    }
}
