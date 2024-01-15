package wojtek.arabia.gateway.inbound;

import wojtek.arabia.gateway.utils.DeliveryCountry;
import wojtek.arabia.gateway.utils.PackageInfo;
import wojtek.arabia.gateway.utils.PackageType;

import java.util.UUID;

public class GatewayPackageQueryResponse {

    private UUID packageId;
    private UUID userId;

    private PackageType packageType;

    private DeliveryCountry deliveryCountry;

    public DeliveryCountry getDeliveryCountry() {
        return deliveryCountry;
    }

    public void setDeliveryCountry(DeliveryCountry deliveryCountry) {
        this.deliveryCountry = deliveryCountry;
    }

    private boolean paid;

    private PackageInfo packageInfo;

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }

    private String openCode;

    public UUID getPackageId() {
        return packageId;
    }

    public void setPackageId(UUID packageId) {
        this.packageId = packageId;
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



    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getOpenCode() {
        return openCode;
    }

    public void setOpenCode(String openCode) {
        this.openCode = openCode;
    }
}
