package fomichev.prak_4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/penguins")
public class ChannelController {
    private final RSocketRequester rSocketRequester;

    @Autowired
    public ChannelController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    @PostMapping("/exp")
    public Flux<Penguin> addPenguinsMultiple(@RequestBody PenguinListWrapper penguinListWrapper){
        List<Penguin> penguinList = penguinListWrapper.getPenguins();
        Flux<Penguin> penguins = Flux.fromIterable(penguinList);
        return rSocketRequester
                .route("penguinChannel")
                .data(penguins)
                .retrieveFlux(Penguin.class);
    }
}
