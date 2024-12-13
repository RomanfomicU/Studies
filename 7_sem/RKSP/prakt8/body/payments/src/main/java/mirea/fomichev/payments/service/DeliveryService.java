package mirea.fomichev.payments.service;

import mirea.fomichev.payments.dto.Client;
import mirea.fomichev.payments.dto.Notification;
import mirea.fomichev.payments.feign.UserServiceFeignClient;
import mirea.fomichev.payments.feign.NotificationServiceFeignClient;
import mirea.fomichev.payments.model.Delivery;
import mirea.fomichev.payments.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryService {
    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private UserServiceFeignClient userServiceFeignClient;

    @Autowired
    private NotificationServiceFeignClient notificationServiceFeignClient;

    public Delivery createDelivery(Delivery delivery) {
        ResponseEntity<Client> clientResponse = userServiceFeignClient.getClientById(delivery.getClientId());
        if (!clientResponse.getStatusCode().is2xxSuccessful()) {
            throw new IllegalArgumentException("User does not exist");
        }

        Delivery savedDelivery = deliveryRepository.save(delivery);

        Notification notification = new Notification();
        notification.setClientId(delivery.getClientId());
        notification.setMessage("Order was delivered.");
        notificationServiceFeignClient.sendNotification(notification);

        return savedDelivery;
    }

    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    public Delivery getDeliveryById(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    public Delivery updateDelivery(Long id, Delivery deliveryDetails) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        delivery.setAmount(deliveryDetails.getAmount());
        return deliveryRepository.save(delivery);
    }
}

