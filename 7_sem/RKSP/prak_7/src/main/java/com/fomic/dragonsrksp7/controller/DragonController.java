package com.fomic.dragonsrksp7.controller;

import com.fomic.dragonsrksp7.exception.CustomException;
import com.fomic.dragonsrksp7.model.Dragon;
import com.fomic.dragonsrksp7.repository.DragonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/dragons")
public class DragonController {
    private final DragonRepository dragonRepository;

    @Autowired
    public DragonController(DragonRepository dragonRepository) {
        this.dragonRepository = dragonRepository;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Dragon>> getDragonById(@PathVariable Long id) {
        return dragonRepository.findById(id)
                .map(dragon -> ResponseEntity.ok(dragon))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<Dragon> getAllDragons(@RequestParam(name = "minage", required = false) Integer minAge) {
        Flux<Dragon> dragons = dragonRepository.findAll();
        if (minAge != null && minAge > 0) {
// Если параметр "minage" указан, применяем фильтрацию
            dragons = dragons.filter(dragon -> dragon.getAge() >= minAge);
        }
        return dragons
                .map(this::transformDragon) // Применение оператора map для преобразования объектов Dragon
                .onErrorResume(e -> {
// Обработка ошибок
                    return Flux.error(new CustomException("Failed to fetch dragons", e));
                })
                .onBackpressureBuffer(); // Работа в формате backpressure
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Dragon> createDragon(@RequestBody Dragon dragon) {
        return dragonRepository.save(dragon);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Dragon>> updateDragon(@PathVariable Long id, @RequestBody Dragon updatedDragon) {
        return dragonRepository.findById(id)
                .flatMap(existingDragon -> {
                    existingDragon.setName(updatedDragon.getName());
                    existingDragon.setColor(updatedDragon.getColor());
                    existingDragon.setAge(updatedDragon.getAge());
                    existingDragon.setBreed(updatedDragon.getBreed());
                    return dragonRepository.save(existingDragon);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteDragon(@PathVariable Long id) {
        return dragonRepository.findById(id)
                .flatMap(existingDragon ->
                        dragonRepository.delete(existingDragon)
                                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    private Dragon transformDragon(Dragon dragon) {
// Пример преобразования объекта Dragon
        dragon.setName(dragon.getName().toUpperCase()); // Преобразование имени кота в верхний регистр
        return dragon;
    }
}
