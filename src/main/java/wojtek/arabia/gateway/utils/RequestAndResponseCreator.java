package wojtek.arabia.gateway.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import wojtek.arabia.gateway.inbound.*;
import wojtek.arabia.gateway.outbound.*;

import java.util.Objects;
import java.util.UUID;

import static wojtek.arabia.gateway.utils.RequestValidator.countryPrefixMap;

@Component
public class RequestAndResponseCreator {

    public GatewayUserRegistrationRequest createGatewayUserRegistrationRequest(ClientRegistrationRequest request) {
        GatewayUserRegistrationRequest gatewayUserRegistrationRequest = new GatewayUserRegistrationRequest();

        String prefix = countryPrefixMap.get(request.getCountry());
        gatewayUserRegistrationRequest.setPhoneNumber(prefix + request.getPhoneNumber());
        return gatewayUserRegistrationRequest;
    }

    public GatewayUserVerificationRequest createGatewayUserVerificationRequest(ClientVerificationRequest request) {
        GatewayUserVerificationRequest gatewayUserVerificationRequest = new GatewayUserVerificationRequest();

        String prefix = countryPrefixMap.get(request.getCountry());
        gatewayUserVerificationRequest.setPhoneNumber(prefix + request.getPhoneNumber());
        gatewayUserVerificationRequest.setOtp(request.getSmsCode());
        return gatewayUserVerificationRequest;
    }

    public ClientVerificationResponse createClientVerificationResponse(ResponseEntity<GatewayUserVerificationResponse> response) {
        ClientVerificationResponse clientVerificationResponse = new ClientVerificationResponse();
        UUID userId = extractUserIdFromResponse(response);
        clientVerificationResponse.setUserId(userId);
        return clientVerificationResponse;
    }

    public UUID extractUserIdFromResponse(ResponseEntity<GatewayUserVerificationResponse> response) {
        return Objects.requireNonNull(response.getBody()).getUserId();
    }

    public GatewayPackageCreationRequest createGatewayPackageCreationRequest(ClientPackageCreationRequest request) {
        GatewayPackageCreationRequest gatewayPackageCreationRequest = new GatewayPackageCreationRequest();

        gatewayPackageCreationRequest.setUserId(request.getUserId());
        gatewayPackageCreationRequest.setPackageType(request.getPackageType());
        gatewayPackageCreationRequest.setInternational(request.isInternational());
        return gatewayPackageCreationRequest;
    }

    public ClientPackageCreationResponse createClientPackageCreationResponse(ResponseEntity<GatewayPackageCreationResponse> response) {
        ClientPackageCreationResponse clientPackageCreationResponse = new ClientPackageCreationResponse();
        UUID packageId = Objects.requireNonNull(response.getBody()).getPackageId();

        clientPackageCreationResponse.setPackageId(packageId);
        return clientPackageCreationResponse;
    }

    public ClientPackageQueryResponse createClientPackageQueryResponse(ResponseEntity<GatewayPackageQueryResponse> response) {
        ClientPackageQueryResponse clientPackageQueryResponse = new ClientPackageQueryResponse();

        clientPackageQueryResponse.setPackageType(Objects.requireNonNull(response.getBody()).getPackageType());
        clientPackageQueryResponse.setPackageInfo(response.getBody().getPackageInfo());
        clientPackageQueryResponse.setInternational(response.getBody().isInternational());
        clientPackageQueryResponse.setPaid(response.getBody().isPaid());
        return clientPackageQueryResponse;
    }



}
