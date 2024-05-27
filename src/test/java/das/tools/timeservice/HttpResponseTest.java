package das.tools.timeservice;

import das.tools.timeservice.entity.AppResponse;
import das.tools.timeservice.entity.ResponseStatus;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class HttpResponseTest {
    protected static final String FORMATTED_TIME_API_PATH = "/api/v1/fmt";
    protected static final String EPOCH_TIME_API_PATH = "/api/v1/epoch";
    @LocalServerPort
    private int port;
    private String baseUrl;
    @Autowired
    private TestRestTemplate restTemplate;

    @PostConstruct
    private void postConstruct() {
        this.baseUrl = "http://localhost:" + port;
    }

    @Test
    void epochGetShouldReturnJsonWithOkStatusAndDigitalEpochTimeLessThanNow() {
        String url = this.baseUrl + EPOCH_TIME_API_PATH;
        log.info("testing URL={}", url);
        AppResponse response = this.restTemplate.getForObject(url, AppResponse.class);
        assertThat(response.getStatus()).isSameAs(ResponseStatus.OK);
        long time = response.getEpochTime();
        assertThat(time).isLessThan(Instant.now().toEpochMilli());
        Pattern pattern = Pattern.compile("^\\d{13}$");
        assertThat(pattern.matcher(String.valueOf(time)).find()).isTrue();
    }

    @Test
    void epochPostShouldReturnJsonWithOkStatusAndDigitalEpochTimeLessThanNow() {
        String url = this.baseUrl + EPOCH_TIME_API_PATH;
        log.info("testing URL={}", url);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<AppResponse> result = this.restTemplate.postForEntity(url, request, AppResponse.class);
        AppResponse response = result.getBody();
        assertThat(response).isNotNull();
        long time = response.getEpochTime();
        assertThat(time).isLessThan(Instant.now().toEpochMilli());
        Pattern pattern = Pattern.compile("^\\d{13}$");
        assertThat(pattern.matcher(String.valueOf(time)).find()).isTrue();
    }

    @Test
    void fmtGetShouldReturnJsonWithOkStatusAndDigitalEpochTimeLessThanNow() {
        String url = this.baseUrl + FORMATTED_TIME_API_PATH;
        log.info("testing URL={}", url);
        AppResponse response = this.restTemplate.getForObject(url, AppResponse.class);
        assertThat(response.getStatus()).isSameAs(ResponseStatus.OK);
        long time = response.getEpochTime();
        assertThat(time).isLessThan(Instant.now().toEpochMilli());
        Pattern pattern = Pattern.compile("^\\d{13}$");
        assertThat(pattern.matcher(String.valueOf(time)).find()).isTrue();
        String fmtTime = response.getFormattedTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            sdf.parse(fmtTime);
            assertThat(true).isTrue();
        } catch (ParseException e) {
            assertThat(true).isFalse();
        }
    }

    @Test
    void fmtPostShouldReturnJsonWithOkStatusAndDigitalEpochTimeLessThanNow() {
        String url = this.baseUrl + FORMATTED_TIME_API_PATH;
        log.info("testing URL={}", url);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<AppResponse> result = this.restTemplate.postForEntity(url, request, AppResponse.class);
        AppResponse response = result.getBody();
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isSameAs(ResponseStatus.OK);
        long time = response.getEpochTime();
        assertThat(time).isLessThan(Instant.now().toEpochMilli());
        Pattern pattern = Pattern.compile("^\\d{13}$");
        assertThat(pattern.matcher(String.valueOf(time)).find()).isTrue();
        String fmtTime = response.getFormattedTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            sdf.parse(fmtTime);
            assertThat(true).isTrue();
        } catch (ParseException e) {
            assertThat(true).isFalse();
        }
    }
}
