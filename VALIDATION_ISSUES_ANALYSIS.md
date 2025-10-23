# 📋 PHÂN TÍCH CÁC VẤN ĐÈ CẦN SỬA - VIETBANK PROJECT

**Ngày phân tích:** 23/10/2025  
**Người thực hiện:** GitHub Copilot  
**Phạm vi:** Logic nghiệp vụ, Validation, Code Quality

---

## 🔴 CRITICAL - Thiếu @Valid trong Controller (Validation không hoạt động!)

> **⚠️ BLOCKING ISSUE**: Tất cả các Controller đều THIẾU @Valid trước @RequestBody - Điều này nghĩa là tất cả validation annotation vừa thêm sẽ KHÔNG hoạt động!

### 1. AuthenticationController.java

**File:** `src/main/java/com/vti/VietBank/controller/AuthenticationController.java`

**Vấn đề:**

```java
@PostMapping("/login")
ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request)
```

**Cần sửa thành:**

```java
@PostMapping("/login")
ApiResponse<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request)
```

## Done

### 2. CustomerController.java

**File:** `src/main/java/com/vti/VietBank/controller/CustomerController.java`

**Các method cần thêm @Valid:**

```java
// Hiện tại
@PostMapping("/create-customer")
ApiResponse<CreateCustomerResponse> createCustomer(@RequestBody CreateCustomerRequest request)

@PutMapping("update-customer/{customerId}")
ApiResponse<UpdateCustomerResponse> updateCustomer(@PathVariable int customerId, @RequestBody UpdateCustomerRequest request)

@PutMapping("/update-myinfo")
ApiResponse<UpdateCustomerResponse> updateMyInfo(@RequestBody UpdateCustomerRequest request)
```

Done
**Cần sửa thành:**

```java
// Thêm @Valid trước @RequestBody
@PostMapping("/create-customer")
ApiResponse<CreateCustomerResponse> createCustomer(@Valid @RequestBody CreateCustomerRequest request)

@PutMapping("update-customer/{customerId}")
ApiResponse<UpdateCustomerResponse> updateCustomer(@PathVariable int customerId, @Valid @RequestBody UpdateCustomerRequest request)

@PutMapping("/update-myinfo")
ApiResponse<UpdateCustomerResponse> updateMyInfo(@Valid @RequestBody UpdateCustomerRequest request)
```

## Done

### 3. AccountController.java

**File:** `src/main/java/com/vti/VietBank/controller/AccountController.java`

**Các method cần thêm @Valid:**

```java
// Hiện tại
@PostMapping("/create-account")
ApiResponse<CreateAccountResponse> createCustomer(@RequestBody CreateAccountRequest request)

@PutMapping("update-account/{citizenId}")
ApiResponse<UpdateAccountResponse> updateAccount(@PathVariable String citizenId, @RequestBody UpdateAccountRequest request)
```

Done
**Cần sửa thành:**

```java
@PostMapping("/create-account")
ApiResponse<CreateAccountResponse> createCustomer(@Valid @RequestBody CreateAccountRequest request)

@PutMapping("update-account/{citizenId}")
ApiResponse<UpdateAccountResponse> updateAccount(@PathVariable String citizenId, @Valid @RequestBody UpdateAccountRequest request)
```

## Done

### 4. TransactionController.java

**File:** `src/main/java/com/vti/VietBank/controller/TransactionController.java`

**Vấn đề:**

```java
@PostMapping("/transfer")
public ApiResponse<TransferMoneyResponse> createTransaction(@RequestBody TransferMoneyRequest request)
```

**Cần sửa thành:**

```java
@PostMapping("/transfer")
public ApiResponse<TransferMoneyResponse> createTransaction(@Valid @RequestBody TransferMoneyRequest request)
```

## Done

### 5. Các Controller khác cần kiểm tra

**Cần kiểm tra và thêm @Valid:**

- `RoleController.java`
- `PermissionController.java`
- `UserController.java`

**Import cần thiết:**

```java
import jakarta.validation.Valid;
```

## Done

## 🟠 HIGH - Logic nghiệp vụ sai/thiếu

### 6. TransactionService.java - So sánh số dư sai ErrorCode

**File:** `src/main/java/com/vti/VietBank/service/Impl/TransactionService.java`

