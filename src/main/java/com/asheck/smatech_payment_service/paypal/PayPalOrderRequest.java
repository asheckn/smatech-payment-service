package com.asheck.smatech_payment_service.paypal;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@RequiredArgsConstructor
public class PayPalOrderRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String approveUrl;

    @Column(nullable = false)
    private BigDecimal requestAmount;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private String orderCode;

    @Column(nullable = false)
    private String invoiceId;

    @Column(nullable = false)
    private String customId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
