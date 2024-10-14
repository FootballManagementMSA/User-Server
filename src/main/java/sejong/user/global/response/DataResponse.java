package sejong.user.global.response;

import lombok.Getter;

@Getter
public class DataResponse<T> extends BaseResponse {
    private T data;

    public DataResponse(Integer status, String message, T data) {
        super(status, message);
        this.data = data;
    }

    public DataResponse(T data) {
        super();
        this.data = data;
    }
    public DataResponse() {
        super();
    }
}
