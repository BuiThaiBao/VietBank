package com.vti.VietBank.config;

import com.vti.VietBank.entity.Role;
import com.vti.VietBank.entity.User;
import com.vti.VietBank.repository.IRoleRepository;
import com.vti.VietBank.repository.IUserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;
    IRoleRepository roleRepository;

    @Bean
    ApplicationRunner applicationRunner(IUserRepository userRepository) {
        return args -> {
            // ===== ROLE =====
            var roleAdmin = roleRepository.findById("ADMIN")
                    .orElseGet(() -> {
                        Role newRole = Role.builder()
                                .name("ADMIN")
                                .description("Administrator role")
                                .build();
                        roleRepository.save(newRole);
                        log.warn("Role ADMIN has been created.");
                        return newRole;
                    });

            var roleStaff = roleRepository.findById("STAFF")
                    .orElseGet(() -> {
                        Role newRole = Role.builder()
                                .name("STAFF")
                                .description("Staff role")
                                .build();
                        roleRepository.save(newRole);
                        log.warn("Role STAFF has been created.");
                        return newRole;
                    });

            var roleCustomer = roleRepository.findById("CUSTOMER")
                    .orElseGet(() -> {
                        Role newRole = Role.builder()
                                .name("CUSTOMER")
                                .description("Customer role")
                                .build();
                        roleRepository.save(newRole);
                        log.warn("Role CUSTOMER has been created.");
                        return newRole;
                    });

            // ===== USERS =====
            if (userRepository.findByUsername("admin").isEmpty()) {
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .role(roleAdmin)
                        .build();

                userRepository.save(user);
                log.warn("Admin user has been created with default password: admin, please change it!");
            }

            if (userRepository.findByUsername("staff").isEmpty()) {
                User staff = User.builder()
                        .username("staff")
                        .password(passwordEncoder.encode("staff"))
                        .role(roleStaff)
                        .build();

                userRepository.save(staff);
                log.warn("Staff user has been created with default password: staff, please change it!");
            }

            if (userRepository.findByUsername("customer").isEmpty()) {
                User customer = User.builder()
                        .username("customer")
                        .password(passwordEncoder.encode("customer"))
                        .role(roleCustomer)
                        .build();

                userRepository.save(customer);
                log.warn("Customer user has been created with default password: customer, please change it!");
            }
        };
    }
}
