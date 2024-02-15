package sejong.user.global.res;

import lombok.*;
import sejong.user.global.res.constant.ResponseMessageConstant;

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
