package br.com.DataPilots.Fileflow.error;

import br.com.DataPilots.Fileflow.exceptions.FolderAlreadyExistsException;
import br.com.DataPilots.Fileflow.exceptions.InvalidPasswordLengthException;
import br.com.DataPilots.Fileflow.exceptions.InvalidTokenException;
import br.com.DataPilots.Fileflow.exceptions.InvalidUserException;
import br.com.DataPilots.Fileflow.exceptions.UserAlreadyExistsException;
import br.com.DataPilots.Fileflow.response.DefaultErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
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
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler({InvalidUserException.class})
    public ResponseEntity<Object> handleUserNotFoundException(InvalidUserException exception) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(exception.getMessage());
    }

    @ExceptionHandler({UserAlreadyExistsException.class})
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(exception.getMessage());
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleRuntimeException(RuntimeException exception) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(exception.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleInvalidBodyException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidTokenException.class})
    public ResponseEntity<Object> handleInvalidTokenException(InvalidTokenException exception) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(exception.getMessage());
    }

    @ExceptionHandler({FolderAlreadyExistsException.class})
    public ResponseEntity<Object> FolderAlreadyExists(FolderAlreadyExistsException exception) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<DefaultErrorResponse> emptyBodyHandler() {
        return ResponseEntity.badRequest().body(new DefaultErrorResponse("Corpo da requisição vazio ou inválido."));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<DefaultErrorResponse> unknownRouteHandler(NoResourceFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new DefaultErrorResponse("Rota inválida!"));
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<DefaultErrorResponse> unknownMethodHandler() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(new DefaultErrorResponse("Esse método não é permitido."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DefaultErrorResponse> unknownErrorHandler(Exception exception) {
        return ResponseEntity.status(500).body(new DefaultErrorResponse("Ocorreu um erro desconhecido."));
    }
}