package Engine;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um segmento de reta num espaço bidimensional.
 * O segmento de reta é definido por dois pontos distintos,
 * chamados extremidades, representados pelas instâncias da classe Engine.Ponto.
 *
 * @author Léo Souza
 * @version 09/02/26
 * @inv Os dois pontos que definem o segmento de reta (A e B) têm de ser diferentes.
 */
public class SegmentoReta implements Obstaculo {
    private final Ponto a;
    private final Ponto b;

    /**
     * Constrói um segmento de reta num espaço bidimensional.
     * O segmento de reta é definido por um ponto de origem e um vetor
     * que indica direção e comprimento do segmento.
     *
     * @param p O ponto de origem do segmento de reta.
     * @param v O vetor que define a direção e o comprimento do segmento de reta.
     * @pre p != null e v != null.
     * @pos getA() == p e getB() == new Engine.Ponto(p.getX() + v.getX(), p.getY() + v.getY()).
     */
    public SegmentoReta(Ponto p, Vetor v) {
        Ponto ponto = new Ponto(p.getX() + v.getX(), p.getY() + v.getY());
        this(p, ponto);
    }

    /**
     * Constrói um segmento de reta definido por dois pontos distintos no espaço bidimensional.
     * Os pontos fornecidos representam as extremidades do segmento de reta.
     * Caso ambos os pontos sejam iguais, será lançada uma IllegalArgumentException.
     *
     * @param a O primeiro ponto extremo do segmento de reta.
     * @param b O segundo ponto extremo do segmento de reta.
     * @pre a != null, b != null e a != b.
     * @pos getA() == a
     * @pos getB() == b
     */
    public SegmentoReta(Ponto a, Ponto b) {
        this.a = new Ponto(a.getX(), a.getY());
        this.b = new Ponto(b.getX(), b.getY());

        check();
    }

    /**
     * Retorna o ponto de origem do segmento de reta.
     *
     * @return o ponto extremo inicial do segmento de reta.
     */
    public Ponto getA() {
        return a;
    }

    /**
     * Retorna o ponto extremo final do segmento de reta.
     *
     * @return o ponto extremo final do segmento de reta.
     */
    public Ponto getB() {
        return b;
    }

