package Engine;

import java.util.ArrayList;
import java.util.List;

/**
 * A classe GestorMaritimo gerencia o tráfego marítimo, garantindo segurança e
 * rotas otimizadas para todos os navios sob seu controle. Implementa a interface TorreDeControlo
 * para supervisionar decisões de navegação, como gestão de rotas, prevenção de colisões
 * e transições de estado dos navios.
 * <p>
 * Responsabilidades incluem:
 * - Inicializar o ambiente marítimo com rotas e obstáculos.
 * - Monitorar e atualizar posições dos navios, garantindo navegação livre de colisões.
 * - Calcular e atribuir rotas otimizadas para navios usando uma estratégia de roteamento específica.
 * - Gerir transições de estado dos navios com base no seu status atual, colisões
 * ou disponibilidade de rota.
 * - Gerir o ciclo de vida dos navios, incluindo adicioná-los e removê-los do controle ativo
 * após partida ou chegada.
 * <p>
 * A classe utiliza uma representação baseada em grafos do ambiente marítimo
 * e integra uma estratégia de roteamento (por exemplo, Dijkstra) para calcular caminhos.
 *
 * @author Acrismede Mendes, Alexandre Guerreiro, Léo Souza
 * @version 11/05/26
 * @inv a lista de navios não pode ser nula
 */
public class GestorMaritimo implements TorreDeControlo {
    private final List<Navio> navios;
    private EstrategiaRota estrategiaRota;
    private Grafo grafo;

    /**
     * Construtor padrão da classe GestorMaritimo.
     * <p>
     * Este construtor inicializa uma lista vazia de navios.
     * A lista será utilizada para gerir e rastrear todos os navios
     * sob o controle deste gestor marítimo.
     */
    public GestorMaritimo() {
        this.navios = new ArrayList<>();
    }

    /**
     * Inicializa o gestor marítimo com um conjunto de rotas e obstáculos especificados.
     * Esta operação cria um grafo com base nas rotas e obstáculos fornecidos e define
     * uma estratégia de cálculo de rotas usando o algoritmo de Dijkstra.
     * Também limpa a lista de navios gerenciados pelo gestor.
     *
     * @param rotas     A lista de rotas disponíveis que serão utilizadas para construir o grafo.
     *                  Cada rota é representada por um objeto {@code Route}.
     * @param obstaculo A lista de obstáculos no espaço marítimo que impactam as rotas.
     *                  Cada obstáculo é representado por um objeto que implementa a interface {@code Obstaculo}.
     */
    public void iniciar(List<Route> rotas, List<Obstaculo> obstaculo) {
        grafo = new Grafo(rotas, obstaculo);
        this.estrategiaRota = new EstrategiaDijkstra(grafo);

        this.navios.clear();
    }

    /**
     * Atualiza as posições de um navio e modifica seu estado com base em sua interação
     * com outros navios e a lógica de colisão definida.
     * <p>
     * O método verifica se o navio fornecido está colidindo com outros navios em sua vizinhança.
     * Se uma colisão for identificada, ele marca os navios envolvidos como em colisão.
     * Caso o código de viagem do navio fornecido seja lexicograficamente menor que o de outro navio
     * em colisão, o estado do navio é alterado para {@code NavioAguardando}.
     * Caso contrário, o estado é alterado para {@code NavioNavegando}.
     *
     * @param navio O navio cuja posição será atualizada, conforme as interações com outros navios
     *              no espaço marítimo gerenciado.
     */
    @Override
    public void atualizarPosicoes(Navio navio) {
        boolean tocando = false;
        boolean caminhoBloqueado = false;

        for (Navio outro : navios) {
            if (navio != outro && navio.intersect(outro)) {
                tocando = true;
                outro.setEmColisao(true);

                // A REGRA DE OURO DO ENUNCIADO:
                // Se o MEU código de viagem for lexicograficamente menor (< 0) que o outro,
                // então EU sou obrigado a esperar (caminhoBloqueado = true).
                if (navio.compareTo(outro) < 0) {
                    caminhoBloqueado = true;
                    break;
                }
            }
        }

        navio.setEmColisao(tocando);

        if (caminhoBloqueado) {
            if (navio.getEstado() instanceof NavioNavegando) {
                navio.mudarEstado(new NavioAguardando());
            }
        } else {
            if (navio.getEstado() instanceof NavioAguardando) {
                navio.mudarEstado(new NavioNavegando());
            }
        }
    }

    /**
     * Atualiza a rota de um navio específico e define seu novo estado com base na viabilidade de rota.
     * <p>
     * Este método verifica se o navio está registrado na lista de navios gerenciados.
     * Em seguida, calcula a rota mais eficiente para o destino do navio utilizando a lógica
     * definida na estratégia de cálculo de rotas. A rota gerada é atribuída ao navio,
     * e o estado do navio é alterado de acordo com o resultado (navegando ou aguardando).
     *
     * @param navio O navio cuja rota será recalculada e que terá seu estado ajustado conforme
     *              as condições avaliadas durante o processo de atualização.
     */
    @Override
    public void atualizarRota(Navio navio) {
        if (!navios.contains(navio))
            return;

        Ponto origem = navio.getPosicao();
        SegmentoReta atual = navio.getSegmentoAtual(origem);
        grafo.adicionarPonto(origem, atual);
        Route rota = estrategiaRota.caminhos(origem, navio.getDestino().getPosicao());
        grafo.removerPonto(origem, atual);
        if (rota != null) {
            navio.receberRota(rota);
            navio.mudarEstado(new NavioNavegando());
        } else {
            navio.mudarEstado(new NavioAguardando());
        }

    }

    /**
     * Libera um navio do porto de origem, atribuindo uma rota para seu destino
     * e alterando seu estado para navegando, se uma rota viável for encontrada.
     *
     * @param origem O porto de onde o navio será liberado. Contém informações
     *               sobre sua posição e fila de navios em espera.
     * @param navio  O navio que será liberado. Este navio terá sua rota
     *               atualizada para alcançar o destino especificado.
     */
    @Override
    public void libertarNavio(Porto origem, Navio navio) {
        Route rota = estrategiaRota.caminhos(origem.getPosicao(), navio.getDestino().getPosicao());
        if (rota != null) {
            navio.receberRota(rota);
            navio.mudarEstado(new NavioNavegando());
            this.navios.add(navio);
        }
    }

    /**
     * Finaliza o percurso de um navio no sistema gerenciado.
     * <p>
     * Este método altera o estado do navio para um estado indicando que ele
     * chegou ao seu destino e remove o navio da lista de navios ativos
     * gerenciados pela instância atual.
     *
     * @param navio O navio que terminou o percurso. Este navio terá seu estado
     *              atualizado para {@code NavioNoDestino} e será removido da
     *              lista interna de navios gerenciados.
     */
    @Override
    public void navioTerminouPercurso(Navio navio) {
        navio.mudarEstado(new NavioNoDestino());
        this.navios.remove(navio);
    }

    /**
     * Retorna a lista de navios gerenciados pelo sistema.
     * <p>
     * Este método fornece acesso à lista interna de navios atualmente
     * sob gestão do objeto, permitindo consultar e trabalhar com os
     * dados dos navios.
     *
     * @return Uma lista contendo os navios registrados no sistema.
     * A lista é composta por objetos do tipo {@code Navio}.
     */
    public List<Navio> getNavios() {
        return this.navios;
    }

}
