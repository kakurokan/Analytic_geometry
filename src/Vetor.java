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
     * Constrói um vetor 2D no sistema de coordenadas cartesianas utilizando dois pontos fornecidos.
     * O vetor construído representa a diferença entre o ponto final e o ponto inicial especificados.
     *
     * @param a O ponto inicial utilizado para calcular o vetor.
     * @param b O ponto final utilizado para calcular o vetor.
     */
    public Vetor(Ponto a, Ponto b) {
        this(b.getX() - a.getX(), b.getY() - a.getY());
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

    /**
     * Multiplica as componentes do vetor atual por um escalar fornecido.
     *
     * @param d O escalar pelo qual as componentes do vetor serão multiplicadas.
     * @return Um novo vetor cujas componentes são o resultado da multiplicação do vetor atual pelo escalar.
     */
    public Vetor mult(double d) {
        return new Vetor(this.x * d, this.y * d);
    }

    /**
     * Soma o vetor atual com outro vetor fornecido.
     * A soma é realizada componente a componente, resultando em um novo vetor.
     *
     * @param v O vetor a ser somado ao vetor atual.
     * @return Um novo vetor que é o resultado da soma dos dois vetores.
     */
    public Vetor add(Vetor v) {
        return new Vetor(v.getX() + this.x, v.getY() + this.y);
    }

    /**
     * Subtrai o vetor atual por outro vetor fornecido. A subtração é realizada
     * componente a componente, resultando em um novo vetor.
     *
     * @param v O vetor a ser subtraído do vetor atual.
     * @return Um novo vetor que é o resultado da subtração do vetor fornecido
     * do vetor atual.
     */
    public Vetor sub(Vetor v) {
        return new Vetor(this.x - v.getX(), this.y - v.getY());
    }

    @Override
    public String toString() {
        return String.format("[%.2f,%.2f]", this.x, this.y);
    }
}
