package Engine;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma rota no espaço 2D definida por uma sequência de pontos.
 * Os pontos na lista fornecem a ordem na qual a rota é seguida,
 * e ela pode conter múltiplos segmentos consecutivos formados entre pares de pontos.
 * A classe é imutável.
 *
 * @author Léo Souza
 * @version 09/03/26
 * @inv Uma rota tem pelo menos dois pontos e quantidade de coordenadas deve ser par.
 */
public class Route {
    private final List<Ponto> pontos;

    /**
     * Constrói um objeto {@code Engine.Route} com base numa lista de pontos fornecida.
     * A rota é imutável e representada pela sequência de pontos especificada.
     *
     * @param pontos A lista de {@code Engine.Ponto} que define a sequência de pontos da rota.
     *               Deve conter pelo menos dois pontos, e dois pontos consecutivos
     *               na rota devem ser diferentes.
     * @pre pontos != null e pontos.size() >= 2
     * @pos Uma cópia da lista pontos é armazenada internamente
     */
    public Route(List<Ponto> pontos) {
        this.pontos = List.copyOf(pontos);
    }

    /**
     * Constrói um objeto {@code Engine.Route} com base em um array de coordenadas
     * fornecido. Cada par de valores do array (x, y) representa as
     * coordenadas de um ponto que compõe a rota. A rota é imutável e
     * precisa conter pelo menos dois pontos para ser válida.
     *
     * @param coordenadas Um array de valores do tipo {@code double},
     *                    onde cada par consecutivo (x, y) representa
     *                    as coordenadas de um ponto da rota.
     *                    O array deve ter um número par de elementos.
     *                    Caso contrário, será lançada uma IllegalArgumentException.
     * @throws IllegalArgumentException Se o array não contiver um número par de elementos
     * @pre coordenadas != null
     * @pos Uma lista com pontos criados a partir das coordenadas é guardada no objeto.
     */
    public Route(double[] coordenadas) {
        if (coordenadas.length % 2 != 0) {
            throw new IllegalArgumentException("Rota:iv");
        }

        pontos = new ArrayList<>();
        for (int i = 1; i < coordenadas.length; i += 2) {
            pontos.add(new Ponto(coordenadas[i - 1], coordenadas[i]));
        }
    }

    /**
     * Gera uma lista de segmentos de reta que compõem a rota. Cada segmento
     * é formado por dois pontos consecutivos da sequência de pontos armazenados.
     * A lista de pontos deve conter pelo menos dois pontos para que os segmentos
     * possam ser criados.
     *
     * @return Uma lista de objetos {@code Engine.SegmentoReta}, onde cada segmento
     * representa uma ligação consecutiva entre dois pontos da rota. A lista
     * retornada nunca é nula e possui uma composição igual a
     * {@code pontos.size() - 1}.
     */
    public List<SegmentoReta> getSegmentos() {
        List<SegmentoReta> segmentos = new ArrayList<>();
        for (int i = 1; i < pontos.size(); i++) {
            segmentos.add(new SegmentoReta(pontos.get(i - 1), pontos.get(i)));
        }
        return segmentos;
    }

    /**
     * Calcula o comprimento total da rota representada pelo conjunto de pontos.
     * O comprimento é a soma das distâncias entre pares consecutivos de pontos
     * na sequência de pontos que compõem a rota.
     *
     * @return O comprimento total da rota como um valor numérico de ponto flutuante.
     * @pos O valor retornado é ≥ 0.0.
     */
    public double comprimento() {
        double total = 0;

        for (int i = 1; i < pontos.size(); i++) {
            total += pontos.get(i - 1).distanciaPara(pontos.get(i));
        }

        return total;
    }

    /**
     * Compara este objeto {@code Route} com o objeto especificado para verificar igualdade.
     * Dois objetos {@code Route} são considerados iguais se suas sequências de pontos forem idênticas.
     *
     * @param o O objeto a ser comparado com esta {@code Route}.
     * @return {@code true} se o objeto especificado for igual a esta {@code Route}, {@code false} caso contrário.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Route r = (Route) o;

        return this.pontos.equals(r.pontos);

    }
}
