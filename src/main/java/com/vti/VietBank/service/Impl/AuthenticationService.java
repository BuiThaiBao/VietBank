package com.vti.VietBank.service.Impl;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.vti.VietBank.dto.request.AuthenticationRequest;
import com.vti.VietBank.dto.request.IntrospectRequest;
import com.vti.VietBank.dto.request.LogoutRequest;
import com.vti.VietBank.dto.request.RefreshRequest;
import com.vti.VietBank.dto.response.AuthenticationResponse;
import com.vti.VietBank.dto.response.IntrospectResponse;
import com.vti.VietBank.entity.InvalidatedToken;
import com.vti.VietBank.entity.Role;
import com.vti.VietBank.entity.User;
import com.vti.VietBank.exception.AppException;
import com.vti.VietBank.exception.ErrorCode;
import com.vti.VietBank.repository.IInvalidatedTokenRepository;
import com.vti.VietBank.repository.IUserRepository;
import com.vti.VietBank.service.IAuthenticationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService implements IAuthenticationService {

    IUserRepository userRepository;
    IInvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    // Kiểm tra tọken có hợp lệ hay không
    // Nếu token hợp lệ valid trả về true ngược lại trả về false
    @Override
    public IntrospectResponse introspectToken(IntrospectRequest request) {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException | JOSEException | ParseException e) {
            isValid = false;
        }
        return IntrospectResponse.builder().valid(isValid).build();
    }

    // Đăng nhập
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Tìm user theo userName
        var user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        // Kiểm tra mật khẩu
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        // Sinh token cho user
        var token = generateToken(user);

        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    // Đăng xuất

    @Override
    public void logout(LogoutRequest request) {
        try {
            var signToken = verifyToken(request.getToken(), true);
            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken =
                    InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();
            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException | ParseException | JOSEException e) {
            log.info("Token already expired ");
        }
    }

    /**
     * Xác minh token (dùng cho cả access token và refresh token).
     * - Kiểm tra chữ ký token.
     * - Kiểm tra thời hạn.
     * - Kiểm tra token có bị vô hiệu hóa chưa.
     */
    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        // Tạo đối tượng xác minh
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        // Phân tích token thành 1 object
        SignedJWT signedJWT = SignedJWT.parse(token);

        // Xác minh thời gian hết hạn của token
        // Nếu isRefresh = true lấy IssuaTime( thời gian token được tạo) cộng với thời gian cho phép refresh
        Date expiryTime = (isRefresh)
                ? new Date(signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                        .toEpochMilli())
                // ísRefresh = false lấy thời gian hết hạn từ claim exp
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        // Xác minh token
        var verified = signedJWT.verify(verifier);

        // Kiểm tra tính hợp lệ của token
        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        // Kiểm tra token đã bị vô hiệu hóa chưa
        // Những token bị vô hiệu hóa sẽ được lưu trong bảng invalidatedToken
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getSubject()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        // Nếu token hợp lệ trả về đối tượng signedJWT
        return signedJWT;
    }

    // Refresh Token
    @Override
    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {

        // Kiểm tra chữ ký của token.
        // Kiểm tra token có hết hạn trong khoảng thời gian cho phép refresh không.
        // Kiểm tra token có bị revoke chưa.
        var signJWT = verifyToken(request.getToken(), true);

        // Lấy id và thời gian hết hạn của token cũ
        var jid = signJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();

        // vô hiệu hóa token cũ
        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jid).expiryTime(expiryTime).build();
        invalidatedTokenRepository.save(invalidatedToken);

        // Lấy user từ token cũ
        var username = signJWT.getJWTClaimsSet().getSubject();
        var user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        // Tạo token mới
        var token = generateToken(user);

        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    // Tạo token
    private String generateToken(User user) {
        // Token gồm
        // Header + Payload + Signature

        // Tạo header cho token
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // Tạo payload cho token
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("VietBank")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        // Ký token
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * Xây dựng chuỗi scope cho user từ role và permission.
     * Scope này sẽ được nhúng vào JWT để phục vụ phân quyền.
     *
     * Ví dụ:
     *   - User có role "ADMIN", permissions ["READ_USER", "WRITE_USER"]
     *     => "ROLE_ADMIN READ_USER WRITE_USER"
     *   - User có role "STAFF", không có permission
     *     => "ROLE_STAFF"
     */
    private String buildScope(User user) {
        // Dùng StringJoiner để nối các role/permission thành 1 chuỗi, phân tách bởi khoảng trắng
        StringJoiner stringJoiner = new StringJoiner(" ");

        // Lấy role của user (vì thiết kế User chỉ có 1 role)
        Role role = user.getRole();

        if (role != null) {
            // Thêm role vào scope với tiền tố "ROLE_"
            // Ví dụ: role = "ADMIN" => "ROLE_ADMIN"
            stringJoiner.add("ROLE_" + role.getName());

            // Nếu role có danh sách permission thì thêm từng permission vào scope
            if (!CollectionUtils.isEmpty(role.getPermissions())) {
                role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            }
        }

        // Trả về chuỗi scope cuối cùng
        return stringJoiner.toString();
    }
}
