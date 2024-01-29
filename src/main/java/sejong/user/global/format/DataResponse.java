package sejong.user.global.format;

import lombok.Getter;
import sejong.user.global.format.BaseResponse;

@Getter
public class DataResponse<T> extends BaseResponse {
    private T data;
    public DataResponse() {
        super();
    }

    public DataResponse(T data) {
        super();
        this.data = data;
    }
    public DataResponse(String reason, String code, Integer status, T data) {
        super(status,code,reason);
        this.data = data;
    }
}
