package com.asheck.smatech_payment_service.paypal;

import com.asheck.smatech_payment_service.config.PayPalConfig;
import com.asheck.smatech_payment_service.config.RestTemplateService;
import com.asheck.smatech_payment_service.payment.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PayPalService {
    private final PayPalConfig config;
    private final RestTemplate restTemplate;

    private final RestTemplateService restTemplateService;
    private final PayPalOrderRequestRepository payPalOrderRequestRepository;

    private final PaymentRepository paymentRepository;



    public PaymentResponse createPayment(PaymentRequest request) {
        // Get payment amount from store service using order code

        OrderDto orderDto = restTemplateService.getOrder(request.getOrderCode());
        if (orderDto == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Order not found");
        }


        String accessToken = getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> orderRequest = new HashMap<>();
        orderRequest.put("intent", "CAPTURE");

        Map<String, Object> amount = new HashMap<>();
        amount.put("currency_code", orderDto.getCurrencyCode());
        amount.put("value", orderDto.getOrderTotal().toString());

        Map<String, Object> purchaseUnit = new HashMap<>();
        purchaseUnit.put("amount", amount);

        purchaseUnit.put("description", "PayPal order for customer "+request.getCustomerName()+ " for Order with reference "+request.getOrderCode());

        purchaseUnit.put("invoice_id", orderDto.getInvoiceNumber());

        purchaseUnit.put("custom_id", request.getOrderCode());

        orderRequest.put("purchase_units", List.of(purchaseUnit));

        Map<String, String> redirectUrls = new HashMap<>();
        redirectUrls.put("return_url", config.getReturnUrl());
        redirectUrls.put("cancel_url", config.getCancelUrl());
        orderRequest.put("application_context", redirectUrls);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(orderRequest, headers);

        ResponseEntity<PayPalOrderResponse> response = restTemplate.postForEntity(
                config.getBaseUrl() + "/v2/checkout/orders",
                entity,
                PayPalOrderResponse.class
        );

        PayPalOrderResponse order = response.getBody();
        String approvalUrl = order.getLinks().stream()
                .filter(link -> "approve".equals(link.getRel()))
                .findFirst()
                .map(PayPalLink::getHref)
                .orElseThrow();

        ///Save Paypal response to database
        PayPalOrderRequest payPalOrderRequest = new PayPalOrderRequest();
        payPalOrderRequest.setRequestAmount(orderDto.getOrderTotal());
        payPalOrderRequest.setCurrency(orderDto.getCurrencyCode());
        payPalOrderRequest.setOrderCode(request.getOrderCode());
        payPalOrderRequest.setOrderId(response.getBody().getId());
        payPalOrderRequest.setApproveUrl(approvalUrl);
        payPalOrderRequest.setInvoiceId(orderDto.getInvoiceNumber());
        payPalOrderRequest.setCustomId(request.getOrderCode());
        payPalOrderRequest.setDescription(request.getCustomerName());

        payPalOrderRequestRepository.save(payPalOrderRequest);

        //Create the Payment and save it before sending back
        Payment payment = new Payment();
        payment.setAmount(orderDto.getOrderTotal());
        payment.setCurrency(orderDto.getCurrencyCode());
        payment.setOrderCode(request.getOrderCode());
        payment.setOrderInvoiceNumber(orderDto.getInvoiceNumber());
        payment.setCustomerId(orderDto.getCustomerId());
        payment.setPayPalOrderId(response.getBody().getId());
        payment.setPaymentStatus(PaymentStatus.INITIATED);
        payment.setPaymentMethod(PaymentMethod.PAYPAL);
        payment.setPaymentReference(request.getOrderCode());
        payment.setPayPalOrderId(response.getBody().getId());
        payment.setPayPalOrderStatus(response.getBody().getStatus());
        payment.setPaymentDescription("PayPal order for customer "+request.getCustomerName()+ " for Order with reference "+request.getOrderCode());
        payment.setPayPalOrderRequest(payPalOrderRequest);

        paymentRepository.save(payment);

       PaymentResponse paymentResponse =  new PaymentResponse();

       paymentResponse.setPaymentId(order.getId());
       paymentResponse.setApprovalUrl(approvalUrl);
       paymentResponse.setStatus(order.getStatus());

       return  paymentResponse;

    }

    public Boolean capturePayment(String payPalOrderId) {
        String accessToken = getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<PayPalOrderResponse> response = restTemplate.postForEntity(
                config.getBaseUrl() + "/v2/checkout/orders/" + payPalOrderId + "/capture",
                entity,
                PayPalOrderResponse.class
        );

        //Update the payment status
        Payment payment = paymentRepository.findPaymentByPayPalOrderId(payPalOrderId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found")
        );

        if(Objects.equals(response.getBody().getStatus(), "COMPLETED")){
            payment.setPaymentStatus(PaymentStatus.COMPLETE);
            payment.setPayPalOrderStatus(response.getBody().getStatus());
            //Update the order service
            restTemplateService.payOrder(payment.getOrderCode());
            return true;
        }else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            payment.setPayPalOrderStatus(response.getBody().getStatus());
            return false;
        }

    }

    private String getAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(config.getClientId(), config.getSecret());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                config.getBaseUrl() + "/v1/oauth2/token",
                request,
                Map.class
        );

        return (String) response.getBody().get("access_token");
    }
}
