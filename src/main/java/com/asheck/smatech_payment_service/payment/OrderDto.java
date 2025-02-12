package com.asheck.smatech_payment_service.payment;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
public class OrderDto {

    private BigDecimal orderTotal;

    private String orderCode;

    private String orderStatus;

    private String reference;

    private Long customerId;

    private String currencyCode;

    private String invoiceNumber;

}
