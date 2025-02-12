package com.asheck.smatech_payment_service.paypal;

import lombok.Data;

@Data
public class PayPalLink {
    private String href;
    private String rel;
    private String method;
}
