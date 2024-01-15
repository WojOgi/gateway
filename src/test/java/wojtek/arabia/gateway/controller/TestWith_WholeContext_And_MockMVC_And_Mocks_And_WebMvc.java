package wojtek.arabia.gateway.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

@WebMvcTest
class TestWith_WholeContext_And_MockMVC_And_Mocks_And_WebMvc {

    @MockBean
    private WebService webService;

    @MockBean
    private RequestAndResponseCreator requestAndResponseCreator;

    @Autowired
    private MockMvc mockMvc;

    // Tutaj wprowadziliśmy jedno SUPER ulepszenie w stosunku do testu "TestWith_WholeContext_And_MockMVC_And_Mocks" -> NIE STAWIAMY CAŁEGO KONTEKSTU!
    // Adnotacja @WebMvcTest robi cuda:
    // stawia tylko warstwę controllera - dzięki czemu nie stawia całego kontekstu springa -> jest szybsza
    // ogarnia sobie mockMvc przy okazaji :)
    // daje możliwość mockowania beanów co widać po testach -> są takie same jak w teście "TestWith_WholeContext_And_MockMVC_And_Mocks"


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