**Vấn đề:**

```java
if(accountFrom.getBalance().compareTo(request.getAmount()) < 0) {
    throw new AppException(ErrorCode.TRANSACTION_INVALID_AMOUNT); // ❌ SAI ErrorCode
}
```

**Giải thích:**

- `TRANSACTION_INVALID_AMOUNT` dùng cho số tiền giao dịch <= 0
- Đang dùng sai cho trường hợp số dư không đủ

**Cần sửa thành:**

```java
if(accountFrom.getBalance().compareTo(request.getAmount()) < 0) {
    throw new AppException(ErrorCode.ACCOUNT_BALANCE_NOT_ENOUGH);
}
```

---

### 7. TransactionService.java - Thứ tự kiểm tra không tối ưu

**File:** `src/main/java/com/vti/VietBank/service/Impl/TransactionService.java`

**Vấn đề:**

- Kiểm tra số dư TRƯỚC khi kiểm tra tài khoản trùng
- Nên kiểm tra tài khoản trùng ngay sau khi lấy accountTo để fail-fast

**Thứ tự hiện tại:**

1. Lấy accountFrom
2. Check accountFrom active
3. Lấy accountTo
4. Check accountTo active
5. Check số dư
6. Check tài khoản trùng ← **Quá muộn!**

**Thứ tự đề xuất:**

1. Lấy accountFrom
2. Lấy accountTo
3. **Check tài khoản trùng** ← Fail-fast
4. Check accountFrom active
5. Check accountTo active
6. Check số dư

**Code đề xuất:**

```java
Account accountFrom = accountRepository.findByAccountNumber(request.getFromAccount())
    .orElseThrow(()-> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

Account accountTo = accountRepository.findByAccountNumber(request.getToAccount())
    .orElseThrow(()-> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

// Check ngay để fail-fast
if(accountFrom.getAccountNumber().equals(accountTo.getAccountNumber())) {
    throw new AppException(ErrorCode.TRANSACTION_SAME_ACCOUNT);
}

// Tiếp tục các check khác...
```

---

### 8. TransactionService.java - Kiểm tra tài khoản đích active

**File:** `src/main/java/com/vti/VietBank/service/Impl/TransactionService.java`

**Hiện trạng:** Đã OK, đang check active của cả 2 tài khoản

**Gợi ý:** Di chuyển logic check trùng lên trước như vấn đề #7

---

### 9. CustomerService.java - updateCustomer() kiểm tra email trùng SAI

**File:** `src/main/java/com/vti/VietBank/service/Impl/CustomerService.java`

**Vấn đề:**

```java
@Override
public UpdateCustomerResponse updateCustomer(int customerId, UpdateCustomerRequest request) {
    if (customerRepository.existsByEmail(request.getEmail())) {
        throw new AppException(ErrorCode.EMAIL_EXISTED);
    }
    // ...
}
```

**Giải thích:**

- Không cho phép user giữ nguyên email của chính mình
- Nếu user update thông tin khác mà không đổi email → lỗi EMAIL_EXISTED

**Cần sửa thành:**

```java
@Override
public UpdateCustomerResponse updateCustomer(int customerId, UpdateCustomerRequest request) {
    Customer customer = customerRepository.findById(customerId)
        .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));

    // Chỉ check nếu email mới khác email cũ
    if (request.getEmail() != null &&
        customerRepository.existsByEmail(request.getEmail()) &&
        !request.getEmail().equals(customer.getEmail())) {
        throw new AppException(ErrorCode.EMAIL_EXISTED);
    }

    customerMapper.updateCustomer(customer, request);
    return customerMapper.toUpdateResponse(customerRepository.save(customer));
}
```

---

### 10. CustomerService.java - updateMyInfo() cần check field null

**File:** `src/main/java/com/vti/VietBank/service/Impl/CustomerService.java`

**Hiện trạng:**

```java
@Override
public UpdateCustomerResponse updateMyInfo(UpdateCustomerRequest request) {
    // ... code hiện tại
    if (customerRepository.existsByEmail(request.getEmail())
            && !request.getEmail().equals(customer.getEmail())) {
        throw new AppException(ErrorCode.EMAIL_EXISTED);
    }
    customerMapper.updateCustomer(customer, request);
    return customerMapper.toUpdateResponse(customerRepository.save(customer));
}
```

