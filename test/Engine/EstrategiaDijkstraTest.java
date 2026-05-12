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


        Route rotaRetornada = dijkstra.caminhos(origem, destino);

        Route rotaEsperada = new Route(List.of(
                new Ponto(0, 0), new Ponto(1, 1), new Ponto(3, 2), new Ponto(3, 5)
        ));

        assertEquals(rotaEsperada, rotaRetornada,
                "Sem obstáculos no mapa, o algoritmo deveria escolher o trajeto mais direto (rota 1).");
    }

    @Test
    void caminhos_ComObstaculosESemObstaculos(){
        Route rota1 = new Route(List.of(
                new Ponto(2.0, 3.0),
                new Ponto(4.0, 5.0),
                new Ponto(1.0, 6.0),
                new Ponto(2.0, 8.0),
                new Ponto(6.0, 9.0))
        );

        Route rota2 = new Route(List.of(
                new Ponto(3.0, 10.0),
                new Ponto(5.0, 8.0),
                new Ponto(10.0, 6.0),
                new Ponto(10.0, 3.0),
                new Ponto(6.0, 4.0),
                new Ponto(2.0, 5.0),
                new Ponto(1.0, 1.0))
        );
        Route rota3 = new Route(List.of(
                new Ponto(1.0, 1.0),
                new Ponto(4.0, 3.0),
                new Ponto(5.0, 8.0)
        ));


        List<Route> rotas = List.of(rota1,rota2,rota3);
        Poligono q = new Poligono(new Ponto[]{
           new Ponto(3,3), new Ponto(5,0), new Ponto(9,0), new Ponto(9,4)
        });
        List<Obstaculo> obstaculos = List.of(q);

        Grafo grafo = new Grafo(rotas, obstaculos);
        EstrategiaDijkstra dijkstra = new EstrategiaDijkstra(grafo);
        GestorMaritimo gestor = new  GestorMaritimo();

        Porto origem = new Porto("A", new Ponto(1,1), gestor);
        Porto destino = new Porto("B", new Ponto(3,10), gestor);
        Porto destino2 = new Porto("C", new Ponto(10,6), gestor);



        Route rotaEsperadaOrigemDestino = new Route(List.of(
         new Ponto (1,1), new Ponto(2,5),new Ponto(3.60,4.60), new Ponto (4.285714285714286,4.428571428571429), new Ponto(5.0,8.0), new Ponto(4.40,8.60),new Ponto(3,10)
        ));
         assertEquals(rotaEsperadaOrigemDestino,dijkstra.caminhos(origem.getPosicao(),destino.getPosicao()));

        Route rotaEsperadaOrigemDestino2 = new Route(List.of(
                new Ponto (1,1), new Ponto(2,5),new Ponto(3.60,4.60), new Ponto (4.285714285714286,4.428571428571429), new Ponto(5.0,8.0), new Ponto(10,6)        ));

        assertEquals(rotaEsperadaOrigemDestino2,dijkstra.caminhos(origem.getPosicao(),destino2.getPosicao()));

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