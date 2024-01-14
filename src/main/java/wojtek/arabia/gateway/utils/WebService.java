package wojtek.arabia.gateway.utils;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import wojtek.arabia.gateway.inbound.GatewayPackageCreationResponse;
import wojtek.arabia.gateway.inbound.GatewayPackageQueryResponse;
import wojtek.arabia.gateway.inbound.GatewayUserRegistrationResponse;
import wojtek.arabia.gateway.outbound.GatewayPackageCreationRequest;
import wojtek.arabia.gateway.outbound.GatewayUserRegistrationRequest;
import wojtek.arabia.gateway.outbound.GatewayUserVerificationRequest;
import wojtek.arabia.gateway.outbound.GatewayUserVerificationResponse;

import java.util.Objects;
import java.util.UUID;

@Component
public class WebService {

    private static final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<GatewayUserRegistrationResponse> getCall(GatewayUserRegistrationRequest gatewayUserRegistrationRequest) {
        return restTemplate.exchange("http://127.0.0.1:8080/users", HttpMethod.POST,
                new HttpEntity<>(gatewayUserRegistrationRequest, defaultHeadersWith()),
                GatewayUserRegistrationResponse.class);
    }

    public ResponseEntity<GatewayUserRegistrationResponse> passGatewayRequestToUserCatalogue(GatewayUserRegistrationRequest gatewayUserRegistrationRequest, String url) {
        return restTemplate.exchange(url, HttpMethod.POST,
                new HttpEntity<>(gatewayUserRegistrationRequest, defaultHeadersWith()),
                GatewayUserRegistrationResponse.class);
    }

    public ResponseEntity<GatewayUserVerificationResponse>
    passGatewayUserVerificationRequestAndCaptureResponse(GatewayUserVerificationRequest gatewayUserVerificationRequest, String url) {
        return restTemplate
                .exchange(url, HttpMethod.POST,
                        new HttpEntity<>(gatewayUserVerificationRequest, defaultHeadersWith()),
                        GatewayUserVerificationResponse.class);
    }

    public ResponseEntity<GatewayPackageCreationResponse> passGatewayPackageCreationRequestAndCaptureResponse(GatewayPackageCreationRequest gatewayPackageCreationRequest, String url) {
        return restTemplate
                .exchange(url, HttpMethod.POST,
                        new HttpEntity<>(gatewayPackageCreationRequest, defaultHeadersWith()),
                        GatewayPackageCreationResponse.class);
    }

    public ResponseEntity<GatewayPackageQueryResponse> passGatewayPackageQueryRequestAndCaptureResponse(UUID id, String url) {
        return restTemplate
                .getForEntity(url + id, GatewayPackageQueryResponse.class);
    }

    private static HttpHeaders defaultHeadersWith() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    public static boolean isPaidAndReadyToBeCollected(ResponseEntity<GatewayPackageQueryResponse> response) {
        return Objects.requireNonNull(response.getBody()).isPaid() && response.getBody().getPackageInfo().equals(PackageInfo.READY_TO_BE_COLLECTED);
    }

}