**Vấn đề:**

- Nếu `request.getEmail()` là null → NPE khi gọi `.equals()`

**Cần sửa thành:**

```java
if (request.getEmail() != null &&
    customerRepository.existsByEmail(request.getEmail()) &&
    !request.getEmail().equals(customer.getEmail())) {
    throw new AppException(ErrorCode.EMAIL_EXISTED);
}
```

---

### 11. AccountService.java - Sai tên parameter giữa Service và Controller

**File:**

- `src/main/java/com/vti/VietBank/service/Impl/AccountService.java`
- `src/main/java/com/vti/VietBank/controller/AccountController.java`

**Vấn đề:**

**Service định nghĩa:**

```java
public UpdateAccountResponse updateAccount(String accountNumber, UpdateAccountRequest request)
```

**Controller gọi:**

```java
@PutMapping("update-account/{citizenId}")
ApiResponse<UpdateAccountResponse> updateAccount(@PathVariable String citizenId, @RequestBody UpdateAccountRequest request) {
    return ApiResponse.<UpdateAccountResponse>builder()
        .success(true)
        .result(accountService.updateAccount(citizenId, request)) // ❌ Truyền citizenId vì accountNumber
        .build();
}
```

**Service sử dụng:**

```java
Account account = accountRepository
    .findByAccountNumber(accountNumber) // ❌ Tìm theo accountNumber nhưng nhận citizenId
    .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
```

**Giải thích:**

- Service mong đợi `accountNumber` (10-14 chữ số)
- Controller truyền `citizenId` (12 chữ số CCCD)
- Repository tìm theo `accountNumber` → sẽ không tìm thấy!

**Giải pháp 1 - Sửa Controller (Khuyến nghị):**

```java
@PutMapping("update-account/{accountNumber}")
ApiResponse<UpdateAccountResponse> updateAccount(
    @PathVariable String accountNumber,
    @Valid @RequestBody UpdateAccountRequest request) {
    return ApiResponse.<UpdateAccountResponse>builder()
        .success(true)
        .result(accountService.updateAccount(accountNumber, request))
        .build();
}
```

**Giải pháp 2 - Sửa Service:**

```java
public UpdateAccountResponse updateAccount(String citizenId, UpdateAccountRequest request) {
    // Tìm customer trước, rồi lấy account
    Customer customer = customerRepository.findByCitizenId(citizenId)
        .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));

    Account account = accountRepository.findByCustomer(customer)
        .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

    // ... logic update
}
```

---

### 12. AccountService.java - updateAccount() thiếu validation số tiền nạp

**File:** `src/main/java/com/vti/VietBank/service/Impl/AccountService.java`

**Vấn đề:**

```java
var newAccountBalance = accountBalance.add(
    request.getAddMoney() != null ? request.getAddMoney() : BigDecimal.ZERO
);
```

**Giải thích:**

- Có check null nhưng KHÔNG cần thiết vì đã có `@NotNull` validation (sau khi thêm @Valid)
- Logic redundant

**Cần sửa thành:**

```java
// Sau khi đã có @Valid trong Controller
var newAccountBalance = accountBalance.add(request.getAddMoney());
```

---

### 13. AccountService.java - createAccount() logic set balance không cần thiết

**File:** `src/main/java/com/vti/VietBank/service/Impl/AccountService.java`

**Vấn đề:**

```java
if (request.getBalance() != null) {
    account.setBalance(request.getBalance());
} else {
    account.setBalance(BigDecimal.ZERO);
}
```

**Giải thích:**

- Đã có `@NotNull` validation trong DTO
- Sau khi thêm @Valid, `request.getBalance()` sẽ không bao giờ null

**Cần sửa thành:**

```java
account.setBalance(request.getBalance());
```

---

### 14. UserService.java - updateUser() thiếu validation password

**File:** `src/main/java/com/vti/VietBank/service/Impl/UserService.java`

**Vấn đề:**

```java
user.setPassword(
    request.getPassword() != null
        ? passwordEncoder.encode(request.getPassword())
        : user.getPassword()
);
```

**Giải thích:**

- Nếu password != null nhưng là empty string ("") → sẽ encode password rỗng → lỗi bảo mật
- Validation DTO đã có @Pattern nên không cần lo

