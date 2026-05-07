package Engine;

/**
 * Representa o estado de um navio localizado na sua origem. Esta classe é uma implementação
 * do estado inicial de um navio que ainda não iniciou o seu movimento ou operações relacionadas
 * à navegação.
 * <p>
 * O comportamento do método atualizar nesta classe reflete a manutenção do estado do navio
 * enquanto este permanece na origem, sendo possível implementar logicamente as transições
 * de estado ou outras operações relevantes.
 * <p>
 * Implementa a interface EstadoNavio, que define o contrato para alteração do estado do navio.
 *
 * @author Acrismede Mendes, Alexandre Guerreiro, Léo Souza
 * @version 11/05/26
 */
public class NavioNaOrigem implements EstadoNavio {
    /**
     * Atualiza o estado de um navio localizado na origem, considerando as condições
     * fornecidas. Este método pode realizar operações de transição de estado ou
     * estabelecer o comportamento do navio enquanto ele permanece na origem.
     *
     * @param navio              o objeto Navio cujo estado será atualizado
     * @param delta              o intervalo de tempo que influencia a atualização do estado
     * @param velocidadeCorrente o vetor que representa a velocidade da corrente no ambiente do navio
     */
    @Override
    public void atualizar(Navio navio, double delta, Vetor velocidadeCorrente) {

    }
}
