package br.com.yomigae.cloudstash.core.d2s.parser;

public class D2ParserException extends RuntimeException {

    public D2ParserException(String message) {
        super(message);
    }

    public D2ParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