**Cần sửa thành:**

```java
// Sau khi có @Valid, chỉ cần check null
if (request.getPassword() != null && !request.getPassword().isBlank()) {
    user.setPassword(passwordEncoder.encode(request.getPassword()));
}
// Không set gì thì giữ nguyên password cũ
```

---

### 15. UserService.java - updateUser() thiếu update role

**File:** `src/main/java/com/vti/VietBank/service/Impl/UserService.java`

**Vấn đề:**

- `UpdateUserRequest` có field `role`
- Nhưng không thấy được sử dụng trong method `updateUser()`

**Cần thêm:**

```java
@Override
public UserResponse updateUser(int id, UpdateUserRequest request) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

    // Update password nếu có
    if (request.getPassword() != null && !request.getPassword().isBlank()) {
        user.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    // Update role nếu có
    if (request.getRole() != null) {
        user.setRole(request.getRole());
    }

    return userMapper.toResponse(userRepository.save(user));
}
```

---

### 16. AuthenticationService.java - Check isActive dùng Magic String

**File:** `src/main/java/com/vti/VietBank/service/Impl/AuthenticationService.java`

**Vấn đề:**

```java
if ("0".equals(user.getIsActive())) {
    throw new AppException(ErrorCode.USER_INACTIVE);
}
```

**Giải thích:**

- Magic string "0", "1" khắp nơi trong code
- Khó maintain, dễ nhầm lẫn

**Cần tạo Constants class:**

```java
// src/main/java/com/vti/VietBank/constants/ActiveStatus.java
package com.vti.VietBank.constants;

public final class ActiveStatus {
    public static final String ACTIVE = "1";
    public static final String INACTIVE = "0";

    private ActiveStatus() {
        // Prevent instantiation
    }
}
```

**Sử dụng:**

```java
import com.vti.VietBank.constants.ActiveStatus;

if (ActiveStatus.INACTIVE.equals(user.getIsActive())) {
    throw new AppException(ErrorCode.USER_INACTIVE);
}
```

---

### 17. AuthenticationService.java - refreshToken() duplicate check isActive

**File:** `src/main/java/com/vti/VietBank/service/Impl/AuthenticationService.java`

**Vấn đề:**

```java
if ("0".equals(user.getIsActive())) {
    throw new AppException(ErrorCode.USER_INACTIVE);
}
```

**Cần sửa:** Giống vấn đề #16, dùng constants

---

## 🟡 MEDIUM - Cải thiện logic

### 18. TransactionService.java - Thiếu logging cho transaction

**File:** `src/main/java/com/vti/VietBank/service/Impl/TransactionService.java`

**Vấn đề:**

- Không có log before/after balance để audit trail
- Chỉ save vào DB, không log ra console/file

**Cần thêm:**

```java
log.info("Transfer initiated - From: {}, To: {}, Amount: {}",
    request.getFromAccount(), request.getToAccount(), request.getAmount());

log.info("Account {} balance before: {}, after: {}",
    accountFrom.getAccountNumber(),
    accountFrom.getBalance(),
    accountFrom.getBalance().subtract(request.getAmount()));

log.info("Account {} balance before: {}, after: {}",
    accountTo.getAccountNumber(),
    accountTo.getBalance(),
    accountTo.getBalance().add(request.getAmount()));

// Thực hiện transfer
accountFrom.setBalance(accountFrom.getBalance().subtract(request.getAmount()));
accountTo.setBalance(accountTo.getBalance().add(request.getAmount()));

log.info("Transfer completed - Transaction ID: {}", tx.getId());
```

---

### 19. TransactionService.java - Thiếu kiểm tra giới hạn giao dịch

**File:** `src/main/java/com/vti/VietBank/service/Impl/TransactionService.java`

**Vấn đề:**

- Không check số tiền tối đa/giao dịch
- Không check số lần giao dịch/ngày

**Gợi ý thêm ErrorCode:**

```java
TRANSACTION_AMOUNT_EXCEEDS_LIMIT(4011, "Số tiền giao dịch vượt quá giới hạn cho phép", HttpStatus.BAD_REQUEST),
TRANSACTION_DAILY_LIMIT_EXCEEDED(4012, "Đã vượt quá số lần giao dịch trong ngày", HttpStatus.BAD_REQUEST),
```

