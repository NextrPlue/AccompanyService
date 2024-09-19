package accompany.service;

import accompany.dto.PaymentRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    private static final String SECRET_KEY = "test_sk_P9BRQmyarY7JaZd0gg0v3J07KzLN";

    public Map<String, Object> confirmPayment(PaymentRequest paymentRequest) throws Exception {
        String paymentKey = paymentRequest.getPaymentKey();
        String orderId = paymentRequest.getOrderId();
        String amount = paymentRequest.getAmount();

        String secretKey = SECRET_KEY + ":";
        String encodedAuth = Base64.getEncoder().encodeToString(secretKey.getBytes());
        String authorizationHeader = "Basic " + encodedAuth;

        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("orderId", orderId);
        requestBodyMap.put("amount", amount);
        requestBodyMap.put("paymentKey", paymentKey);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(requestBodyMap);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/payments/confirm"))
                .header("Content-Type", "application/json")
                .header("Authorization", authorizationHeader)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Map<String, Object> paymentResponse = objectMapper.readValue(
                    response.body(), new TypeReference<>() {});
            return paymentResponse;
        } else {
            // Handle error response
            Map<String, Object> errorResponse = objectMapper.readValue(
                    response.body(), new TypeReference<>() {});
            throw new Exception("Payment confirmation failed: " + errorResponse);
        }
    }
}