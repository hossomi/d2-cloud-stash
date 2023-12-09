package br.com.yomigae.cloudstash.core.io.text;

import java.io.IOException;

public interface SummaryWriter<S extends SummaryWriter<S>> {

    default S line(String text) throws IOException {
        return write(text + '\n');
    }

    S write(String text) throws IOException;

    S h1(String label) throws IOException;

    S h2(String label) throws IOException;

    S w1(String label) throws IOException;

    S w2(String label) throws IOException;
}
