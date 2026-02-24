import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class VetorTest {

    @Test
    void getX() {
        assertEquals(2, new Vetor(2, 3).getX());
        assertEquals(1.752, new Vetor(1.752, 0.2).getX());
    }

    @Test
    void getY() {
        assertEquals(2, new Vetor(3, 2).getY());
        assertEquals(1.752, new Vetor(0.2, 1.752).getY());
    }

    @Test
    void modulo() {
        assertEquals(4.471028964, new Vetor(3.74, -2.45).modulo(), Ponto.eps);
    }

    @Test
    void produtoInterno() {
        assertEquals(15.90, new Vetor(2, 1).produtoInterno(new Vetor(6.2, 3.5)), Ponto.eps);
    }

    @Test
    void cossineSimilarity() {
        assertEquals(0.953230637699, new Vetor(1, 3).cossineSimilarity(new Vetor(4, 5.5)), Ponto.eps);
        assertEquals(-0.835604010078, new Vetor(-7, 4).cossineSimilarity(new Vetor(16, 1)), Ponto.eps);
    }

    @Test
    void intersect() {
        Ponto expected = new Ponto(1, 1);
        Vetor v = new Vetor(2, 2);
        SegmentoReta sr = new SegmentoReta(new Ponto(0, 1), new Ponto(4, 1));
        assertEquals(expected, v.intersect(sr));

        v = new Vetor(1, 4);
        sr = new SegmentoReta(new Ponto(4, 4), new Ponto(6, 6));
        assertNull(v.intersect(sr));
    }

    @Test
    void mult() {
        Vetor expected = new Vetor(11.7, 27.1);
        Vetor result = new Vetor(2.34, 5.42).mult(5);

        assertEquals(result.getX(), expected.getX(), Ponto.eps);
        assertEquals(result.getY(), expected.getY(), Ponto.eps);
    }

    @Test
    void add() {
        Vetor expected = new Vetor(6, 4);
        Vetor result = new Vetor(2, -3).add(new Vetor(4, 7));

        assertEquals(result.getX(), expected.getX(), Ponto.eps);
        assertEquals(result.getY(), expected.getY(), Ponto.eps);
    }

    @Test
    void sub() {
        Vetor expected = new Vetor(-2, -9);
        Vetor result = new Vetor(2, -3).sub(new Vetor(4, 6));

        assertEquals(result.getX(), expected.getX(), Ponto.eps);
        assertEquals(result.getY(), expected.getY(), Ponto.eps);
    }

    @Test
    void testToString() {
        assertEquals("[3.00,4.00]", new Vetor(3, 4).toString());
        assertEquals("[-2.40,7.51]", new Vetor(-2.4, 7.512).toString());
    }
}