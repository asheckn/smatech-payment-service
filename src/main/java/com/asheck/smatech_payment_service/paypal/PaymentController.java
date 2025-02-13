package com.asheck.smatech_payment_service.paypal;

import com.asheck.smatech_payment_service.payment.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
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

    @GetMapping("/cancel")
    public ResponseEntity<String> paymentCancel() {
        return ResponseEntity.ok("Payment cancelled");
    }
}
