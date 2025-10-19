package com.vti.VietBank.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth"; // Tên của security scheme

        return new OpenAPI()
                // Cấu hình thông tin chung cho API
                .info(new Info()
                        .title("VietBank API")
                        .version("1.0.0")
                        .description("Tài liệu API cho dự án VietBank. Cung cấp các endpoint để quản lý khách hàng, tài khoản và giao dịch.")
                )
                // Thêm yêu cầu bảo mật chung cho tất cả các API
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                // Cấu hình thành phần bảo mật (JWT Bearer Token)
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}
