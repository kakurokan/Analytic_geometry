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
     * Verifica se o objeto atual do tipo {@code Movel} intercepta outro objeto
     * do tipo {@code Movel} fornecido como argumento.
     *
     * @param objeto O objeto do tipo {@code Movel} cuja interseção será verificada
     *               em relação ao objeto atual. Não pode ser {@code null}.
     * @return {@code true} se os objetos {@code Movel} se interceptarem,
     * {@code false} caso contrário.
     */
    public boolean intersect(Movel objeto);

    /**
     * Move o objeto em um espaço bidimensional com base no intervalo de tempo e em
     * uma velocidade oposta fornecida.
     *
     * @param delta            O intervalo de tempo em segundos utilizado para calcular o deslocamento.
     *                         Deve ser um valor positivo.
     * @param velocidadeOposta O vetor de velocidade oposta ao movimento que será considerado no cálculo
     *                         do movimento. Não pode ser {@code null}.
     */
    public void mover(double delta, Vetor velocidadeOposta);

    /**
     * Recupera a posição atual do objeto no espaço bidimensional.
     *
     * @return Um objeto {@code Ponto} que representa a posição atual do objeto.
     */
    public Ponto getPosicao();

    /**
     * Atualiza o estado do objeto com base no intervalo de tempo e numa velocidade oposta fornecida.
     *
     * @param delta            O intervalo de tempo em segundos utilizado para atualizar o estado do objeto.
     *                         Deve ser um valor positivo.
     * @param velocidadeOposta O vetor representando a velocidade oposta ao movimento atual.
     *                         Não pode ser {@code null}.
     */
    public void atualizar(double delta, Vetor velocidadeOposta);

    /**
     * Retorna a área associada ao objeto no formato de um círculo.
     * O círculo representa a área ocupada pelo objeto no espaço bidimensional.
     *
     * @return um objeto {@code Circulo} que define a área associada ao objeto.
     */
    public Circulo getArea();

    /**
     * Calcula e retorna a direção do movimento com base num vetor de velocidade oposta.
     *
     * @param velocidadeOposta O vetor representando a velocidade oposta que será utilizado
     *                         para determinar a direção. Não pode ser {@code null}.
     * @return Um objeto {@code Vetor} que representa a direção do movimento calculada.
     */
    public Vetor getDirecao(Vetor velocidadeOposta);
}