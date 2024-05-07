package MyApp.MapMaker.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandle {
    @ExceptionHandler()
    public String handleException(Exception e){
        return "error";
    }
}
