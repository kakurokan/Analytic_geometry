package Engine;

import java.util.List;
import java.util.Map;

/**
 * Representa um snapshot (foto) do estado atual da simulação de navegação.
 * Contém informações sobre navios em espera em diferentes portos, dados detalhados
 * dos navios, e o tempo de simulação em que o estado foi registrado.
 *
 * @author Acrismede Mendes, Alexandre Guerreiro, Léo Souza
 * @version 11/05/26
 * @inv naviosEmEsperaPorPorto != null
 * @inv dadosNavios != null
 * @inv tempoSimulacao >= 0
 */
public class SnapshotSimulacao {

    private final Map<String, List<NavioEmEspera>> naviosEmEsperaPorPorto;
    private final List<DadosNavio> dadosNavios;
    private final double tempoSimulacao;

    /**
     * Constrói um snapshot do estado atual da simulação.
     * Isto inclui informações sobre navios em espera em diferentes portos,
     * dados detalhados dos navios, e o tempo de simulação em que o snapshot foi tirado.
     *
     * @param naviosEmEsperaPorPorto um mapa onde as chaves representam identificadores de portos,
     *                               e os valores são listas de navios em espera nos respetivos portos
     * @param dadosNavios            uma lista contendo dados detalhados sobre navios na simulação,
     *                               incluindo posições, direções, estado de colisão e raio da área
     * @param tempoSimulacao         o tempo de simulação em que o snapshot foi tirado
     */
    public SnapshotSimulacao(Map<String, List<NavioEmEspera>> naviosEmEsperaPorPorto, List<DadosNavio> dadosNavios, double tempoSimulacao) {
        this.naviosEmEsperaPorPorto = Map.copyOf(naviosEmEsperaPorPorto);
        this.dadosNavios = dadosNavios;
        this.tempoSimulacao = tempoSimulacao;
    }

    /**
     * Obtém o tempo de simulação associado ao snapshot.
     *
     * @return o tempo de simulação em que o snapshot foi registrado, representado como um valor do tipo double.
     */
    public double getTempoSimulacao() {
        return tempoSimulacao;
    }

    /**
     * Obtém um mapa que associa identificadores de portos a listas de navios que estão em
     * espera nesses portos.
     *
     * @return um mapa onde as chaves representam os identificadores dos portos, e os valores
     * são listas de objetos do tipo NavioEmEspera correspondentes aos navios em
     * espera nesses portos.
     */
    public Map<String, List<NavioEmEspera>> getNaviosEmEsperaPorPorto() {
        return naviosEmEsperaPorPorto;
    }

    /**
     * Obtém a lista de objetos DadosNavio associados ao snapshot da simulação.
     * Cada objeto DadosNavio contém informações detalhadas sobre um navio,
     * como a sua posição, direção, estado de colisão e raio da área ao seu redor.
     *
     * @return uma lista de objetos DadosNavio representando os dados detalhados
     * dos navios na simulação.
     */
    public List<DadosNavio> getDadosNavios() {
        return dadosNavios;
    }

    /**
     * Representa um navio em espera associado a um determinado porto no contexto de uma simulação.
     * Um navio em espera possui informações sobre o horário de saída, destino e velocidade linear.
     *
     * @author Acrismede Mendes, Alexandre Guerreiro, Léo Souza
     * @version 11/05/26
     * @inv destino != null, !destino.isEmpty(), velocidadeLinear > 0, horarioSaida >= 0
     */
    public static class NavioEmEspera {
        private final int horarioSaida;
        private final String destino;
        private final double velocidadeLinear;

        /**
         * Constrói uma instância de NavioEmEspera com os valores fornecidos para o horário de saída, destino e velocidade linear.
         *
         * @param horarioSaida     o horário de saída do navio em espera, representado em formato de tempo discreto.
         * @param destino          o destino associado a este navio em espera.
         * @param velocidadeLinear a velocidade linear do navio em unidades de medida específicas.
         */
        public NavioEmEspera(int horarioSaida, String destino, double velocidadeLinear) {
            this.horarioSaida = horarioSaida;
            this.destino = destino;
            this.velocidadeLinear = velocidadeLinear;
        }

