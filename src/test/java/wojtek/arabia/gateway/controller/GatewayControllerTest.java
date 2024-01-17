package wojtek.arabia.gateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import wojtek.arabia.gateway.utils.WebService;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static wojtek.arabia.gateway.utils.RequestValidator.clientRegistrationRequestIsValid;

//@SpringBootTest
@AutoConfigureMockMvc
@SpringBootTest
class GatewayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RequestAndResponseCreator requestAndResponseCreator;
    @MockBean
    private WebService webServiceMock;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("When the request contains a valid phone number and country gatewayUserRegistrationRequest is created")
    void gatewayUserRegistrationRequestIsCreated() throws Exception {
        //given
        ClientRegistrationRequest request = createValidRegistrationRequest();

        //then
        Assertions.assertTrue(clientRegistrationRequestIsValid(request));
    }

    @Test
    @DisplayName("When the request contains a valid phone number " +
            "but country is invalid gatewayUserRegistrationRequest is NOT created")
    void gatewayUserRegistrationRequestIsNotCreated() throws Exception {
        //given
        ClientRegistrationRequest request = createInvalidRegistrationRequest();

        //then
        Assertions.assertFalse(clientRegistrationRequestIsValid(request));
    }

    @Test
    @DisplayName("Valid user registration request returns OK")
    void validClientRegistrationRequestPassedToUcWthOkStatus() throws Exception {
        //given
        ClientRegistrationRequest validRequest = createValidRegistrationRequest();

        //when
        GatewayUserRegistrationRequest gatewayUserRegistrationRequest =
                requestAndResponseCreator.createGatewayUserRegistrationRequest(validRequest);
        when(webServiceMock.passGatewayRequestToUserCatalogue(gatewayUserRegistrationRequest, "http://127.0.0.1:8080/users"))
                .thenReturn(ResponseEntity.ok().build());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/users/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andReturn();

        //then
        Assertions.assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Invalid user registration request returns Bad Request")
    void invalidClientRegistrationRequestNotPassedToUcWithBadRequestStatus() throws Exception {
        //given
        ClientRegistrationRequest invalidRequest = createInvalidRegistrationRequest();

        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/users/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andReturn();

        //then
        Assertions.assertEquals(result.getResponse().getStatus(), HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Valid user verification request returns OK with ClientVerificationResponse in body")
    void validClientVerificationResponsePassedToUcWithOKStatusAndCorrectBody() throws Exception {
        //given
        ClientVerificationRequest validRequest = createValidVerificationRequest();
        GatewayUserVerificationResponse response = new GatewayUserVerificationResponse();
        response.setUserId(UUID.randomUUID());
        ResponseEntity<GatewayUserVerificationResponse> responseEntity = ResponseEntity.ok(response);

        //when
        GatewayUserVerificationRequest gatewayUserVerificationRequest =
                requestAndResponseCreator.createGatewayUserVerificationRequest(validRequest);
        when(webServiceMock.passGatewayUserVerificationRequestAndCaptureResponse(gatewayUserVerificationRequest,
                "http://127.0.0.1:8080/users/verification")).thenReturn(responseEntity);
        ClientVerificationResponse clientVerificationResponse =
                requestAndResponseCreator.createClientVerificationResponse(responseEntity);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/users/verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andReturn();

        //then
        Assertions.assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        Assertions.assertEquals(result.getResponse().getContentAsString().substring(11, 47), clientVerificationResponse.getUserId().toString());
    }

    private static ClientVerificationRequest createValidVerificationRequest() {
        ClientVerificationRequest validRequest = new ClientVerificationRequest();
        validRequest.setPhoneNumber("544700589");
        validRequest.setCountry("Germany");
        validRequest.setSmsCode("123456");
        return validRequest;
    }


    private static ClientRegistrationRequest createInvalidRegistrationRequest() {
        ClientRegistrationRequest request = new ClientRegistrationRequest();
        request.setPhoneNumber("544700589");
        request.setCountry("Denmark");
        return request;
    }

    private static ClientRegistrationRequest createValidRegistrationRequest() {
        ClientRegistrationRequest request = new ClientRegistrationRequest();
        request.setPhoneNumber("544700589");
        request.setCountry("Germany");
        return request;
    }



}