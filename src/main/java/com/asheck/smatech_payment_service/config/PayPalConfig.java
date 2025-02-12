package com.asheck.smatech_payment_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "paypal")
@Data
public class PayPalConfig {
    private String clientId;
    private String secret;
    private String mode;
    private String returnUrl;
    private String cancelUrl;

    public String getBaseUrl() {
        return "sandbox".equalsIgnoreCase(mode) ?
                "https://api.sandbox.paypal.com" : "https://api.paypal.com";
    }
}
