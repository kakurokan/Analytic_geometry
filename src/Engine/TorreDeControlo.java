package Engine;

import java.util.List;

/**
 * Interface TorreDeControlo representa um sistema responsável pelo controle e gerenciamento
 * de navios num espaço marítimo. Ela define métodos para atualizar rotas, monitorar
 * posições, liberar navios de portos, registrar o término de percursos, inicializar o sistema
 * e consultar a lista de navios gerenciados.
 *
 * @author Acrismede Mendes, Alexandre Guerreiro, Léo Souza
 * @version 11/05/26
 */
public interface TorreDeControlo {
    /**
     * Atualiza a rota associada ao movel informado, ajustando a sua trajetória
     * com base nos dados fornecidos e nas condições atuais do sistema.
     *
     * @param movel o movel cuja rota será atualizada
     * @pre movel != null e deve estar registado na torre.
     * @pos Se existir rota, o movel recebe-a e passa a MovelNavegando, caso contrário, passa a MovelAguardando
     */
    void atualizarRota(Movel movel);

    /**
     * Atualiza a posição do movel informado no sistema, realizando os ajustes necessários
     * com base em mudanças de localização ou outras informações pertinentes ao monitoramento
     * e controle do espaço marítimo.
     *
     * @param movel o movel cuja posição será atualizada no sistema
     * @pre movel != null.
     * @pos O estado de colisão do movel é atualizado e, se necessário, o seu estado comportamental (Aguardando/Navegando).
     */
    void atualizarPosicoes(Movel movel);

    /**
     * Libera o movel especificado do porto de origem, removendo sua associação com
     * o porto e permitindo que ele continue seu trajeto. Este método é utilizado para
     * gerenciar a saída de navios de portos sob o controle do sistema.
     *
     * @param origem o porto de onde o movel será liberado
     * @param movel  o movel que será liberado do porto de origem
     * @pre origem != null e movel != null.
     * @pos O movel é adicionado à lista de gestão. Se houver rota, inicia navegação, caso contrário, aguarda.
     */
    void libertarMovel(Porto origem, Movel movel);

    /**
     * Registra o término do percurso de um movel no sistema, concluindo o monitoramento
     * da sua trajetória e realizando atualizações necessárias para refletir o estado final
     * desse movel.
     *
     * @param movel o movel que terminou o percurso
     * @pre movel != null.
     * @pos O movel passa para o estado MovelNoDestino e é removido da lista de gestão da torre.
     */
    void movelTerminouPercurso(Movel movel);

    /**
     * Inicializa o sistema de controle marítimo com as rotas e os obstáculos fornecidos.
     * Este método configura os recursos necessários para o funcionamento do sistema,
     * permitindo que as operações de controle e monitoramento sejam realizadas
     * com base nos parâmetros especificados.
     *
     * @param rotas     a lista de rotas que serão gerenciadas pelo sistema. Cada rota é representada
     *                  por uma sequência de pontos, descrevendo trajetórias possíveis no espaço marítimo.
     * @param obstaculo a lista de objetos que representam os obstáculos no espaço marítimo. Cada
     *                  obstáculo define interseções potenciais com as rotas fornecidas.
     * @pre rotas != null e obstaculo != null.
     * @pos O sistema (grafo e estratégia) é reiniciado com os novos dados e a lista de navios é limpa.
     */
    void iniciar(List<Route> rotas, List<Obstaculo> obstaculo);

    /**
     * Retorna a lista de móveis sob gestão.
     * @return Lista de objetos Movel.
     * @pos A lista retornada não é nula.
     */
    List<Movel> getMovels();
}
