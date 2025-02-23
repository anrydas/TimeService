package das.tools.timeservice;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class InfoControllerTests {
    @Value("${server.api.prefix}")
    private String apiPrefix;
    private String HEALTH_API_PATH;
    @LocalServerPort
    private int port;
    private String baseUrl;
    @Autowired
    private TestRestTemplate restTemplate;

    @PostConstruct
    private void postConstruct() {
        this.baseUrl = "http://localhost:" + port;
        HEALTH_API_PATH = apiPrefix + "/api/v1/health";
    }
    @Test
    void healthGetShouldReturn200StatusCode() {
        String url = this.baseUrl + HEALTH_API_PATH;
        log.info("testing URL={}", url);
        ResponseEntity<?> response = this.restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    }
    @Test
    void healthPostShouldReturn200StatusCode() {
        String url = this.baseUrl + HEALTH_API_PATH;
        log.info("testing URL={}", url);
        ResponseEntity<?> response = this.restTemplate.exchange(url, HttpMethod.POST, null, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    }
}
