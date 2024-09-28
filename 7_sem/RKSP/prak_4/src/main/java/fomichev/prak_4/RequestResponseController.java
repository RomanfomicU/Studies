package fomichev.prak_4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/penguins")
public class RequestResponseController {
    private final RSocketRequester rSocketRequester;

    @Autowired
    public RequestResponseController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    @GetMapping("/{id}")
    public Mono<Penguin> getPenguin(@PathVariable Long id) {
        return rSocketRequester
                .route("getPenguin")
                .data(id)
                .retrieveMono(Penguin.class);
    }

    @PostMapping
    public Mono<Penguin> addPenguin(@RequestBody Penguin penguin) {
        return rSocketRequester
                .route("addPenguin")
                .data(penguin)
                .retrieveMono(Penguin.class);
    }
}
