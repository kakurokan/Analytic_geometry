package Engine;

import java.util.*;

/**
 * A classe EstrategiaDijkstra é uma implementação da interface EstrategiaRota
 * que utiliza o algoritmo de Dijkstra para calcular o caminho mais curto entre dois pontos
 * num grafo. Considera restrições específicas como obstáculos de navegação durante o
 * processo de determinação do caminho.
 * <p>
 * Esta estratégia utiliza uma fila de prioridade para explorar pontos em ordem crescente de
 * distância a partir do ponto de origem. Para cada ponto, avalia os seus vizinhos,
 * atualizando as suas distâncias se for encontrado um caminho mais curto, e mantém registo dos
 * caminhos usando um mapeamento inverso de pontos.
 * <p>
 * A classe também fornece métodos utilitários para reconstruir o caminho usando o
 * mapeamento inverso calculado e aplicar restrições, como garantir que não existem
 * obstáculos ou áreas restritas ao longo de segmentos específicos do caminho.
 *
 * @author Acrismede Mendes, Alexandre Guerreiro, Léo Souza
 * @version 11/05/26
 * @inv o grafo não pode ser nulo e deve conter ligações válidas entre nós (pontos)
 */

public class EstrategiaDijkstra implements EstrategiaRota {
    private Grafo grafo;

    /**
     * Constrói uma instância de EstrategiaDijkstra com o grafo especificado.
     *
     * @param grafo o grafo que representa a estrutura navegável.
     *              Este grafo não pode ser nulo e deve conter ligações válidas
     *              entre nós (pontos). Se o grafo for nulo, uma
     *              IllegalArgumentException é lançada.
     * @pre grafo != null
     * @pos O objeto é inicializado com o grafo fornecido.
     */
    EstrategiaDijkstra(Grafo grafo) {
        if (grafo == null) {
            throw new IllegalArgumentException("EstrategiaDijkstra:iv");
        }
        this.grafo = grafo;
    }

    /**
     * Reverte o caminho fornecido, criando uma rota a partir do destino
     * até a origem com base no mapeamento de pontos.
     *
     * @param caminhoInverso Um mapa que associa cada ponto ao seu ponto anterior
     *                       no caminho inverso. Não deve ser nulo.
     * @param destino        O ponto de destino inicial. Não pode ser nulo.
     * @return Uma instância de {@code Route} que representa o caminho invertido,
     * ou {@code null} se o destino não tiver um ponto associado no mapa.
     * @see <a href="https://stackoverflow.com/questions/56609206/how-do-i-keep-track-of-the-shortest-paths-in-the-dijkstra-algorithm-when-using-a">Stack Overflow</a>
     */
    private Route inverteCaminho(Map<Ponto, Ponto> caminhoInverso, Ponto destino) {
        LinkedList<Ponto> caminhoCerto = new LinkedList<>();
        Ponto passo = destino;

        if (caminhoInverso.get(passo) == null) return null;

        while (passo != null) {
            caminhoCerto.addFirst(passo);
            passo = caminhoInverso.get(passo);
        }
        List<Ponto> pontosRota = new ArrayList<>(caminhoCerto);
        return new Route(pontosRota);
    }

    /**
     * Calcula a rota mais curta entre dois pontos em um grafo navegável considerando
     * as restrições impostas pelos navios existentes.
     *
     * @param origem  O ponto de partida no grafo. Não pode ser nulo e deve estar presente no grafo.
     * @param destino O ponto de destino no grafo. Não pode ser nulo e deve estar presente no grafo.
     * @return Um objeto {@code Route} representando o caminho mais curto entre a origem e o destino,
     * levando em conta as restrições de tráfego por navios. Retorna {@code null} se a
     * origem ou o destino não estiverem no grafo, ou se não houver caminho viável.
     * @see <a href="https://www.geeksforgeeks.org/dsa/dijkstras-shortest-path-algorithm-using-priority_queue-stl/">GeeksforGeeks</a>
     */
    @Override
    public Route caminhos(Ponto origem, Ponto destino) {
        if (!grafo.getGrafo().containsKey(origem) || !grafo.getGrafo().containsKey(destino)) {
            return null;
        }

        TreeMap<Ponto, Double> distancias = new TreeMap<>(Grafo.comparador);

        TreeMap<Ponto, Ponto> caminhoInverso = new TreeMap<>(Grafo.comparador);

        PriorityQueue<No> pq = new PriorityQueue<>();

        for (Ponto p : grafo.getGrafo().keySet()) {
            distancias.put(p, Double.MAX_VALUE);
        }
        distancias.put(origem, 0.0);
        pq.add(new No(origem, 0.0));

        while (!pq.isEmpty()) {
            No n = pq.poll();
            Ponto pontoN = n.ponto;
            double distanciaN = n.distanciaAcumulada;

            Set<Ponto> vizinhos = grafo.getGrafo().get(pontoN);
            if (vizinhos == null) continue;

            for (Ponto p : vizinhos) {
                double distanciaParaP = pontoN.distanciaPara(p);


                double distanciaAcumuladaParaP = distanciaN + distanciaParaP;
                if (distanciaAcumuladaParaP < distancias.get(p)) {
                    distancias.put(p, distanciaAcumuladaParaP);
                    caminhoInverso.put(p, pontoN);
                    pq.offer(new No(p, distanciaAcumuladaParaP));
                }
            }
        }
        return inverteCaminho(caminhoInverso, destino);
    }

    /**
     * Representa um nó utilizado no algoritmo de Dijkstra. Cada nó contém um ponto
     * e a distância acumulada desse ponto em relação à origem no grafo.
     * <p>
     * Esta classe é projetada para ser utilizada como parte do algoritmo de cálculo
     * de rotas mais curtas, com a capacidade de comparar nós com base na suas distâncias
     * acumuladas, para priorizar o nó com a menor distância no processamento.
     *
     * @author Acrismede Mendes, Alexandre Guerreiro, Léo Souza
     * @version 11/05/26
     * @inv ponto != null e distanciaAcumulada >= 0
     */
    private static class No implements Comparable<No> {
        private final Ponto ponto;
        private final double distanciaAcumulada;

        /**
         * Constrói um nó contendo um ponto e a sua distância acumulada relativamente ao início do grafo.
         *
         * @param ponto              O ponto representado pelo nó no grafo.
         * @param distanciaAcumulada A distância acumulada do ponto relativamente ao nó de origem.
         */
        No(Ponto ponto, double distanciaAcumulada) {
            this.ponto = ponto;
            this.distanciaAcumulada = distanciaAcumulada;
        }


        /**
         * Compara este nó com o nó especificado com base na distância acumulada.
         * Este método é usado para determinar a ordem de prioridade entre dois nós
         * no contexto do algoritmo de Dijkstra, priorizando o nó com a menor distância acumulada.
         *
         * @param n o nó a ser comparado com este nó
         * @return um valor negativo se este nó tiver uma distância acumulada menor que o nó especificado;
         * zero se ambos os nós tiverem a mesma distância acumulada;
         * um valor positivo se este nó tiver uma distância acumulada maior que o nó especificado
         */
        @Override
        public int compareTo(No n) {
            return Double.compare(this.distanciaAcumulada, n.distanciaAcumulada);
        }
    }

    public void setGrafo(Grafo grafo){
        this.grafo=grafo;
    }
}
