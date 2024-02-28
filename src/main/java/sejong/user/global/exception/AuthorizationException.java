package sejong.user.global.exception;

import lombok.Getter;

@Getter
public class AuthorizationException extends RuntimeException{
    private final Integer status;
    private final String message;

    public AuthorizationException(Integer status, String message) {
        super();
        this.status = status;
        this.message = message;
    }
}
