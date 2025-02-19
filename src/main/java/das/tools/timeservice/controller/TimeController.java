package das.tools.timeservice.controller;

import das.tools.timeservice.service.TimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${server.api.prefix}/api/v1")
public class TimeController {
    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @RequestMapping(value="/epoch", method = { RequestMethod.GET, RequestMethod.POST })
    public ResponseEntity<?> getEpochTime(@RequestParam(value = "h", defaultValue = "0", required = false) int hour,
                                          @RequestParam(value = "m", defaultValue = "0", required = false) int minute,
                                          @RequestParam(value = "d", defaultValue = "0", required = false) int day) {
        return ResponseEntity.ok(timeService.getEpochTime(hour, minute, day));
    }

    @RequestMapping(value = "/fmt", method = { RequestMethod.GET, RequestMethod.POST })
    public ResponseEntity<?> getFormattedTime(@RequestParam(value = "mask", required = false) String mask,
                                              @RequestParam(value = "h", defaultValue = "0", required = false) int hour,
                                              @RequestParam(value = "m", defaultValue = "0", required = false) int minute,
                                              @RequestParam(value = "d", defaultValue = "0", required = false) int day) {
        return ResponseEntity.ok(timeService.getFormattedTime(mask, hour, minute, day));
    }
}
