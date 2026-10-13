package das.tools.timeservice;

import das.tools.timeservice.entity.AppResponse;
import das.tools.timeservice.entity.ResponseStatus;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class HttpResponseTest {
    @Value("${server.api.prefix}")
    private String apiPrefix;
    private String FORMATTED_TIME_API_PATH;
    private String EPOCH_TIME_API_PATH;
    @LocalServerPort
    private int port;
    private String baseUrl;
    @Autowired
    private TestRestTemplate restTemplate;

    @PostConstruct
    private void postConstruct() {
        this.baseUrl = "http://localhost:" + port;
        FORMATTED_TIME_API_PATH = apiPrefix + "/api/v1/fmt";
        EPOCH_TIME_API_PATH = apiPrefix + "/api/v1/epoch";
    }

    @Test
    void epochGetShouldReturnJsonWithOkStatusAndEpochTimeLessThanNow() {
        String url = this.baseUrl + EPOCH_TIME_API_PATH;
        log.info("testing URL={}", url);
        AppResponse response = this.restTemplate.getForObject(url, AppResponse.class);
        assertThat(response.getStatus()).isSameAs(ResponseStatus.OK);
        long time = response.getEpochTime();
        long actualTime = Instant.now().toEpochMilli();
        assertThat(time).isLessThan(actualTime);
        Pattern pattern = Pattern.compile("^\\d{13}$");
        assertThat(pattern.matcher(String.valueOf(time)).find()).isTrue();
    }

    @Test
    void epochPostShouldReturnJsonWithOkStatusAndTimeLessThanNow() {
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
    void fmtGetShouldReturnJsonWithOkStatusAndTimeLessThanNow() {
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
    void fmtPostShouldReturnJsonWithOkStatusAndEpochTimeLessThanNow() {
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

    @Test
    void epochGetShouldReturnTimeShiftedByMinusFiveDays() {
        int valueToAdd = -5;
        String url = this.baseUrl + EPOCH_TIME_API_PATH + "?d=" + valueToAdd;
        log.info("testing URL={}", url);
        long before = Instant.now().plus(valueToAdd, ChronoUnit.DAYS).toEpochMilli();
        AppResponse response = this.restTemplate.getForObject(url, AppResponse.class);
        long after = Instant.now().plus(valueToAdd, ChronoUnit.DAYS).toEpochMilli();
        assertThat(response.getStatus()).isSameAs(ResponseStatus.OK);
        assertThat(response.getEpochTime()).isBetween(before, after);
    }

    @Test
    void fmtWithCustomMaskShouldReturnTimeInThatFormat() {
        String url = this.baseUrl + FORMATTED_TIME_API_PATH + "?mask=yyyy";
        AppResponse response = this.restTemplate.getForObject(url, AppResponse.class);
        assertThat(response.getStatus()).isSameAs(ResponseStatus.OK);
        assertThat(response.getFormattedTime()).matches("^\\d{4}$");
    }

    @Test
    void fmtWithInvalidMaskShouldReturnBadRequestWithErrorStatus() {
        String url = this.baseUrl + FORMATTED_TIME_API_PATH + "?mask=Q";
        ResponseEntity<AppResponse> result = this.restTemplate.getForEntity(url, AppResponse.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getStatus()).isSameAs(ResponseStatus.ERROR);
        assertThat(result.getBody().getEpochTime()).isNull();
        assertThat(result.getBody().getMessage()).contains("Invalid mask");
    }

    @Test
    void epochWithNonNumericShiftShouldReturnBadRequestWithErrorStatus() {
        String url = this.baseUrl + EPOCH_TIME_API_PATH + "?h=abc";
        ResponseEntity<AppResponse> result = this.restTemplate.getForEntity(url, AppResponse.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getStatus()).isSameAs(ResponseStatus.ERROR);
        assertThat(result.getBody().getMessage()).contains("'h'");
    }
}
