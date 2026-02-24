/**
 * @author Léo Souza
 * @version 24/02/26
 * A classe AutoPilot representa um sistema de navegação automatizado que calcula
 * a velocidade ajustada e o tempo necessário para percorrer uma rota entre dois pontos,
 * levando em consideração fatores como velocidade do vento.
 */
public class AutoPilot {
    private final Vetor r;

    /**
     * Constrói um objeto da classe AutoPilot representando a rota entre dois pontos.
     *
     * @param a O ponto inicial da rota.
     * @param b O ponto final da rota.
     */
    public AutoPilot(Ponto a, Ponto b) {
        this.r = new Vetor(a, b);
    }

    /**
     * Calcula a velocidade ajustada conforme o vetor de velocidade do vento e o tempo fornecido.
     * A velocidade ajustada é obtida subtraindo o vetor da velocidade do vento do vetor de rota
     * e multiplicando pelo inverso do tempo.
     *
     * @param windSpeed O vetor que representa a velocidade do vento, que será subtraído do vetor de rota.
     * @param time      O tempo em que a rota é percorrida, utilizado para ajustar a velocidade.
     * @return Um novo vetor que representa a velocidade ajustada com base no vetor de rota,
     * na velocidade do vento e no tempo.
     */
    public Vetor speed(Vetor windSpeed, double time) {
        return r.sub(windSpeed).mult(1 / time);
    }

    /**
     * Calcula o tempo necessário para percorrer a rota do vetor com uma velocidade linear específica,
     * ajustando o cálculo pela influência de um vetor de velocidade do vento fornecido.
     *
     * @param windSpeed   O vetor que representa a velocidade do vento, utilizado para ajustar a rota.
     * @param linearSpeed A velocidade linear constante utilizada na travessia da rota.
     * @return O tempo necessário para percorrer a rota, ajustado pela velocidade do vento.
     */
    public double time(Vetor windSpeed, double linearSpeed) {
        Vetor diferenca = r.sub(windSpeed);
        return diferenca.modulo() / linearSpeed;
    }
}