**Logic đề xuất:**

```java
// Constants
private static final BigDecimal MAX_TRANSFER_AMOUNT = new BigDecimal("500000000"); // 500 triệu
private static final int MAX_DAILY_TRANSACTIONS = 20;

// Trong transferMoney()
if (request.getAmount().compareTo(MAX_TRANSFER_AMOUNT) > 0) {
    throw new AppException(ErrorCode.TRANSACTION_AMOUNT_EXCEEDS_LIMIT);
}

// Check số lần giao dịch trong ngày
long todayTransactions = transactionRepository.countTodayTransactionsByAccount(accountFrom);
if (todayTransactions >= MAX_DAILY_TRANSACTIONS) {
    throw new AppException(ErrorCode.TRANSACTION_DAILY_LIMIT_EXCEEDED);
}
```

---

### 20. TransactionService.java - Description mặc định thiếu thông tin

**File:** `src/main/java/com/vti/VietBank/service/Impl/TransactionService.java`

**Vấn đề:**

```java
if(request.getDescription() == null) {
    request.setDescription(accountFrom.getCustomer().getFullName() + " chuyển khoản ");
}
```

**Cần cải thiện:**

```java
if(request.getDescription() == null || request.getDescription().isBlank()) {
    request.setDescription(String.format(
        "%s chuyển %s VND cho %s lúc %s",
        accountFrom.getCustomer().getFullName(),
        request.getAmount(),
        accountTo.getCustomer().getFullName(),
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
    ));
}
```

---

### 21. CustomerService.java - createCustomer() thiếu check phone trùng

**File:** `src/main/java/com/vti/VietBank/service/Impl/CustomerService.java`

**Vấn đề:**

```java
if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
    throw new AppException(ErrorCode.USER_EXISTED);
}
```

**Giải thích:**

- Chỉ check trong bảng User
- Không check trong bảng Customer (nếu có trường phone riêng)

**Hiện trạng:** Có vẻ OK vì Customer lấy phone từ User

**Gợi ý:** Thêm comment để rõ ràng

```java
// Check phone trong User table (Customer phone được lấy từ User)
if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
    throw new AppException(ErrorCode.USER_EXISTED);
}
```

---

### 22. CustomerService.java - searchCustomers() validation input yếu

**File:** `src/main/java/com/vti/VietBank/service/Impl/CustomerService.java`

**Vấn đề:**

```java
fullName = (fullName != null && !fullName.isBlank()) ? fullName : null;
phoneNumber = (phoneNumber != null && !phoneNumber.isBlank()) ? phoneNumber : null;
citizenId = (citizenId != null && !citizenId.isBlank()) ? citizenId : null;
```

**Giải thích:**

- Chỉ check blank → null
- Không validate định dạng phone, citizenId

**Gợi ý cải thiện:**

```java
// Validate phone format nếu có
if (phoneNumber != null && !phoneNumber.isBlank()) {
    if (!phoneNumber.matches("^(0[35789][0-9]{8}|\\+84[35789][0-9]{8})$")) {
        throw new AppException(ErrorCode.CUSTOMER_PHONE_INVALID);
    }
}

// Validate citizenId format nếu có
if (citizenId != null && !citizenId.isBlank()) {
    if (!citizenId.matches("^\\d{12}$")) {
        throw new AppException(ErrorCode.CUSTOMER_CITIZENID_INVALID);
    }
}
```

---

### 23. AccountService.java - generateUniqueAccountNumber() maxAttempts quá thấp

**File:** `src/main/java/com/vti/VietBank/service/Impl/AccountService.java`

**Vấn đề:**

```java
int maxAttempts = 5; // Giới hạn số lần thử để tránh vòng lặp vô tận
```

**Giải thích:**

- Chỉ 5 lần thử
- Với hệ thống lớn (nhiều account) dễ fail

**Cần sửa thành:**

```java
int maxAttempts = 100; // Tăng số lần thử
```

**Hoặc dùng thuật toán tốt hơn:**

