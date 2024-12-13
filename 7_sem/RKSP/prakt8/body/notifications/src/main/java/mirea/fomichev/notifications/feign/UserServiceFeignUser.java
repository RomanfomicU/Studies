package mirea.fomichev.notifications.feign;

import mirea.fomichev.notifications.dto.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "clients")
public interface UserServiceFeignUser {
    @GetMapping("/clients/{id}")
    ResponseEntity<User> getUserById(@PathVariable("id") Long id);
}