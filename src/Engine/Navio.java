package Engine;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um navio num sistema de navegação.
 * Um navio pode se mover num espaço bidimensional, interagir com outros
 * objetos móveis e atualizar o seu estado com base numa rota, velocidade e tempo.
 * Ele também é monitorado por uma torre de controle marítimo e mantém o seu estado
 * de colisão e deslocamento.
 *
 * @author Acrismede Mendes, Alexandre Guerreiro, Léo Souza
 * @version 11/05/26
 * @inv area != null && area.getRaio() == 1.0
 * @inv codigoViagem != null && !codigoViagem.isEmpty()
 * @inv destino != null
 * @inv navegante != null
 * @inv torre != null
 * @inv estado != null
 * @inv velocidadeLinear > 0
 * @inv horarioPartida >= 0
 * @inv tempoNavegando >= 0
 */
public class Navio implements Movel {
    private final String codigoViagem;
    private final Porto destino;
    private final Navegante navegante;
    private final TorreDeControlo torre;
    private final double velocidadeLinear;
    private final Circulo area;
    private final int horarioPartida;
    private EstadoMovel estado;
    private double tempoNavegando;
    private boolean emColisao;

    /**
     * Construtor da classe Navio. Inicializa um objeto Navio com os parâmetros fornecidos.
     *
     * @param area             O objeto Circulo representando a área do navio.
     *                         Esse valor define a sua posição inicial e raio.
     * @param velocidadeLinear A velocidade linear inicial do navio.
     * @param horario          O horário de partida do navio.
     * @param origem           O porto de origem do navio. Não pode ser nulo.
     * @param destino          O porto de destino do navio. Não pode ser nulo.
     * @param torre            A torre de controle responsável por gerir os navios.
     *                         Não pode ser nula.
     * @pre area != null, velocidadeLinear > 0, horario >= 0, origem != null, destino != null e torre != null
     * @pos O Navio é criado no estado MovelNaOrigem com tempoNavegando a zero.
     */
    public Navio(Circulo area, double velocidadeLinear, int horario, Porto origem, Porto destino, TorreDeControlo torre) {
        this.area = new Circulo(area.getCentro(), area.getRaio());
        this.destino = destino;
        this.codigoViagem = origem.getNome() + horario;
        this.horarioPartida = horario;
        this.velocidadeLinear = velocidadeLinear;
        this.navegante = new Navegante();
        this.estado = new MovelNaOrigem();
        this.torre = torre;
        this.emColisao = false;
    }

    /**
     * Verifica se o navio está em estado de colisão.
     *
     * @return true se o navio estiver em colisão, ou false caso contrário.
     */
    public boolean isEmColisao() {
        return emColisao;
    }

    /**
     * Configura o estado de colisão do navio.
     *
     * @param emColisao um valor booleano que indica se o navio está ou não em colisão.
     *                  true para indicar que o navio está em colisão, false caso contrário.
     * @pos O estado de colisão é atualizado para o valor fornecido.
     */
    public void setEmColisao(boolean emColisao) {
        this.emColisao = emColisao;
    }

    /**
     * Verifica se a área do navio interceta outro círculo fornecido.
     * Dois círculos são considerados como intercetando se a distância entre
     * seus centros for menor ou igual à soma dos seus raios.
     *
     * @param circulo O círculo a ser verificado quanto à interseção com a área do navio.
     *                Não pode ser nulo.
     * @return true se a área do navio intercetar o círculo fornecido,
     * false caso contrário.
     */
    private boolean intersect(Circulo circulo) {
        return this.area.intersect(circulo);
    }

    /**
     * Verifica se a área do navio interceta a área de outro objeto do tipo {@code Movel}.
     * A interseção é determinada com base nas áreas circulares associadas aos objetos.
     *
     * @param objeto O objeto do tipo {@code Movel} cuja área será verificada quanto à interseção
     *               com a área do navio atual. Não pode ser {@code null}.
     * @return {@code true} se a área do navio intercetar a área do objeto fornecido,
     * {@code false} caso contrário.
     * @pre objeto != null
     */
    @Override
    public boolean intersect(Movel objeto) {
        return this.intersect(objeto.getArea());
    }

