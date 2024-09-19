package accompany.controller;

import accompany.dto.PaymentRequest;
import accompany.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/payments/toss")
@CrossOrigin(origins = "*") // Allow CORS requests from any origin
public class PaymentController {

    @Autowired
    private PaymentService paymentsService;

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            Map<String, Object> paymentResponse = paymentsService.confirmPayment(paymentRequest);
            return ResponseEntity.ok(paymentResponse);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}