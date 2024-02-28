package sejong.user.global;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sejong.user.global.exception.AuthorizationException;
import sejong.user.global.exception.BadRequestException;
import sejong.user.global.exception.NotFoundException;
import sejong.user.global.res.BaseResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<?> handleAllExceptions(Exception ex) {
        BaseResponse baseResponse = new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());

        return new ResponseEntity(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<?> notFoundExceptionHandler(NotFoundException ex) {
        BaseResponse baseResponse = new BaseResponse(ex.getStatus(), ex.getMessage());

        return new ResponseEntity(baseResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<?> badRequestExceptionHandler(BadRequestException ex) {
        BaseResponse baseResponse = new BaseResponse(ex.getStatus(), ex.getMessage());

        return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationException.class)
    public final ResponseEntity<?> authorizationExceptionHandler(AuthorizationException ex) {
        BaseResponse baseResponse = new BaseResponse(ex.getStatus(), ex.getMessage());

        return new ResponseEntity(baseResponse, HttpStatus.UNAUTHORIZED);
    }
}
