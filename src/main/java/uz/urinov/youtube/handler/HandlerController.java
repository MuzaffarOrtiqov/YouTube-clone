package uz.urinov.youtube.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uz.urinov.youtube.exp.AppBadException;

@ControllerAdvice
public class HandlerController {
    @ExceptionHandler(AppBadException.class)
    public ResponseEntity<String> handler(AppBadException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