    /**
     * Move o navio atualizando a sua posição com base no incremento de tempo fornecido
     * e ajusta outros atributos relacionados, tais como o tempo de viagem e a posição.
     *
     * @param delta            O incremento de tempo pelo qual o tempo de navegação é ajustado.
     *                         Representa a duração durante a qual o navio se movimentou.
     * @param velocidadeOposta O vetor de velocidade oposta que afeta o movimento do navio.
     *                         Pode representar forças externas como correntes ou vento.
     * @pre delta > 0 e velocidadeOposta != null
     * @pos O tempo de navegação é incrementado e a posição atualizada no mapa.
     * Notifica a torre se terminar o percurso.
     */
    @Override
    public void mover(double delta, Vetor velocidadeOposta) {
        //Incrementa o tempo apenas quando o navio se move.
        tempoNavegando += delta;

        Ponto novaPosicao = navegante.posicao(this.velocidadeLinear, this.tempoNavegando);
        if (novaPosicao != null) {
            this.area.setCentro(novaPosicao);

            torre.atualizarPosicoes(this);

            double tempoTotalViagem = this.navegante.tempoParaPercorrer(velocidadeLinear);

            if (this.tempoNavegando >= tempoTotalViagem) {
                this.torre.movelTerminouPercurso(this);
            }
        }
    }

    /**
     * Obtém a posição atual do navio no sistema de coordenadas cartesianas.
     * A posição corresponde ao centro da área circular que define o navio.
     *
     * @return um objeto {@code Ponto} representando as coordenadas do centro do navio.
     */
    @Override
    public Ponto getPosicao() {
        return area.getCentro();
    }

    /**
     * Obtém o código da viagem associada ao navio.
     * O código da viagem é uma identificação única que representa uma viagem específica
     * realizada pelo navio.
     *
     * @return uma {@code String} representando o código da viagem do navio.
     */
    public String getCodigoViagem() {
        return this.codigoViagem;
    }

    /**
     * Atualiza o estado do navio com base no incremento de tempo fornecido e
     * no vetor de velocidade oposta.
     *
     * @param delta            O incremento de tempo para o qual o estado deve ser atualizado.
     *                         Deve ser um valor positivo.
     * @param velocidadeOposta O vetor de velocidade oposta que pode influenciar o estado
     *                         e o movimento do navio.
     * @throws IllegalArgumentException Se o valor de delta for menor ou igual a zero.
     * @pre delta > 0 e velocidadeOposta != null
     * @pos Delega a atualização ao estado atual (EstadoMovel).
     */
    public void atualizar(double delta, Vetor velocidadeOposta) {
        if (delta <= 0) {
            throw new IllegalArgumentException("O delta precisa ser um número positivo");
        }

        estado.atualizar(this, delta, velocidadeOposta);
    }

    /**
     * Retorna a área associada ao navio.
     * A área é representada por um objeto do tipo {@code Circulo},
     * que define a posição e o raio do navio no espaço.
     *
     * @return o objeto {@code Circulo} representando a área do navio.
     */
    @Override
    public Circulo getArea() {
        return this.area;
    }

    /**
     * Calcula e retorna o vetor direcional associado ao movimento do navio
     * com base na sua velocidade linear, tempo de navegação e um vetor de
     * velocidade oposta.
     *
     * @param velocidadeOposta O vetor de velocidade oposta que representa forças
     *                         externas, como correntes ou ventos, influenciando
     *                         o movimento do navio. Não pode ser nulo.
     * @return O vetor que indica a direção do movimento do navio agora.
     */
    @Override
    public Vetor getDirecao(Vetor velocidadeOposta) {
        return this.navegante.direcao(this.velocidadeLinear, tempoNavegando, velocidadeOposta);
    }

