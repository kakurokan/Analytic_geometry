package Engine;

import java.util.*;

/**
 * Representa um grafo direcionado ou não direcionado que utiliza {@code Ponto} como os vértices
 * e armazena as conexões entre eles em forma de arestas. O grafo é construído com base numa lista de rotas e uma lista de obstáculos, garantindo que as conexões entre os pontos
 * não intersectem obstáculos fornecidos.
 *
 * @author Acrismede Mendes, Alexandre Guerreiro, Léo Souza
 * @version 11/05/26
 * @inv a lista de rotas não pode ser vazia
 */
public class Grafo {
    public static Comparator<Ponto> comparador;
    private final Map<Ponto, Set<Ponto>> grafo;

    /**
     * Construtor da classe {@code Grafo}, que cria um grafo com base numa lista de rotas e uma lista
     * de obstáculos. O grafo é construído utilizando os segmentos das rotas fornecidas, desde que
     * esses segmentos não intersectem nenhum obstáculo na lista de obstáculos fornecida.
     *
     * @param rotas     A lista de objetos {@code Route} representando as rotas que serão utilizadas
     *                  para construir o grafo. Cada rota deve conter pelo menos dois pontos.
     *                  Não pode ser vazia.
     * @param obstaculo A lista de objetos {@code Obstaculo} representando os obstáculos no plano
     *                  cartesiano. Esses obstáculos serão utilizados para verificar interseções
     *                  com os segmentos das rotas fornecidas.
     * @throws IllegalArgumentException Se a lista de rotas estiver vazia ou se, após a validação,
     *                                  nenhum segmento livre (sem interseção) for encontrado.
     */
    public Grafo(List<Route> rotas, List<Obstaculo> obstaculo) {
        if (rotas.isEmpty()) {
            throw new IllegalArgumentException("Grafo:iv");
        }

        comparador = Comparator.comparingDouble(Ponto::getX).thenComparingDouble(Ponto::getY);
        grafo = new TreeMap<>(comparador);
        List<SegmentoReta> segmentosRotas = getSegmentosRotas(rotas);
        for (SegmentoReta segmento : segmentosRotas) {
            if (intersetaObstaculo(segmento, obstaculo)) {
                continue;
            }
            Ponto p1 = segmento.getA();
            Ponto p2 = segmento.getB();
            grafo.computeIfAbsent(p1, k -> new TreeSet<>(comparador)).add(p2);
            grafo.computeIfAbsent(p2, k -> new TreeSet<>(comparador)).add(p1);
        }
        if (grafo.isEmpty()) {
            throw new IllegalArgumentException("Não existe nenhum segmento livre");
        }
    }

    /**
     * Retorna o mapa interno que representa as conexões do grafo.
     * <p>
     * Neste mapa, cada chave corresponde a um {@code Ponto} (vértice) e o valor
     * associado é um conjunto de pontos adjacentes (arestas) a esse vértice.
     * Este método retorna a referência direta, permitindo a alteração do grafo
     * <p>
     * @return O mapa que mapeia cada {@code Ponto} para o seu conjunto de ligações diretas.
     */
    public Map<Ponto, Set<Ponto>> getGrafo() {
        return this.grafo;
    }

