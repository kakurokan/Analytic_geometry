package Engine;

/**
 * Representa o estado de um navio no seu ciclo de vida. Esta ‘interface’ define um comportamento
 * que permite a atualização de um navio com base no seu estado atual, tempo decorrido e
 * velocidade atual.
 *
 * <p>Diferentes implementações desta ‘interface’ representam estados específicos nos quais um navio pode estar,
 * como estar na sua origem, navegando, aguardando ou chegando ao seu destino.</p>
 *
 * <p>Cada estado determina como o navio interage com o seu ambiente, incluindo a verificação
 * de colisões, solicitação de atualizações de posição à torre de controle e a transição
 * para outros estados.</p>
 *
 * @author Acrismede Mendes, Alexandre Guerreiro, Léo Souza
 * @version 11/05/26
 * @see <a href="https://refactoring.guru/pt-br/design-patterns/state">Padrão de design</a>
 */
public interface EstadoNavio {
    /**
     * Atualiza o estado de um navio considerando o tempo decorrido e a velocidade da corrente.
     *
     * @param navio              o objeto Navio que será atualizado.
     * @param delta              o tempo decorrido desde a última atualização, especificado em segundos.
     * @param velocidadeCorrente o vetor representando a velocidade da corrente na qual o navio está inserido.
     */
    void atualizar(Navio navio, double delta, Vetor velocidadeCorrente);
}
