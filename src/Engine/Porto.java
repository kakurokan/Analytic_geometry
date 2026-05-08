package Engine;

import java.util.*;

/**
 * Representa um porto numa simulação marítima, responsável por gerir navios à espera
 * de partir e fornecer operações relacionadas.
 * <p>
 * Esta classe mantém uma fila de navios à espera de partir, ordenados por hora de partida.
 * Cada porto tem um nome, uma localização (posição), uma torre de controlo associada e uma
 * área operacional para navios.
 *
 * @author Acrismede Mendes, Alexandre Guerreiro, Léo Souza
 * @version 11/05/26
 * @inv nome != null, !nome.isEmpty(), posicao != null, torre != null e naviosEmEspera != null
 */
public class Porto {
    private static final double RAIO_AREA_NAVIO = 1;
    private static final Comparator<Navio> COMPARADOR_POR_HORARIO_PARTIDA =
            Comparator.comparingInt(Navio::getHorarioPartida);
    private final String nome;
    private final TorreDeControlo torre;
    private final Ponto posicao;
    private final PriorityQueue<Navio> naviosEmEspera;

    /**
     * Constrói um novo objeto Porto com um nome, posição, e uma torre de controle associada.
     *
     * @param nome    O nome do porto.
     * @param posicao A posição do porto especificada como um ponto 2D.
     * @param torre   A torre de controle associada responsável por gerir os navios.
     */
    public Porto(String nome, Ponto posicao, TorreDeControlo torre) {
        this.nome = nome;
        this.posicao = posicao;
        this.torre = torre;
        this.naviosEmEspera = new PriorityQueue<>(COMPARADOR_POR_HORARIO_PARTIDA);
    }

    /**
     * Adiciona um novo navio à lista de navios em espera no porto.
     *
     * @param velocidadeLinear A velocidade linear do navio a ser criado.
     * @param horarioPartida   O horário previsto de partida do navio.
     * @param destino          O porto de destino do navio.
     * @return O objeto Navio que foi criado e adicionado à lista de espera.
     */
    public Navio adicionarNavio(double velocidadeLinear, int horarioPartida, Porto destino) {
        Navio navio = criarNavio(velocidadeLinear, horarioPartida, destino);
        naviosEmEspera.add(navio);
        return navio;
    }

    /**
     * Cria uma instância de Navio associada ao porto atual.
     *
     * @param velocidadeLinear A velocidade linear do navio.
     * @param horarioPartida   O horário previsto de partida do navio.
     * @param destino          O porto de destino do navio.
     * @return Uma nova instância de Navio configurada com os parâmetros fornecidos.
     */
    private Navio criarNavio(double velocidadeLinear, int horarioPartida, Porto destino) {
        Circulo area = new Circulo(posicao, RAIO_AREA_NAVIO);
        return new Navio(area, velocidadeLinear, horarioPartida, this, destino, torre);
    }

    /**
     * Limpa a lista de navios em espera no porto, removendo todos os elementos dela.
     * <p>
     * Esta operação serve para reiniciar o estado da fila de navios que aguardam
     * no porto, garantindo que nenhum navio permaneça na lista de espera.
     */
    public void limparFila() {
        this.naviosEmEspera.clear();
    }

    /**
     * Retorna a posição atual do porto no sistema de coordenadas cartesianas.
     *
     * @return Um objeto da classe {@code Ponto} que representa a posição do porto,
     * definido pelas coordenadas x e y.
     */
    public Ponto getPosicao() {
        return this.posicao;
    }

    /**
     * Retorna o nome do porto.
     *
     * @return O nome do porto.
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Obtém uma lista de navios atualmente à espera no porto.
     * Cada entrada na lista representa um instantâneo do estado do navio em espera,
     * incluindo a sua hora de partida agendada, destino e velocidade linear.
     *
     * @return Uma lista de objetos {@code SnapshotSimulacao.NavioEmEspera} representando
     * os navios à espera no porto.
     */
    public List<SnapshotSimulacao.NavioEmEspera> getNaviosEmEspera() {
        List<SnapshotSimulacao.NavioEmEspera> fila = new ArrayList<>();

        for (Navio navio : naviosEmEspera) {
            fila.add(new SnapshotSimulacao.NavioEmEspera(
                    navio.getHorarioPartida(),
                    navio.getPortoDestino().getNome(),
                    navio.getVelocidadeLinear()
            ));
        }

        return fila;
    }

    /**
     * Retorna um iterador que percorre os navios prontos para partir com base no tempo atual.
     * Um navio é considerado pronto para partir se a sua hora atual for maior ou igual ao tempo
     * especificado no parâmetro.
     *
     * @param tempoAtual O tempo atual utilizado para verificar se os navios estão prontos para partir.
     * @return Um iterador que fornece acesso aos navios prontos para partir no porto.
     */
    public Iterator<Navio> naviosProntos(double tempoAtual) {
        return new IteradorNaviosProntos(tempoAtual);
    }

    /**
     * Iterador responsável por percorrer a fila de navios em espera no porto para identificar
     * quais navios estão prontos para partir com base no tempo atual fornecido.
     * <p>
     * Esta classe privada é utilizada internamente para permitir a iteração eficiente
     * sobre os navios cuja hora de partida é menor ou igual ao tempo atual informado.
     * O iterador consome os navios da fila conforme eles são iterados.
     * <p>
     * Implementa a interface {@code Iterator<Navio>}, fornecendo os métodos necessários para
     * navegar pelos navios elegíveis.
     */
    private class IteradorNaviosProntos implements Iterator<Navio> {
        private final double tempoAtual;

        /**
         * Construtor privado da classe IteradorNaviosProntos.
         * <p>
         * Inicializa o iterador configurando o tempo atual que será utilizado
         * para determinar os navios prontos para partir.
         *
         * @param tempoAtual O instante de tempo atual utilizado como referência
         *                   para verificar quais navios estão prontos para partir.
         */
        private IteradorNaviosProntos(double tempoAtual) {
            this.tempoAtual = tempoAtual;
        }

        /**
         * Verifica se ainda existem navios na fila de espera cujo horário de partida é menor ou igual ao tempo atual.
         *
         * @return true se a fila de navios em espera não estiver vazia e o próximo navio na fila estiver
         * pronto para partir (ou seja, seu horário de partida é menor ou igual ao tempo atual);
         * false caso contrário.
         */
        @Override
        public boolean hasNext() {
            return !naviosEmEspera.isEmpty()
                    && naviosEmEspera.peek().getHorarioPartida() <= tempoAtual;
        }

        /**
         * Retorna o próximo navio na fila de espera que está pronto para partir, caso exista.
         * O método verifica se há um próximo navio elegível com base no horário de partida
         * em comparação com o tempo atual fornecido.
         *
         * @return o próximo navio na fila de espera que está pronto para partir se {@code hasNext()} for true,
         * ou {@code null} caso contrário.
         */
        @Override
        public Navio next() {
            return hasNext() ? naviosEmEspera.poll() : null;
        }
    }
}
