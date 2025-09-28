package com.vti.VietBank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vti.VietBank.entity.User;

public interface IUserRepository extends JpaRepository<User, Integer> {
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumber(String phoneNumber);
}
