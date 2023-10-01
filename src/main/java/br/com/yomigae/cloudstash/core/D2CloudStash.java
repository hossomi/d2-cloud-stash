package br.com.yomigae.cloudstash.core;

import br.com.yomigae.cloudstash.core.model.character.Character;
import br.com.yomigae.cloudstash.core.parser.DelegatingCharacterParser;

import static br.com.yomigae.cloudstash.core.model.Difficulty.NIGHTMARE;
import static br.com.yomigae.cloudstash.core.model.Difficulty.NORMAL;
import static java.util.Objects.requireNonNull;

public class D2CloudStash {
    public static void main(String[] args) throws Exception {
        var p = new DelegatingCharacterParser();
        for (int i = 1; i <= 6; i++) {
            System.out.println("----- Starfire-%d -----".formatted(i));
            Character c = p.parse(requireNonNull(D2CloudStash.class.getResourceAsStream("/samples/Starfire-%d.d2s".formatted(i))));
            System.out.println(c.progression(NORMAL).act5().prisonOfIce());
            System.out.println(c.progression(NIGHTMARE).act5().prisonOfIce());
        }
    }
}
