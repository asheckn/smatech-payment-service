package com.asheck.smatech_payment_service.paypal;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PayerAddress {
    private String address_line_1;
    private String address_line_2;
    private String admin_area_2;
    private String admin_area_1;
    private String postal_code;
    private String country_code;
}
