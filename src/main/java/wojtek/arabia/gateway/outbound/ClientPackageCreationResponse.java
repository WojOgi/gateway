package wojtek.arabia.gateway.outbound;

import java.util.UUID;

public class ClientPackageCreationResponse {

    private UUID packageId;

    public UUID getPackageId() {
        return packageId;
    }

    public void setPackageId(UUID packageId) {
        this.packageId = packageId;
    }
}
