package mirea.fomichev.payments.feign;

import mirea.fomichev.payments.dto.Notification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notifications")
public interface NotificationServiceFeignClient {
    @PostMapping("/notifications")
    ResponseEntity<Notification> sendNotification(@RequestBody Notification notification);
}

