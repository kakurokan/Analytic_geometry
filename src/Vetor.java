/**
 * Representa um vetor bidimensional no sistema de coordenadas cartesianas.
 * Um vetor é definido pelas componentes x e y e inclui operações para
 * calcular o módulo, o produto interno entre dois vetores e a similaridade
 * cosseno.
 *
 * @author Léo Souza
 * @version 09/02/26
 * @inv O módulo do vetor precisa ser positivo
 */
public class Vetor {
    private final double x;
    private final double y;

    /**
     * Constrói um vetor 2D no sistema de coordenadas cartesianas com as componentes especificadas.
     * O vetor representado por estas componentes deve ter módulo maior que zero.
     *
     * @param x Componente x do vetor.
     * @param y Componente y do vetor.
     */
    public Vetor(double x, double y) {
        if (Math.abs(x) < Ponto.eps && Math.abs(y) < Ponto.eps) {
            System.out.println("Vetor:iv");
            System.exit(0);
        }

        this.x = x;
        this.y = y;
    }

    /**
     * Constrói um vetor 2D no sistema de coordenadas cartesianas utilizando as
     * coordenadas de um ponto fornecido. O vetor representado por estas
     * componentes deve ter módulo maior que zero.
     *
     * @param p O ponto utilizado para definir as componentes x e y do vetor.
     */
    public Vetor(Ponto p) {
        this(p.getX(), p.getY());
    }

    /**
     * Retorna a coordenada x do vetor no sistema de coordenadas cartesianas.
     *
     * @return o valor da coordenada x.
     */
    public double getX() {
        return x;
    }

    /**
     * Retorna a coordenada y do vetor no sistema de coordenadas cartesianas.
     *
     * @return o valor da coordenada y.
     */
    public double getY() {
        return y;
    }

    /**
     * Calcula o módulo do vetor bidimensional, definido como a raiz quadrada
     * da soma dos quadrados das componentes x e y.
     *
     * @return o módulo do vetor.
     */
    public double modulo() {
        double sum = Math.pow(x, 2) + Math.pow(y, 2);
        return Math.sqrt(sum);
    }

    /**
     * Calcula o produto interno (ou produto escalar) entre o vetor atual e outro vetor fornecido.
     * O produto interno é definido como a soma do produto das componentes x e y dos dois vetores.
     *
     * @param v O vetor fornecido com o qual o produto interno será calculado.
     * @return o valor do produto interno entre os dois vetores.
     */
    public double produtoInterno(Vetor v) {

        return (this.x * v.x) + (this.y * v.y);
    }

    /**
     * Calcula a similaridade cosseno entre o vetor atual e outro vetor fornecido.
     * A similaridade cosseno é definida como o quociente entre o produto interno dos vetores
     * e o produto dos módulos dos dois vetores.
     *
     * @param v O vetor fornecido com o qual a similaridade cosseno será calculada.
     * @return o valor da similaridade cosseno entre os dois vetores.
     */
    public double cossineSimilarity(Vetor v) {

        return produtoInterno(v) / (this.modulo() * v.modulo());
    }

    /**
     * Calcula o ponto de interseção entre o vetor atual e um segmento de reta fornecido.
     * A interseção é determinada por meio do método de interseção implementado na classe
     * SegmentoReta.
     *
     * @param v O segmento de reta fornecido para calcular o ponto de interseção
     *          com o vetor atual.
     * @return O ponto de interseção entre o vetor atual e o segmento de reta fornecido,
     * ou {@code null} se não houver interseção.
     * @see SegmentoReta#intersect(Vetor)
     */
    public Ponto intersect(SegmentoReta v) {
        return v.intersect(this);
    }
}
