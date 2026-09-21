package io.github.quizup.gateway;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

class GatewayWebSocketRoutingConfigTest {

    @SuppressWarnings("unchecked")
    static Map<String, Object> loadYaml(String resource) throws Exception {
        try (InputStream is = GatewayWebSocketRoutingConfigTest.class.getClassLoader()
                .getResourceAsStream(resource)) {
            Yaml yaml = new Yaml(new SafeConstructor(new LoaderOptions()));
            return (Map<String, Object>) yaml.load(is);
        }
    }

    @SuppressWarnings("unchecked")
    static List<Map<String, Object>> routes(Map<String, Object> root) {
        Map<String, Object> spring = (Map<String, Object>) root.get("spring");
        Map<String, Object> cloud = (Map<String, Object>) spring.get("cloud");
        Map<String, Object> gw = (Map<String, Object>) cloud.get("gateway");
        Map<String, Object> server = (Map<String, Object>) gw.get("server");
        Map<String, Object> webflux = (Map<String, Object>) server.get("webflux");
        return (List<Map<String, Object>>) webflux.get("routes");
    }

    @ParameterizedTest
    @CsvSource({
        "/game-service/ws",
        "/social-service/ws",
        "/matchmaking-service/ws",
        "/profile-service/ws",
    })
    void ws_route_exists(String path) throws Exception {
        List<Map<String, Object>> allRoutes = routes(loadYaml("application.yml"));
        boolean found = allRoutes.stream().anyMatch(r ->
                r.get("uri") != null
                        && r.get("uri").toString().contains("ws://")
                        && r.get("predicates") instanceof List<?>
                        && r.get("predicates").toString().contains("Path=" + path)
                        && r.get("filters") instanceof List<?>
                        && r.get("filters").toString().contains("StripPrefix=1"));
        assertThat(found)
                .as("application.yml contient la route WS %s avec StripPrefix=1", path)
                .isTrue();
    }
}