package mirea.fomichev.notifications;

import mirea.fomichev.notifications.service.NotificationService;
import org.junit.jupiter.api.Test;
import mirea.fomichev.notifications.dto.User;
import mirea.fomichev.notifications.feign.UserServiceFeignUser;
import mirea.fomichev.notifications.model.Notification;
import mirea.fomichev.notifications.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {
	@Mock
	private NotificationRepository notificationRepository;

	@Mock
	private UserServiceFeignUser userServiceFeignUser;

	@InjectMocks
	private NotificationService notificationService;

	private Notification notification1;
	private Notification notification2;

	@BeforeEach
	public void setUp() {
		notification1 = new Notification();
		notification1.setId(1L);
		notification1.setUserId(100L);
		notification1.setMessage("Payment of $250.0 was successfully processed.");

		notification2 = new Notification();
		notification2.setId(2L);
		notification2.setUserId(101L);
		notification2.setMessage("Your subscription has been renewed.");
	}

	@Test
	public void testCreateNotificationSuccess() {
		User user = new User(100L, "Иван Иванов", "ivan@example.com");
		when(userServiceFeignUser.getUserById(notification1.getUserId()))
				.thenReturn(new ResponseEntity<>(user, HttpStatus.OK));
		when(notificationRepository.save(any(Notification.class))).thenReturn(notification1);

		Notification createdNotification = notificationService.createNotification(notification1);

		assertNotNull(createdNotification);
		assertEquals(notification1.getId(), createdNotification.getId());
		assertEquals(notification1.getUserId(), createdNotification.getUserId());
		assertEquals(notification1.getMessage(), createdNotification.getMessage());

		verify(userServiceFeignUser, times(1)).getUserById(notification1.getUserId());
		verify(notificationRepository, times(1)).save(notification1);
	}

	@Test
	public void testCreateNotificationUserNotFound() {
		when(userServiceFeignUser.getUserById(notification1.getUserId()))
				.thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			notificationService.createNotification(notification1);
		});

		assertEquals("User does not exist", exception.getMessage());

		verify(userServiceFeignUser, times(1)).getUserById(notification1.getUserId());
		verify(notificationRepository, never()).save(any(Notification.class));
	}

	@Test
	public void testGetAllNotifications() {
		// Arrange
		List<Notification> notifications = Arrays.asList(notification1, notification2);
		when(notificationRepository.findAll()).thenReturn(notifications);

		// Act
		List<Notification> result = notificationService.getAllNotifications();

		// Assert
		assertEquals(2, result.size());
		assertTrue(result.contains(notification1));
		assertTrue(result.contains(notification2));

		verify(notificationRepository, times(1)).findAll();
	}

	@Test
	public void testGetNotificationById_Found() {
		// Arrange
		when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification1));

		// Act
		Notification foundNotification = notificationService.getNotificationById(1L);

		// Assert
		assertNotNull(foundNotification);
		assertEquals(notification1.getId(), foundNotification.getId());
		assertEquals(notification1.getUserId(), foundNotification.getUserId());
		assertEquals(notification1.getMessage(), foundNotification.getMessage());

		verify(notificationRepository, times(1)).findById(1L);
	}

	@Test
	public void testGetNotificationById_NotFound() {
		// Arrange
		when(notificationRepository.findById(3L)).thenReturn(Optional.empty());

		// Act & Assert
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			notificationService.getNotificationById(3L);
		});

		assertEquals("Notification not found", exception.getMessage());

		verify(notificationRepository, times(1)).findById(3L);
	}
}

