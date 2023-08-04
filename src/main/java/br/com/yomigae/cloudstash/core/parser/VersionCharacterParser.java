package br.com.yomigae.cloudstash.core.parser;

import lombok.AllArgsConstructor;

import static java.lang.String.format;

@AllArgsConstructor
public abstract class VersionCharacterParser extends CharacterParser {

    private final int version;

    @Override
    protected void checkVersion(int version) {
        if (version != this.version) {
            throw new D2ParserException(format("Cannot parse version %d (expected %d)", version, this.version));
        }
    }
}
