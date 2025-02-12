package com.asheck.smatech_payment_service.paypal;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PayPalOrderResponse {
    private String id;
    private String status;
    private List<PayPalLink> links;
    private String create_time;
    private String update_time;
    private String intent;
    private Payer payer;
}
