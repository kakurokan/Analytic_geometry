package Engine;

/**
 * Representa o estado "Aguardando" de um navio no seu ciclo de vida. Este estado é caracterizado
 * por um navio que está a aguardar numa posição específica, possivelmente aguardando
 * permissão para navegar ou resolver situações de colisão.
 *
 * <p>Durante este estado, o navio pode entrar em processo de resolução de colisões pedindo à
 * torre de controle uma atualização nas posições dos objetos ao seu redor. Caso o navio não esteja
 * mais em colisão, ele transita para o estado "Navegando".</p>
 *
 * <p>A implementação deste estado segue o padrão State, permitindo que o comportamento de
 * atualização do navio seja completamente definido por este estado específico.</p>
 *
 * @author Acrismede Mendes, Alexandre Guerreiro, Léo Souza
 * @version 11/05/26
 */
public class NavioAguardando implements EstadoNavio {
    /**
     * Atualiza o estado do navio enquanto ele está no estado "Aguardando".
     * Este método verifica se o navio está em situação de colisão e, caso esteja,
     * solicita à torre de controle a atualização das posições ao redor. Se o navio
     * não estiver mais em colisão, seu estado é alterado para "Navegando".
     *
     * @param navio              o navio cujo estado está sendo atualizado
     * @param delta              o intervalo de tempo usado para atualização
     * @param velocidadeCorrente o vetor representando a velocidade atual da corrente
     */
    @Override
    public void atualizar(Navio navio, double delta, Vetor velocidadeCorrente) {
        if (navio.isEmColisao()) {
            //Pede para torre para sair
            navio.getTorre().atualizarPosicoes(navio);
        }

        if (!navio.isEmColisao()) {
            navio.mudarEstado(new NavioNavegando());
        }
    }
}
