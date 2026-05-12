package Engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NavioTest {

    private Porto origem;
    private Porto destino;
    private Navio navio,navio2;
    private TorreDeControlo torre;

    @BeforeEach
    void setUp() {
        Route melhorRotaEsperada = new Route(List.of(
                new Ponto(0, 0), new Ponto(1, 1), new Ponto(3, 2), new Ponto(3, 5)
        ));
        List<Route> rotas = List.of(melhorRotaEsperada);
        List<Obstaculo> obstaculos = new ArrayList<>();


        torre = new GestorMaritimo();
        torre.iniciar(rotas, obstaculos);
        origem = new Porto("Porto de Lisboa", new Ponto(0, 0), torre);
        destino = new Porto("Porto de Faro", new Ponto(3, 5), torre);

        // Navio principal usado na maioria dos testes
        navio = origem.adicionarNavio(5, 2, destino);
        navio2 = origem.adicionarNavio(5,3, destino);
    }

    @Test
    void isEmColisaoTrue(){
        torre.libertarMovel(origem,navio);
        torre.libertarMovel(origem,navio2);
        navio.mover(0.1,new Vetor(1,1));
        navio2.mover(0.05, new Vetor(1,1));
        assertTrue(navio.isEmColisao());
    }

    @Test
    void isEmColisaoFalse(){
        torre.libertarMovel(origem,navio);
        torre.libertarMovel(origem,navio2);
        navio.mover(0.6,new Vetor(1,1));
        navio2.mover(0.05, new Vetor(1,1));
        assertFalse(navio.isEmColisao());
    }

    @Test
    void getDirecao(){
        torre.libertarMovel(origem,navio);
        assertEquals(new Vetor(2.5355339059,2.5355339059),navio.getDirecao(new Vetor(1,1)));
    }

    @Test
    void compareTo(){
        assertEquals(-1,navio.compareTo(navio2));

        assertEquals(-1,navio2.compareTo(navio));
    }
    @Test
    void intersect_ComNavioNaMesmaArea_RetornaTrue() {
        // Cria um navio numa posição que interseta o navio principal na origem
        Navio navioInterseta = new Navio(new Circulo(new Ponto(1, 1), 1), 20, 5, origem, destino, torre);

        assertTrue(navio.intersect(navioInterseta), "Os navios deveriam intersetar-se devido à proximidade das suas áreas.");
    }


    @Test
    void intersect_ComNavioDistante_RetornaFalse() {
        // Cria um navio muito longe
        Navio navioDistante = new Navio(new Circulo(new Ponto(100, 100), 1), 20, 5, origem, destino, torre);

        assertFalse(navio.intersect(navioDistante), "Os navios não deveriam intersetar-se pois estão longe um do outro.");
    }

    @Test
    void mover_ComDeltaPositivo_AtualizaPosicao() {
        double delta = 1;
        torre.libertarMovel(origem, navio);
        Ponto posicaoInicial = navio.getPosicao();

        navio.mover(delta, new Vetor(2, 2));
        Ponto posicaoAtual = navio.getPosicao();

        assertNotEquals(posicaoInicial, posicaoAtual, "O navio deveria ter saído da posição inicial após invocar mover().");
    }

    @Test
    void atualizar_ComTempoNegativo_LancaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> navio.atualizar(-1, new Vetor(5, 5)));
    }

    @Test
    void atualizar_DelegaAoEstadoAtual() {
        // Criamos um estado falso para ver se o navio o chama corretamente
        class EstadoNavioTemp implements EstadoMovel {
            boolean foiChamado = false;
            double deltaRecebido = 0;
            Navio navioRecebido = null;

            @Override
            public void atualizar(Movel movel, double delta, Vetor velocidadeCorrente) {
                this.foiChamado = true;
                this.navioRecebido = (Navio) movel;
                this.deltaRecebido = delta;

            }
        }
        EstadoNavioTemp estadoMock = new EstadoNavioTemp();
        navio.mudarEstado(estadoMock);

        navio.atualizar(5, new Vetor(5, 5));

        assertTrue(estadoMock.foiChamado, "O método atualizar do estado não foi invocado.");
        assertEquals(navio, estadoMock.navioRecebido, "A referência do navio passada ao estado está incorreta.");
        assertEquals(5.0, estadoMock.deltaRecebido, "O delta de tempo passado ao estado está incorreto.");
    }

    @Test
    void mudarEstado_NovoEstadoDefinido_AlteraOEstadoDoNavio() {
        EstadoMovel novoEstado = new MovelNoDestino();

        navio.mudarEstado(novoEstado);

        assertInstanceOf(MovelNoDestino.class, navio.getEstado(), "O estado do navio não foi atualizado corretamente.");
    }

    @Test
    void receberRota_RotaValida_AtualizaSegmentosDaRotaDoNavio() {
        Route novaRota = new Route(List.of(
                new Ponto(0, 0), new Ponto(1, 1), new Ponto(3, 2), new Ponto(3, 5)
        ));

        navio.receberRota(novaRota);

        assertEquals(novaRota.getSegmentos(), navio.getSegmentosRota(), "Os segmentos de rota do navio deveriam coincidir com a nova rota fornecida.");
    }

    @Test
    void compareTo_ComparaPorCodigoDeViagem_RetornaValorCorreto() {
        Navio navioComCodigoMaior = new Navio(new Circulo(new Ponto(1, 1), 1), 20, 5, origem, destino, torre);

        assertTrue(navio.compareTo(navioComCodigoMaior) < 0, "A comparação deveria ser feita através da ordem lexicográfica do código de viagem.");
    }

    @Test
    void getCodigoViagem_InstanciadoViaPorto_RetornaNomeDoPortoEHorario() {
        assertEquals("Porto de Lisboa2", navio.getCodigoViagem());
    }

    @Test
    void getEstado_NavioRecemCriado_RetornaNavioNaOrigem() {
        assertInstanceOf(MovelNaOrigem.class, navio.getEstado(), "Um navio recém-criado deve ter o estado NavioNaOrigem.");
    }

    @Test
    void getDestino_RetornaPortoDeDestinoCorreto() {
        assertEquals(destino.getPosicao(), navio.getDestino());
    }

    @Test
    void getHorarioPartida_RetornaHorarioAtribuido() {
        assertEquals(2, navio.getHorarioPartida());
    }

    @Test
    void getArea_RetornaCirculoComRaioEPosicaoCorretos() {
        Circulo areaRetornada = navio.getArea();

        assertEquals(1.0, areaRetornada.getRaio(), "O raio da área gerado pelo Porto deveria ser 1.");
        assertEquals(navio.getPosicao(), areaRetornada.getCentro(), "O centro da área deve ser idêntico à posição do navio.");
    }

}