import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PontoTest {

    @Test
    void getX() {
        assertEquals(2, new Ponto(2, 3).getX());
        assertEquals(1.752, new Ponto(1.752, 0.2).getX());
    }

    @Test
    void getY() {
        assertEquals(2, new Ponto(3, 2).getY());
        assertEquals(1.752, new Ponto(0.2, 1.752).getY());
    }

    @Test
    void distanciaDaOrigem() {
        assertEquals(3.605551275, new Ponto(2, 3).distanciaDaOrigem(), Ponto.eps);
    }

    @Test
    void testEquals() {
        assertTrue(new Ponto(2, 3).equals(new Ponto(2, 3)));
    }

    @Test
    void produtoVetorial() {
        assertEquals(-25.84, new Ponto(5.6, 7.8).produtoVetorial(new Ponto(9.2, 8.2)), Ponto.eps);
    }

    @Test
    void subtracao() {
        assertEquals(new Ponto(1, 3), new Ponto(3, 8).subtracao(new Ponto(2, 5)));
    }

    @Test
    void testToString() {
        assertEquals("(3.00,4.00)", new Ponto(3, 4).toString());
    }
}