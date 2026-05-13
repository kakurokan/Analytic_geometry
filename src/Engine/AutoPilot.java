package Engine;

/**
 * @author Acrismede Mendes, Alexandre Guerreiro, Léo Souza
 * @version 24/02/26
 * @inv o vetor r não pode ser nulo
 * A classe Engine.AutoPilot representa um sistema de navegação automatizado que calcula
 * a velocidade ajustada e o tempo necessário para percorrer uma rota entre dois pontos,
 * levando em consideração fatores como velocidade vetorial da corrente.
 */
public class AutoPilot {
    private final Vetor r;

    /**
     * Constrói um objeto da classe Engine.AutoPilot representando a rota entre dois pontos.
     *
     * @param a O ponto inicial da rota.
     * @param b O ponto final da rota.
     * @pre a != null, b != null e a != b.
     * @pos O objeto é inicializado com um vetor de rota válido (r != null e r.modulo() > 0)
     */
    public AutoPilot(Ponto a, Ponto b) {
        this.r = new Vetor(a, b);
    }

    /**
     * Calcula a velocidade ajustada conforme o vetor de velocidade vetorial da corrente e o tempo fornecido.
     * A velocidade ajustada é obtida subtraindo o vetor da velocidade vetorial da corrente de rota
     * e multiplicando pelo inverso do tempo.
     *
     * @param velocidadeCorrente O vetor que representa a velocidade vetorial da corrente, que será subtraído do vetor de rota.
     * @param tempo              O tempo em que a rota é percorrida, utilizado para ajustar a velocidade.
     * @return Um novo vetor que representa a velocidade ajustada com base no vetor de rota,
     * na velocidade da corrente e no tempo.
     * @pre velocidadeCorrente != null e tempo > 0.0
     * @pos O vetor retornado não é nulo.
     */
    public Vetor speed(Vetor velocidadeCorrente, double tempo) {
        return r.multi(1 / tempo).sub(velocidadeCorrente);
    }

    /**
     * Calcula o tempo necessário para percorrer a rota do vetor com uma velocidade linear específica,
     * ajustando o cálculo pela influência de um vetor de velocidade vetorial da corrente fornecida.
     *
     * @param linearSpeed A velocidade linear constante utilizada na travessia da rota.
     * @return O tempo necessário para percorrer a rota, ajustado pela velocidade do vento.
     * @pre linearSpeed > 0.0
     * @pos O valor retornado é > 0.0.
     */
    public double time(double linearSpeed) {
        return r.modulo() / linearSpeed;
    }
}
