package sejong.user.global.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final Integer status;
    private final String message;

    public NotFoundException(Integer status, String message) {
        super();
        this.status = status;
        this.message = message;
    }
}