package com.asheck.smatech_payment_service.payment;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ApiResponseDto {

    private boolean success;

    private String message;

    private OrderDto data;
}
