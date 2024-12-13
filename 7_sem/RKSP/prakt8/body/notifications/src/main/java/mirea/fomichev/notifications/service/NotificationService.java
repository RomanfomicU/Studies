package mirea.fomichev.notifications.service;

import mirea.fomichev.notifications.dto.User;
import mirea.fomichev.notifications.feign.UserServiceFeignUser;
import mirea.fomichev.notifications.model.Notification;
import mirea.fomichev.notifications.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserServiceFeignUser clientServiceFeignClient;

    public Notification createNotification(Notification notification) {
        ResponseEntity<User> clientResponse = clientServiceFeignClient.getClientById(notification.getClientId());
        if (!clientResponse.getStatusCode().is2xxSuccessful()) {
            throw new IllegalArgumentException("Client does not exist");
        }

        // Save notification
        return notificationRepository.save(notification);
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
    }
}
