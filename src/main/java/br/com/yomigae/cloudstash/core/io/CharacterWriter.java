package br.com.yomigae.cloudstash.core.io;

import br.com.yomigae.cloudstash.core.model.character.Character;

import java.io.OutputStream;

public interface CharacterWriter {
    void write(Character character, OutputStream output);
}
