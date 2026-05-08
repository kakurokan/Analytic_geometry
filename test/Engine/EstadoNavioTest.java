package Engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EstadoNavioTest {
    private Navio navio;
    private EstadoNavio navegando;
    private EstadoNavio naOrigem;
    private EstadoNavio noDestino;
    private EstadoNavio aguardando;

    @BeforeEach
    void setUp() {
        Ponto centro = new Ponto(0, 0);
        Circulo areaNavio = new Circulo(centro, 10.0);

        TorreDeControlo torreAux = new TorreDeControloSAux();
        Porto origem = new Porto("Albufeira", new Ponto(0, 0), torreAux);
        Porto portoDestino = new Porto("Porto Faro", new Ponto(100.0, 100.0), torreAux);
        Route rota = new Route(new double[]{0.0, 0.0, 100.0, 100.0
        });
        navio = new Navio(areaNavio, 20.0, 1, origem, portoDestino, torreAux);
        navio.receberRota(rota);
        navegando = new NavioNavegando();
        naOrigem = new NavioNaOrigem();
        noDestino = new NavioNoDestino();
        aguardando = new NavioAguardando();
    }

    @Test
    void atualizar_EstadoNaOrigem_NaoLancaExcecoes() {
        assertDoesNotThrow(() -> navio.mudarEstado(naOrigem));
        assertDoesNotThrow(() -> naOrigem.atualizar(navio, 1.0, new Vetor(5, 5)));
        assertEquals(new Ponto(0, 0), navio.getPosicao());
        assertInstanceOf(EstadoNavio.class, naOrigem);
    }

    @Test
    void atualizar_EstadoAguardando_NaoLancaExcecoes() {
        assertDoesNotThrow(() -> navio.mudarEstado(aguardando));
        assertDoesNotThrow(() -> aguardando.atualizar(navio, 5.0, new Vetor(5, 5)));
        assertInstanceOf(EstadoNavio.class, aguardando);
    }

    @Test
    void atualizar_EstadoNavegando_NaoLancaExcecoesEValidaInterface() {
        assertDoesNotThrow(() -> navio.mudarEstado(navegando));
        assertDoesNotThrow(() -> navegando.atualizar(navio, 10.0, new Vetor(5, 5)));

        assertInstanceOf(EstadoNavio.class, navegando);
        assertInstanceOf(Movel.class, navio, "O navio deve implementar a interface Movel para permitir a navegação.");
    }

    @Test
    void atualizar_EstadoNoDestino_NaoLancaExcecoes() {
        assertDoesNotThrow(() -> navio.mudarEstado(noDestino));
        assertDoesNotThrow(() -> noDestino.atualizar(navio, 2.0, new Vetor(5, 5)));
        assertInstanceOf(EstadoNavio.class, noDestino);
    }

    @Test
    void atualizar_NavioNavegando_AlteraPosicaoGeometrica() {
        Route rota = new Route(List.of(new Ponto(0, 0), new Ponto(20, 20)));
        navio.receberRota(rota);

        EstadoNavio navegando = new NavioNavegando();
        navio.mudarEstado(navegando);

        navegando.atualizar(navio, 1.0, new Vetor(5, 5));

        Ponto posicaoAtual = navio.getPosicao();

        assertNotNull(posicaoAtual, "A posição não deveria ser nula após o movimento.");
        assertEquals(14.14, posicaoAtual.getX(), 0.01, "A coordenada X deveria refletir o deslocamento da velocidade.");
        assertEquals(14.14, posicaoAtual.getY(), 0.01, "A coordenada Y deveria refletir o deslocamento da velocidade.");
    }

    static class TorreDeControloSAux implements TorreDeControlo {
        @Override
        public void atualizarRota(Navio navio) {
        }

        @Override
        public void atualizarPosicoes(Navio navio) {
        }

        @Override
        public void libertarNavio(Porto origem, Navio navio) {
        }

        @Override
        public void navioTerminouPercurso(Navio movel) {
        }

        @Override
        public void iniciar(List<Route> rotas, List<Obstaculo> obstaculo) {

        }

        @Override
        public List<Navio> getNavios() {
            return List.of();
        }
    }
}