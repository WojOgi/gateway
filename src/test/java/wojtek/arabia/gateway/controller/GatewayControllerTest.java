package wojtek.arabia.gateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import wojtek.arabia.gateway.inbound.ClientRegistrationRequest;
import wojtek.arabia.gateway.inbound.ClientVerificationRequest;
import wojtek.arabia.gateway.outbound.ClientVerificationResponse;
import wojtek.arabia.gateway.outbound.GatewayUserRegistrationRequest;
import wojtek.arabia.gateway.outbound.GatewayUserVerificationRequest;
import wojtek.arabia.gateway.outbound.GatewayUserVerificationResponse;
import wojtek.arabia.gateway.utils.RequestAndResponseCreator;
import wojtek.arabia.gateway.utils.WebRequestService;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static wojtek.arabia.gateway.utils.RequestValidator.clientRegistrationRequestIsValid;

//@SpringBootTest
@AutoConfigureMockMvc
@WebMvcTest(GatewayController.class)
class GatewayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //here I want it to use the actual one - he is not finding it for the test purpose
    @MockBean
    private RequestAndResponseCreator requestAndResponseCreator;

    @MockBean
    private WebRequestService webRequestServiceMock;

    @InjectMocks
    private GatewayController gatewayController;


    private static final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @DisplayName("When the request contains a valid phone number and country gatewayUserRegistrationRequest is created")
    void gatewayUserRegistrationRequestIsCreated() throws Exception {
        //given
        ClientRegistrationRequest request = new ClientRegistrationRequest();
        request.setPhoneNumber("544700589");
        request.setCountry("Germany");

        //then
        Assertions.assertTrue(clientRegistrationRequestIsValid(request));
    }

    @Test
    @DisplayName("When the request contains a valid phone number " +
            "but country is invalid gatewayUserRegistrationRequest is NOT created")
    void gatewayUserRegistrationRequestIsNotCreated() throws Exception {
        //given
        ClientRegistrationRequest request = new ClientRegistrationRequest();
        request.setPhoneNumber("544700589");
        request.setCountry("Denmark");

        //then
        Assertions.assertFalse(clientRegistrationRequestIsValid(request));
    }

    @Test
    @DisplayName("Accept a correct user registration request and return status OK")
    void correctGatewayUserRegistrationRequestPassedToUCAndOKReturned() throws Exception {
        //given
        ClientRegistrationRequest request = new ClientRegistrationRequest();
        request.setPhoneNumber("544700589");
        request.setCountry("Germany");

        //when
        GatewayUserRegistrationRequest gatewayUserRegistrationRequest =
                createGatewayUserRegistrationRequest(request);
        when(webRequestServiceMock.passGatewayRequestToUserCatalogue(gatewayUserRegistrationRequest, "http://127.0.0.1:8080/users"))
                .thenReturn(ResponseEntity.ok().build());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/users/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        //then
        Assertions.assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Accept a correct user registration request and return status OK")
    void incorrectGatewayUserRegistrationRequestPassedToUCAndOKReturned() throws Exception {
        //given
        ClientRegistrationRequest request = new ClientRegistrationRequest();
        request.setPhoneNumber("544700589");
        request.setCountry("Denmark");

        //when
        GatewayUserRegistrationRequest gatewayUserRegistrationRequest =
                createGatewayUserRegistrationRequest(request);

        when(webRequestServiceMock
                .passGatewayRequestToUserCatalogue(gatewayUserRegistrationRequest, "http://127.0.0.1:8080/users"))
                .thenReturn(ResponseEntity.ok().build());

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/v1/users/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        //then
        Assertions.assertNotEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Correct user verification upon receipt of a correct request with correct SmsCode")
    void correctUserVerification() throws Exception {
        //given
        ClientVerificationRequest request = new ClientVerificationRequest();
        request.setPhoneNumber("544700589");
        request.setCountry("Germany");
        request.setSmsCode("123456");
        UUID userId = UUID.randomUUID();

        GatewayUserVerificationRequest gatewayUserVerificationRequest =
                createGatewayUserVerificationRequest(request);

        GatewayUserVerificationResponse gatewayUserVerificationResponse = new GatewayUserVerificationResponse();
        gatewayUserVerificationResponse.setUserId(userId);

        ResponseEntity<GatewayUserVerificationResponse> gatewayUserVerificationResponseResponseEntity = ResponseEntity.ofNullable(gatewayUserVerificationResponse);

        ClientVerificationResponse clientVerificationResponse = createClientVerificationResponse(gatewayUserVerificationResponseResponseEntity);

        when(webRequestServiceMock
                .passGatewayUserVerificationRequestAndCaptureResponse(gatewayUserVerificationRequest, "http://127.0.0.1:8080/users/verification"))
                .thenReturn(gatewayUserVerificationResponseResponseEntity);

        //when
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/v1/users/verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        //then
        Assertions.assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());


    }

    private ClientVerificationResponse createClientVerificationResponse(ResponseEntity<GatewayUserVerificationResponse> gatewayUserVerificationResponseResponseEntity) {
            RequestAndResponseCreator requestAndResponseCreator = new RequestAndResponseCreator();
            return requestAndResponseCreator.createClientVerificationResponse(gatewayUserVerificationResponseResponseEntity);
    }

    private GatewayUserRegistrationRequest createGatewayUserRegistrationRequest(ClientRegistrationRequest request) {
        RequestAndResponseCreator requestAndResponseCreator = new RequestAndResponseCreator();
        return requestAndResponseCreator.createGatewayUserRegistrationRequest(request);
    }

    private GatewayUserVerificationRequest createGatewayUserVerificationRequest(ClientVerificationRequest request) {
            RequestAndResponseCreator requestAndResponseCreator1 = new RequestAndResponseCreator();
            return requestAndResponseCreator1.createGatewayUserVerificationRequest(request);
    }

}