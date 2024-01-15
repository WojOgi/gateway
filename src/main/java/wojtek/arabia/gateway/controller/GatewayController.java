package wojtek.arabia.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wojtek.arabia.gateway.inbound.*;
import wojtek.arabia.gateway.outbound.*;
import wojtek.arabia.gateway.utils.RequestValidator;
import wojtek.arabia.gateway.utils.Service;

import java.util.Objects;
import java.util.UUID;

import static wojtek.arabia.gateway.utils.Service.isPaidAndReadyToBeCollected;

@RestController
public class GatewayController {
    private final Service service;

    private final RequestValidator requestValidator;


    public GatewayController(Service service, RequestValidator requestValidator) {
        this.service = service;
        this.requestValidator = requestValidator;
    }

    @PostMapping(value = "/v1/users/registration")
    public ResponseEntity<Void> registerUser(@RequestBody ClientRegistrationRequest request) {

        if (requestValidator.clientRegistrationRequestIsValid(request)) {
            GatewayUserRegistrationRequest gatewayUserRegistrationRequest =
                    service.createGatewayUserRegistrationRequest(request);

            service.passGatewayRequestToUserCatalogue(gatewayUserRegistrationRequest, "http://127.0.0.1:8080/users");

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping(value = "/v1/users/verification")
    public ResponseEntity<ClientVerificationResponse> verifyUser(@RequestBody ClientVerificationRequest request) {

        if (requestValidator.clientVerificationRequestIsValid(request)) {

            GatewayUserVerificationRequest gatewayUserVerificationRequest =
                    service.createGatewayUserVerificationRequest(request);

            ResponseEntity<GatewayUserVerificationResponse> response =

                    service.passGatewayUserVerificationRequestAndCaptureResponse(gatewayUserVerificationRequest, "http://127.0.0.1:8080/users/verification");

            ClientVerificationResponse clientVerificationResponse =
                    service.createClientVerificationResponse(response);

            return ResponseEntity.ok(clientVerificationResponse);
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping(value = "/v1/packages/create")
    public ResponseEntity<ClientPackageCreationResponse> createPackage(@RequestBody ClientPackageCreationRequest request) {

        if (requestValidator.clientPackageCreationRequestIsValid(request)) {

            GatewayPackageCreationRequest gatewayPackageCreationRequest =
                    service.createGatewayPackageCreationRequest(request);

            ResponseEntity<GatewayPackageCreationResponse> response =
                    service.passGatewayPackageCreationRequestAndCaptureResponse(gatewayPackageCreationRequest, "http://127.0.0.1:8082/packages");

            ClientPackageCreationResponse clientPackageCreationResponse =
                    service.createClientPackageCreationResponse(response);

            return ResponseEntity.ok(clientPackageCreationResponse);
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/v1/packages/{id}")
    public ResponseEntity<ClientPackageQueryResponse> getPackageInfoById(@PathVariable UUID id) {

        ResponseEntity<GatewayPackageQueryResponse> response =
                service.passGatewayPackageQueryRequestAndCaptureResponse(id, "http://127.0.0.1:8082/packages/");

        ClientPackageQueryResponse clientPackageQueryResponse =
                service.createClientPackageQueryResponse(response);

        if (isPaidAndReadyToBeCollected(response)) {
            clientPackageQueryResponse.setOpenCode(Objects.requireNonNull(response.getBody()).getOpenCode());
        }

        return ResponseEntity.ok(clientPackageQueryResponse);
    }


}
