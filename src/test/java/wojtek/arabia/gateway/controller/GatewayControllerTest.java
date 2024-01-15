package wojtek.arabia.gateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import wojtek.arabia.gateway.inbound.ClientRegistrationRequest;
import wojtek.arabia.gateway.inbound.GatewayUserRegistrationResponse;
import wojtek.arabia.gateway.outbound.GatewayUserRegistrationRequest;
import wojtek.arabia.gateway.utils.RequestAndResponseCreator;
import wojtek.arabia.gateway.utils.RequestValidator;
import wojtek.arabia.gateway.utils.Service;
import wojtek.arabia.gateway.utils.WebService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@WebMvcTest(GatewayController.class)
class GatewayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RequestValidator requestValidator;

    @Autowired
    private Service service;

    @MockBean
    private WebService webServiceMock;

    @MockBean
    private RequestAndResponseCreator requestAndResponseCreatorMock;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("When the request contains a valid phone number and country gatewayUserRegistrationRequest is created")
    void gatewayUserRegistrationRequestIsCreated() throws Exception {
        //given
        ClientRegistrationRequest request = new ClientRegistrationRequest();
        request.setPhoneNumber("544700589");
        request.setCountry("Germany");

        //then
        Assertions.assertTrue(requestValidator.clientRegistrationRequestIsValid(request));
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
        Assertions.assertFalse(requestValidator.clientRegistrationRequestIsValid(request));
    }

    @Test
    @DisplayName("Accept a correct user registration request and return status OK")
    void correctGatewayUserRegistrationRequestPassedToUCAndOKReturned() throws Exception {
        //given
        ClientRegistrationRequest validRequest = new ClientRegistrationRequest();
        validRequest.setPhoneNumber("544700589");
        validRequest.setCountry("Germany");
        GatewayUserRegistrationResponse gatewayUserRegistrationResponse = new GatewayUserRegistrationResponse();

        //when

        when(webServiceMock.passGatewayRequestToUserCatalogue(any(), anyString())).thenReturn(ResponseEntity.ok().build());


        MvcResult result = mockMvc.perform(post("/v1/users/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest))).andExpect(status().isOk()).andReturn();

        //then



    }

    @Test
    @DisplayName("Correctly verify user upon receiving a valid verification request with smsCode")
    void correctlyVerifyUserWithValidRequestAndSmsCode() {


    }


}