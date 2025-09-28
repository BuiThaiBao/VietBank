package com.vti.VietBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vti.VietBank.entity.Role;

public interface IRoleRepository extends JpaRepository<Role, String> {}
