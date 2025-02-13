package com.asheck.smatech_payment_service.paypal;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
public class PaymentRequest {

    private String customerName;

    private String orderCode;

    private String returnUrl;

    private String cancelUrl;

}
