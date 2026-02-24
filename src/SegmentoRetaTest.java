import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SegmentoRetaTest {

    @Test
    void testToString() {
        assertEquals("sr((1.00,2.00); (2.00,2.00))", new SegmentoReta(new Ponto(2, 2), new Vetor(-1, 0)).toString());
        assertEquals("sr((3.00,3.00); (5.00,7.50))", new SegmentoReta(new Ponto(3, 3), new Vetor(2, 4.5)).toString());
    }

    @Test
    void intersect() {
        Ponto expected = new Ponto(1, 1);
        Vetor v = new Vetor(2, 2);
        SegmentoReta sr = new SegmentoReta(new Ponto(0, 1), new Ponto(4, 1));
        assertEquals(expected, sr.intersect(v));

        v = new Vetor(1, 4);
        sr = new SegmentoReta(new Ponto(4, 4), new Ponto(6, 6));
        assertNull(sr.intersect(v));
    }
}