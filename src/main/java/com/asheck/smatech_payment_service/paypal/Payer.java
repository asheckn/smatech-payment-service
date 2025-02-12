package com.asheck.smatech_payment_service.paypal;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Payer {
    private String email_address;
    private PayerName name;
    private String payer_id;
    private PayerAddress address;
    private String country_code;
    private PayerPhone phone;
    private String tax_id;
    private String tax_id_type;
    private String birth_date;
}
