package mirea.fomichev.clients;

import mirea.fomichev.clients.model.User;
import mirea.fomichev.clients.repository.UserRepository;
import mirea.fomichev.clients.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

	@Mock
	private UserRepository clientRepository;

	@InjectMocks
	private UserService clientService;

	private User client1;
	private User client2;

	@BeforeEach
	public void setUp() {
		client1 = new User();
		client1.setId(1L);
		client1.setName("Иван Иванов");
		client1.setEmail("ivan@example.com");

		client2 = new User();
		client2.setId(2L);
		client2.setName("Мария Петрова");
		client2.setEmail("maria@example.com");
	}

	@Test
	public void testCreateClient() {
		when(clientRepository.save(any(User.class))).thenReturn(client1);

		User created = clientService.createUser(client1);

		assertNotNull(created);
		assertEquals(client1.getId(), created.getId());
		assertEquals(client1.getName(), created.getName());
		assertEquals(client1.getEmail(), created.getEmail());

		verify(clientRepository, times(1)).save(client1);
	}

	@Test
	public void testGetAllClients() {
		List<User> clients = Arrays.asList(client1, client2);
		when(clientRepository.findAll()).thenReturn(clients);

		List<User> result = clientService.getAllUsers();

		assertEquals(2, result.size());
		assertTrue(result.contains(client1));
		assertTrue(result.contains(client2));

		verify(clientRepository, times(1)).findAll();
	}

	@Test
	public void testGetClientById_Found() {
		when(clientRepository.findById(1L)).thenReturn(Optional.of(client1));

		User found = clientService.getUserById(1L);

		assertNotNull(found);
		assertEquals(client1.getId(), found.getId());
		assertEquals(client1.getName(), found.getName());

		verify(clientRepository, times(1)).findById(1L);
	}

	@Test
	public void testGetClientById_NotFound() {
		when(clientRepository.findById(3L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			clientService.getUserById(3L);
		});

		assertEquals("Client not found", exception.getMessage());

		verify(clientRepository, times(1)).findById(3L);
	}

	@Test
	public void testUpdateClient() {
		User updatedDetails = new User();
		updatedDetails.setName("Иван Смирнов");
		updatedDetails.setEmail("ivan.smirnov@example.com");

		when(clientRepository.findById(1L)).thenReturn(Optional.of(client1));
		when(clientRepository.save(any(User.class))).thenReturn(client1);

		User updated = clientService.updateUser(1L, updatedDetails);

		assertNotNull(updated);
		assertEquals(client1.getId(), updated.getId());
		assertEquals("Иван Смирнов", updated.getName());
		assertEquals("ivan.smirnov@example.com", updated.getEmail());

		verify(clientRepository, times(1)).findById(1L);
		verify(clientRepository, times(1)).save(client1);
	}

	@Test
	public void testDeleteClient() {
		doNothing().when(clientRepository).deleteById(1L);

		clientService.deleteUser(1L);

		verify(clientRepository, times(1)).deleteById(1L);
	}
}

