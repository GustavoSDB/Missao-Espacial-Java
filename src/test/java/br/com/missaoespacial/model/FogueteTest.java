package br.com.missaoespacial.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.Test;

class FogueteTest {

    @Test
    void abasteceAteCombustivelMaximo() {
        Foguete foguete = new Foguete("Artemis", 9900, 1000, "Pronto");

        foguete.abastecer(100);

        assertThat(foguete.getCombustivelRestante()).isEqualTo(Foguete.COMBUSTIVEL_MAXIMO);
    }

    @Test
    void naoAbasteceAcimaDoCombustivelMaximo() {
        Foguete foguete = new Foguete("Artemis", 9900, 1000, "Pronto");

        assertThatIllegalArgumentException()
                .isThrownBy(() -> foguete.abastecer(101))
                .withMessage("Combustivel maximo do foguete e 10000.");
    }
}
