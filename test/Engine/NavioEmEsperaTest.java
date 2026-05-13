package Engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NavioEmEsperaTest {
    @Test
    void testetoString() {
        SnapshotSimulacao.NavioEmEspera navioEmEspera = new SnapshotSimulacao.NavioEmEspera(5, "Lisboa", 5);
        String esperada = new String("T=5, Lisboa, 5,00");
        assertEquals(esperada, navioEmEspera.toString());
    }

    @Test
    void testeEquals_True() {
        SnapshotSimulacao.NavioEmEspera navioEmEspera = new SnapshotSimulacao.NavioEmEspera(5, "Lisboa", 5);
        assertTrue(navioEmEspera.equals(new SnapshotSimulacao.NavioEmEspera(5, "Lisboa", 5)));
    }

    @Test
    void testeEquals_False() {
        SnapshotSimulacao.NavioEmEspera navioEmEspera = new SnapshotSimulacao.NavioEmEspera(5, "Lisboa", 5);
        assertFalse(navioEmEspera.equals(new SnapshotSimulacao.NavioEmEspera(6, "Lisboa ", 5)));
    }

}
