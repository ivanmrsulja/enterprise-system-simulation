package rs.enterprise.paymentserviceprovider.config;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import rs.enterprise.paymentserviceprovider.exception.InvalidUsernameOrPasswordException;
import rs.enterprise.paymentserviceprovider.exception.NotFoundException;
import rs.enterprise.paymentserviceprovider.util.ErrorObject;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ErrorHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    ErrorObject handleNotFoundException(HttpServletRequest request, NotFoundException ex) {
        return new ErrorObject(request, ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FeignException.class)
    @ResponseBody
    ErrorObject handleFeignException(HttpServletRequest request, FeignException ex) {
        return new ErrorObject(request, ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidUsernameOrPasswordException.class)
    @ResponseBody
    ErrorObject handleInvalidUsernameOrPasswordException(HttpServletRequest request, InvalidUsernameOrPasswordException ex) {
        return new ErrorObject(request, ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(RuntimeException.class)
//    @ResponseBody
//    ErrorObject handlePSQLException(HttpServletRequest request, RuntimeException ex) {
//        return new ErrorObject(request, ex.getMessage(), HttpStatus.BAD_REQUEST);
//    }
}
