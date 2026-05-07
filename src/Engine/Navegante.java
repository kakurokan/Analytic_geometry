package Engine;

import java.util.ArrayList;
import java.util.List;

/**
 * A classe {@code Engine.Navegante} é responsável por realizar cálculos relacionados à navegação
 * numa rota composta por segmentos de reta. Os cálculos incluem a determinação da
 * velocidade em cada segmento considerando a interferência de um vetor de vento, o tempo
 * total necessário para percorrer a rota e a posição em função de uma velocidade constante
 * e o tempo transcorrido.
 *
 * @author Léo Souza
 * @version 01/04/26
 */
public class Navegante {
    private List<SegmentoReta> segmentos = new ArrayList<>();

    /**
     * Constrói uma instância padrão de {@code Navegante} sem inicializar uma rota associada.
     * <p>
     * Este construtor cria uma instância de {@code Navegante} com uma lista de segmentos vazia.
     * A rota e seus segmentos podem ser atribuídos posteriormente utilizando métodos disponíveis
     * na classe.
     */
    public Navegante() {
    }

    /**
     * Constrói uma instância de Engine.Navegante utilizando a rota fornecida.
     *
     * @param rota a rota contendo os segmentos para inicializar esta instância de Engine.Navegante
     * @pre rota != null e rota.getSegmentos() != null
     */
    public Navegante(Route rota) {
        mudarRota(rota);
    }

    /**
     * Altera a rota associada à instância do objeto, substituindo os segmentos
     * existentes pelos segmentos da nova rota fornecida.
     *
     * @param rota A nova rota contendo os segmentos a serem associados à instância.
     *             Deve ser um objeto {@code Engine.Route} válido cuja lista de segmentos
     *             não seja nula.
     * @pre rota != null e rota.getSegmentos() != null
     */
    public void mudarRota(Route rota) {
        this.segmentos = rota.getSegmentos();
    }

    /**
     * Calcula a velocidade por segmento de uma rota, considerando a velocidade do vento
     * e uma velocidade linear constante. Para cada segmento da rota, é calculado o tempo
     * requerido para atravessá-lo e, com base nesses parâmetros, determina-se a velocidade
     * resultante para o segmento.
     *
     * @param velocidadeCorrente Um objeto {@code Engine.Vetor} representando a velocidade do vento.
     *                           Não pode ser {@code null}.
     * @param velocidadeLinear   A velocidade linear constante utilizada para o cálculo.
     *                           Deve ser um valor positivo maior que zero.
     * @return Uma lista de objetos {@code Engine.Vetor} representando a velocidade em cada
     * segmento da rota, considerando os efeitos da velocidade do vento.
     * A lista estará vazia se não houver segmentos na rota.
     * @pre velocidadeLinear > 0
     */
    public List<Vetor> velocidadePorSegmento(Vetor velocidadeCorrente, double velocidadeLinear) {
        List<Vetor> velocidades = new ArrayList<>();
        for (SegmentoReta seg : segmentos) {
            AutoPilot ap = new AutoPilot(seg.getA(), seg.getB());
            double time = ap.time(velocidadeLinear);
            velocidades.add(ap.speed(velocidadeCorrente, time));
        }
        return velocidades;
    }

    /**
     * Calcula o tempo total necessário para percorrer a rota representada pelos segmentos,
     * considerando uma velocidade linear constante.
     * <p>
     * O método utiliza a sequência de segmentos da rota e, para cada um deles, calcula o tempo
     * requerido para atravessá-lo com base na velocidade linear fornecida. O tempo total é a soma
     * dos tempos necessários para percorrer todos os segmentos consecutivos.
     *
     * @param linearSpeed A velocidade linear constante utilizada para calcular o tempo de percurso.
     *                    Deve ser um valor positivo maior que zero.
     * @return O tempo total necessário para percorrer a rota em segundos. Se a lista de segmentos
     * estiver vazia, o retorno será 0.
     */
    public double tempoParaPercorrer(double linearSpeed) {
        double resultado = 0;
        for (SegmentoReta seg : segmentos) {
            AutoPilot ap = new AutoPilot(seg.getA(), seg.getB());
            resultado += ap.time(linearSpeed);
        }
        return resultado;
    }