    /**
     * Verifica se um segmento de reta interceta algum dos obstáculos fornecidos.
     *
     * @param segmento  O segmento de reta a ser verificado. Este é representado
     *                  por dois pontos {@code A} e {@code B}, que definem o
     *                  início e o fim do segmento.
     * @param obstaculo A lista de obstáculos contra os quais a interseção será
     *                  verificada. Cada obstáculo fornece métodos para calcular
     *                  pontos de interseção com o segmento.
     * @return {@code true} se o segmento de reta intercetar qualquer um dos
     * obstáculos, {@code false} caso contrário.
     */
    private boolean intersetaObstaculo(SegmentoReta segmento, List<Obstaculo> obstaculo) {
        if (obstaculo == null || obstaculo.isEmpty()) return false;
        Route rota = new Route(
                List.of(segmento.getA(), segmento.getB()));
        for (Obstaculo o : obstaculo) {
            List<Ponto> pontos = o.intersect(rota);
            if (pontos != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtém uma lista de segmentos de reta a partir de uma lista de rotas.
     * Cada rota contém uma lista de segmentos que são extraídos e adicionados
     * à lista resultante.
     *
     * @param rotas A lista de objetos {@code Route} representando as rotas.
     *              Cada objeto {@code Route} deve conter uma ou mais definições
     *              de segmentos de reta.
     * @return Uma lista de objetos {@code SegmentoReta} extraídos das rotas fornecidas.
     */
    private List<SegmentoReta> getSegmentosRotas(List<Route> rotas) {
        List<SegmentoReta> segmentos = new ArrayList<>();
        for (Route rota : rotas) {
            segmentos.addAll(rota.getSegmentos());
        }
        Map<SegmentoReta, List<Ponto>> pontosPorSegmento = new HashMap<>();
        for (SegmentoReta seg : segmentos) {
            pontosPorSegmento.put(seg, new ArrayList<>());
            pontosPorSegmento.get(seg).add(seg.getA());
            pontosPorSegmento.get(seg).add(seg.getB());
        }

        for (int i = 0; i < segmentos.size(); i++) {
            for (int j = i + 1; j < segmentos.size(); j++) {
                SegmentoReta seg1 = segmentos.get(i);
                SegmentoReta seg2 = segmentos.get(j);

                Ponto inter = seg1.intersect(seg2);
                if (inter == null) {
                    continue;
                }

                adicionarPontoUnico(pontosPorSegmento.get(seg1), inter);
                adicionarPontoUnico(pontosPorSegmento.get(seg2), inter);
            }
        }
        List<SegmentoReta> segmentosValidos = new ArrayList<>();
        for (SegmentoReta seg : pontosPorSegmento.keySet()) {
            List<Ponto> pontosSeg = pontosPorSegmento.get(seg);

            pontosSeg.sort(Comparator.comparingDouble(
                    p -> p.distanciaPara(seg.getA())
            ));

            for (int i = 1; i < pontosSeg.size(); i++) {
                segmentosValidos.add(new SegmentoReta(pontosSeg.get(i - 1), pontosSeg.get(i)));
            }
        }

        return segmentosValidos;
    }

    /**
     * Adiciona um ponto a uma lista apenas se este ainda não estiver presente,
     * garantindo a unicidade dos elementos na coleção fornecida.
     * <p>
     * O método percorre a lista e utiliza o método {@code equals} da classe {@link Ponto}
     * para verificar se já existe uma instância com as mesmas coordenadas antes de
     * realizar a inserção.
     *
     * @param lista A lista de pontos onde a inserção será tentada.
     * @param ponto O ponto que se deseja adicionar à lista.
     */
    private void adicionarPontoUnico(List<Ponto> lista, Ponto ponto) {
        for (Ponto p : lista) {
            if (p.equals(ponto)) return;
        }
        lista.add(ponto);
    }

    /**
     * Adiciona um ponto ao grafo e estabelece conexões entre o ponto fornecido e os extremos
     * de um segmento de reta especificado, se o ponto não for igual a um dos extremos do segmento.
     *
     * @param posicao O ponto que será adicionado ao grafo. Se for igual a um dos extremos
     *                do segmento, nenhuma ação será realizada.
     * @param seg     O segmento de reta cujos extremos serão conectados ao ponto fornecido.
     * @pre posicao != null
     * @pre seg!=null
     * @pos Se os extremos de 'seg' pertencerem ao grafo e 'posicao' ainda não pertencer e for diferente de ambos, é adicionado ao grafo
     * com as ligações seg.getA() e seg.getB()
     * Caso contrario o grafo permanece inalterado
     */
    public void adicionarPonto(Ponto posicao, SegmentoReta seg) {
        if (!grafo.containsKey(seg.getA()) || !grafo.containsKey(seg.getB())) {
            return;
        }
        if (grafo.containsKey(posicao)) {
            return;
        }
        grafo.computeIfAbsent(posicao, _ -> new TreeSet<>(comparador)).add(seg.getA());
        grafo.computeIfAbsent(seg.getA(), _ -> new TreeSet<>(comparador)).add(posicao);


        grafo.computeIfAbsent(posicao, _ -> new TreeSet<>(comparador)).add(seg.getB());
        grafo.computeIfAbsent(seg.getB(), _ -> new TreeSet<>(comparador)).add(posicao);

    }

    /**
     * Remove um ponto do grafo e desconecta todas as arestas associadas a ele,
     * caso o ponto não corresponda a nenhum dos extremos do segmento de reta fornecido.
     *
     * @param posicao O ponto a ser removido. Caso seja equivalente a um dos extremos
     *                do segmento especificado, nenhuma ação será realizada.
     * @param seg     O segmento de reta cujos extremos são avaliados para garantir que
     *                o ponto a ser removido não seja um deles. As conexões entre os
     *                extremos do segmento e o ponto serão removidas, caso o ponto seja
     *                desconectável.
     * @pre posicao != null
     * @pre seg!=null
     *@pos Se os extremos de 'seg' pertencerem ao grafo e 'posicao' também pertencer e for diferente de ambos, é removido do grafo
     * tal como as suas ligações com seg.getA() e seg.getB()
     */
    public void removerPonto(Ponto posicao, SegmentoReta seg) {
        if (!grafo.containsKey(seg.getA()) || !grafo.containsKey(seg.getB())) {
            return;
        }
        if (posicao.equals(seg.getA()) || posicao.equals(seg.getB())) {
            return;
        }
        grafo.remove(posicao);
        grafo.get(seg.getA()).remove(posicao);
        grafo.get(seg.getB()).remove(posicao);
    }
}
