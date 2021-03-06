package com.springboot.restboilerplate.demo.services.user;

import com.springboot.restboilerplate.demo.enums.RoleType;
import com.springboot.restboilerplate.demo.models.User;

import java.util.List;

public interface UserService {

    User findByUserName(String userName);

    User findByEmail(String email);

    User findByResetToken(String token);

    boolean checkUserByEmail(String email);

    boolean canRegister(User user);

    List<User> findAll();

    List<User> findAllByRole(RoleType roleType);

    List<User> findAllByRoleAndStatus(RoleType roleType,boolean status);

    List<User> findAllByStatus(boolean status);

    User findById(Long id);

    User findByUUID(String uuid);

    User create(User user, RoleType roleType);

    User update(Long id, User updatedUser);

    void delete(User user);

    User getLoggedInUser();

    User changePassword(User user, String password);

    boolean loginControl(String username, String password);

    User findByActivationToken(String activationToken);
}
