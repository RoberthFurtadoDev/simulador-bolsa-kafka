package dashboard_service.dto;

public record WebSocketMessage(
        String type,
        Object payload
) {}
