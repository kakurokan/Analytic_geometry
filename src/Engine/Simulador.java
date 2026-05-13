package Engine;

import java.util.*;

/**
 * A classe Simulador representa um sistema de simulação para navegação marítima que gere
 * rotas, portos, obstáculos, navios e uma torre de controlo para coordenação. Permite a
 * simulação de movimentos de navios, eventos meteorológicos e outras operações marítimas ao longo do tempo.
 *
 * @author Acrismede Mendes, Alexandre Guerreiro, Léo Souza
 * @version 11/05/26
 * @inv corrente != null, rotas != null, portos != null, obstaculos != null, torreDeControlo != null, tempoAcumulado >= 0
 */
public class Simulador {
    private static final double MAX_RAIO_TEMPESTADE = 1.7;
    private static final double MIN_RAIO_TEMPESTADE = 0.5;
    private final List<Route> rotas;
    private final List<Porto> portos;
    private final List<Obstaculo> obstaculos;
    private final TorreDeControlo torreDeControlo;
    private Vetor corrente;
    private double tempoAcumulado;

    /**
     * Constrói um objeto Simulador com os parâmetros de simulação especificados.
     *
     * @param corrente        O vetor que representa o fluxo de corrente na simulação.
     * @param rotas           A lista de rotas disponíveis na simulação.
     * @param portos          A lista de portos incluídos na simulação.
     * @param obstaculo       A lista de obstáculos presentes no ambiente da simulação.
     * @param torreDeControlo A torre de controlo responsável por gerir a simulação.
     * @pre corrente != null, rotas != null, portos != null, obstaculo != null e torreDeControlo != null
     * @pos O simulador é inicializado com os parâmetros fornecidos e o tempo acumulado começa a zero.
     */
    public Simulador(Vetor corrente, List<Route> rotas, List<Porto> portos, List<Obstaculo> obstaculo, TorreDeControlo torreDeControlo) {
        this.corrente = corrente;
        this.rotas = rotas;
        this.portos = portos;
        this.obstaculos = obstaculo;
        this.torreDeControlo = torreDeControlo;
        this.tempoAcumulado = 0;
    }

    /**
     * Define o vetor que representa o fluxo de corrente na simulação.
     *
     * @param corrente O vetor que representa o fluxo de corrente a ser configurado.
     * @pre corrente != null
     * @pos O fluxo de corrente do simulador é atualizado para o vetor fornecido.
     */
    public void setCorrente(Vetor corrente) {
        this.corrente = corrente;
    }

    public Vetor getCorrente() {
        return this.corrente;
    }

    /**
     * Inicia a simulação utilizando as rotas e obstáculos previamente definidos.
     * <p>
     * Este método delega o início da simulação à torre de controlo associada,
     * fornecendo a lista de rotas e obstáculos configurados na instância atual.
     * @pos A torre de controlo é inicializada com as rotas e obstáculos do simulador.
     */
    public void iniciar() {
        torreDeControlo.iniciar(this.rotas, this.obstaculos);
    }

    /**
     * Atualiza o estado da simulação com base no tempo decorrido e interage com os portos,
     * navios e a torre de controlo para refletir as mudanças ocorridas no ambiente simulado.
     *
     * @param delta O intervalo de tempo (em unidades de tempo de simulação) que deve ser adicionado ao tempo acumulado.
     * @pre delta > 0
     * @pos O tempo acumulado é incrementado, navios prontos são libertados dos portos
     * e todos os navios ativos são atualizados no espaço.
     */
    public void atualizar(double delta) {
        tempoAcumulado += delta;

        for (Porto porto : portos) {
            Iterator<Navio> naviosProntos = porto.naviosProntos(tempoAcumulado);
            while (naviosProntos.hasNext()) {
                Navio navio = naviosProntos.next();

                torreDeControlo.libertarMovel(porto, navio);
            }
        }

        List<Movel> navios = torreDeControlo.getMovels();
        for (Movel navio : new ArrayList<>(navios)) {
            navio.atualizar(delta, this.corrente);
        }
    }