```java
private String generateUniqueAccountNumber() {
    // Dùng timestamp + random để giảm khả năng trùng
    String timestamp = String.valueOf(System.currentTimeMillis() % 10000); // 4 chữ số cuối
    Random random = new Random();
    StringBuilder sb = new StringBuilder();

    // 6 chữ số random
    for (int i = 0; i < 6; i++) {
        sb.append(random.nextInt(10));
    }

    String accountNumber = timestamp + sb.toString(); // 10 chữ số

    // Check trong DB, nếu trùng thì retry (ít khả năng xảy ra)
    int attempts = 0;
    while (accountRepository.existsByAccountNumber(accountNumber) && attempts < 10) {
        accountNumber = generateRandomAccountNumber(); // fallback
        attempts++;
    }

    if (attempts >= 10) {
        throw new RuntimeException("Không thể tạo số tài khoản duy nhất");
    }

    return accountNumber;
}
```

---

### 24. AccountService.java - getMyAccount() error message không thân thiện

**File:** `src/main/java/com/vti/VietBank/service/Impl/AccountService.java`

**Vấn đề:**

```java
if (accounts.isEmpty()) {
    throw new AppException(ErrorCode.ACCOUNT_NOT_FOUND);
}
```

**Giải thích:**

- Message "Không tìm thấy tài khoản" không rõ ràng
- User có thể chưa mở tài khoản → nên hướng dẫn

**Gợi ý thêm ErrorCode:**

```java
ACCOUNT_NOT_OPENED_YET(3011, "Bạn chưa mở tài khoản nào. Vui lòng liên hệ ngân hàng để mở tài khoản.", HttpStatus.NOT_FOUND),
```

**Sử dụng:**

```java
if (accounts.isEmpty()) {
    throw new AppException(ErrorCode.ACCOUNT_NOT_OPENED_YET);
}
```

---

## 🟢 LOW - Code quality & best practices

### 25. Tất cả Service - Magic strings cho isActive

**Vấn đề:**

- "1", "0" xuất hiện khắp nơi
- Khó maintain và refactor

**Giải pháp:**

**Tạo file Constants:**

```java
// src/main/java/com/vti/VietBank/constants/ActiveStatus.java
package com.vti.VietBank.constants;

public final class ActiveStatus {
    public static final String ACTIVE = "1";
    public static final String INACTIVE = "0";

    private ActiveStatus() {
        throw new UnsupportedOperationException("Utility class");
    }
}
```

**Các file cần sửa:**

- `AuthenticationService.java`
- `CustomerService.java`
- `AccountService.java`
- `TransactionService.java`

**Tìm và thay thế:**

- `"1"` → `ActiveStatus.ACTIVE`
- `"0"` → `ActiveStatus.INACTIVE`

---

### 26. GlobalExceptionHandler - Hardcode error code 405

**File:** `src/main/java/com/vti/VietBank/exception/GlobalExceptionHandler.java`

**Vấn đề:**

```java
@ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
ResponseEntity<ApiResponse> handlingMethodNotSupported(HttpRequestMethodNotSupportedException exception) {
    ApiResponse apiResponse = new ApiResponse();
    apiResponse.setCode(405); // ❌ Hardcode
    apiResponse.setMessage("Method not allowed: " + exception.getMethod());
    apiResponse.setSuccess(false);
    return ResponseEntity.status(405).body(apiResponse);
}
```

**Cần thêm ErrorCode:**

```java
METHOD_NOT_ALLOWED(1006, "Phương thức HTTP không được hỗ trợ", HttpStatus.METHOD_NOT_ALLOWED),
```

**Sửa thành:**

```java
@ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
ResponseEntity<ApiResponse> handlingMethodNotSupported(HttpRequestMethodNotSupportedException exception) {
    ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;
    ApiResponse apiResponse = ApiResponse.builder()
        .code(errorCode.getCode())
        .message(errorCode.getMessage() + ": " + exception.getMethod())
        .success(false)
        .build();
    return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
}
```

---

### 27. Thiếu validation cho PathVariable và RequestParam

**Các Controller cần thêm validation:**

#### CustomerController.java

```java
// Hiện tại
@GetMapping("view-customer-detail")
ApiResponse<CustomerResponse> viewCustomerDetail(@RequestParam String phoneNumber)

// Cần thêm validation
@GetMapping("view-customer-detail")
ApiResponse<CustomerResponse> viewCustomerDetail(
    @RequestParam
    @Pattern(regexp = "^(0[35789][0-9]{8}|\\+84[35789][0-9]{8})$", message = "CUSTOMER_PHONE_INVALID")
    String phoneNumber
)
```

