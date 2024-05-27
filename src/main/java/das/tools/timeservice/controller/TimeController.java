package das.tools.timeservice.controller;

import das.tools.timeservice.entity.AppResponse;
import das.tools.timeservice.entity.ResponseStatus;
import das.tools.timeservice.service.TimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1")
public class TimeController {
    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @RequestMapping(value="/epoch", method = { RequestMethod.GET, RequestMethod.POST })
    public ResponseEntity<?> getEpochTime() {
        return ResponseEntity.ok(timeService.getEpochTime());
    }

    /*@PostMapping("/epoch")
    public ResponseEntity<?> getEpochTime() {
        return ResponseEntity.ok(timeService.getEpochTime());
    }*/

    @RequestMapping(value = "/fmt", method = { RequestMethod.GET, RequestMethod.POST })
    public ResponseEntity<?> getFormattedTime(@RequestParam(value = "mask", required = false) String mask) {
        return ResponseEntity.ok(timeService.getFormattedTime(mask));
    }
}
