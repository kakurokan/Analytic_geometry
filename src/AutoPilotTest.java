import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AutoPilotTest {

    @Test
    void speed() {
        AutoPilot ap = new AutoPilot(new Ponto(2, 2), new Ponto(2, 4));
        Vetor speed = new Vetor(1, 1);
        double time = 5;
        Vetor expected = new Vetor(-0.2, 0.2);
        Vetor result = ap.speed(speed, time);
        assertEquals(expected.getX(), result.getX(), Ponto.eps);
        assertEquals(expected.getY(), result.getY(), Ponto.eps);
    }

    @Test
    void time() {
        AutoPilot ap = new AutoPilot(new Ponto(2, 4), new Ponto(2, 2));
        Vetor windSpeed = new Vetor(1, 1);
        double speed = 0.632;
        Vetor expected = new Vetor(-0.2, -0.6);
        double result = ap.time(windSpeed, speed);
        assertEquals(5, result, 0.01);
    }
}