#### AccountController.java

```java
// Hiện tại
@PutMapping("deactive-account/{accountNumber}")
ApiResponse<String> deActiveAccount(@PathVariable String accountNumber)

// Cần thêm validation
@PutMapping("deactive-account/{accountNumber}")
ApiResponse<String> deActiveAccount(
    @PathVariable
    @Pattern(regexp = "^\\d{10,14}$", message = "TRANSACTION_ACCOUNT_NUMBER_INVALID")
    String accountNumber
)
```

**Lưu ý:** Cần thêm `@Validated` ở class level:

```java
import org.springframework.validation.annotation.Validated;

@Validated
@RestController
@RequestMapping("/customers")
public class CustomerController {
    // ...
}
```

---

### 28. Các Response DTO - Thiếu validation output

**Vấn đề:**

- Không đảm bảo data trả về luôn hợp lệ
- VD: balance có thể âm, phone có thể null

**Gợi ý:**

- Thêm validation annotations trong Response DTO (optional)
- Hoặc validate trong Service trước khi trả về

**Ví dụ:**

```java
@Data
@Builder
public class AccountResponse {
    @NotNull
    private String accountNumber;

    @DecimalMin("0.0")
    private BigDecimal balance;

    @NotNull
    private String isActive;

    // ...
}
```

---

## 📊 TỔNG KẾT & KHUYẾN NGHỊ

### Thống kê vấn đề

| Mức độ      | Số lượng | Mô tả                         |
| ----------- | -------- | ----------------------------- |
| 🔴 CRITICAL | 5        | Thiếu @Valid - blocking issue |
| 🟠 HIGH     | 12       | Logic nghiệp vụ sai/nguy hiểm |
| 🟡 MEDIUM   | 7        | Cải thiện logic               |
| 🟢 LOW      | 4        | Code quality                  |
| **TỔNG**    | **28**   |                               |

---

### Ưu tiên thực hiện

#### 🎯 Phase 1 - URGENT (Bắt buộc làm ngay)

**Thời gian:** 1-2 giờ

