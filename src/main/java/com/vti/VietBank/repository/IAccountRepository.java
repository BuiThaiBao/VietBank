package com.vti.VietBank.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vti.VietBank.entity.Account;

public interface IAccountRepository extends JpaRepository<Account, Integer> {
    boolean existsByAccountNumber(String accountNumber);

    Optional<Account> findByAccountNumber(String accountNumber);

    @Query(
            value =
                    "Select a.* From accounts a inner join customers c on a.customer_id = c.id where c.citizen_id = :citizenId",
            nativeQuery = true)
    Optional<Account> findByCitizenId(@Param("citizenId") String citizenId);

    @Query(
            value =
                    "SELECT a.* FROM accounts a JOIN customers c ON a.customer_id = c.id JOIN users u ON c.user_id = u.id WHERE u.phone_number = :phoneNumber",
            nativeQuery = true)
    List<Account> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}
