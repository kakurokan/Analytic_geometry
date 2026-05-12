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
    private Route rota1,rota2;
    @BeforeEach
    void setUp() {
         rota1 = new Route(List.of(
                new Ponto(0, 0), new Ponto(1, 1), new Ponto(3, 2), new Ponto(3, 5)
        ));

         rota2 = new Route(List.of(
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
    void iniciarTest(){
        class GestorTemporario implements TorreDeControlo{
            List<Obstaculo> obstaculos;
            List<Route> rotas;
            boolean chamado=false;
            @Override
            public void atualizarRota(Movel movel) {}
            @Override
            public void atualizarPosicoes(Movel movel) {}
            @Override
            public void libertarMovel(Porto origem, Movel movel){}
            @Override
            public void movelTerminouPercurso(Movel movel) {}

            @Override
            public void iniciar(List<Route> rotas, List<Obstaculo> obstaculo) {
                this.chamado = true;
                this.rotas = rotas;
                this.obstaculos= obstaculo;
            }
            @Override
            public List<Movel> getMovels() {
                return List.of();
            }
        }

        List<Route> rotas = List.of(new Route(List.of(
            new Ponto(1,1), new Ponto (4,4), new Ponto (9,6)
        )));

        List<Obstaculo> obstaculos = List.of(new Triangulo(new Ponto[]{
                new Ponto(0,0), new Ponto(1,1), new Ponto(0,1)
        }));

        GestorTemporario gestor = new GestorTemporario();
        gestor.iniciar(rotas, obstaculos);

        assertTrue(gestor.chamado);
        assertEquals(rotas,gestor.rotas);
        assertEquals(obstaculos,gestor.obstaculos);
    }
    @Test
    void atualizarRota() {
        Porto destino2 = new Porto("A", new Ponto(0,4),gestor);
        Navio navio2 = origem.adicionarNavio(2, 10, destino2);
        gestor.libertarMovel(origem,navio2);
        navio2.mover(1.8723, new Vetor(1,1));
        Route inicial = new Route(List.of(
                new Ponto(0,0),new Ponto(1,1), new Ponto(3,2),
                new Ponto(2,3), new Ponto(0,4)
        ));
        gestor.atualizarRota(navio2);
        assertNotEquals(inicial,navio2.getRota());
        }

    @Test
    void getRota(){
        gestor.libertarMovel(origem,navio);
        assertEquals(rota1,navio.getRota());
    }
    @Test
    void atualizarPosicoes() {
        gestor.libertarMovel(destino, navio2);
        navio2.atualizar(1.3, new Vetor(1, 1));
        gestor.libertarMovel(origem, navio);
        navio.atualizar(1.9, new Vetor(1, 1));

        gestor.atualizarPosicoes(navio2);
        assertEquals(MovelAguardando.class, navio.getEstado().getClass());
    }

    @Test
    void libertarMovelNavio() {


        gestor.libertarMovel(destino, navio2);
        gestor.libertarMovel(origem, navio);

        assertInstanceOf(MovelNavegando.class, navio.getEstado());

        assertInstanceOf(MovelNavegando.class, navio2.getEstado());

        assertNotNull(gestor.getMovels());
    }

    @Test
    void movelNavioTerminouPercurso() {
        gestor.libertarMovel(origem, navio2);
        gestor.atualizarPosicoes(navio2);
        gestor.movelTerminouPercurso(navio2);
        assertFalse(gestor.getMovels().contains(navio2));
    }

    @Test
    void getMovels(){
        gestor.libertarMovel(destino, navio);
        gestor.libertarMovel(destino,navio2);
        assertTrue(gestor.getMovels().contains(navio2) && gestor.getMovels().contains(navio));
    }
}