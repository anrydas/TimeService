package das.tools.timeservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("${server.api.prefix}/api/v1")
public class InfoController {
    @RequestMapping("/health")
    public ResponseEntity<?> getHealthResponse() {
        return ResponseEntity.ok("OK");
    }
}
