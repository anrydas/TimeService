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
    public static String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public AppResponse getEpochTime() {
        return AppResponse.builder()
                .status(ResponseStatus.OK)
                .epochTime(Instant.now().toEpochMilli())
                .build();
    }

    public AppResponse getFormattedTime(String mask) {
        SimpleDateFormat sdf = new SimpleDateFormat((mask != null && !"".equals(mask)) ? mask : DEFAULT_DATETIME_FORMAT);
        Instant now = Instant.now();
        return AppResponse.builder()
                .status(ResponseStatus.OK)
                .epochTime(now.toEpochMilli())
                .formattedTime(sdf.format(Date.from(now)))
                .build();
    }

}
