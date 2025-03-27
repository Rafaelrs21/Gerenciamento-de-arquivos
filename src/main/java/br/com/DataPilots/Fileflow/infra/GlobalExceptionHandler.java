package br.com.DataPilots.Fileflow.infra;

import br.com.DataPilots.Fileflow.dtos.DefaultErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<DefaultErrorResponse> emptyBodyHandler() {
        return ResponseEntity.badRequest().body(new DefaultErrorResponse("Corpo da requisição vazio ou inválido."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> invalidRequestHandler(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<DefaultErrorResponse> unknownRouteHandler(Exception ignored) {
        return ResponseEntity.status(404).body(new DefaultErrorResponse("Essa rota não existe."));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<DefaultErrorResponse> unknownMethodHandler(Exception ignored) {
        return ResponseEntity.status(404).body(new DefaultErrorResponse("Esse metodo não existe."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DefaultErrorResponse> unknownErrorHandler(Exception exception) {
        return ResponseEntity.status(500).body(new DefaultErrorResponse("Ocorreu um erro desconhecido. Tente mais tarde."));
    }
}
