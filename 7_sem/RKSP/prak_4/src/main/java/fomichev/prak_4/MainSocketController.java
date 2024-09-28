package fomichev.prak_4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class MainSocketController {
    private final PenguinRepository penguinRepository;
    @Autowired
    public MainSocketController(PenguinRepository penguinRepository) {
        this.penguinRepository = penguinRepository;
    }
    @MessageMapping("getPenguin")
    public Mono<Penguin> getPenguin(Long id) {
        return Mono.justOrEmpty(penguinRepository.findPenguinById(id));
    }
    @MessageMapping("addPenguin")
    public Mono<Penguin> addPenguin(Penguin Penguin) {
        return Mono.justOrEmpty(penguinRepository.save(Penguin));
    }
    @MessageMapping("getPenguins")
    public Flux<Penguin> getPenguins() {
        return Flux.fromIterable(penguinRepository.findAll());
    }
    @MessageMapping("deletePenguin")
    public Mono<Void> deletePenguin(Long id){
        Penguin Penguin = penguinRepository.findPenguinById(id);
        penguinRepository.delete(Penguin);
        return Mono.empty();
    }
    @MessageMapping("penguinChannel")
    public Flux<Penguin> penguinChannel(Flux<Penguin> penguins){
        return penguins.flatMap(Penguin -> Mono.fromCallable(() ->
                        penguinRepository.save(Penguin)))
                .collectList()
                .flatMapMany(savedPenguins -> Flux.fromIterable(savedPenguins));
    }
}
