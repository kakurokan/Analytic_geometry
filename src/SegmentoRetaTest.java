import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SegmentoRetaTest {

    @Test
    void testToString() {
        assertEquals("sr((1.00,2.00); (2.00,2.00))", new SegmentoReta(new Ponto(2, 2), new Vetor(-1, 0)).toString());
        assertEquals("sr((3.00,3.00); (5.00,7.50))", new SegmentoReta(new Ponto(3, 3), new Vetor(2, 4.5)).toString());
    }

    @Test
    void intersect() {
        Ponto expected = new Ponto(1.1, 1.28);
        Vetor v = new Vetor(3.22, 3.72);
        SegmentoReta sr = new SegmentoReta(new Ponto(3.4, -0.24), new Ponto(-2.78, 3.84));
        Ponto result = sr.intersect(v);
        assertEquals(expected.getX(), result.getX(), 0.01);
        assertEquals(expected.getY(), result.getY(), 0.01);

        v = new Vetor(1, 4);
        sr = new SegmentoReta(new Ponto(4, 4), new Ponto(6, 6));
        assertNull(sr.intersect(v));

        // Paralelo
        v = new Vetor(4, 0);
        sr = new SegmentoReta(new Ponto(0, 2), new Ponto(4, 2));
        assertNull(sr.intersect(v));

        // Colineares, mas à frente
        v = new Vetor(2, 2);
        sr = new SegmentoReta(new Ponto(4, 4), new Ponto(6, 6));
        assertNull(sr.intersect(v));

        //Colinear
        v = new Vetor(4, 4);
        sr = new SegmentoReta(new Ponto(2, 2), new Ponto(6, 6));
        result = sr.intersect(v);
        assertTrue(result.equals(new Ponto(4, 4)) || result.equals(new Ponto(2, 2)));
    }
}