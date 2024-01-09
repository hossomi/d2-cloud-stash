package br.com.yomigae.cloudstash.core.io;

public class D2DataException extends RuntimeException {

    public D2DataException(String message) {
        super(message);
    }

    public D2DataException(String message, Throwable cause) {
        super(message, cause);
    }
}
