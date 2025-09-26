package com.vti.VietBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vti.VietBank.entity.InvalidatedToken;

public interface IInvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {}
