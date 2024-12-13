package mirea.fomichev.payments.controller;

import mirea.fomichev.payments.model.Delivery;
import mirea.fomichev.payments.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class DeliveryController {
    @Autowired
    private DeliveryService paymentService;

    @PostMapping
    public ResponseEntity<Delivery> createPayment(@RequestBody Delivery payment) {
        return ResponseEntity.ok(paymentService.createDelivery(payment));
    }

    @GetMapping
    public ResponseEntity<List<Delivery>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllDeliveries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Delivery> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getDeliveryById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Delivery> updatePayment(@PathVariable Long id, @RequestBody Delivery paymentDetails) {
        return ResponseEntity.ok(paymentService.updateDelivery(id, paymentDetails));
    }
}


