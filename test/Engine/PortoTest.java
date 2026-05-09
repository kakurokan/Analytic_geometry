package Engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PortoTest {

    private Porto portoOrigem, portoDestino;

    @BeforeEach
    void setUp() {
        TorreDeControloAux torre = new TorreDeControloAux();
        portoOrigem = new Porto("Porto de Lisboa", new Ponto(0, 0), torre);
        portoDestino = new Porto("Porto de Faro", new Ponto(10, 10), torre);
    }

    @Test
    void naviosProntos_PortoSemNavios_RetornaIteradorVazio() {
        Iterator<Navio> it = portoOrigem.naviosProntos(10.0);

        assertFalse(it.hasNext(), "O hasNext deve ser false quando não há navios na fila.");
        assertNull(it.next(), "O next deve retornar null quando a fila está vazia.");
    }

    @Test
    void adicionarNavio_ParametrosValidos_CriaERegistaNovoNavio() {
        Navio navio = portoOrigem.adicionarNavio(20, 10, portoDestino);

        assertNotNull(navio, "O método deve criar e retornar uma instância válida de Navio.");
    }

    @Test
    void getPosicao_PortoInstanciado_RetornaPosicaoCorreta() {
        Ponto posicaoEsperada = new Ponto(0, 0);

        assertEquals(posicaoEsperada, portoOrigem.getPosicao(), "O porto deve retornar a posição com que foi instanciado.");
    }

    @Test
    void getNome_PortoInstanciado_RetornaNomeCorreto() {
        String nomeEsperado = "Porto de Lisboa";

        assertEquals(nomeEsperado, portoOrigem.getNome(), "O porto deve retornar o nome com que foi instanciado.");
    }

    static class TorreDeControloAux implements TorreDeControlo {

        @Override
        public void atualizarRota(Movel movel) {

        }

        @Override
        public void atualizarPosicoes(Movel movel) {

        }

        @Override
        public void libertarMovel(Porto origem, Movel movel) {

        }

        @Override
        public void movelTerminouPercurso(Movel movel) {

        }

        @Override
        public void iniciar(List<Route> rotas, List<Obstaculo> obstaculo) {

        }

        @Override
        public List<Movel> getMovels() {
            return List.of();
        }
    }}