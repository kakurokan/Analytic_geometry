package Engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimuladorTest {

    private List<Movel> naviosSistema;
    private Navio navio;
    private List<Route> rotas;
    private List<Obstaculo> obstaculos;
    private List<Porto> portos;
    private Simulador simulador;
    private Vetor corrente;
    private GestorMaritimo gestor;
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

        rotas = new ArrayList<>(List.of(rota1, rota2));
        obstaculos = new ArrayList<>();
        gestor = new GestorMaritimo();

        gestor.iniciar(rotas, obstaculos);
        origem = new Porto("Albufeira", new Ponto(0, 0), gestor);
        destino = new Porto("Lisboa", new Ponto(3, 5), gestor);
        portos = List.of(origem, destino);

        navio = origem.adicionarNavio(5, 2, destino);
        naviosSistema = List.of(navio);
        corrente = new Vetor(-3, 2);

        simulador = new Simulador(corrente, rotas, portos, obstaculos, gestor);
    }

    @Test
    void atualizar_ComNavioNavegando_AlteraPosicaoDoNavio() {
        gestor.libertarMovel(origem, navio);

        Ponto posicaoInicial = navio.getPosicao();

        simulador.atualizar(1.0);

        Ponto novaPosicao = navio.getPosicao();

        assertNotNull(novaPosicao, "A posição do navio não deve ser nula após a atualização do simulador.");
        assertNotEquals(posicaoInicial, novaPosicao, "O navio deveria ter-se movido após a invocação do método atualizar() do simulador.");
    }

    @Test
    void criarTempestade_Invocado_AdicionaNovaTempestadeAosObstaculos() {
        int quantidadeObstaculosInicial = simulador.getObstaculos().size();

        simulador.criarTempestade();

        int quantidadeObstaculosFinal = simulador.getObstaculos().size();

        assertEquals(quantidadeObstaculosInicial + 1, quantidadeObstaculosFinal, "Deveria ter sido adicionado exatamente 1 obstáculo (Tempestade) à simulação.");
        assertFalse(simulador.getObstaculos().isEmpty(), "A lista de obstáculos não deve estar vazia após criar uma tempestade.");
    }

    @Test
    void getObstaculos_SimuladorComObstaculos_RetornaListaCorreta() {
        EstadoNavioTest.TorreDeControloSAux torre = new EstadoNavioTest.TorreDeControloSAux();
        obstaculos.add(new Tempestade(new Circulo(new Ponto(1, 1), 5)));
        Simulador simuladorComObstaculos = new Simulador(corrente, rotas, portos, obstaculos, torre);

        assertEquals(obstaculos, simuladorComObstaculos.getObstaculos(), "O simulador deveria retornar a mesma lista de obstáculos com a qual foi instanciado.");
    }
}