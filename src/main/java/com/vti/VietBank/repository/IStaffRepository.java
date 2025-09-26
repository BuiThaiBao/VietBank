package com.vti.VietBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vti.VietBank.entity.Staff;

public interface IStaffRepository extends JpaRepository<Staff, Integer> {}
