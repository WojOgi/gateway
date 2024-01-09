package wojtek.arabia.gateway.outbound;

import java.util.UUID;

public class GatewayUserVerificationResponse {

    private UUID userId;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
