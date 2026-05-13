package Engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EstadoMovelTest {
    private Navio navio;
    private EstadoMovel navegando;
    private EstadoMovel naOrigem;
    private EstadoMovel noDestino;
    private EstadoMovel aguardando;
    private Porto origem, portoDestino;
    private TorreDeControlo gestor;
    @BeforeEach
    void setUp() {
        Ponto centro = new Ponto(0, 0);
        Circulo areaNavio = new Circulo(centro, 10.0);

        gestor = new GestorMaritimo();

        origem = new Porto("Albufeira", new Ponto(0, 0), gestor);
        portoDestino = new Porto("Porto Faro", new Ponto(100.0, 100.0), gestor);
        Route rota = new Route(new double[]{0.0, 0.0, 100.0, 100.0
        });
        List<Route> rotas = List.of(rota);
        List<Obstaculo> obstaculos = new ArrayList<>();
        gestor.iniciar(rotas,obstaculos);
        navio = new Navio(areaNavio, 20.0, 1, origem, portoDestino, gestor);
        navio.receberRota(rota);
        navegando = new MovelNavegando();
        naOrigem = new MovelNaOrigem();
        noDestino = new MovelNoDestino();
        aguardando = new MovelAguardando();
    }

    @Test
    void atualizar_CicloDeVidaNavio(){
        Navio navio1 = origem.adicionarNavio(5,2,portoDestino);
        Navio navio2 = portoDestino.adicionarNavio(5,1,origem);
        assertEquals(naOrigem.getClass(),navio1.getEstado().getClass(),"Ao ser criado o seu estado deve ser MovelNaOrigem");

        gestor.libertarMovel(origem,navio1);
        gestor.libertarMovel(portoDestino,navio2);

        assertEquals(navegando.getClass(),navio1.getEstado().getClass(), "Após ser invocado libertarMovel, o navio deve começar a navegar e portanto ter estado MovelNavegando");

        navio2.atualizar(4.15,new Vetor(1,1));
        navio1.atualizar(23.8,new Vetor(1,1));

        assertTrue(navio1.isEmColisao(), "O navio1 deve encontrar-se em colisão pela proximidade ao navio2");
        assertEquals(aguardando.getClass(),navio1.getEstado().getClass());

        navio2.atualizar(100, new Vetor(1,1));
        navio1.atualizar(0.1,new Vetor(1,1));
        assertFalse(navio1.isEmColisao());
        assertEquals(navegando.getClass(),navio1.getEstado().getClass(),"Deve voltar ao estado MovelNavegando por já não ter risco de colidir com navio2");

        navio1.atualizar(10,new Vetor(1,1));
        assertEquals(noDestino.getClass(),navio1.getEstado().getClass());
    }



    @Test
    void atualizar_EstadoNaOrigem_NaoLancaExcecoes() {
        assertDoesNotThrow(() -> navio.mudarEstado(naOrigem));
        assertDoesNotThrow(() -> naOrigem.atualizar(navio, 1.0, new Vetor(5, 5)));
        assertEquals(new Ponto(0, 0), navio.getPosicao());
        assertInstanceOf(EstadoMovel.class, naOrigem);
    }

    @Test
    void atualizar_EstadoAguardandoComColisao(){
        Navio navio1= origem.adicionarNavio(2,1,portoDestino);
        Navio navio2= origem.adicionarNavio(2,2,portoDestino);
        gestor.libertarMovel(origem,navio1);
        gestor.libertarMovel(origem,navio2);
        navio1.mover(0.1,new Vetor(1,1));
        navio2.mover(0.5,new Vetor(2,2));
        assertTrue(navio1.isEmColisao());
        assertEquals(aguardando.getClass(),navio1.getEstado().getClass());

        navio2.atualizar(5,new Vetor(2,2));
        navio1.atualizar(0.1,new Vetor(1,1));

        assertFalse(navio1.isEmColisao());
        assertNotEquals(aguardando.getClass(),navio1.getEstado().getClass(),"O estado do navio já não deve ser aguardando, pois não se encontra mais em colisão");
        assertEquals(navegando.getClass(),navio1.getEstado().getClass(), "O estado do navio deve ser navegando");
    }
    @Test
    void atualizar_EstadoAguardando_NaoLancaExcecoes() {
        assertDoesNotThrow(() -> navio.mudarEstado(aguardando));
        assertDoesNotThrow(() -> aguardando.atualizar(navio, 5.0, new Vetor(5, 5)));
        assertInstanceOf(EstadoMovel.class, aguardando);
    }

    @Test
    void atualizar_EstadoNavegando_NaoLancaExcecoesEValidaInterface() {
        assertDoesNotThrow(() -> navio.mudarEstado(navegando));
        assertDoesNotThrow(() -> navegando.atualizar(navio, 10.0, new Vetor(5, 5)));

        assertInstanceOf(EstadoMovel.class, navegando);
        assertInstanceOf(Movel.class, navio, "O navio deve implementar a interface Movel para permitir a navegação.");
    }

    @Test
    void atualizar_EstadoNoDestino_NaoLancaExcecoes() {
        assertDoesNotThrow(() -> navio.mudarEstado(noDestino));
        assertDoesNotThrow(() -> noDestino.atualizar(navio, 2.0, new Vetor(5, 5)));
        assertInstanceOf(EstadoMovel.class, noDestino);
    }

    @Test
    void atualizar_NavioNavegando_AlteraPosicaoGeometrica() {
        Route rota = new Route(List.of(new Ponto(0, 0), new Ponto(20, 20)));
        navio.receberRota(rota);

        EstadoMovel navegando = new MovelNavegando();
        navio.mudarEstado(navegando);

        navegando.atualizar(navio, 1.0, new Vetor(5, 5));

        Ponto posicaoAtual = navio.getPosicao();

        assertNotNull(posicaoAtual, "A posição não deveria ser nula após o movimento.");
        assertEquals(14.14, posicaoAtual.getX(), 0.01, "A coordenada X deveria refletir o deslocamento da velocidade.");
        assertEquals(14.14, posicaoAtual.getY(), 0.01, "A coordenada Y deveria refletir o deslocamento da velocidade.");
    }

   }