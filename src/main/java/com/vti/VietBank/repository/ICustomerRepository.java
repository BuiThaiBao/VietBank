package com.vti.VietBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vti.VietBank.entity.Customer;

public interface ICustomerRepository extends JpaRepository<Customer, Integer> {
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByCitizenId(String citizenId);
}