    /**
     * Cria e posiciona uma nova instância de {@code Tempestade} em uma localização segura
     * dentro da simulação, garantindo que ela não interfira com os portos existentes.
     * A localização e o raio da tempestade são gerados aleatoriamente, dentro de
     * limites pré-definidos.
     * <p>
     * Durante a criação, o método verifica se a área ocupada pela nova tempestade
     * está a uma distância segura de todos os portos. Caso contrário, um novo
     * posicionamento e raio são recalculados até que a posição seja considerada segura.
     * <p>
     * Após a criação, a tempestade é adicionada à lista de obstáculos gerenciados pela simulação.
     *
     * @return A nova instância de {@code Tempestade} criada e posicionada.
     * @pos A tempestade gerada é adicionada à lista interna de obstáculos da simulação.
     */
    public Tempestade criarTempestade() {
        Random rand = new Random();
        Tempestade tempestade = null;
        boolean posicaoSegura = false;
        double margem = 0.5;

        while (!posicaoSegura) {
            double raioAleatorio = MIN_RAIO_TEMPESTADE + rand.nextDouble() * (MAX_RAIO_TEMPESTADE - MIN_RAIO_TEMPESTADE);

            Route rotaAleatoria = rotas.get(rand.nextInt(rotas.size()));
            List<SegmentoReta> segmentosRotaAleatoria = rotaAleatoria.getSegmentos();
            SegmentoReta segmentoAleatorio = segmentosRotaAleatoria.get(rand.nextInt(segmentosRotaAleatoria.size()));

            Vetor direcaoSegmento = new Vetor(segmentoAleatorio.getA(), segmentoAleatorio.getB());

            double t = rand.nextDouble();

            double x = segmentoAleatorio.getA().getX() + direcaoSegmento.getX() * t;
            double y = segmentoAleatorio.getA().getY() + direcaoSegmento.getY() * t;

            Ponto centroAleatorio = new Ponto(x, y);

            posicaoSegura = true;

            for (Porto porto : portos) {
                if (porto.getPosicao().distanciaPara(centroAleatorio) <= raioAleatorio + margem) {
                    posicaoSegura = false;
                    break;
                }
            }

            if (posicaoSegura) {
                Circulo area = new Circulo(centroAleatorio, raioAleatorio);
                tempestade = new Tempestade(area);
            }
        }

        this.obstaculos.add(tempestade);
        return tempestade;
    }

    /**
     * Retorna a lista de obstáculos presentes no ambiente da simulação.
     *
     * @return Uma lista de objetos do tipo {@code Obstaculo}, representando os obstáculos
     * configurados no contexto da simulação.
     */
    public List<Obstaculo> getObstaculos() {
        return obstaculos;
    }

    /**
     * Gera um snapshot do estado atual da simulação, contendo informações sobre os
     * navios em espera por porto, os dados dos navios ativos e o tempo acumulado da simulação.
     *
     * @return Uma instância de {@code SnapshotSimulacao} que encapsula o estado atual da simulação,
     * incluindo a configuração dos portos e navios registrados no momento da chamada.
     */
    public SnapshotSimulacao gerarSnapshot() {
        Map<String, List<SnapshotSimulacao.NavioEmEspera>> filaPorPorto = new HashMap<>();
        for (Porto porto : portos) {
            filaPorPorto.put(porto.getNome(), porto.getNaviosEmEspera());
        }

        List<Movel> navios = torreDeControlo.getMovels();
        List<SnapshotSimulacao.DadosNavio> dadosNavios = new ArrayList<>();

        for (Movel navio : navios) {
            SegmentoReta seg = navio.getSegmentoAtual(navio.getPosicao());
            Vetor direcaoRota = (seg != null) ? new Vetor(seg.getA(), seg.getB()) : null;

            dadosNavios.add(new SnapshotSimulacao.DadosNavio(
                    navio.getPosicao(),
                    navio.getDirecao(this.corrente),
                    direcaoRota,
                    navio.isEmColisao(),
                    navio.getArea().getRaio()
            ));
        }

        return new SnapshotSimulacao(filaPorPorto, dadosNavios, this.tempoAcumulado);
    }

    /**
     * Reinicia o estado da simulação, removendo as tempestades existentes
     * e criando instâncias de tempestades aleatórias. Como parte do
     * processo de reinicialização, o tempo acumulado é zerado, as filas de
     * portos são esvaziadas e a torre de controlo é reiniciada com as rotas
     * e obstáculos atualizados.
     *
     * @return Uma lista contendo as novas instâncias de {@code Tempestade}
     * criadas e posicionadas no ambiente da simulação.
     * @pos O tempo acumulado volta a zero, as filas dos portos são limpas,
     * as tempestades antigas são removidas da lista de obstáculos,
     * novas tempestades são adicionadas e a torre é reiniciada.
     */
    public List<Tempestade> reiniciarSimulacao() {
        this.tempoAcumulado = 0;

        for (Porto porto : portos) {
            porto.limparFila();
        }

        // Remover apenas as tempestades
        this.obstaculos.removeIf(obs -> obs instanceof Tempestade);

        // Cria tempestades aleatórias
        List<Tempestade> novasTempestades = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            novasTempestades.add(criarTempestade());
        }

        this.torreDeControlo.iniciar(this.rotas, this.obstaculos);

        return novasTempestades;
    }
}
