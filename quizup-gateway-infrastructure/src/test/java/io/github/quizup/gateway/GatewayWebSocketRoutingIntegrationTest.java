package io.github.quizup.gateway;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        classes = GatewayServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Timeout(30)
class GatewayWebSocketRoutingIntegrationTest {

    @Value("${local.server.port}")
    int gatewayPort;

    @Test
    void ws_route_game_is_resolved() throws Exception {
        HttpResponse<Void> resp = sendGet("/game-service/ws");
        assertThat(resp.statusCode())
                .as("la route WS game est résolue (pas de 404)")
                .isNotEqualTo(404);
    }

    @Test
    void ws_route_social_is_resolved() throws Exception {
        HttpResponse<Void> resp = sendGet("/social-service/ws");
        assertThat(resp.statusCode())
                .as("la route WS social est résolue (pas de 404)")
                .isNotEqualTo(404);
    }

    @Test
    void ws_route_matchmaking_is_resolved() throws Exception {
        HttpResponse<Void> resp = sendGet("/matchmaking-service/ws");
        assertThat(resp.statusCode())
                .as("la route WS matchmaking est résolue (pas de 404)")
                .isNotEqualTo(404);
    }

    @Test
    void unknown_ws_path_returns_404() throws Exception {
        HttpResponse<Void> resp = sendGet("/inexistant-service/ws");
        assertThat(resp.statusCode())
                .as("un path inconnu retourne 404")
                .isEqualTo(404);
    }

    private HttpResponse<Void> sendGet(String path) throws Exception {
        return HttpClient.newHttpClient()
                .send(
                        HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:" + gatewayPort + path))
                                .GET()
                                .build(),
                        HttpResponse.BodyHandlers.discarding());
    }
}