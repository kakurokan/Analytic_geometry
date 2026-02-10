/**
 * Representa um segmento de reta num espaço bidimensional.
 * O segmento de reta é definido por dois pontos distintos,
 * chamados extremidades, representados pelas instâncias da classe Ponto.
 *
 * @author Léo Souza
 * @version 09/02/26
 * @inv As coordenadas do ponto p precisa ser diferentes das coordenadas do vetor v
 */
public class SegmentoReta {
    private final Ponto a;
    private final Ponto b;

    /**
     * Constrói um segmento de reta em um espaço bidimensional.
     * O segmento de reta é definido por um ponto de origem e um vetor
     * que indica direção e comprimento do segmento.
     *
     * @param p O ponto de origem do segmento de reta.
     * @param v O vetor que define a direção e o comprimento do segmento de reta.
     */
    public SegmentoReta(Ponto p, Vetor v) {
        Ponto ponto = new Ponto(p.getX() + v.getX(), p.getY() + v.getY());
        this(p, ponto);
    }

    /**
     * Constrói um segmento de reta definido por dois pontos distintos no espaço bidimensional.
     * Os pontos fornecidos representam as extremidades do segmento de reta.
     * Caso ambos os pontos sejam iguais, o programa será encerrado.
     *
     * @param a O primeiro ponto extremo do segmento de reta.
     * @param b O segundo ponto extremo do segmento de reta.
     */
    public SegmentoReta(Ponto a, Ponto b) {
        this.a = a;
        this.b = b;

        check();
    }

    /**
     * Verifica a validade do segmento de reta com base nas extremidades definidas.
     * Se as extremidades do segmento de reta forem consideradas iguais,
     * conforme o critério de igualdade estabelecido na classe {@code Ponto},
     * imprime uma mensagem de erro e encerra a execução do programa.
     *
     * @inv Os dois pontos que definem o segmento de reta devem ser distintos.
     */
    private void check() {
        if (this.a.equals(this.b)) {
            System.out.println("SegmentoReta:iv");
            System.exit(0);
        }
    }

    /**
     * Gera uma representação em texto do segmento de reta.
     * A representação descreve os dois pontos extremos do segmento de reta (a ordem dos pontos é determinada
     * com base na sua proximidade relativamente à origem do sistema de coordenadas cartesianas),
     * conforme o formato "sr(P1; P2)", onde P1 e P2 são as representações textuais dos pontos.
     *
     * @return uma representação em texto do segmento de reta no formato "sr(P1; P2)".
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("sr(");

        double distancia_a = a.distanciaDaOrigem();
        double distancia_b = b.distanciaDaOrigem();

        String primeiro;
        String segundo;

        if (distancia_a < distancia_b) {
            primeiro = a.toString();
            segundo = b.toString();
        } else {
            primeiro = b.toString();
            segundo = a.toString();
        }

        sb.append(primeiro).append("; ").append(segundo).append(")");

        return sb.toString();

    }


    /**
     * Verifica se um ponto especificado está localizado dentro dos limites do segmento de reta,
     * considerando as coordenadas mínimas e máximas dos extremos do segmento.
     *
     * @param p O ponto a ser verificado.
     * @return {@code true} se o ponto está dentro dos limites do segmento de reta, {@code false} caso contrário.
     * @see <a href="https://www.geeksforgeeks.org/dsa/check-if-two-given-line-segments-intersect/">...</a>
     */
    private boolean noSegmento(Ponto p) {
        return p.getX() <= Math.max(this.a.getX(), this.b.getX())
                && p.getX() >= Math.min(this.a.getX(), this.b.getX())
                && p.getY() <= Math.max(this.a.getY(), this.b.getY())
                && p.getY() >= Math.min(this.a.getY(), this.b.getY());
    }

    /**
     * Calcula o ponto de interseção entre o segmento de reta atual e um vetor fornecido,
     * caso a interseção exista. O vetor é considerado como um segmento de reta que
     * começa na origem e segue na direção do vetor.
     *
     * @param v O vetor que define o segmento de reta com o qual será verificada a interseção.
     * @return O ponto de interseção entre os dois segmentos de reta, ou {@code null}
     * caso não exista interseção ou os segmentos sejam paralelos (incluindo o caso colinear).
     * @see <a href="https://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect">...</a>
     */
    Ponto intersect(Vetor v) {
        SegmentoReta seg = new SegmentoReta(new Ponto(0, 0), v);

        Ponto r = this.b.subtracao(this.a);
        Ponto s = seg.b.subtracao(seg.a);
        Ponto k = seg.a.subtracao(this.a);

        double numerador = (k).produtoVetorial(r);
        double denominador = r.produtoVetorial(s);

        //Neste caso, os segmentos de reta sao colineares
        if (Math.abs(numerador) < Ponto.eps && Math.abs(denominador) < Ponto.eps) {
            if (this.noSegmento(seg.a))
                return seg.a;
            if (this.noSegmento(seg.b))
                return seg.b;
            if (seg.noSegmento(this.a))
                return this.a;
            if (seg.noSegmento(this.b))
                return this.b;
        }

        //Neste caso, os segmentos de reta são paralelos
        if (denominador < Ponto.eps)
            return null;

        double u = numerador / denominador;
        double t = (k).produtoVetorial(s) / denominador;

        if (t >= 0 && t <= 1 && u >= 0 && u <= 1) {
            double intersecao_x = this.a.getX() + t * r.getX();
            double intersecao_y = this.a.getY() + t * r.getY();

            return new Ponto(intersecao_x, intersecao_y);
        }

        return null;
    }

}
