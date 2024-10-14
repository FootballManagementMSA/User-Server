package sejong.user.global.response;

import lombok.*;
import sejong.user.global.response.constant.ResponseMessageConstant;

@Data
@AllArgsConstructor
@Builder
public class BaseResponse {
    private Integer status;
    private String message;
    public BaseResponse(){
        this.message  = ResponseMessageConstant.SUCCESS;
        this.status = 200;
    }
}
