package mirea.fomichev.payments.service;

import mirea.fomichev.payments.dto.Client;
import mirea.fomichev.payments.dto.Notification;
import mirea.fomichev.payments.feign.ClientServiceFeignClient;
import mirea.fomichev.payments.feign.NotificationServiceFeignClient;
import mirea.fomichev.payments.model.Payment;
import mirea.fomichev.payments.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ClientServiceFeignClient clientServiceFeignClient;

    @Autowired
    private NotificationServiceFeignClient notificationServiceFeignClient;

    public Payment createPayment(Payment payment) {
        // Check if client exists
        ResponseEntity<Client> clientResponse = clientServiceFeignClient.getClientById(payment.getClientId());
        if (!clientResponse.getStatusCode().is2xxSuccessful()) {
            throw new IllegalArgumentException("Client does not exist");
        }

        Payment savedPayment = paymentRepository.save(payment);

        Notification notification = new Notification();
        notification.setClientId(payment.getClientId());
        notification.setMessage("Payment of $" + payment.getAmount() + " was successfully processed.");
        notificationServiceFeignClient.sendNotification(notification);

        return savedPayment;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
    }

    public Payment updatePayment(Long id, Payment paymentDetails) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        payment.setAmount(paymentDetails.getAmount());
        return paymentRepository.save(payment);
    }
}

