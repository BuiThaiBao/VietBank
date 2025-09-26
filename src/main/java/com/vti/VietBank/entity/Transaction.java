package com.vti.VietBank.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Transaction from account
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_acc_id")
    private Account fromAccount;

    // Transaction to account
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_acc_id")
    private Account toAccount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    //    @Column(name = "transaction_type", nullable = false, length = 255)
    //    private String transactionType;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
