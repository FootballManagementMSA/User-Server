package sejong.user.global;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sejong.user.global.exception.NotFoundException;
import sejong.user.global.res.BaseResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<?> handleAllExceptions(Exception ex) {
        BaseResponse baseResponse = new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());

        return new ResponseEntity(baseResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<?> notFound(NotFoundException ex) {
        BaseResponse baseResponse = new BaseResponse(ex.getStatus(), ex.getMessage());

        return new ResponseEntity(baseResponse, HttpStatus.NOT_FOUND);
    }
}
