package fomichev.prak_4;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/penguins")
public class RequestStreamController {
    private final RSocketRequester rSocketRequester;
    
    @Autowired
    public RequestStreamController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }
    
    @GetMapping
    public Publisher<Penguin> getPenguins() {
        return rSocketRequester
                .route("getPenguins")
                .data(new Penguin())
                .retrieveFlux(Penguin.class);
    }
}
