package wojtek.arabia.gateway.inbound;

import wojtek.arabia.gateway.utils.DeliveryCountry;
import wojtek.arabia.gateway.utils.PackageType;

import java.util.UUID;

public class ClientPackageCreationRequest {

    private UUID userId;

    private PackageType packageType;

    private DeliveryCountry deliveryCountry;

    public DeliveryCountry getDeliveryCountry() {
        return deliveryCountry;
    }

    public void setDeliveryCountry(DeliveryCountry deliveryCountry) {
        this.deliveryCountry = deliveryCountry;
    }

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


}
