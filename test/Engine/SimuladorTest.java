package Engine;

import com.sun.source.doctree.VersionTree;
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

        navio = origem.adicionarNavio(2, 2, destino);
        naviosSistema = List.of(navio);
        corrente = new Vetor(-3, 2);

        simulador = new Simulador(corrente, rotas, portos, obstaculos, gestor);
    }

    @Test
    void reiniciarSimulacao(){
        simulador.atualizar(2.0);
        SnapshotSimulacao snapshotAntes = simulador.gerarSnapshot();
        simulador.reiniciarSimulacao();
        SnapshotSimulacao snapshotDepois = simulador.gerarSnapshot();
        assertNotEquals(snapshotAntes,snapshotDepois);
    }

    @Test
    void getSnapshotSimulacao_VerificaNavioEmEspera(){
        SnapshotSimulacao.NavioEmEspera navioEmEspera = new SnapshotSimulacao.NavioEmEspera(navio.getHorarioPartida(),navio.getPortoDestino().getNome(),navio.getVelocidadeLinear());
        SnapshotSimulacao snapshot = simulador.gerarSnapshot();
        assertTrue(snapshot.getNaviosEmEsperaPorPorto().get(origem.getNome()).contains(navioEmEspera));
    }

    @Test
    void getSnapshotSimulacao_VerificaDadosNavio(){
        simulador.atualizar(2.0);
        Ponto posicao = new Ponto(navio.getPosicao().getX(), navio.getPosicao().getY());
        SegmentoReta segAtual = navio.getSegmentoAtual(navio.getPosicao());
        Vetor direcaoRota = new Vetor(segAtual.getB(),segAtual.getA());
        Vetor direcaoContraCorrente = navio.getDirecao(corrente);
        boolean isEmColisao = false;
        SnapshotSimulacao.DadosNavio dados = new SnapshotSimulacao.DadosNavio(posicao,direcaoContraCorrente,direcaoRota,isEmColisao,navio.getArea().getRaio());
        SnapshotSimulacao snapshot = simulador.gerarSnapshot();
        assertTrue(snapshot.getDadosNavios().contains(dados));

    }

    @Test
    void getSnapshotSimulacao_TempoAcumulado(){
        simulador.atualizar(2.0);
        SnapshotSimulacao snapshotSimulacao = simulador.gerarSnapshot();
        assertEquals(2.0,snapshotSimulacao.getTempoSimulacao());
    }
    @Test
    void setCorrente(){
        assertEquals(new Vetor(-3,2), simulador.getCorrente());

        Vetor novaCorrente = new Vetor(-4,7);
        simulador.setCorrente(novaCorrente);
        assertEquals(novaCorrente, simulador.getCorrente());
    }

    @Test
    void iniciarTest(){
        TorreControloAux torre = new  TorreControloAux();
        Simulador simulador1 = new Simulador(corrente,rotas,portos,obstaculos, torre);
        simulador1.iniciar();
        assertTrue(torre.iniciarChamado);
        assertEquals(rotas,torre.rotas);
        assertEquals(obstaculos,torre.obstaculos);
    }

    @Test
    void deveLibertarNavioQuandoTempoAcumuladoForSuficiente() {
        Ponto p1 = new Ponto(0, 0);
        Porto porto = new Porto("Porto Principal", p1, gestor);

        porto.adicionarNavio(2,5,destino);

        TorreControloAux torre = new TorreControloAux();
        List<Porto> portos = List.of(porto);
        Simulador simulador = new Simulador(new Vetor(1,1), new ArrayList<>(), portos, new ArrayList<>(), torre);

        simulador.atualizar(6);

       assertTrue(torre.libertaMovel);
    }

    @Test
    void deveAtualizarPosicaoDeNavio() {
        MovelAux navioAtivo = new MovelAux();
        TorreControloAux torreAUX= new TorreControloAux();
        torreAUX.libertarMovel(origem,navioAtivo);

        Simulador simulador = new Simulador(new Vetor(1,1), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), torreAUX);

        simulador.atualizar(5.0);
        assertTrue(navioAtivo.atualizouNavio, "O navio ativo deveria ter sido atualizado.");
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


    class TorreControloAux implements TorreDeControlo {

        public boolean iniciarChamado=false;
        public boolean libertaMovel=false;
        List<Route> rotas = null;
        List<Obstaculo> obstaculos = null;
        List<Movel> movels= new  ArrayList<>();
        @Override
        public void atualizarRota(Movel movel) {

        }

        @Override
        public void atualizarPosicoes(Movel movel) {

        }

        @Override
        public void libertarMovel(Porto origem, Movel movel) {
            this.libertaMovel=true;
            this.movels.add(movel);
        }

        @Override
        public void movelTerminouPercurso(Movel movel) {

        }

        @Override
        public void iniciar(List<Route> rotas, List<Obstaculo> obstaculo) {
        this.iniciarChamado=true;
        this.rotas = rotas;
        this.obstaculos = obstaculo;
        }

        @Override
        public List<Movel> getMovels() {
            return this.movels;
        }
    }

    class MovelAux implements Movel {

        public boolean atualizouNavio=false;
        @Override
        public boolean intersect(Movel objeto) {
            return false;
        }

        @Override
        public void mover(double delta, Vetor velocidadeOposta) {

        }

        @Override
        public Ponto getPosicao() {
            return null;
        }

        @Override
        public void atualizar(double delta, Vetor velocidadeOposta) {
        this.atualizouNavio=true;
        }

        @Override
        public Circulo getArea() {
            return null;
        }

        @Override
        public Vetor getDirecao(Vetor velocidadeOposta) {
            return null;
        }

        @Override
        public int compareTo(Movel outro) {
            return 0;
        }

        @Override
        public SegmentoReta getSegmentoAtual(Ponto origem) {
            return null;
        }

        @Override
        public void receberRota(Route rota) {

        }

        @Override
        public void mudarEstado(EstadoMovel estado) {

        }

        @Override
        public Ponto getDestino() {
            return null;
        }

        @Override
        public Object getEstado() {
            return null;
        }

        @Override
        public boolean isEmColisao() {
            return false;
        }

        @Override
        public void setEmColisao(boolean b) {

        }

        @Override
        public TorreDeControlo getTorre() {
            return null;
        }
    }
}