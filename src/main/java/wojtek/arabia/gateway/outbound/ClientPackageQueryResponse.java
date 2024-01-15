package wojtek.arabia.gateway.outbound;

import wojtek.arabia.gateway.utils.DeliveryCountry;
import wojtek.arabia.gateway.utils.PackageInfo;
import wojtek.arabia.gateway.utils.PackageType;

public class ClientPackageQueryResponse {

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
