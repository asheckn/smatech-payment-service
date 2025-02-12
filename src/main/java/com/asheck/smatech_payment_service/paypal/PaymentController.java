package com.asheck.smatech_payment_service.paypal;

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
    public ResponseEntity<String> paymentSuccess(@RequestParam("token") String orderId) {
        boolean success = payPalService.capturePayment(orderId);
        return success ?
                ResponseEntity.ok("Payment captured successfully") :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment capture failed");
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> paymentCancel() {
        return ResponseEntity.ok("Payment cancelled");
    }
}
