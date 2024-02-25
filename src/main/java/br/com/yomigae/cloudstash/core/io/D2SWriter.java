package br.com.yomigae.cloudstash.core.io;


import br.com.yomigae.cloudstash.core.d2s.model.D2S;

import java.io.IOException;
import java.io.OutputStream;

public interface D2SWriter {
    void write(D2S character, OutputStream output) throws IOException;
}
