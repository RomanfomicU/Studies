package com.fomic.dragonsrksp7;

import com.fomic.dragonsrksp7.controller.DragonController;
import com.fomic.dragonsrksp7.model.Dragon;
import com.fomic.dragonsrksp7.repository.DragonRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DragonControllerTest {
    @Test
    public void testGetDragonById() {
        Dragon dragon = new Dragon();
        dragon.setId(1L);
        dragon.setName("Whiskers");

        DragonRepository dragonRepository = Mockito.mock(DragonRepository.class);
        when(dragonRepository.findById(1L)).thenReturn(Mono.just(dragon));

        DragonController dragonController = new DragonController(dragonRepository);

        ResponseEntity<Dragon> response = dragonController.getDragonById(1L).block();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dragon, response.getBody());
    }

    @Test
    public void testGetAllDragons() {
        Dragon dragon1 = new Dragon();
        dragon1.setId(1L);
        dragon1.setName("Whiskers");
        dragon1.setAge(3);
        Dragon dragon2 = new Dragon();
        dragon2.setId(2L);
        dragon2.setName("Fluffy");
        dragon2.setAge(4);

        DragonRepository dragonRepository = Mockito.mock(DragonRepository.class);
        when(dragonRepository.findAll()).thenReturn(Flux.just(dragon1, dragon2));

        DragonController dragonController = new DragonController(dragonRepository);

        Flux<Dragon> response = dragonController.getAllDragons(null);
        assertEquals(2, response.collectList().block().size());
    }

    @Test
    public void testCreateDragon() {
        Dragon dragon = new Dragon();
        dragon.setId(1L);
        dragon.setName("Whiskers");

        DragonRepository dragonRepository = Mockito.mock(DragonRepository.class);
        when(dragonRepository.save(dragon)).thenReturn(Mono.just(dragon));

        DragonController dragonController = new DragonController(dragonRepository);

        Mono<Dragon> response = dragonController.createDragon(dragon);
        assertEquals(dragon, response.block());
    }

    @Test
    public void testUpdateDragon() {
        Dragon existingDragon = new Dragon();
        existingDragon.setId(1L);
        existingDragon.setName("Whiskers");

        Dragon updatedDragon = new Dragon();
        updatedDragon.setId(1L);
        updatedDragon.setName("Fluffy");

        DragonRepository dragonRepository = Mockito.mock(DragonRepository.class);
        when(dragonRepository.findById(1L)).thenReturn(Mono.just(existingDragon));
        when(dragonRepository.save(existingDragon)).thenReturn(Mono.just(updatedDragon));

        DragonController dragonController = new DragonController(dragonRepository);

        ResponseEntity<Dragon> response = dragonController.updateDragon(1L, updatedDragon).block();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDragon, response.getBody());
    }

    @Test
    public void testDeleteDragon() {
        Dragon dragon = new Dragon();
        dragon.setId(1L);
        dragon.setName("Whiskers");

        DragonRepository dragonRepository = Mockito.mock(DragonRepository.class);
        when(dragonRepository.findById(1L)).thenReturn(Mono.just(dragon));
        when(dragonRepository.delete(dragon)).thenReturn(Mono.empty());

        DragonController dragonController = new DragonController(dragonRepository);

        ResponseEntity<Void> response = dragonController.deleteDragon(1L).block();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}
