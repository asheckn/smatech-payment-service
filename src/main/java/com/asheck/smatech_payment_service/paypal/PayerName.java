package com.asheck.smatech_payment_service.paypal;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PayerName {
    private String given_name;
    private String surname;
}
