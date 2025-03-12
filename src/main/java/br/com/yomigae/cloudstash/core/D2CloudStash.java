package br.com.yomigae.cloudstash.core;

import br.com.yomigae.cloudstash.core.d2s.model.D2S;
import br.com.yomigae.cloudstash.core.d2s.parser.D2SParser;
import br.com.yomigae.cloudstash.core.d2s.parser.D2SParserFactory;
import br.com.yomigae.cloudstash.core.io.D2BinaryReader;
import br.com.yomigae.cloudstash.core.io.TextD2SWriter;

import java.io.FileOutputStream;
import java.io.InputStream;

import static java.util.Objects.requireNonNull;

public class D2CloudStash {
    public static void main(String[] args) throws Exception {
        String sampleName = "Starfire-6";
        InputStream input = requireNonNull(D2CloudStash.class.getResourceAsStream("/samples/" + sampleName + ".d2s"));
        D2BinaryReader reader = D2BinaryReader.from(input);
        D2SParser p = D2SParserFactory.instance().createParser(reader);
        D2S c = p.parse(reader);
        new TextD2SWriter().write(c, new FileOutputStream(sampleName + ".txt"));
    }
}
