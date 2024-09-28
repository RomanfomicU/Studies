package fomichev.prak_4;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/penguins")
public class FireForgetController {
    private final RSocketRequester rSocketRequester;
    @Autowired
    public FireForgetController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    @DeleteMapping("/{id}")
    public Publisher<Void> deletePenguin(@PathVariable Long id){
        return rSocketRequester
                .route("deletePenguin")
                .data(id)
                .send();
    }
}
