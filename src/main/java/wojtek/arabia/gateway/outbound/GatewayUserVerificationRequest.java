package wojtek.arabia.gateway.outbound;

import java.util.Objects;

public class GatewayUserVerificationRequest {

    private String phoneNumber;

    private String otp;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GatewayUserVerificationRequest that = (GatewayUserVerificationRequest) o;
        return Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(otp, that.otp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneNumber, otp);
    }
}
