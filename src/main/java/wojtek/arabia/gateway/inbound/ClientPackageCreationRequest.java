package wojtek.arabia.gateway.inbound;

import wojtek.arabia.gateway.utils.PackageType;

import java.util.UUID;

public class ClientPackageCreationRequest {

    private UUID userId;

    private PackageType packageType;

    private boolean isInternational;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public boolean isInternational() {
        return isInternational;
    }

    public void setInternational(boolean international) {
        isInternational = international;
    }
}
