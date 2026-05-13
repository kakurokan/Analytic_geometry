package Engine;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GrafoTest {

    @Test
    void construtor_ListaRotasVazia_LancaIllegalArgumentException() {
        List<Route> rotas = new ArrayList<>();
        List<Obstaculo> obstaculos = new ArrayList<>();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Grafo(rotas, obstaculos);
        });

        assertEquals("Grafo:iv", exception.getMessage());
    }

    @Test
    void construtor_RotasValidasSemObstaculos_NaoLancaExcecao() {
        Route rota1 = new Route(List.of(
                new Ponto(0, 0), new Ponto(1, 1), new Ponto(3, 2), new Ponto(3, 5)
        ));
        Route rota2 = new Route(List.of(
                new Ponto(9, 1), new Ponto(5, 1), new Ponto(3, 2),
                new Ponto(2, 3), new Ponto(0, 4), new Ponto(3, 5)
        ));
        List<Route> rotas = List.of(rota1, rota2);
        List<Obstaculo> obstaculos = new ArrayList<>();

        assertDoesNotThrow(() -> {
            new Grafo(rotas, obstaculos);
        }, "Um grafo com rotas válidas e sem obstáculos deve ser instanciado sem lançar exceções.");
    }

    @Test
    void construtor_ComObstaculos_RemoveSegmentosBloqueadosDoGrafo() {
        Route rota1 = new Route(List.of(
                new Ponto(0, 0), new Ponto(1, 1), new Ponto(3, 2), new Ponto(3, 5)
        ));
        Route rota2 = new Route(List.of(
                new Ponto(3, 2), new Ponto(1, 3), new Ponto(3, 5)
        ));
        List<Route> rotas = List.of(rota1, rota2);
        List<Obstaculo> obstaculos = List.of(
                new Tempestade(new Circulo(new Ponto(4, 3), 1))
        );

        Grafo grafo = new Grafo(rotas, obstaculos);
        Map<Ponto, Set<Ponto>> ligacoesPontos = grafo.getGrafo();

        Ponto pontoA = new Ponto(3, 2);
        Ponto excluido = new Ponto(3, 5); // Este segmento passaria pela tempestade
        Set<Ponto> ligacoesA = ligacoesPontos.get(pontoA);

        assertFalse(ligacoesA.contains(excluido), "O segmento bloqueado pela tempestade não deve estar presente nas ligações do grafo.");
    }

    @Test
    void construtor_TodosOsSegmentosBloqueados_LancaIllegalArgumentException() {
        Route rota1 = new Route(List.of(
                new Ponto(0, 0), new Ponto(1, 1), new Ponto(3, 2), new Ponto(3, 5)
        ));
        List<Route> rotas = List.of(rota1);

        Triangulo triangulo = new Triangulo(new Ponto[]{
                new Ponto(2, 1), new Ponto(2, 3), new Ponto(5, 2)
        });
        Triangulo triangulo1 = new Triangulo(new Ponto[]{
                new Ponto(1, 0), new Ponto(0, 1), new Ponto(2, 2)
        });
        List<Obstaculo> obstaculos = List.of(triangulo1, triangulo);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Grafo(rotas, obstaculos);
        });

        assertEquals("Não existe nenhum segmento livre", exception.getMessage(),
                "Se todos os segmentos forem bloqueados, o grafo deve avisar que não há caminhos disponíveis.");
    }

    @Test
    void getGrafo(){
        List<Route> rotas = List.of(
                new Route(List.of(
                        new Ponto(0,0), new Ponto(1,1), new Ponto(9,8)
                ))
        );
        List<Obstaculo> obstaculos = new  ArrayList<>();

        Grafo grafo = new Grafo(rotas,obstaculos);
        assertNotNull(grafo.getGrafo());
    }

    @Test
    void adicionarPonto_PontoNovo(){
        List<Route> rotas = List.of(
                new Route(List.of(
                        new Ponto(0,0), new Ponto(5,1), new Ponto(5,4)
                ))
        );
        List<Obstaculo> obstaculos = new  ArrayList<>();

        Grafo grafo = new Grafo(rotas,obstaculos);

        grafo.adicionarPonto(new Ponto(5,3),new SegmentoReta(new Ponto(5,1),new Ponto(5,4)));

        assertTrue(grafo.getGrafo().containsKey(new Ponto(5,3)));
    }

    @Test
    void adicionarPonto_PontoJaExistente(){
        List<Route> rotas = List.of(
                new Route(List.of(
                        new Ponto(0,1), new Ponto(4,1), new Ponto(5,4)
                ))
        );
        List<Obstaculo> obstaculos = new  ArrayList<>();

        Grafo grafo = new Grafo(rotas,obstaculos);
        int tamanhoOriginal=grafo.getGrafo().size();

        grafo.adicionarPonto(new Ponto(4,1),new SegmentoReta(new Ponto(4,1),new Ponto(5,4)));

        assertEquals(tamanhoOriginal,grafo.getGrafo().size());
    }

    @Test
    void adicionarPonto_EmSegmentoNaoExistenteNoGrafo(){
        List<Route> rotas = List.of(
                new Route(List.of(
                        new Ponto(0,1), new Ponto(0,3), new Ponto(5,4)
                ))
        );
        List<Obstaculo> obstaculos = new  ArrayList<>();

        Grafo grafo = new Grafo(rotas,obstaculos);
        int tamanhoOriginal=grafo.getGrafo().size();

        grafo.adicionarPonto(new Ponto(6,2),new SegmentoReta(new Ponto(6,1),new Ponto(6,4)));

        assertEquals(tamanhoOriginal,grafo.getGrafo().size());
    }

    @Test
    void removerPonto_PontoNaoExistente(){
        List<Route> rotas = List.of(
                new Route(List.of(
                     new Ponto(0,0), new Ponto(1,1), new Ponto(3,4)
                ))
        );
        List<Obstaculo> obstaculos = new  ArrayList<>();

        Grafo grafo = new Grafo(rotas,obstaculos);
        int tamanhoOriginal=grafo.getGrafo().size();

        grafo.removerPonto(new Ponto(0.5,0.5), new SegmentoReta(new Ponto(0,0),new Ponto(1,1)));

        assertEquals(tamanhoOriginal,grafo.getGrafo().size());
    }

    @Test
    void removerPonto_PontoNovo(){
        List<Route> rotas = List.of(
                new Route(List.of(
                        new Ponto(0,0), new Ponto(1,1), new Ponto(5,4), new Ponto(3,2), new Ponto(5,0),new Ponto(6,0)
                ))
        );
        List<Obstaculo> obstaculos = new  ArrayList<>();

        Grafo grafo = new Grafo(rotas,obstaculos);
        int tamanhoOriginal=grafo.getGrafo().size();
        grafo.adicionarPonto(new Ponto(5.5,0),new SegmentoReta(new Ponto(5,0),new Ponto(6,0)));
        assertEquals(tamanhoOriginal+1,grafo.getGrafo().size());

        grafo.removerPonto(new Ponto(5.5,0), new SegmentoReta(new Ponto(5,0),new Ponto(6,0)));

        assertEquals(tamanhoOriginal,grafo.getGrafo().size());

    }
}