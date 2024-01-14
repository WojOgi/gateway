package wojtek.arabia.gateway.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import wojtek.arabia.gateway.outbound.GatewayUserRegistrationRequest;
import wojtek.arabia.gateway.utils.RequestAndResponseCreator;
import wojtek.arabia.gateway.utils.WebService;

@SpringBootTest
@AutoConfigureMockMvc
class TestWith_WholeContext_And_MockMVC_And_Mocks {

    @MockBean
    private WebService webService;

    @MockBean
    private RequestAndResponseCreator requestAndResponseCreator;

    @Autowired
    private MockMvc mockMvc;

    // Tutaj wprowadziliśmy jedno SUPER ulepszenie w stosunku do testu "TestWith_WholeContext_And_MockMVC" -> dodaliśmy mockowanie!
    // Cały czas każemy Springowi postawić cały kontekst (dodając @SpringBootTest)
    // Cały czas każemy użyć featerów mockMvc (dodając @AutoConfigureMockMvc)
    // Każemy stworzyć mocka beana, który zostanie wrzucony do kontekstu (dodając @MockBean)


    @Test
    void tutaj_test_spodziewa_sie_OK_bo_webService_odpowie_nam_200() throws Exception {
        //given
        Mockito.when(requestAndResponseCreator.getRequest()).thenReturn(new GatewayUserRegistrationRequest());
        Mockito.when(webService.getCall(Mockito.any(GatewayUserRegistrationRequest.class))).thenReturn(ResponseEntity.ok().build());

        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        //then
        Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    void tutaj_test_spodziewa_sie_500_bo_webService_odpowie_nam_nie200_czyli_badRequest() throws Exception {
        //given
        Mockito.when(requestAndResponseCreator.getRequest()).thenReturn(new GatewayUserRegistrationRequest());
        Mockito.when(webService.getCall(Mockito.any(GatewayUserRegistrationRequest.class))).thenReturn(ResponseEntity.badRequest().build());

        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        //then
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getResponse().getStatus());
    }

}