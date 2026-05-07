package Engine;

/**
 * Representa o estado de um navio enquanto está navegando.
 * <p>
 * Esta classe implementa a interface EstadoNavio e define o comportamento de um navio
 * em movimento pela água sob a influência de um vetor de corrente e do tempo decorrido.
 * Enquanto o navio está neste estado, ele será constantemente movido conforme os
 * cálculos baseados na velocidade corrente e no tempo decorrido.
 * <p>
 * Responsabilidades desta classe incluem a interação com o objeto Navio e a invocação
 * da sua ação de movimento, que calcula a próxima posição baseada nos parâmetros fornecidos.
 *
 * @author Acrismede Mendes, Alexandre Guerreiro, Léo Souza
 * @version 11/05/26
 */
public class NavioNavegando implements EstadoNavio {
    /**
     * Atualiza o estado do navio em movimento considerando o tempo decorrido e a velocidade da corrente.
     * <p>
     * Este método aplica as movimentações necessárias ao objeto Navio com base no vetor de velocidade
     * da corrente e no intervalo de tempo fornecido, representado por delta. A interação resulta em
     * uma mudança de posição do navio de acordo com as condições especificadas.
     *
     * @param navio              o objeto Navio que terá a sua posição atualizada
     * @param delta              o tempo decorrido que influencia o deslocamento do navio
     * @param velocidadeCorrente o vetor representando a velocidade corrente que afeta o movimento do navio
     */
    @Override
    public void atualizar(Navio navio, double delta, Vetor velocidadeCorrente) {
        navio.mover(delta, velocidadeCorrente);
    }
}
