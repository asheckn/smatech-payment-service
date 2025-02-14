package com.asheck.smatech_payment_service.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long>{


    Optional<Payment> findPaymentByPayPalOrderId(String payPalOrderId);

    Optional<List<Payment>> findPaymentByCustomerId(long customerId);

}
