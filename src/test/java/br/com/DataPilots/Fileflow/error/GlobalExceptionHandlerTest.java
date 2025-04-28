package br.com.DataPilots.Fileflow.error;

import br.com.DataPilots.Fileflow.exceptions.InvalidPasswordLengthException;
import br.com.DataPilots.Fileflow.exceptions.InvalidTokenException;
import br.com.DataPilots.Fileflow.exceptions.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler handler;

    @BeforeEach
    public void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    public void test_handleUserNotFoundException() {
        var response = handler.handleUserNotFoundException(new InvalidPasswordLengthException());

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Tamanho da senha inválido.", response.getBody());
    }

    @Test
    public void test_handleUserAlreadyExistsException() {
        var response = handler.handleUserAlreadyExistsException(new UserAlreadyExistsException());

        assertEquals(HttpStatus.CONFLICT.value(), response.getStatusCode().value());
        assertEquals("Esse usuário já existe.", response.getBody());
    }

    @Test
    public void test_handleRuntimeException() {
        var response = handler.handleRuntimeException(new RuntimeException("Ocorreu um erro"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode().value());
        assertEquals("Ocorreu um erro", response.getBody());
    }

    @Test
    public void test_handleInvalidBodyException() {
        var response = handler.handleInvalidBodyException(Mockito.mock(MethodArgumentNotValidException.class));

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    }

    @Test
    public void test_handleInvalidTokenException() {
        var response = handler.handleInvalidTokenException(new InvalidTokenException());

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode().value());
        assertEquals("Token inválido ou expirado.", response.getBody());
    }

    @Test
    public void test_emptyBodyHandler() {
        var response = handler.emptyBodyHandler();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
        assertEquals("Corpo da requisição vazio ou inválido.", response.getBody().message());
    }

    @Test
    public void test_unknownRouteHandler() {
        HttpMethod method = HttpMethod.GET;
        var response = handler.unknownRouteHandler(new NoResourceFoundException(method, "/"));

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode().value());
        assertEquals("Rota inválida!", response.getBody().message());
    }

    @Test
    public void test_unknownMethodHandler() {
        var response = handler.unknownMethodHandler();

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), response.getStatusCode().value());
        assertEquals("Esse método não é permitido.", response.getBody().message());
    }

    @Test
    public void test_unknownErrorHandler() {
        var response = handler.unknownErrorHandler(new Exception());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode().value());
        assertEquals("Ocorreu um erro desconhecido.", response.getBody().message());
    }
}
