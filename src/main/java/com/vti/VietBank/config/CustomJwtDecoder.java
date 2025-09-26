package com.vti.VietBank.config;

import com.nimbusds.jose.JOSEException;
import com.vti.VietBank.service.IAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.signerKey}")
    private String signerKey;

    @Autowired
    private IAuthenticationService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {

        try {
            // Gọi service để introspect token (check token có valid không)
            var response = authenticationService.introspectToken(
                    com.vti.VietBank.dto.request.IntrospectRequest.builder().token(token).build());
            // Nếu token không hợp lệ -> ném lỗi ngay
            if (!response.isValid()) throw new JwtException("Token invalid");
        } catch (JOSEException | ParseException e) {
            throw new JwtException(e.getMessage());
        }

        // Nếu bộ giải mã chưa được khởi tạo thì khởi tạo
        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            // Khởi tạo NimbusJwtDecoder với secret key + thuật toán HS512
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
        // Dùng NimbusJwtDecoder để decode token -> trả về đối tượng Jwt (đã được verify)
        return nimbusJwtDecoder.decode(token);
    }
}
