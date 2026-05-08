package Engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GestorMaritimoTest {

    private GestorMaritimo gestor;
    private Navio navio;
    private Navio navio2;
    private Porto origem, destino;

    @BeforeEach
    void setUp() {
        Route rota1 = new Route(List.of(
                new Ponto(0, 0), new Ponto(1, 1), new Ponto(3, 2), new Ponto(3, 5)
        ));

        Route rota2 = new Route(List.of(
                new Ponto(9, 1), new Ponto(5, 1), new Ponto(3, 2),
                new Ponto(2, 3), new Ponto(0, 4), new Ponto(3, 5)
        ));

        List<Route> rotas = new ArrayList<>(List.of(rota1, rota2));
        List<Obstaculo> obstaculos = new ArrayList<>();

        gestor = new GestorMaritimo();
        gestor.iniciar(rotas, obstaculos);

        origem = new Porto("Albufeira", new Ponto(0, 0), gestor);
        destino = new Porto("Lisboa", new Ponto(3, 5), gestor);

        navio = origem.adicionarNavio(2, 10, destino);
        navio2 = destino.adicionarNavio(2, 10, origem);

    }

    @Test
    void atualizarRota() {
        gestor.libertarMovel(origem, navio);
        gestor.atualizarRota(navio);

        Route melhorRotaEsperada = new Route(List.of(
                new Ponto(0, 0), new Ponto(1, 1), new Ponto(3, 2), new Ponto(3, 5)
        ));
        assertEquals(melhorRotaEsperada.getSegmentos(), navio.getSegmentosRota());
    }

    @Test
    void atualizarPosicoes() {
        gestor.libertarMovel(destino, navio2);
        navio2.atualizar(1.3, new Vetor(1, 1));
        gestor.libertarMovel(origem, navio);
        navio.atualizar(1.9, new Vetor(1, 1));

        gestor.atualizarPosicoes(navio2);
        assertEquals(NavioAguardando.class, navio.getEstado().getClass());
    }

    @Test
    void libertarNavio() {


        gestor.libertarMovel(destino, navio2);
        gestor.libertarMovel(origem, navio);

        assertInstanceOf(NavioNavegando.class, navio.getEstado());

        assertInstanceOf(NavioNavegando.class, navio2.getEstado());

        assertNotNull(gestor.getNavios());
    }

    @Test
    void navioTerminouPercurso() {
        gestor.libertarMovel(origem, navio2);
        gestor.atualizarPosicoes(navio2);
        gestor.movelTerminouPercurso(navio2);
        assertFalse(gestor.getNavios().contains(navio2));
    }
}