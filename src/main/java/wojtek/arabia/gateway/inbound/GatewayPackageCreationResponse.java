package wojtek.arabia.gateway.inbound;

import java.util.UUID;

public class GatewayPackageCreationResponse {

    private UUID packageId;

    public UUID getPackageId() {
        return packageId;
    }

    public void setPackageId(UUID packageId) {
        this.packageId = packageId;
    }
}
