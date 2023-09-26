package br.com.yomigae.cloudstash.core;

import br.com.yomigae.cloudstash.core.model.character.Character;
import br.com.yomigae.cloudstash.core.parser.DelegatingCharacterParser;

public class D2CloudStash {
    public static void main(String[] args) throws Exception {
        var p = new DelegatingCharacterParser();
        Character c = p.parse(D2CloudStash.class.getResourceAsStream("/samples/Starfire-4.d2s"));
        System.out.println(c);
    }
}
