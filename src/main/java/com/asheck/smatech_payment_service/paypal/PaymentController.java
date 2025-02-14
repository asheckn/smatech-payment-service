package com.asheck.smatech_payment_service.paypal;

import com.asheck.smatech_payment_service.payment.Payment;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@SecurityRequirement(name = "authorization")
@RequiredArgsConstructor
public class PaymentController {
    private final PayPalService payPalService;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest request) {
        PaymentResponse response = payPalService.createPayment(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/success")
    public ResponseEntity<?> paymentSuccess(@RequestParam("token") String orderId) {
        Payment payment = payPalService.capturePayment(orderId);
        return
                ResponseEntity.ok(payment);
    }


    @GetMapping("/get-all-payments")
    public ResponseEntity<?> getAllPayments() {
        List<Payment> payments = payPalService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/get-payment-by-customerId/{customerId}")
    public ResponseEntity<?> getPaymentsByCustomerId(@PathVariable("customerId") Long customerId) {
        List<Payment> payment = payPalService.getAllPaymentsByCustomerId(customerId);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> paymentCancel() {
        return ResponseEntity.ok("Payment cancelled");
    }
}