    /**
     * Calcula a posição numa sequência de segmentos a partir de uma velocidade linear
     * constante e um tempo transcorrido.
     * <p>
     * A posição é determinada percorrendo os segmentos sequencialmente, calculando a
     * distância a percorrer em cada segmento até que o tempo informado seja atingido.
     * Se o tempo transcorrido resultar numa posição além do último segmento,
     * a posição final será o ponto final do último segmento.
     *
     * @param velocidadeLinear A velocidade linear constante em unidades de comprimento por unidade
     *                         de tempo. Deve ser um valor positivo maior que zero.
     * @param tempo            O tempo transcorrido pelo movimento ao longo dos segmentos.
     *                         Deve ser um valor positivo maior ou igual a zero.
     * @return A posição representada por um ponto 2D no sistema de coordenadas cartesianas
     * correspondente ao local alcançado após o movimento. Se os segmentos estiverem
     * vazios, retorna null.
     * @pre tempo > 0 e velocidadeLinear > 0
     */
    public Ponto posicao(double velocidadeLinear, double tempo) {
        //o array é preenchido dentro da função
        double[] percorrido = new double[1];

        int segIndice = getIndiceSegmentoAtual(velocidadeLinear, tempo, percorrido);
        SegmentoReta seg = this.segmentos.get(segIndice);

        Vetor r = new Vetor(seg.getA(), seg.getB());
        double percorreNoSegmento = percorrido[0] / r.modulo();

        // Se a percentagem a percorrer for tão pequena que vai criar um vetor (0,0), ignoramos
        if (percorreNoSegmento < 0.0001) {
            return seg.getA();
        }

        Vetor deslocamento = r.multi(percorreNoSegmento);

        Ponto inicio = seg.getA();

        return new Ponto(inicio.getX() + deslocamento.getX(),
                inicio.getY() + deslocamento.getY());
    }

    /**
     * Calcula a direção do movimento em função da velocidade linear atual, tempo transcorrido
     * e a velocidade corrente num determinado ponto da rota.
     *
     * @param velocidadeLinear   A velocidade linear constante em unidades de comprimento por unidade
     *                           de tempo. Deve ser um valor positivo maior que zero.
     * @param tempo              O tempo transcorrido em unidades de tempo. Deve ser um valor não negativo.
     * @param velocidadeCorrente Um vetor representando a velocidade atual do movimento.
     *                           Não pode ser {@code null}.
     * @return Um vetor representando a direção do movimento no segmento correspondente,
     * considerando os parâmetros fornecidos.
     */
    public Vetor direcao(double velocidadeLinear, double tempo, Vetor velocidadeCorrente) {
        int segIndice = getIndiceSegmentoAtual(velocidadeLinear, tempo, new double[1]);

        List<Vetor> velocidades = velocidadePorSegmento(velocidadeCorrente, velocidadeLinear);

        return velocidades.get(segIndice);
    }

    /**
     * Determina o índice do segmento atual sendo percorrido com base numa velocidade
     * linear e tempo decorrido fornecidos. Também calcula a distância percorrida
     * dentro do segmento atual.
     *
     * @param velocidadeLinear A velocidade linear em unidades de distância por unidade de tempo.
     *                         Deve ser um valor positivo maior que zero.
     * @param tempo            O tempo decorrido para o movimento ao longo dos segmentos.
     *                         Deve ser um valor positivo maior ou igual a zero.
     * @param percorrido       Um array de tamanho 1 utilizado para armazenar a distância percorrida
     *                         dentro do segmento atual após a conclusão do método.
     *                         Não pode ser nulo.
     * @return O índice do segmento atual sendo percorrido. Se a distância total
     * percorrida exceder o comprimento cumulativo de todos os segmentos, o índice do
     * último segmento é retornado.
     */
    private int getIndiceSegmentoAtual(double velocidadeLinear, double tempo, double[] percorrido) {
        double percorrer = velocidadeLinear * tempo;

        for (int i = 0; i < this.segmentos.size(); i++) {
            SegmentoReta seg = this.segmentos.get(i);
            Vetor r = new Vetor(seg.getA(), seg.getB());
            double distancia = r.modulo();

            if (percorrer <= distancia) {
                percorrido[0] = percorrer;
                return i;
            }

            percorrer -= distancia;
        }

        SegmentoReta ultimo = this.segmentos.getLast();
        percorrido[0] = new Vetor(ultimo.getA(), ultimo.getB()).modulo();
        return this.segmentos.size() - 1;
    }

    /**
     * Obtém a lista de segmentos associados à instância de Navegante.
     *
     * @return Uma lista contendo os segmentos da rota representados por
     * objetos do tipo {@code SegmentoReta}. Se não houver segmentos
     * associados, retorna uma lista vazia.
     */
    public List<SegmentoReta> getSegmentos() {
        return segmentos;
    }

    /**
     * Retorna o segmento atual da lista de segmentos em que o ponto fornecido está localizado.
     * O método verifica se o ponto pertence a algum dos segmentos da lista e retorna o
     * segmento correspondente.
     *
     * @param posicao A posição representada por um ponto que será verificado relativamente
     *                aos segmentos. Não pode ser nulo.
     * @return O segmento correspondente ao ponto fornecido, ou {@code null} se o ponto não
     * pertencer a nenhum segmento.
     */
    public SegmentoReta getSegmentoAtual(Ponto posicao) {
        for (SegmentoReta seg : segmentos) {
            if (seg.noSegmento(posicao)) return seg;
        }
        return null;
    }
}
