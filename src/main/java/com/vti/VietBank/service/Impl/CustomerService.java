package com.vti.VietBank.service.Impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vti.VietBank.dto.request.customer.CreateCustomerRequest;
import com.vti.VietBank.dto.request.customer.UpdateCustomerRequest;
import com.vti.VietBank.dto.response.customer.CreateCustomerResponse;
import com.vti.VietBank.dto.response.customer.CustomerResponse;
import com.vti.VietBank.dto.response.customer.UpdateCustomerResponse;
import com.vti.VietBank.entity.Customer;
import com.vti.VietBank.entity.User;
import com.vti.VietBank.exception.AppException;
import com.vti.VietBank.exception.ErrorCode;
import com.vti.VietBank.mapper.CustomerMapper;
import com.vti.VietBank.mapper.UserMapper;
import com.vti.VietBank.repository.ICustomerRepository;
import com.vti.VietBank.repository.IRoleRepository;
import com.vti.VietBank.repository.IUserRepository;
import com.vti.VietBank.service.ICustomerService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

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
    CustomerMapper customerMapper;

    @Override
    @PreAuthorize("hasAuthority('CREATE_CUSTOMER')")
    @Transactional(rollbackFor = Exception.class)
    public CreateCustomerResponse createCustomer(CreateCustomerRequest request) {
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        if (customerRepository.existsByCitizenId(request.getCitizenId())) {
            throw new AppException(ErrorCode.CUSTOMER_CITIZENID_EXISTS);
        }
        var roleCustomer =
                roleRepository.findById("CUSTOMER").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        User user = new User();
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(roleCustomer);

        Customer customer = new Customer();
        customer.setUser(user); // Liên kết với User chưa save
        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setCitizenId(request.getCitizenId());
        customer.setAddress(request.getAddress());
        customer.setDob(request.getDob());

        try {
            // Lưu User trước
            User savedUser = userRepository.saveAndFlush(user);
            log.info("User saved with ID: {}", savedUser.getId());

            // Đảm bảo customer có user với ID đúng
            customer.setUser(savedUser);

            // Lưu Customer
            Customer savedCustomer = customerRepository.saveAndFlush(customer);
            log.info(
                    "Customer saved with ID: {} for user ID: {}",
                    savedCustomer.getId(),
                    savedCustomer.getUser().getId());

            return  CreateCustomerResponse.builder()
                    .fullName(savedCustomer.getFullName())
                    .citizenId(savedCustomer.getCitizenId())
                    .phoneNumber(savedCustomer.getUser().getPhoneNumber())
                    .email(savedCustomer.getEmail())
                    .address(savedCustomer.getAddress())
                    .dob(savedCustomer.getDob())
                    .isActive(savedCustomer.getIsActive())
                    .createdAt(LocalDate.from(savedCustomer.getCreatedAt()))
                    .updatedAt(LocalDate.from(savedCustomer.getUpdatedAt()))
                    .build();


        } catch (Exception e) {
            log.error("Error creating customer account: {}", e.getMessage(), e);
            throw new RuntimeException("Error creating customer account", e);
        }
    }

    @PreAuthorize("hasAuthority('UPDATE_CUSTOMER')")
    @Override
    public UpdateCustomerResponse updateCustomer(int customerId, UpdateCustomerRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        Customer customer =
                customerRepository.findById(customerId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        customerMapper.updateCustomer(customer, request);
        return customerMapper.toUpdateResponse(customerRepository.save(customer));
    }

    @PreAuthorize("hasAuthority('DELETE_CUSTOMER')")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String customerDelete(int customerId) {
        Customer customer =
                customerRepository.findById(customerId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        customer.setIsActive("0");
        customerRepository.save(customer);

        User user = customer.getUser();
        if (user != null) {
            user.setIsActive("0");
            userRepository.save(user);
        }
        return "Customer soft deleted successfully";
    }



    @PreAuthorize("hasAuthority('VIEW_CUSTOMER_DETAIL')")
    @Override
    public CustomerResponse getCustomerByPhoneNumber(String phoneNumber) {
        Customer customerInfo = customerRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        return CustomerResponse.builder()
                .phoneNumber(phoneNumber)
                .fullName(customerInfo.getFullName())
                .citizenId(customerInfo.getCitizenId())
                .address(customerInfo.getAddress())
                .dateOfBirth(customerInfo.getDob())
                .email(customerInfo.getEmail())
                .isActive(customerInfo.getIsActive())
                .createdAt(LocalDate.from(customerInfo.getCreatedAt()))
                .updatedAt(LocalDate.from(customerInfo.getUpdatedAt()))
                .build();
    }



    @PreAuthorize("hasAuthority('SEARCH_CUSTOMER')")
    @Override
    public Page<CustomerResponse> searchCustomers(
            String fullName, String phoneNumber, String citizenId, Pageable pageable) {
        // Chuẩn hóa input: nếu rỗng hoặc blank, chuyển thành null
        fullName = (fullName != null && !fullName.isBlank()) ? fullName : null;
        phoneNumber = (phoneNumber != null && !phoneNumber.isBlank()) ? phoneNumber : null;
        citizenId = (citizenId != null && !citizenId.isBlank()) ? citizenId : null;

        // Gọi repository
        Page<Customer> customers = customerRepository.searchCustomers(fullName, phoneNumber, citizenId, pageable);

        // Map sang CustomerResponse giữ nguyên phân trang
        return customers.map(customer -> CustomerResponse.builder()
                .fullName(customer.getFullName())
                .phoneNumber(customer.getUser().getPhoneNumber())
                .citizenId(customer.getCitizenId())
                .address(customer.getAddress())
                .email(customer.getEmail())
                .isActive(customer.getIsActive())
                .dateOfBirth(customer.getDob())
                .createdAt(LocalDate.from(customer.getCreatedAt()))
                .updatedAt(LocalDate.from(customer.getUpdatedAt()))
                .build());
    }

    public CustomerResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String phoneNumber = context.getAuthentication().getName();
        Customer customer = customerRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        return CustomerResponse.builder()
                .phoneNumber(phoneNumber)
                .fullName(customer.getFullName())
                .citizenId(customer.getCitizenId())
                .address(customer.getAddress())
                .email(customer.getEmail())
                .isActive(customer.getIsActive())
                .dateOfBirth(customer.getDob())
                .createdAt(LocalDate.from(customer.getCreatedAt()))
                .updatedAt(LocalDate.from(customer.getUpdatedAt()))
                .build();
    }

    @PreAuthorize("hasAuthority('UPDATE_MY_INFO')")
    @Override
    public UpdateCustomerResponse updateMyInfo(UpdateCustomerRequest request) {
        String phoneNumber =
                SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        if (customerRepository.existsByEmail(request.getEmail())
                && !request.getEmail().equals(customer.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        customerMapper.updateCustomer(customer, request);
        return customerMapper.toUpdateResponse(customerRepository.save(customer));
    }
}
