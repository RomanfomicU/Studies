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
// Создайте фиктивного дракона
        Dragon dragon = new Dragon();
        dragon.setId(1L);
        dragon.setName("Whiskers");
// Создайте мок репозитория
        DragonRepository dragonRepository = Mockito.mock(DragonRepository.class);
        when(dragonRepository.findById(1L)).thenReturn(Mono.just(dragon));
// Создайте экземпляр контроллера
        DragonController dragonController = new DragonController(dragonRepository);
// Вызовите метод контроллера и проверьте результат
        ResponseEntity<Dragon> response = dragonController.getDragonById(1L).block();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dragon, response.getBody());
    }

    @Test
    public void testGetAllDragons() {
// Создайте список фиктивных драконов
        Dragon dragon1 = new Dragon();
        dragon1.setId(1L);
        dragon1.setName("Whiskers");
        dragon1.setAge(3);
        Dragon dragon2 = new Dragon();
        dragon2.setId(2L);
        dragon2.setName("Fluffy");
        dragon2.setAge(4);
// Создайте мок репозитория
        DragonRepository dragonRepository = Mockito.mock(DragonRepository.class);
        when(dragonRepository.findAll()).thenReturn(Flux.just(dragon1, dragon2));
// Создайте экземпляр контроллера
        DragonController dragonController = new DragonController(dragonRepository);
// Вызовите метод контроллера и проверьте результат
        Flux<Dragon> response = dragonController.getAllDragons(null);
        assertEquals(2, response.collectList().block().size());
    }

    @Test
    public void testCreateDragon() {
// Создайте фиктивного дракона
        Dragon dragon = new Dragon();
        dragon.setId(1L);
        dragon.setName("Whiskers");
// Создайте мок репозитория
        DragonRepository dragonRepository = Mockito.mock(DragonRepository.class);
        when(dragonRepository.save(dragon)).thenReturn(Mono.just(dragon));
// Создайте экземпляр контроллера
        DragonController dragonController = new DragonController(dragonRepository);
// Вызовите метод контроллера и проверьте результат
        Mono<Dragon> response = dragonController.createDragon(dragon);
        assertEquals(dragon, response.block());
    }

    @Test
    public void testUpdateDragon() {
// Создайте фиктивного дракона
        Dragon existingDragon = new Dragon();
        existingDragon.setId(1L);
        existingDragon.setName("Whiskers");
// Создайте фиктивного обновленного дракона
        Dragon updatedDragon = new Dragon();
        updatedDragon.setId(1L);
        updatedDragon.setName("Fluffy");
// Создайте мок репозитория
        DragonRepository dragonRepository = Mockito.mock(DragonRepository.class);
        when(dragonRepository.findById(1L)).thenReturn(Mono.just(existingDragon));
        when(dragonRepository.save(existingDragon)).thenReturn(Mono.just(updatedDragon));
// Создайте экземпляр контроллера
        DragonController dragonController = new DragonController(dragonRepository);
// Вызовите метод контроллера и проверьте результат
        ResponseEntity<Dragon> response = dragonController.updateDragon(1L, updatedDragon).block();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDragon, response.getBody());
    }

    @Test
    public void testDeleteDragon() {
// Создайте фиктивного дракона
        Dragon dragon = new Dragon();
        dragon.setId(1L);
        dragon.setName("Whiskers");
// Создайте мок репозитория
        DragonRepository dragonRepository = Mockito.mock(DragonRepository.class);
        when(dragonRepository.findById(1L)).thenReturn(Mono.just(dragon));
        when(dragonRepository.delete(dragon)).thenReturn(Mono.empty());
// Создайте экземпляр контроллера
        DragonController dragonController = new DragonController(dragonRepository);
// Вызовите метод контроллера и проверьте результат
        ResponseEntity<Void> response = dragonController.deleteDragon(1L).block();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}
