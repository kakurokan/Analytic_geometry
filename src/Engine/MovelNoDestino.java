package Engine;

/**
 * Representa o estado de um navio ao chegar ao seu destino.
 * <p>
 * Esta classe implementa o comportamento específico de um navio quando ele alcança
 * a localização final planeada. Durante este estado, nenhum movimento adicional do
 * navio é esperado, e suas propriedades ou interações com o ambiente podem ser
 * atualizadas para refletir a chegada ao destino.
 * <p>
 * Implementa o método da interface {@code EstadoNavio} para gerenciar as atualizações
 * necessárias ao navio neste estado.
 *
 * @author Acrismede Mendes, Alexandre Guerreiro, Léo Souza
 * @version 11/05/26
 */
public class MovelNoDestino implements EstadoMovel {
    /**
     * Atualiza o estado de um movel quando ele está no destino.
     *
     * @param movel              o movel que será atualizado
     * @param delta              o intervalo de tempo em segundos desde a última atualização
     * @param velocidadeCorrente o vetor representando a velocidade da corrente no ambiente
     */
    @Override
    public void atualizar(Movel movel, double delta, Vetor velocidadeCorrente) {

    }
}
