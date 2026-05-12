package Engine;

import java.util.List;

/**
 * Representa uma tempestade como um obstáculo num espaço cartesiano.
 * A tempestade é modelada por um círculo que define a sua área de impacto.
 * Esta classe implementa a interface {@code Obstaculo}, permitindo que as interseções
 * de rotas com a área da tempestade sejam calculadas.
 *
 * @author Acrismede Mendes, Alexandre Guerreiro, Léo Souza
 * @version 11/05/26
 * @inv area != null
 */
public class Tempestade implements Obstaculo {
    private final Circulo area;

    /**
     * Constrói uma instância de Tempestade com base em um círculo que define sua área.
     * A tempestade é representada por uma cópia do círculo fornecido, garantindo que
     * a instância seja independente do objeto original.
     *
     * @param area O círculo que representa a área de impacto da tempestade.
     *             Não pode ser nulo e deve ter um raio maior que zero.
     * @throws IllegalArgumentException Se o círculo fornecido tiver um raio inválido
     *                                  ou se parâmetro for nulo.
     * @pre area != null e area.getRaio() > 0
     * @pos A tempestade é inicializada com uma cópia independente da área fornecida.
     */
    public Tempestade(Circulo area) {
        this.area = new Circulo(area.getCentro(), area.getRaio());
    }

    /**
     * Retorna o círculo que define a área de impacto da tempestade.
     * O círculo contém informações sobre o centro e o raio, representando
     * graficamente a delimitação da área afetada.
     *
     * @return o objeto Circulo que representa a área da tempestade.
     * @pos Retorna a área circular da tempestade, não sendo nula.
     */
    public Circulo getArea() {
        return area;
    }

    /**
     * Calcula os pontos de interseção entre a área da tempestade e uma rota fornecida.
     * A interseção é representada por uma lista de pontos que pertencem tanto à área
     * circular da tempestade quanto à rota especificada.
     *
     * @param rota A rota para a qual serão calculados os pontos de interseção com a área da tempestade.
     *             Não pode ser nula e deve conter informações válidas de geometria espacial.
     * @return Uma lista de pontos representando as interseções entre a área da tempestade
     * e a rota. Se não houver interseção, retorna uma lista vazia.
     * @pre rota != null
     * @pos Retorna null se não houver interseção, ou uma lista com os pontos de interseção únicos entre a área e a rota.
     */
    @Override
    public List<Ponto> intersect(Route rota) {
        return area.intersect(rota);
    }
}
