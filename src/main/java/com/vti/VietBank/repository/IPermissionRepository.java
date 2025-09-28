package com.vti.VietBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vti.VietBank.entity.Permission;

public interface IPermissionRepository extends JpaRepository<Permission, String> {}
