package das.tools.timeservice.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppResponse {
    private ResponseStatus status;
    private long epochTime;
    private String formattedTime;
    private String message;
}
