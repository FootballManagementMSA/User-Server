package sejong.user.global.res;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class BaseResponse {
    private Integer status;
    private String message;
}
