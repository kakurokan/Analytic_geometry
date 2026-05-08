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
     */
    void atualizarRota(Movel movel);

    /**
     * Atualiza a posição do movel informado no sistema, realizando os ajustes necessários
     * com base em mudanças de localização ou outras informações pertinentes ao monitoramento
     * e controle do espaço marítimo.
     *
     * @param movel o movel cuja posição será atualizada no sistema
     */
    void atualizarPosicoes(Movel movel);

    /**
     * Libera o movel especificado do porto de origem, removendo sua associação com
     * o porto e permitindo que ele continue seu trajeto. Este método é utilizado para
     * gerenciar a saída de navios de portos sob o controle do sistema.
     *
     * @param origem o porto de onde o movel será liberado
     * @param movel  o movel que será liberado do porto de origem
     */
    void libertarNavio(Porto origem, Movel movel);

    /**
     * Registra o término do percurso de um movel no sistema, concluindo o monitoramento
     * da sua trajetória e realizando atualizações necessárias para refletir o estado final
     * desse movel.
     *
     * @param movel o movel que terminou o percurso
     */
    void navioTerminouPercurso(Movel movel);

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
     */
    void iniciar(List<Route> rotas, List<Obstaculo> obstaculo);

    List<Movel> getMovels();
}
