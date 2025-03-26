package br.com.DataPilots.Fileflow.error;

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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {
    @ExceptionHandler({InvalidUserException.class})
    public ResponseEntity<Object> handleStudentNotFoundException(InvalidUserException exception) {
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

    @ExceptionHandler({InvalidPasswordLengthException.class})
    public ResponseEntity<Object> handleInvalidPasswordLengthExection(InvalidPasswordLengthException exception) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(exception.getMessage());
    }

    @ExceptionHandler({InvalidTokenException.class})
    public ResponseEntity<Object> handleInvalidPasswordLengthExection(InvalidTokenException exception) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
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
    public ResponseEntity<DefaultErrorResponse> unknownMethodHandler(Exception ignored) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(new DefaultErrorResponse("Esse método não é permitido."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DefaultErrorResponse> unknownErrorHandler(Exception exception) {
        return ResponseEntity.status(500).body(new DefaultErrorResponse("Ocorreu um erro desconhecido."));
    }
}
