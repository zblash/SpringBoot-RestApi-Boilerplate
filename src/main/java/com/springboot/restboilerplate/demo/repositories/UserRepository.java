package com.springboot.restboilerplate.demo.repositories;

import com.springboot.restboilerplate.demo.models.Role;
import com.springboot.restboilerplate.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findAllByStatus(boolean status);

    List<User> findAllByRole(Role role);

    List<User> findAllByRoleAndStatus(Role role,boolean status);

    Optional<User> findByUuid(UUID uuid);

    Optional<User> findByUsernameOrEmailOrName(String username, String email, String name);

    Optional<User> findByPasswordResetToken(String passwordResetToken);

    Optional<User> findByActivationToken(String activationToken);

}