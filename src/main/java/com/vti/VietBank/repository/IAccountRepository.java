package com.vti.VietBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vti.VietBank.entity.Account;

public interface IAccountRepository extends JpaRepository<Account, Integer> {}
