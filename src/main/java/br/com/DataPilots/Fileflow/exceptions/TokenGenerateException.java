package br.com.DataPilots.Fileflow.exceptions;

public class TokenGenerateException extends Exception {
    @Override
    public String getMessage() {
        return "Ocorreu um erro ao gerar um token.";
    }
}
