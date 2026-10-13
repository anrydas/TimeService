package das.tools.timeservice.service;

import das.tools.timeservice.entity.AppResponse;
import das.tools.timeservice.entity.ResponseStatus;
import das.tools.timeservice.exception.InvalidRequestException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@Service
public class TimeService {
    protected static final long SECONDS_PER_DAY = 86400L;
    protected static final long SECONDS_PER_MINUTE = 60L;
    protected static final long SECONDS_PER_HOUR = 3600L;
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public AppResponse getEpochTime(int hour, int minute, int day) {
        Instant now = Instant.now().plusSeconds(getSecondsToAdd(hour, minute, day));
        return AppResponse.builder()
                .status(ResponseStatus.OK)
                .epochTime(now.toEpochMilli())
                .build();
    }

    public AppResponse getFormattedTime(String mask, int hour, int minute, int day) throws InvalidRequestException {
        SimpleDateFormat sdf = createDateFormat((mask != null && !"".equals(mask)) ? mask : DEFAULT_DATETIME_FORMAT);
        Instant now = Instant.now().plusSeconds(getSecondsToAdd(hour, minute, day));
        return AppResponse.builder()
                .status(ResponseStatus.OK)
                .epochTime(now.toEpochMilli())
                .formattedTime(sdf.format(Date.from(now)))
                .build();
    }

    private SimpleDateFormat createDateFormat(String mask) throws InvalidRequestException {
        try {
            return new SimpleDateFormat(mask);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Invalid mask '" + mask + "': " + e.getMessage());
        }
    }

    private long getSecondsToAdd(int hour, int minute, int day){
        return hour * SECONDS_PER_HOUR + minute * SECONDS_PER_MINUTE + day * SECONDS_PER_DAY;
    }

}
