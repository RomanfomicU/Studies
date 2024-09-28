package fomichev.prak_4;

import io.rsocket.frame.decoder.PayloadDecoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class Prak4ApplipenguinionTests {

	@Autowired
	private PenguinRepository penguinRepository;
	private RSocketRequester requester;
	@BeforeEach
	public void setup() {
		requester = RSocketRequester.builder()
				.rsocketStrategies(builder -> builder.decoder(new Jackson2JsonDecoder()))
				.rsocketStrategies(builder -> builder.encoder(new Jackson2JsonEncoder()))
				.rsocketConnector(connector -> connector
						.payloadDecoder(PayloadDecoder.ZERO_COPY)
						.reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2))))
				.dataMimeType(MimeTypeUtils.APPLICATION_JSON)
				.tcp("localhost", 5200);
	}
	@AfterEach
	public void cleanup() {
		requester.dispose();
	}
	@Test
	public void testGetPenguin() {
		Penguin penguin = new Penguin();
		penguin.setName("TestPenguin");
		penguin.setAge(2);
		penguin.setColor("Black");
		penguin.setBreed("Siamese");
		Penguin savedPenguin = penguinRepository.save(penguin);
		Mono<Penguin> result = requester.route("getPenguin")
				.data(savedPenguin.getId())
				.retrieveMono(Penguin.class);
		assertNotNull(result.block());
	}
	@Test
	public void testAddPenguin() {
		Penguin penguin = new Penguin();
		penguin.setName("TestPenguin");
		penguin.setAge(2);
		penguin.setColor("Black");
		penguin.setBreed("Siamese");
		Mono<Penguin> result = requester.route("addPenguin")
				.data(penguin)
				.retrieveMono(Penguin.class);
		Penguin savedPenguin = result.block();
		assertNotNull(savedPenguin);
		assertNotNull(savedPenguin.getId());
		assertTrue(savedPenguin.getId() > 0);
	}
	@Test
	public void testGetPenguins() {
		Flux<Penguin> result = requester.route("getPenguins")
				.retrieveFlux(Penguin.class);
		assertNotNull(result.blockFirst());
	}
	@Test
	public void testDeletePenguin() {
		Penguin penguin = new Penguin();
		penguin.setName("TestPenguin");
		penguin.setAge(2);
		penguin.setColor("Black");
		penguin.setBreed("Siamese");
		Penguin savedPenguin = penguinRepository.save(penguin);
		Mono<Void> result = requester.route("deletePenguin")
				.data(savedPenguin.getId())
				.send();
		result.block();
		Penguin deletedPenguin = penguinRepository.findPenguinById(savedPenguin.getId());
		assertNotSame(deletedPenguin, savedPenguin);
	}
}
