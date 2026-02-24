import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
        assertEquals(5.0, new Ponto(-3, -4).distanciaDaOrigem(), Ponto.eps);
    }

    @Test
    void testEquals() {
        Ponto p1 = new Ponto(2, 3);
        assertEquals(new Ponto(2, 3), p1);           // Igualdade
        assertNotEquals(new Ponto(2.0000001, 3), p1);  // Diferença acima do eps
        assertNotEquals(null, p1);                     // Caso nulo
    }

    @Test
    void produtoVetorial() {
        assertEquals(-25.84, new Ponto(5.6, 7.8).produtoVetorial(new Ponto(9.2, 8.2)), Ponto.eps);
    }

    @Test
    void subtracao() {
        Ponto resultado = new Ponto(5.5, 10).subtracao(new Ponto(2, 3));
        assertEquals(3.5, resultado.getX(), Ponto.eps);
        assertEquals(7.0, resultado.getY(), Ponto.eps);
    }

    @Test
    void testToString() {
        assertEquals("(3.00,4.00)", new Ponto(3, 4).toString());
        assertEquals("(-2.40,7.51)", new Ponto(-2.4, 7.512).toString());
    }
}