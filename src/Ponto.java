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
     * Calcula a distância do ponto atual à origem no sistema de coordenadas cartesianas.
     * A distância é calculada usando a fórmula da distância euclidiana, dada como a
     * raiz quadrada da soma dos quadrados das coordenadas x e y do ponto.
     *
     * @return a distância do ponto atual à origem.
     */
    public double distanciaDaOrigem() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    /**
     * Compara o objeto atual com outro objeto especificado para verificar a igualdade.
     * Dois objetos da classe Ponto são considerados iguais se as suas coordenadas x e y
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
     * Calcula o produto vetorial (ou determinante) entre o ponto atual e outro ponto fornecido.
     * O produto vetorial é definido como (this.x * p.getY()) - (this.y * p.getX()).
     *
     * @param p O ponto fornecido para calcular o produto vetorial com o ponto atual.
     * @return O valor do produto vetorial entre os dois pontos.
     */
    public double produtoVetorial(Ponto p) {
        return this.x * p.getY() - this.y * p.getX();
    }

    public Ponto subtracao(Ponto p) {
        return new Ponto(this.x - p.getX(), this.y - p.getY());
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
