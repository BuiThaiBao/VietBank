package com.vti.VietBank.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "users")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "phone_number", nullable = false, unique = true, length = 100)
    private String phoneNumber;

    @Column(nullable = false, length = 255)
    private String password;

    @ManyToOne
    private Role role;

    @Column(nullable = false, length = 1)
    private String isActive = "1";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
