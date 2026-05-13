package Engine;

/**
 * Representa um ponto 2D no sistema de coordenadas cartesianas
 * Cada ponto é definido pelas coordenadas x e y
 *
 * @author Léo Souza
 * @version 09/02/26
 */
public class Ponto {
    public static final double eps = 1e-9;
    private final double x;
    private final double y;

    /**
     * Constrói um ponto no sistema de coordenadas cartesianas com as coordenadas especificadas.
     *
     * @param x Coordenada x do ponto.
     * @param y Coordenada y do ponto.
     * @pos getX == x
     * @pos getY == y
     */
    public Ponto(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Retorna a coordenada x do ponto no sistema de coordenadas cartesianas.
     *
     * @return o valor da coordenada x.
     */
    public double getX() {
        return x;
    }

    /**
     * Retorna a coordenada y do ponto ou vetor no sistema de coordenadas cartesianas.
     *
     * @return o valor da coordenada y.
     */
    public double getY() {
        return y;
    }

    /**
     * Compara o objeto atual com outro objeto especificado para verificar a igualdade.
     * Dois objetos da classe Engine.Ponto são considerados iguais se as suas coordenadas x e y
     * diferem por valores menores que um limite estabelecido (eps).
     *
     * @param o O objeto a ser comparado com o objeto atual.
     * @return {@code true} se os objetos forem considerados iguais, {@code false} caso contrário.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ponto ponto = (Ponto) o;

        double distancia_y = Math.abs(this.y - ponto.getY());
        double distancia_x = Math.abs(this.x - ponto.getX());

        return distancia_y < eps && distancia_x < eps;
    }

    /**
     * Calcula o código hash para este ponto com base nas suas coordenadas.
     * O código hash é derivado usando o método {@code Objects.hash}, incorporando os valores
     * das coordenadas {@code x} e {@code y}.
     *
     * @return um valor inteiro representando o código hash deste ponto.
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(x, y);
    }

    /**
     * Subtrai as coordenadas do ponto fornecido das coordenadas do ponto atual,
     * resultando num novo ponto representando a diferença vetorial entre eles.
     *
     * @param p O ponto cujas coordenadas serão subtraídas do ponto atual.
     * @return Um novo objeto Engine.Ponto representando a subtração das coordenadas dos dois pontos.
     * @pre p != null
     */
    public Ponto subtracao(Ponto p) {
        return new Ponto(this.x - p.getX(), this.y - p.getY());
    }

    /**
     * Calcula a distância entre o ponto atual e um outro ponto fornecido.
     * Se os dois pontos forem considerados iguais (com base no método {@code equals}),
     * a distância será considerada zero. Caso contrário, a distância é calculada
     * utilizando o módulo do vetor formado pelos dois pontos.
     *
     * @param p O ponto de destino para o qual a distância será calculada.
     * @return A distância entre o ponto atual e o ponto fornecido.
     * Retorna {@code 0.0} se os pontos forem iguais.
     * @pre p != null
     * @see <a href="https://pt.wikipedia.org/wiki/Dist%C3%A2ncia_euclidiana"> Wikipédia</a>
     */
    public double distanciaPara(Ponto p) {
        return (this.equals(p)) ? 0.0 : new Vetor(this, p).modulo();
    }

    /**
     * Retorna uma representação em formato textual do ponto no sistema de coordenadas cartesianas.
     * A representação segue o formato "(x, y)", onde x e y são as coordenadas do ponto
     * arredondadas para duas casas decimais.
     *
     * @return uma ‘string’ representando o ponto no formato "(x, y)".
     */
    @Override
    public String toString() {
        return String.format("(%.2f,%.2f)", this.x, this.y);
    }
}
