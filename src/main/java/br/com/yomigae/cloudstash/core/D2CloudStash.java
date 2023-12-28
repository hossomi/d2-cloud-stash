package br.com.yomigae.cloudstash.core;

import br.com.yomigae.cloudstash.core.model.character.Character;
import br.com.yomigae.cloudstash.core.parser.DelegatingCharacterParser;

import static java.util.Objects.requireNonNull;

public class D2CloudStash {
    public static void main(String[] args) throws Exception {
        var p = new DelegatingCharacterParser();
        Character c = p.parse(requireNonNull(D2CloudStash.class.getResourceAsStream("/samples/Zeus.d2s")));
    }
}
