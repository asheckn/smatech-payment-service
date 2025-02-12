package com.asheck.smatech_payment_service.paypal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PaymentResponse {
    private String paymentId;
    private String approvalUrl;
    private String status;
}
