package com.asheck.smatech_payment_service.paypal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PayPalOrderRequestRepository extends JpaRepository<PayPalOrderRequest, Long>{


    Optional<PayPalOrderRequest> findPayPalOrderRequestByOrderId(String orderId);
}
