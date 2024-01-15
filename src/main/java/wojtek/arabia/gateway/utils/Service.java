package wojtek.arabia.gateway.utils;


import org.springframework.http.ResponseEntity;
import wojtek.arabia.gateway.inbound.*;
import wojtek.arabia.gateway.outbound.*;

import java.util.Objects;
import java.util.UUID;

@org.springframework.stereotype.Service
public class Service {

    private final WebService webService;

    private final RequestAndResponseCreator requestAndResponseCreator;


    public Service(WebService webService, RequestAndResponseCreator requestAndResponseCreator) {
        this.webService = webService;
        this.requestAndResponseCreator = requestAndResponseCreator;
    }

    public GatewayUserRegistrationRequest createGatewayUserRegistrationRequest(ClientRegistrationRequest request) {
        return requestAndResponseCreator.createGatewayUserRegistrationRequest(request);
    }

    public GatewayUserVerificationRequest createGatewayUserVerificationRequest(ClientVerificationRequest request) {
        return requestAndResponseCreator.createGatewayUserVerificationRequest(request);
    }

    public ClientVerificationResponse createClientVerificationResponse(ResponseEntity<GatewayUserVerificationResponse> response) {
        return requestAndResponseCreator.createClientVerificationResponse(response);
    }

    public GatewayPackageCreationRequest createGatewayPackageCreationRequest(ClientPackageCreationRequest request) {
        return requestAndResponseCreator.createGatewayPackageCreationRequest(request);
    }

    public ClientPackageCreationResponse createClientPackageCreationResponse(ResponseEntity<GatewayPackageCreationResponse> response) {
        return requestAndResponseCreator.createClientPackageCreationResponse(response);
    }

    public ClientPackageQueryResponse createClientPackageQueryResponse(ResponseEntity<GatewayPackageQueryResponse> response) {
        return requestAndResponseCreator.createClientPackageQueryResponse(response);
    }

    public ResponseEntity<GatewayUserRegistrationResponse> passGatewayRequestToUserCatalogue(GatewayUserRegistrationRequest gatewayUserRegistrationRequest, String url) {
        return webService.passGatewayRequestToUserCatalogue(gatewayUserRegistrationRequest, url);
    }

    public ResponseEntity<GatewayUserVerificationResponse>
    passGatewayUserVerificationRequestAndCaptureResponse(GatewayUserVerificationRequest gatewayUserVerificationRequest, String url) {
        return webService.passGatewayUserVerificationRequestAndCaptureResponse(gatewayUserVerificationRequest, url);
    }

    public ResponseEntity<GatewayPackageCreationResponse> passGatewayPackageCreationRequestAndCaptureResponse(GatewayPackageCreationRequest gatewayPackageCreationRequest, String url) {
        return webService.passGatewayPackageCreationRequestAndCaptureResponse(gatewayPackageCreationRequest, url);
    }

    public ResponseEntity<GatewayPackageQueryResponse> passGatewayPackageQueryRequestAndCaptureResponse(UUID id, String url) {
        return webService.passGatewayPackageQueryRequestAndCaptureResponse(id, url);
    }

    public static boolean isPaidAndReadyToBeCollected(ResponseEntity<GatewayPackageQueryResponse> response) {
        return Objects.requireNonNull(response.getBody()).isPaid() && response.getBody().getPackageInfo().equals(PackageInfo.READY_TO_BE_COLLECTED);
    }

}
