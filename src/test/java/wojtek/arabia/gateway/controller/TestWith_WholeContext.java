package wojtek.arabia.gateway.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class TestWith_WholeContext {

    @Autowired
    private GatewayController gatewayController;

    // Ten test wywala się błędem: org.springframework.web.client.ResourceAccessException: I/O error on POST request for "http://127.0.0.1:8080/users": Połączenie odrzucone
    // i w sumie logiczne -> adnotacja z linijki 12 (@SpringBootTest) stawia cały kontekst Springa.
    // W konsekwencji wywoływane są prawdziwe beany i prawdziwe metody. Więc prawdziwy bean chciał uderzyć pod ten adres http://127.0.0.1:8080/users"
    @Test
    void test_sie_posypie_bo_podczas_polaczenia_https_ktore_wysyla_webService_zwracany_jest_exception() {
        //given
        //when
        ResponseEntity<Void> result = gatewayController.testingTesting();

        //then
        Assertions.assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
    }

}