package dashboard_service.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Component
public class DashboardWebSocketHandler implements WebSocketHandler {

    private final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();
    private final ObjectMapper objectMapper;

    public DashboardWebSocketHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.send(sink.asFlux().map(session::textMessage))
                .and(session.receive().map(WebSocketMessage::getPayloadAsText).log());
    }

    public void broadcast(Object message) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            sink.tryEmitNext(jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
