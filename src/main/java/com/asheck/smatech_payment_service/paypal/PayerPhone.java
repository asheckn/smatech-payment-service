package com.asheck.smatech_payment_service.paypal;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PayerPhone {
    private String phone_number;
    private String phone_type;
}
