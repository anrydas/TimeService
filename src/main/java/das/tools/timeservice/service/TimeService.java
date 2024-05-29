package das.tools.timeservice.service;

import das.tools.timeservice.entity.AppResponse;
import das.tools.timeservice.entity.ResponseStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;

@Service
public class TimeService {
    protected static final long SECONDS_PER_DAY = 86400L;
    protected static final long SECONDS_PER_MINUTE = 60L;
    protected static final long SECONDS_PER_HOUR = 3600L;
    public static String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public AppResponse getEpochTime(int hour, int minute, int day) {
        Instant now = Instant.now().plusSeconds(getSecondsToAdd(hour, minute, day));
        return AppResponse.builder()
                .status(ResponseStatus.OK)
                .epochTime(now.toEpochMilli())
                .build();
    }

    public AppResponse getFormattedTime(String mask, int hour, int minute, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat((mask != null && !"".equals(mask)) ? mask : DEFAULT_DATETIME_FORMAT);
        Instant now = Instant.now().plusSeconds(getSecondsToAdd(hour, minute, day));
        return AppResponse.builder()
                .status(ResponseStatus.OK)
                .epochTime(now.toEpochMilli())
                .formattedTime(sdf.format(Date.from(now)))
                .build();
    }

    private long getSecondsToAdd(int hour, int minute, int day){
        return hour * SECONDS_PER_HOUR + minute * SECONDS_PER_MINUTE + day * SECONDS_PER_DAY;
    }

}