    /**
     * Verifica a validade do segmento de reta com base nas extremidades definidas.
     * Conforme o critério de igualdade estabelecido na classe {@code Engine.Ponto},
     *
     * @throws IllegalArgumentException Se as extremidades do segmento de reta forem consideradas iguais,
     *                                  com a mensagem "Engine.SegmentoReta:iv".
     */
    private void check() {
        if (this.a.equals(this.b)) {
            throw new IllegalArgumentException("Engine.SegmentoReta:iv");
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

        Ponto origem = new Ponto(0, 0);

        double distancia_a = a.distanciaPara(origem);
        double distancia_b = b.distanciaPara(origem);

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
     * @pre p != null
     * @see <a href="https://www.geeksforgeeks.org/dsa/check-if-two-given-line-segments-intersect/">Geeks for geeks</a>
     */
    public boolean noSegmento(Ponto p) {

        double distAP = this.a.distanciaPara(p);

        double distPB = p.distanciaPara(this.b);

        double distAB = this.a.distanciaPara(this.b);

        double diferenca = Math.abs((distAP +distPB)-distAB);
        return diferenca <= Ponto.eps;
    }

    /**
     * Calcula o ponto de interseção entre o segmento de reta atual e outro segmento de reta especificado,
     * caso a interseção exista. Se os segmentos forem colineares, verifica os pontos em comum e retorna
     * o primeiro ponto de interseção encontrado. Retorna {@code null} caso não haja interseção ou se
     * os segmentos forem paralelos (exceto no caso colinear).
     *
     * @param seg O segmento de reta com o qual será calculada a interseção.
     * @return O ponto de interseção entre os dois segmentos de reta, ou {@code null} caso não exista
     * interseção, os segmentos sejam paralelos ou não compartilhem uma região comum.
     * @pre seg != null
     * @see <a href="https://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect">Stack Overflow</a>,
     * <a href="https://theswissbay.ch/pdf/Gentoomen%20Library/Game%20Development/Programming/Graphics%20Gems%201.pdf">Livro de referência</a>
     */
    public Ponto intersect(SegmentoReta seg) {
        Vetor r = new Vetor(this.a, this.b);
        Vetor s = new Vetor(seg.getA(), seg.getB());
        if (this.a.equals(seg.getA())) {return a;}
        Vetor k = new Vetor(this.a, seg.getA());

        double numerador = (k).produtoVetorial(r);
        double denominador = r.produtoVetorial(s);

        //Neste caso, os segmentos de reta são colineares
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
        if (Math.abs(denominador) < Ponto.eps)
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

    /**
     * Calcula o ponto de interseção entre o segmento de reta atual e um vetor fornecido,
     * caso a interseção exista. O vetor é tratado como um segmento de reta que começa
     * na origem (0,0) e segue na direção definida pelo vetor.
     *
     * @param v O vetor que define a direção e o comprimento do segmento de reta a ser utilizado
     *          para cálculo da interseção com o segmento de reta atual.
     * @return O ponto de interseção entre os dois segmentos de reta, ou {@code null} caso
     * não exista interseção ou os segmentos sejam paralelos (incluindo o caso colinear).
     * @pre v != null
     */
    public Ponto intersect(Vetor v) {
        SegmentoReta seg = new SegmentoReta(new Ponto(0, 0), v);
        return this.intersect(seg);
    }

    /**
     * Calcula os pontos de interseção entre o segmento de reta atual e um círculo especificado.
     * Se o segmento de reta não intersectar o círculo, retorna {@code null}.
     * Caso existam pontos de interseção, eles são retornados como uma lista de objetos da classe {@code Engine.Ponto}.
     *
     * @param circulo O círculo com o qual será calculada a interseção do segmento de reta.
     *                Deve possuir um centro e um raio válidos (raio maior que zero).
     * @return Uma lista contendo os pontos de interseção entre o segmento de reta e o círculo.
     * Caso não haja interseção, retorna {@code null}.
     * @pre circulo != null
     * @see <a href="https://stackoverflow.com/a/1084899/13884223/">StackOverflow</a>
     */
    public List<Ponto> intersect(Circulo circulo) {
        List<Ponto> intersecoes = new ArrayList<>();

        Vetor d = new Vetor(this.a, this.b);
        double fX = this.a.getX() - circulo.getCentro().getX();
        double fY = this.a.getY() - circulo.getCentro().getY();

        double a = d.produtoInterno(d);
        double b = 2 * (fX * d.getX() + fY * d.getY());
        double c = (fX * fX + fY * fY) - (circulo.getRaio() * circulo.getRaio());

        double discriminante = (b * b) - (4 * a * c);

        //Não há interseção
        if (discriminante < 0) {
            return null;
        }

        discriminante = Math.sqrt(discriminante);

        //A solução pode estar atrás ou na frente do segmento, é necessário testar os dois
        //t1 é sempre o menor valor
        double t1 = (-b - discriminante) / (2 * a);
        double t2 = (-b + discriminante) / (2 * a);

        // Verifica o t1 (sempre o primeiro ponto alcançado no sentido de A para B)
        if (t1 >= 0 && t1 <= 1) {
            intersecoes.add(new Ponto(this.a.getX() + t1 * d.getX(), this.a.getY() + t1 * d.getY()));
        }

        // Verifica o t2 (garantindo que não é o mesmo ponto em caso de tangência)
        if (Math.abs(t1 - t2) > Ponto.eps && t2 >= 0 && t2 <= 1) {
            intersecoes.add(new Ponto(this.a.getX() + t2 * d.getX(), this.a.getY() + t2 * d.getY()));
        }

        return intersecoes.isEmpty() ? null : intersecoes;
    }

    /**
     * Calcula o comprimento do segmento de reta baseado na distância entre os seus dois pontos extremos.
     * O comprimento é determinado utilizando o método {@code distanciaPara} da classe {@code Engine.Ponto},
     * que calcula a distância entre dois pontos no espaço bidimensional.
     *
     * @return O comprimento do segmento de reta, correspondente à distância entre os dois pontos extremos.
     */
    public double comprimento() {
        return this.a.distanciaPara(this.b);
    }

    /**
     * Verifica se o segmento de reta atual tem o mesmo comprimento que outro segmento de reta especificado.
     * A comparação é feita a utilizar uma margem de erro definida por 1e-9.
     *
     * @param seg O segmento de reta a ser comparado com o segmento atual.
     * @return {@code true} se os dois segmentos possuem comprimentos iguais dentro do limite de precisão definido,
     * {@code false} caso contrário.
     * @pre seg != null
     */
    public boolean comprimentoDiferente(SegmentoReta seg) {
        return !(Math.abs(this.comprimento() - seg.comprimento()) < Ponto.eps);
    }

    /**
     * Calcula os pontos de interseção entre o segmento de reta atual e uma rota especificada.
     * A rota é representada como uma coleção de segmentos de reta, e este método determina os
     * pontos nos quais os segmentos de reta da rota intersetam o segmento atual. Se não houver
     * interseções, o método retorna {@code null}.
     *
     * @param rota A rota com a qual calcular os pontos de interseção. A rota é definida
     *             como uma coleção de segmentos de reta.
     * @return Uma lista contendo os pontos de interseção entre o segmento de reta atual e a rota.
     * Se nenhuma interseção for encontrada, retorna {@code null}.
     * @pre rota != null
     */
    @Override
    public List<Ponto> intersect(Route rota) {
        ArrayList<Ponto> intersecoes = new ArrayList<>();
        List<SegmentoReta> segmentos = rota.getSegmentos();

        for (SegmentoReta segmentoRota : segmentos) {
            Ponto p = this.intersect(segmentoRota);
            if (p != null)
                intersecoes.add(p);

        }

        return (intersecoes.isEmpty()) ? null : intersecoes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SegmentoReta seg = (SegmentoReta) o;

        return ((this.a.equals(seg.getA()) || this.a.equals(seg.getB())) && (this.b.equals(seg.getA()) || this.b.equals(seg.getB())));
    }
}
