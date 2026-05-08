package Engine;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EstrategiaDijkstraTest {
    @Test
    void caminhos_ComObstaculoNaRotaMaisCurta_DesviaParaRotaAlternativa() {
        Route rota1 = new Route(List.of(
                new Ponto(0, 0), new Ponto(1, 1), new Ponto(3, 2), new Ponto(3, 5)
        ));
        Route rota2 = new Route(List.of(
                new Ponto(9, 1), new Ponto(5, 1), new Ponto(3, 2),
                new Ponto(2, 3), new Ponto(0, 4), new Ponto(3, 5)
        ));
        List<Route> rotas = List.of(rota1, rota2);

        Obstaculo obstaculo = new Triangulo(new Ponto[]{
                new Ponto(2, 4), new Ponto(4, 5), new Ponto(5, 4)
        });
        List<Obstaculo> obstaculos = List.of(obstaculo);

        Grafo grafo = new Grafo(rotas, obstaculos);
        EstrategiaDijkstra dijkstra = new EstrategiaDijkstra(grafo);


        Ponto origem = new Ponto(0, 0);
        Ponto destino = new Ponto(3, 5);
        List<Navio> navios = new ArrayList<>();

        Route rotaRetornada = dijkstra.caminhos(origem, destino);

        Route rotaEsperada = new Route(List.of(
                new Ponto(0, 0), new Ponto(1, 1), new Ponto(3, 2),
                new Ponto(2, 3), new Ponto(0, 4), new Ponto(3, 5)
        ));

        assertEquals(rotaEsperada, rotaRetornada,
                "Deveria desviar para a rota 2 devido à presença do triângulo (obstáculo) na rota 1.");
    }

    @Test
    void caminhos_SemObstaculos_RetornaRotaMaisCurtaDireta() {
        Route rota1 = new Route(List.of(
                new Ponto(0, 0), new Ponto(1, 1), new Ponto(3, 2), new Ponto(3, 5)
        ));
        Route rota2 = new Route(List.of(
                new Ponto(9, 1), new Ponto(5, 1), new Ponto(3, 2),
                new Ponto(2, 3), new Ponto(0, 4), new Ponto(3, 5)
        ));
        List<Route> rotas = List.of(rota1, rota2);
        List<Obstaculo> obstaculos = new ArrayList<>();

        Grafo grafo = new Grafo(rotas, obstaculos);
        EstrategiaDijkstra dijkstra = new EstrategiaDijkstra(grafo);

        Ponto origem = new Ponto(0, 0);
        Ponto destino = new Ponto(3, 5);
        List<Navio> navios = new ArrayList<>();

        Route rotaRetornada = dijkstra.caminhos(origem, destino);

        Route rotaEsperada = new Route(List.of(
                new Ponto(0, 0), new Ponto(1, 1), new Ponto(3, 2), new Ponto(3, 5)
        ));

        assertEquals(rotaEsperada, rotaRetornada,
                "Sem obstáculos no mapa, o algoritmo deveria escolher o trajeto mais direto (rota 1).");
    }

    @Test
    void construtor_GrafoNulo_Excecao(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new EstrategiaDijkstra(null);
        });
        assertEquals("EstrategiaDijkstra:iv", exception.getMessage());
    }

    @Test
    void caminho_GrafoDesconexo(){
        Route rota1 = new Route(List.of(
                new Ponto(0, 0), new Ponto(1, 1), new Ponto(3, 2), new Ponto(3, 5)
        ));
        Route rota2 = new Route(List.of(
                new Ponto(6,6), new Ponto(7,7)
        ));
        List<Route> rotas = List.of(rota1, rota2);
        List<Obstaculo> obstaculos = new ArrayList<>();
        Grafo grafo = new Grafo(rotas,obstaculos);
        Ponto origem = new Ponto(0, 0);
        Ponto destino = new Ponto(7,7);
        List<Navio> navios = new ArrayList<>();
        EstrategiaDijkstra dijkstra= new EstrategiaDijkstra(grafo);
        assertNull(dijkstra.caminhos(origem, destino),"Não existe caminho possivel entre a origem e o destino porque o grafo não é conexo");
    }
}