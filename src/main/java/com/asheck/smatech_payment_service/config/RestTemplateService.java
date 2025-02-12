package com.asheck.smatech_payment_service.config;
import com.asheck.smatech_payment_service.payment.ApiResponseDto;
import com.asheck.smatech_payment_service.payment.OrderDto;
import com.asheck.smatech_payment_service.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RestTemplateService {


    private final RestTemplate restTemplateEureka;

    @Value("${service.auth.url}")
    private String AUTH_SERVICE_URL;

    @Value("${service.store.url}")
    private String STORE_SERVICE_URL;

    public UserDto getAuthenticatedUser(String token) {

        String serviceUrl = AUTH_SERVICE_URL + token;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token); // Add JWT token

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<UserDto> response =  restTemplateEureka.exchange(serviceUrl, HttpMethod.GET, entity, UserDto.class);

        return response.getBody();
    }

    public OrderDto getOrder(String orderCode) {
        String serviceUrl = STORE_SERVICE_URL + "order/get-order-by-code?orderCode=" + orderCode;

        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth("Authorization"); // Add JWT token

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<ApiResponseDto> response =  restTemplateEureka.exchange(serviceUrl, HttpMethod.GET, entity, ApiResponseDto.class);

        return response.getBody().getData();
    }

    public OrderDto payOrder(String orderCode) {
        String serviceUrl = STORE_SERVICE_URL + "order/pay-order/" + orderCode;

        HttpHeaders headers = new HttpHeaders();
//      headers.setBearerAuth("Authorization"); // Add JWT token

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<ApiResponseDto> response =  restTemplateEureka.exchange(serviceUrl, HttpMethod.PUT, entity, ApiResponseDto.class);

        return response.getBody().getData();
    }

}
