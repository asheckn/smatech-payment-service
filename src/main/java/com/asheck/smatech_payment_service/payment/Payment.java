package com.asheck.smatech_payment_service.payment;


import com.asheck.smatech_payment_service.paypal.PayPalOrderRequest;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@RequiredArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String paymentReference;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private String orderCode;


    @Column(nullable = false)
    private String orderInvoiceNumber;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String paymentDescription;

    @Column(nullable = false)
    private String payPalOrderId;

    @Column(nullable = false)
    private String payPalOrderStatus;

    @OneToOne(cascade = CascadeType.ALL)
    private PayPalOrderRequest payPalOrderRequest;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