1. ✅ Thêm `@Valid` vào tất cả Controller (#1-5)
2. ✅ Sửa ErrorCode sai trong TransactionService (#6)
3. ✅ Sửa logic check email trong CustomerService (#9, #10)
4. ✅ Sửa parameter mismatch trong AccountService (#11)

**Impact:** Mở khóa toàn bộ validation system, sửa các bug nghiêm trọng

---

#### 🎯 Phase 2 - HIGH PRIORITY (Nên làm trong tuần)

**Thời gian:** 3-4 giờ

5. ✅ Tối ưu thứ tự check trong TransactionService (#7)
6. ✅ Sửa logic updateAccount, createAccount (#12, #13)
7. ✅ Sửa updateUser (#14, #15)
8. ✅ Tạo Constants class cho ActiveStatus (#16, #17, #25)

**Impact:** Cải thiện performance, bảo mật, maintainability

---

#### 🎯 Phase 3 - MEDIUM PRIORITY (Trong 2 tuần)

**Thời gian:** 4-5 giờ

9. ✅ Thêm logging cho transaction (#18)
10. ✅ Thêm giới hạn giao dịch (#19)
11. ✅ Cải thiện description mặc định (#20)
12. ✅ Validation input search (#22)
13. ✅ Cải thiện generateUniqueAccountNumber (#23)

**Impact:** Tăng khả năng audit, bảo mật, UX

---

#### 🎯 Phase 4 - LOW PRIORITY (Khi rảnh)

**Thời gian:** 2-3 giờ

14. ✅ Sửa GlobalExceptionHandler (#26)
15. ✅ Thêm validation cho PathVariable/RequestParam (#27)
16. ✅ Validation Response DTO (#28)
17. ✅ Code cleanup và refactor

**Impact:** Code quality, consistency

---

### Checklist thực hiện

#### Phase 1 - URGENT

- [ ] #1: Thêm @Valid vào AuthenticationController
- [ ] #2: Thêm @Valid vào CustomerController (3 methods)
- [ ] #3: Thêm @Valid vào AccountController (2 methods)
- [ ] #4: Thêm @Valid vào TransactionController
- [ ] #5: Kiểm tra RoleController, PermissionController, UserController
- [ ] #6: Sửa ErrorCode TRANSACTION_INVALID_AMOUNT → ACCOUNT_BALANCE_NOT_ENOUGH
- [ ] #9: Sửa logic check email trong updateCustomer()
- [ ] #10: Thêm null check cho email trong updateMyInfo()
- [ ] #11: Sửa parameter mismatch accountNumber/citizenId

#### Phase 2 - HIGH

- [ ] #7: Tối ưu thứ tự check trong transferMoney()
- [ ] #12: Xóa null check không cần thiết trong updateAccount()
- [ ] #13: Xóa if-else không cần thiết trong createAccount()
- [ ] #14: Thêm blank check cho password trong updateUser()
- [ ] #15: Thêm logic update role trong updateUser()
- [ ] #16-17: Tạo ActiveStatus constants và thay thế magic strings
- [ ] #25: Replace tất cả "1"/"0" bằng constants

#### Phase 3 - MEDIUM

- [ ] #18: Thêm logging cho transaction
- [ ] #19: Thêm check giới hạn giao dịch
- [ ] #20: Cải thiện description mặc định
- [ ] #22: Thêm validation format cho search parameters
- [ ] #23: Cải thiện generateUniqueAccountNumber()
- [ ] #24: Thêm ErrorCode ACCOUNT_NOT_OPENED_YET

#### Phase 4 - LOW

- [ ] #26: Sửa hardcode 405 trong GlobalExceptionHandler
- [ ] #27: Thêm @Validated và validation cho PathVariable/RequestParam
- [ ] #28: Thêm validation cho Response DTO

---

### Lưu ý khi thực hiện

1. **Testing sau mỗi Phase:**

   - Chạy unit tests
   - Chạy integration tests
   - Test thủ công các API đã sửa

2. **Git commits:**

   - Mỗi issue nên là 1 commit riêng
   - Format: `fix: #<number> - <description>`
   - VD: `fix: #1-5 - Add @Valid to all Controllers`

3. **Documentation:**

   - Update API documentation nếu có thay đổi endpoint
   - Update README nếu có thay đổi lớn

4. **Code review:**
   - Self-review trước khi commit
   - Peer review cho Phase 1 & 2

---

## 📝 GHI CHÚ BỔ SUNG

### Import cần thêm

```java
// Validation
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

// Constants
import com.vti.VietBank.constants.ActiveStatus;
```

### ErrorCode mới cần thêm

```java
// Trong ErrorCode.java

// System
METHOD_NOT_ALLOWED(1006, "Phương thức HTTP không được hỗ trợ", HttpStatus.METHOD_NOT_ALLOWED),

// Account
ACCOUNT_NOT_OPENED_YET(3011, "Bạn chưa mở tài khoản nào. Vui lòng liên hệ ngân hàng để mở tài khoản.", HttpStatus.NOT_FOUND),

// Transaction
TRANSACTION_AMOUNT_EXCEEDS_LIMIT(4011, "Số tiền giao dịch vượt quá giới hạn cho phép", HttpStatus.BAD_REQUEST),
TRANSACTION_DAILY_LIMIT_EXCEEDED(4012, "Đã vượt quá số lần giao dịch trong ngày", HttpStatus.BAD_REQUEST),
```

### Constants class mới

```java
// src/main/java/com/vti/VietBank/constants/ActiveStatus.java
package com.vti.VietBank.constants;

public final class ActiveStatus {
    public static final String ACTIVE = "1";
    public static final String INACTIVE = "0";

    private ActiveStatus() {
        throw new UnsupportedOperationException("Utility class");
    }
}
```

---

## 🔗 Tài liệu tham khảo

1. [Spring Validation Guide](https://spring.io/guides/gs/validating-form-input/)
2. [Jakarta Bean Validation](https://beanvalidation.org/)
3. [Spring Boot Best Practices](https://docs.spring.io/spring-boot/docs/current/reference/html/)

---

**Tài liệu này được tạo tự động bởi GitHub Copilot**  
**Ngày:** 23/10/2025  
**Version:** 1.0