        /**
         * Retorna uma representação em string da instância NavioEmEspera, incluindo o horário de saída,
         * destino e velocidade linear.
         *
         * @return uma representação em string formatada do objeto no formato "T=<horarioSaida>, <destino>, <velocidadeLinear>".
         */
        @Override
        public String toString() {
            return String.format("T=%d, %s, %.2f", horarioSaida, destino, velocidadeLinear);
        }

        @Override
        public boolean equals(Object o){
            if (o == null || getClass() != o.getClass()) return false;
            NavioEmEspera navio = (NavioEmEspera) o;
            return this.destino.equals(navio.destino) && this.velocidadeLinear == navio.velocidadeLinear && this.horarioSaida ==  navio.horarioSaida;
        }
    }

    /**
     * Classe que encapsula os dados de um navio numa simulação.
     * Esta classe é utilizada para representar informações sobre a posição, direção,
     * estado de colisão e o raio de ação do navio, permitindo o acompanhamento
     * do estado do mesmo num determinado momento da simulação.
     *
     * @author Acrismede Mendes, Alexandre Guerreiro, Léo Souza
     * @version 11/05/26
     * @inv posicao != null, direcao != null, raioArea > 0
     */
    public static class DadosNavio {
        private final Ponto posicao;
        private final Vetor direcao;
        private final Vetor direcaoRota;
        private final boolean emColisao;
        private final double raioArea;

        /**
         * Constrói um objeto que encapsula os dados de um navio numa simulação.
         *
         * @param posicao   A posição atual do navio representada por um objeto da classe Ponto.
         * @param direcao   O vetor de direção do navio representado por um objeto da classe Vetor.
         * @param emColisao Indica se o navio está atualmente em estado de colisão. O valor é {@code true}
         *                  se o navio estiver em colisão, caso contrário {@code false}.
         * @param raioArea  O raio da área de ação do navio, usado para determinar sua influência na simulação.
         */
        public DadosNavio(Ponto posicao, Vetor direcao, Vetor direcaoRota, boolean emColisao, double raioArea) {
            this.posicao = posicao;
            this.direcao = direcao;
            this.direcaoRota = direcaoRota;
            this.emColisao = emColisao;
            this.raioArea = raioArea;
        }

        /**
         * Retorna a posição atual do navio representada por um objeto da classe {@code Ponto}.
         *
         * @return um objeto {@code Ponto} que representa a posição cartesianas (x, y) do navio.
         */
        public Ponto getPosicao() {
            return posicao;
        }

        /**
         * Retorna o vetor que representa a direção atual do navio.
         *
         * @return um objeto {@code Vetor} que indica a direção do navio na simulação.
         */
        public Vetor getDirecao() {
            return direcao;
        }

        /**
         * Retorna o vetor que representa a direção atual do navio conforme a rota.
         *
         * @return um objeto {@code Vetor} que indica a direção do navio na simulação.
         */
        public Vetor getDirecaoRota() {
            return direcaoRota;
        }

        /**
         * Verifica se o navio está atualmente em estado de colisão.
         *
         * @return {@code true} se o navio estiver em colisão, caso contrário {@code false}.
         */
        public boolean isEmColisao() {
            return emColisao;
        }

        /**
         * Retorna o raio da área de ação do navio na simulação.
         *
         * @return o raio da área de ação do navio como um valor de ponto flutuante do tipo {@code double}.
         */
        public double getRaioArea() {
            return raioArea;
        }

        @Override
        public boolean equals(Object o){
            if (o == null || getClass() != o.getClass()) return false;
            DadosNavio dadosNavio = (DadosNavio) o;
            return dadosNavio.emColisao == this.emColisao && dadosNavio.direcao.equals(this.direcao) && dadosNavio.posicao.equals(this.posicao) && dadosNavio.getRaioArea()==this.raioArea;

        }
    }
}
