package mirea.fomichev.payments;

import mirea.fomichev.payments.service.DeliveryService;
import org.junit.jupiter.api.Test;
import mirea.fomichev.payments.dto.Client;
import mirea.fomichev.payments.dto.Notification;
import mirea.fomichev.payments.feign.UserServiceFeignClient;
import mirea.fomichev.payments.feign.NotificationServiceFeignClient;
import mirea.fomichev.payments.model.Delivery;
import mirea.fomichev.payments.repository.DeliveryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
public class PaymentServiceTest {

	@Mock
	private DeliveryRepository paymentRepository;

	@Mock
	private UserServiceFeignClient clientServiceFeignClient;

	@Mock
	private NotificationServiceFeignClient notificationServiceFeignClient;

	@InjectMocks
	private DeliveryService deliveryService;

	private Delivery payment1;
	private Delivery payment2;

	@BeforeEach
	public void setUp() {
		payment1 = new Delivery();
		payment1.setId(1L);
		payment1.setClientId(100L);
		payment1.setAmount(250.00);

		payment2 = new Delivery();
		payment2.setId(2L);
		payment2.setClientId(101L);
		payment2.setAmount(150.50);
	}

	@Test
	public void testCreatePayment_Success() {
		// Arrange
		when(clientServiceFeignClient.getClientById(payment1.getClientId()))
				.thenReturn(new ResponseEntity<>(new Client(payment1.getClientId(), "Иван Иванов", "ivan@example.com"), HttpStatus.OK));
		when(paymentRepository.save(any(Delivery.class))).thenReturn(payment1);
		when(notificationServiceFeignClient.sendNotification(any(Notification.class))).thenReturn(ResponseEntity.ok().build());

		// Act
		Delivery createdPayment = deliveryService.createDelivery(payment1);

		// Assert
		assertNotNull(createdPayment);
		assertEquals(payment1.getId(), createdPayment.getId());
		assertEquals(payment1.getClientId(), createdPayment.getClientId());
		assertEquals(payment1.getAmount(), createdPayment.getAmount());

		verify(clientServiceFeignClient, times(1)).getClientById(payment1.getClientId());
		verify(paymentRepository, times(1)).save(payment1);
		verify(notificationServiceFeignClient, times(1)).sendNotification(any(Notification.class));

		// Дополнительная проверка отправленного уведомления
		ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
		verify(notificationServiceFeignClient).sendNotification(notificationCaptor.capture());
		Notification sentNotification = notificationCaptor.getValue();
		assertEquals(payment1.getClientId(), sentNotification.getClientId());
		assertEquals("Payment of $250.0 was successfully processed.", sentNotification.getMessage());
	}

	@Test
	public void testCreatePayment_ClientNotFound() {
		// Arrange
		when(clientServiceFeignClient.getClientById(payment1.getClientId()))
				.thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

		// Act & Assert
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			deliveryService.createDelivery(payment1);
		});

		assertEquals("Client does not exist", exception.getMessage());

		verify(clientServiceFeignClient, times(1)).getClientById(payment1.getClientId());
		verify(paymentRepository, never()).save(any(Delivery.class));
		verify(notificationServiceFeignClient, never()).sendNotification(any(Notification.class));
	}

	@Test
	public void testGetAllPayments() {
		// Arrange
		List<Delivery> payments = Arrays.asList(payment1, payment2);
		when(paymentRepository.findAll()).thenReturn(payments);

		// Act
		List<Delivery> result = deliveryService.getAllDeliveries();

		// Assert
		assertEquals(2, result.size());
		assertTrue(result.contains(payment1));
		assertTrue(result.contains(payment2));

		verify(paymentRepository, times(1)).findAll();
	}

	@Test
	public void testGetPaymentById_Found() {
		// Arrange
		when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment1));

		// Act
		Delivery foundPayment = deliveryService.getDeliveryById(1L);

		// Assert
		assertNotNull(foundPayment);
		assertEquals(payment1.getId(), foundPayment.getId());
		assertEquals(payment1.getClientId(), foundPayment.getClientId());
		assertEquals(payment1.getAmount(), foundPayment.getAmount());

		verify(paymentRepository, times(1)).findById(1L);
	}

	@Test
	public void testGetPaymentById_NotFound() {
		// Arrange
		when(paymentRepository.findById(3L)).thenReturn(Optional.empty());

		// Act & Assert
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			deliveryService.getDeliveryById(3L);
		});

		assertEquals("Payment not found", exception.getMessage());

		verify(paymentRepository, times(1)).findById(3L);
	}

	@Test
	public void testUpdatePayment_Success() {
		// Arrange
		Delivery updatedDetails = new Delivery();
		updatedDetails.setAmount(300.00);

		when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment1));
		when(paymentRepository.save(any(Delivery.class))).thenReturn(payment1);

		// Act
		Delivery updatedPayment = deliveryService.updateDelivery(1L, updatedDetails);

		// Assert
		assertNotNull(updatedPayment);
		assertEquals(payment1.getId(), updatedPayment.getId());
		assertEquals(payment1.getClientId(), updatedPayment.getClientId());
		assertEquals(300.00, updatedPayment.getAmount());

		verify(paymentRepository, times(1)).findById(1L);
		verify(paymentRepository, times(1)).save(payment1);
	}

	@Test
	public void testUpdatePayment_NotFound() {
		// Arrange
		Delivery updatedDetails = new Delivery();
		updatedDetails.setAmount(300.00);

		when(paymentRepository.findById(3L)).thenReturn(Optional.empty());

		// Act & Assert
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			deliveryService.updateDelivery(3L, updatedDetails);
		});

		assertEquals("Payment not found", exception.getMessage());

		verify(paymentRepository, times(1)).findById(3L);
		verify(paymentRepository, never()).save(any(Delivery.class));
	}
}
