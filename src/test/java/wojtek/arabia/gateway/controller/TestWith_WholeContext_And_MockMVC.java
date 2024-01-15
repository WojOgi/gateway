package wojtek.arabia.gateway.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class TestWith_WholeContext_And_MockMVC {

    @Autowired
    private MockMvc mockMvc;

    // Tutaj wprowadziliśmy jedno uleprzenie w stosunku do testu "TestWith_WholeContext" -> dodaliśmy mockMvc. Dzięki temu
    // nie musimy bezpośrednio wywoływać metody "gatewayController.testingTesting()" tylko używamy strzału REST, który jest obsługiwany
    // przez odpowiednią metodę controllera. Dlatego usunąłem pole 'gatewayController' :)

    // Natomiast wynik taki sam -> dostajemy ten sam błąd --> org.springframework.web.client.ResourceAccessException: I/O error on POST request for "http://127.0.0.1:8080/users": Połączenie odrzucone
    // ponieważ nadal Spring stawia cały kontest i używa prawdziwych Beanów i prawdziwych metod tychże beanów
    @Test
    void test_sie_rowniez_posypie_bo_podczas_polaczenia_https_ktore_wysyla_webService_zwracany_jest_exception() throws Exception {
        //given
        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/test").contentType(MediaType.APPLICATION_JSON)).andReturn();

        //then
        Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

}