    /**
     * Altera o estado atual do navio para o estado especificado.
     *
     * @param estado o novo estado que será atribuído ao navio. Deve ser uma instância válida de {@code EstadoNavio}.
     * @pre estado != null
     * @pos O estado interno do navio passa a ser a instância fornecida.
     */
    public void mudarEstado(EstadoMovel estado) {
        this.estado = estado;
    }

    /**
     * Recebe e define uma nova rota para o navio, delegando a mudança de rota
     * para o objeto navegante associado ao navio.
     *
     * @param rota O objeto {@code Route} que representa a nova rota a ser seguida
     *             pelo navio. Não pode ser nulo.
     * @pre rota != null
     * @pos A rota é atualizada no Navegante e o tempoNavegando é reiniciado a zero.
     */
    public void receberRota(Route rota) {
        this.tempoNavegando = 0;
        this.navegante.mudarRota(rota);
    }

    /**
     * Compara a instância atual de {@code Navio} com o objeto {@code Navio} especificado
     * com base no seu código de viagem ({@code codigoViagem}).
     *
     * @param o o objeto {@code Navio} a ser comparado com a instância atual.
     *          Não pode ser nulo.
     * @return um inteiro negativo, zero ou um inteiro positivo conforme o código de viagem deste
     * {@code Navio} seja menor, igual ou maior que o código de viagem do
     * {@code Navio} especificado.
     * @pre o != null
     */
    @Override
    public int compareTo(Movel o) {
        Navio outro = (Navio) o;
        return this.codigoViagem.compareTo(outro.getCodigoViagem());
    }

    /**
     * Obtém os segmentos da rota atualmente associados ao navio.
     * Os segmentos de rota representam trechos definidos que compõem a rota
     * que o navio está seguindo no momento.
     *
     * @return uma lista de objetos {@code SegmentoReta} que representam
     * os segmentos da rota do navio. A lista pode estar vazia caso
     * o navio não tenha uma rota definida.
     */
    public List<SegmentoReta> getSegmentosRota() {
        return navegante.getSegmentos();
    }

    /**
     * Obtém a torre de controle associada ao navio.
     * A torre de controle é responsável por gerenciar rotas, posições e
     * interações com outros navios ou obstáculos.
     *
     * @return o objeto {@code TorreDeControlo} associado ao navio.
     */
    public TorreDeControlo getTorre() {
        return torre;
    }

    /**
     * Obtém o estado atual do navio.
     *
     * @return o estado do navio representado por um objeto {@code EstadoNavio}.
     */
    public EstadoMovel getEstado() {
        return this.estado;
    }

    /**
     * Obtém o ponto do porto de destino associado ao navio.
     *
     * @return o objeto {@code Ponto} que representa o destino do navio.
     */
    public Ponto getDestino() {
        return this.destino.getPosicao();
    }

    /**
     * Obtém o porto de destino associado ao navio.
     *
     * @return o objeto {@code Porto} que representa o destino do navio.
     */
    public Porto getPortoDestino() {
        return this.destino;
    }

    /**
     * Obtém o horário de partida do navio.
     *
     * @return um valor inteiro representando o horário de partida do navio.
     */
    public int getHorarioPartida() {
        return this.horarioPartida;
    }

    /**
     * Obtém a velocidade linear atual do navio.
     * A velocidade linear representa a magnitude da velocidade do navio em unidades de
     * distância por unidade de tempo.
     *
     * @return o valor da velocidade linear do navio como um número do tipo {@code double}.
     */
    public double getVelocidadeLinear() {
        return velocidadeLinear;
    }

    public SegmentoReta getSegmentoAtual() {
        return this.navegante.getSegmentoAtual(tempoNavegando,this.velocidadeLinear);
    }

    public Route getRota() {
        List<Ponto> pontosRota = new ArrayList<>();
        List<SegmentoReta> segmentosRota = this.navegante.getSegmentos();
        if (segmentosRota == null || segmentosRota.isEmpty()) {
            return null;
        }
        pontosRota.add(segmentosRota.getFirst().getA());
        for (SegmentoReta seg : segmentosRota) {
            pontosRota.add(seg.getB());
        }
        return new Route(pontosRota);
    }
}
