package com.vti.VietBank.service.Impl;

import com.vti.VietBank.dto.request.CreateCustomerRequest;
import com.vti.VietBank.dto.response.CreateCustomerResponse;
import com.vti.VietBank.entity.Customer;
import com.vti.VietBank.entity.User;
import com.vti.VietBank.exception.AppException;
import com.vti.VietBank.exception.ErrorCode;
import com.vti.VietBank.mapper.UserMapper;
import com.vti.VietBank.repository.ICustomerRepository;
import com.vti.VietBank.repository.IRoleRepository;
import com.vti.VietBank.repository.IUserRepository;
import com.vti.VietBank.service.ICustomerService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class CustomerService implements ICustomerService {

    IUserRepository userRepository;
    ICustomerRepository customerRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    IRoleRepository roleRepository;
    @Override
    @PreAuthorize("hasAuthority('CREATE_CUSTOMER')")
    @Transactional(rollbackFor = Exception.class)
    public CreateCustomerResponse createCustomer(CreateCustomerRequest request) {
        if(userRepository.existsByUsername(request.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        if(customerRepository.existsByPhone(request.getPhone())){
            throw new AppException(ErrorCode.CUSTOMER_PHONE_EXISTS);
        }
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }


        if(customerRepository.existsByCitizenId(request.getCitizenId())){
            throw new AppException(ErrorCode.CUSTOMER_CITIZENID_EXISTS);
        }
        var roleCustomer = roleRepository.findById("CUSTOMER").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(roleCustomer);



        Customer customer = new Customer();
        customer.setUser(user);  // Liên kết với User chưa save
        customer.setFullName(request.getFullName());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail());
        customer.setCitizenId(request.getCitizenId());
        customer.setAddress(request.getAddress());
        customer.setDateOfBirth(request.getDob());

        try {
            // Lưu User trước
            User savedUser = userRepository.saveAndFlush(user);
            log.info("User saved with ID: {}", savedUser.getId());

            // Đảm bảo customer có user với ID đúng
            customer.setUser(savedUser);

            // Lưu Customer
            Customer savedCustomer = customerRepository.saveAndFlush(customer);
            log.info("Customer saved with ID: {} for user ID: {}",
                    savedCustomer.getId(), savedCustomer.getUser().getId());

            return new CreateCustomerResponse(
                    savedCustomer.getFullName(),
                    savedCustomer.getPhone(),
                    savedCustomer.getEmail(),
                    savedCustomer.getCitizenId(),
                    savedCustomer.getAddress(),
                    savedCustomer.getDateOfBirth()
            );

        } catch (Exception e) {
            log.error("Error creating customer account: {}", e.getMessage(), e);
            throw new RuntimeException("Error creating customer account", e);
        }
    }
}
