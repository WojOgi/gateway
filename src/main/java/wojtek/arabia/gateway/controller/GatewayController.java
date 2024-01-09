package wojtek.arabia.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wojtek.arabia.gateway.inbound.*;
import wojtek.arabia.gateway.outbound.*;
import wojtek.arabia.gateway.utils.RequestAndResponseCreator;
import wojtek.arabia.gateway.utils.WebRequestService;

import java.util.Objects;
import java.util.UUID;

import static wojtek.arabia.gateway.utils.RequestValidator.clientRegistrationRequestIsValid;
import static wojtek.arabia.gateway.utils.WebRequestService.isPaidAndReadyToBeCollected;

@RestController
public class GatewayController {
    private final WebRequestService webRequestService;
    private final RequestAndResponseCreator requestAndResponseCreator;

    public GatewayController(WebRequestService webRequestService, RequestAndResponseCreator requestAndResponseCreator) {
        this.webRequestService = webRequestService;
        this.requestAndResponseCreator = requestAndResponseCreator;
    }


    @PostMapping(value = "/v1/users/registration")
    public ResponseEntity<Void> registerUser(@RequestBody ClientRegistrationRequest request) {

        if (clientRegistrationRequestIsValid(request)) {
            GatewayUserRegistrationRequest gatewayUserRegistrationRequest =
                    requestAndResponseCreator.createGatewayUserRegistrationRequest(request);

            webRequestService.passGatewayRequestToUserCatalogue(gatewayUserRegistrationRequest, "http://127.0.0.1:8080/users");

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping(value = "/v1/users/verification")
    public ResponseEntity<ClientVerificationResponse> verifyUser(@RequestBody ClientVerificationRequest request) {

        GatewayUserVerificationRequest gatewayUserVerificationRequest =
                requestAndResponseCreator.createGatewayUserVerificationRequest(request);

        ResponseEntity<GatewayUserVerificationResponse> response =

                webRequestService.passGatewayUserVerificationRequestAndCaptureResponse(gatewayUserVerificationRequest, "http://127.0.0.1:8080/users/verification");

        ClientVerificationResponse clientVerificationResponse =
                requestAndResponseCreator.createClientVerificationResponse(response);

        return ResponseEntity.ok(clientVerificationResponse);
    }

    @PostMapping(value = "/v1/packages/create")
    public ResponseEntity<ClientPackageCreationResponse> createPackage(@RequestBody ClientPackageCreationRequest request) {

        GatewayPackageCreationRequest gatewayPackageCreationRequest =
                requestAndResponseCreator.createGatewayPackageCreationRequest(request);


        ResponseEntity<GatewayPackageCreationResponse> response =
                webRequestService.passGatewayPackageCreationRequestAndCaptureResponse(gatewayPackageCreationRequest, "http://127.0.0.1:8082/packages");

        ClientPackageCreationResponse clientPackageCreationResponse =
                requestAndResponseCreator.createClientPackageCreationResponse(response);

        return ResponseEntity.ok(clientPackageCreationResponse);
    }

    @GetMapping(value = "/v1/packages/{id}")
    public ResponseEntity<ClientPackageQueryResponse> getPackageInfoById(@PathVariable UUID id) {

        ResponseEntity<GatewayPackageQueryResponse> response =
                webRequestService.passGatewayPackageQueryRequestAndCaptureResponse(id, "http://127.0.0.1:8082/packages/");

        ClientPackageQueryResponse clientPackageQueryResponse =
                requestAndResponseCreator.createClientPackageQueryResponse(response);

        if (isPaidAndReadyToBeCollected(response)) {
            clientPackageQueryResponse.setOpenCode(Objects.requireNonNull(response.getBody()).getOpenCode());
        }

        return ResponseEntity.ok(clientPackageQueryResponse);
    }


}
