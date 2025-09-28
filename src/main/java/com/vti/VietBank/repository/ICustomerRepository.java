package com.vti.VietBank.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vti.VietBank.entity.Customer;

public interface ICustomerRepository extends JpaRepository<Customer, Integer> {

    boolean existsByEmail(String email);

    boolean existsByCitizenId(String citizenId);

    @Query(
            value =
                    "Select c.* From customers c inner join users u on c.user_id = u.id where u.phone_number = :phoneNumber",
            nativeQuery = true)
    Optional<Customer> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Query(
            value = "SELECT c.* FROM customers c " + "JOIN users u ON c.user_id = u.id "
                    + "WHERE (:fullName IS NULL OR c.full_name LIKE %:fullName%) "
                    + "AND (:phoneNumber IS NULL OR u.phone_number LIKE %:phoneNumber%) "
                    + "AND (:citizenId IS NULL OR c.citizen_id LIKE %:citizenId%)",
            countQuery = "SELECT COUNT(*) FROM customers c " + "JOIN users u ON c.user_id = u.id "
                    + "WHERE (:fullName IS NULL OR c.full_name LIKE %:fullName%) "
                    + "AND (:phoneNumber IS NULL OR u.phone_number LIKE %:phoneNumber%) "
                    + "AND (:citizenId IS NULL OR c.citizen_id LIKE %:citizenId%)",
            nativeQuery = true)
    Page<Customer> searchCustomers(
            @Param("fullName") String fullName,
            @Param("phoneNumber") String phoneNumber,
            @Param("citizenId") String citizenId,
            Pageable pageable);

    Optional<Customer> findByCitizenId(String citizenId);
}
