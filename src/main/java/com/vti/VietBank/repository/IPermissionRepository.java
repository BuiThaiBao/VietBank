package com.vti.VietBank.repository;

import com.vti.VietBank.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPermissionRepository extends JpaRepository<Permission,String> {
}
