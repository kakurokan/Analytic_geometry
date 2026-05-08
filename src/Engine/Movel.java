package Engine;

/**
 * Interface que representa um objeto móvel num espaço bidimensional, capaz de
 * executar várias operações como verificar interseções, atualizar o seu
 * estado e mover-se com base em parâmetros fornecidos.
 *
 * @author Acrismede Mendes, Alexandre Guerreiro, Léo Souza
 * @version 11/05/26
 */
public interface Movel {
    /**
     * Verifica se o objeto atual do tipo {@code Movel} interceta outro objeto
     * do tipo {@code Movel} fornecido como argumento.
     *
     * @param objeto O objeto do tipo {@code Movel} cuja interseção será verificada
     *               em relação ao objeto atual. Não pode ser {@code null}.
     * @return {@code true} se os objetos {@code Movel} se intercetarem,
     * {@code false} caso contrário.
     */
    boolean intersect(Movel objeto);

    /**
     * Move o objeto num espaço bidimensional com base no intervalo de tempo e em
     * uma velocidade oposta fornecida.
     *
     * @param delta            O intervalo de tempo em segundos utilizado para calcular o deslocamento.
     *                         Deve ser um valor positivo.
     * @param velocidadeOposta O vetor de velocidade oposta ao movimento que será considerado no cálculo
     *                         do movimento. Não pode ser {@code null}.
     */
    void mover(double delta, Vetor velocidadeOposta);

    /**
     * Recupera a posição atual do objeto no espaço bidimensional.
     *
     * @return Um objeto {@code Ponto} que representa a posição atual do objeto.
     */
    Ponto getPosicao();

    /**
     * Atualiza o estado do objeto com base no intervalo de tempo e numa velocidade oposta fornecida.
     *
     * @param delta            O intervalo de tempo em segundos utilizado para atualizar o estado do objeto.
     *                         Deve ser um valor positivo.
     * @param velocidadeOposta O vetor representando a velocidade oposta ao movimento atual.
     *                         Não pode ser {@code null}.
     */
    void atualizar(double delta, Vetor velocidadeOposta);

    /**
     * Retorna a área associada ao objeto no formato de um círculo.
     * O círculo representa a área ocupada pelo objeto no espaço bidimensional.
     *
     * @return um objeto {@code Circulo} que define a área associada ao objeto.
     */
    Circulo getArea();

    /**
     * Calcula e retorna a direção do movimento com base num vetor de velocidade oposta.
     *
     * @param velocidadeOposta O vetor representando a velocidade oposta que será utilizado
     *                         para determinar a direção. Não pode ser {@code null}.
     * @return Um objeto {@code Vetor} que representa a direção do movimento calculada.
     */
    Vetor getDirecao(Vetor velocidadeOposta);

    /**
     * Altera o estado de colisão do objeto móvel.
     * * @param b {@code true} para indicar que o objeto está em colisão, {@code false} caso contrário.
     */
    void setEmColisao(boolean b);

    /**
     * Compara o objeto móvel atual com outro objeto móvel para estabelecer
     * uma ordem de prioridade (útil para regras de desempate em cruzamentos ou colisões).
     * * @param outro O objeto do tipo {@code Movel} a ser comparado. Não pode ser {@code null}.
     *
     * @return um valor negativo, zero ou um valor positivo consoante a prioridade deste
     * objeto seja menor, igual ou maior que a do objeto especificado.
     */
    int compareTo(Movel outro);

    /**
     * Obtém o segmento de reta da rota onde o objeto móvel se encontra atualmente,
     * com base numa posição de origem especificada.
     * * @param origem O {@code Ponto} que representa a posição atual ou base de referência.
     *
     * @return O objeto {@code SegmentoReta} em que o móvel se encontra, ou {@code null}
     * se não estiver associado a nenhum segmento reconhecido.
     */
    SegmentoReta getSegmentoAtual(Ponto origem);

    /**
     * Atribui uma nova rota que ditará a trajetória do objeto móvel.
     * * @param rota O objeto {@code Route} que representa o novo percurso. Não pode ser {@code null}.
     */
    void receberRota(Route rota);

    /**
     * Altera o estado comportamental atual do objeto móvel no seu ciclo de vida
     * (por exemplo: na origem, a navegar, a aguardar, no destino).
     * * @param estado A instância de {@code EstadoNavio} que representa o novo estado a assumir.
     */
    void mudarEstado(EstadoNavio estado);

    /**
     * Obtém a localização de destino do objeto móvel.
     * * @return O {@code Ponto} que representa a meta ou o destino planeado do objeto.
     */
    Ponto getDestino();

    /**
     * Recupera o estado comportamental atual do objeto móvel.
     * * @return Um {@code Object} (geralmente uma implementação do estado correspondente)
     * que representa a situação ou fase atual do objeto.
     */
    Object getEstado();
}