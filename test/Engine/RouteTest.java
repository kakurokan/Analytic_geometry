package Engine;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RouteTest {

    @Test
    void construtor_ArrayComNumeroImparDeCoordenadas_LancaIllegalArgumentException() {
        double[] coordenadas = {2.5, 3.0, 2.0};

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Route(coordenadas);
        });

        assertEquals("Rota:iv", exception.getMessage());
    }

    @Test
    void construtor_ArrayComNumeroParDeCoordenadas_NaoLancaExcecao() {
        double[] coordenadas = {2.5, 3.0, 2.0, 4.0};

        assertDoesNotThrow(() -> {
            new Route(coordenadas);
        });
    }

    @Test
    void comprimento_RotaComVariosSegmentos_RetornaSomaDasDistancias() {
        List<Ponto> pontos = List.of(
                new Ponto(0, 1),
                new Ponto(1, 1),
                new Ponto(4, 4),
                new Ponto(4, 3)
        );
        Route rota = new Route(pontos);

        assertEquals(6.24, rota.comprimento(), 0.01, "O comprimento total deve ser a soma das distâncias entre os pontos consecutivos.");
    }

    @Test
    void comprimento_RotaLinear_RetornaDistanciaCorreta() {
        List<Ponto> pontos = List.of(
                new Ponto(4, 4),
                new Ponto(6, 6),
                new Ponto(7, 7)
        );
        Route rota = new Route(pontos);

        assertEquals(4.24, rota.comprimento(), 0.01);
    }

    @Test
    void comprimento_RotaComPontosConsecutivosIguais_IgnoraDistanciaZero() {
        List<Ponto> pontos = List.of(
                new Ponto(4, 4),
                new Ponto(4, 4),
                new Ponto(6, 7)
        );
        Route rota = new Route(pontos);

        assertEquals(3.61, rota.comprimento(), 0.01, "Pontos consecutivos iguais na rota não devem aumentar o comprimento.");
    }

    @Test
    void equals_RotasComMesmosPontosNaMesmaOrdem_RetornaTrue() {
        Ponto p1 = new Ponto(0, 0);
        Ponto p2 = new Ponto(2, 4);
        Ponto p3 = new Ponto(5, 1);
        List<Ponto> pontos = List.of(p1, p2, p3);

        Route rota = new Route(pontos);
        Route rota1 = new Route(pontos);

        assertEquals(rota, rota1, "Duas rotas instanciadas com a mesma lista de pontos devem ser iguais.");
    }

    @Test
    void equals_RotasComMesmosPontosEmOrdemDiferente_RetornaFalse() {
        Ponto p1 = new Ponto(0, 0);
        Ponto p2 = new Ponto(2, 4);
        Ponto p3 = new Ponto(5, 1);

        Route rota = new Route(List.of(p1, p2, p3));
        Route rota1 = new Route(List.of(p2, p1, p3));

        assertNotEquals(rota, rota1, "A ordem dos pontos é relevante para a igualdade de rotas.");
    }

    @Test
    void getSegmentos_RotaValida_RetornaListaDeSegmentosCorreta() {
        Ponto p1 = new Ponto(0, 0);
        Ponto p2 = new Ponto(2, 4);
        Ponto p3 = new Ponto(5, 1);
        List<Ponto> pontos = List.of(p1, p2, p3);
        Route rota = new Route(pontos);

        List<SegmentoReta> result = rota.getSegmentos();

        assertNotNull(result);
        assertEquals(2, result.size(), "Uma rota de 3 pontos deve gerar exatamente 2 segmentos.");

        assertEquals(p1, result.get(0).getA(), "A origem do primeiro segmento deve ser o Engine.Ponto 1.");
        assertEquals(p2, result.get(0).getB(), "O destino do primeiro segmento deve ser o Engine.Ponto 2.");

        assertEquals(p2, result.get(1).getA(), "A origem do segundo segmento deve ser o Engine.Ponto 2.");
        assertEquals(p3, result.get(1).getB(), "O destino do segundo segmento deve ser o Engine.Ponto 3.");
    }
}