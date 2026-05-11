package GUI;

import Engine.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * A classe JanelaPrincipal representa a janela principal da aplicação para um simulador marítimo.
 * Esta classe é responsável por renderizar o mapa, incorporar interações com os
 * componentes da simulação e gerir as atualizações temporizadas do estado da simulação.
 * <p>
 * A janela inclui um painel de mapa, um botão para reiniciar a simulação e um
 * temporizador que controla as atualizações periódicas para simular comportamento em tempo real.
 * <p>
 * Coordena interações entre o modelo de simulação, os elementos da interface do utilizador
 * e os componentes relacionados da aplicação.
 *
 * @author Acrismede Mendes, Alexandre Guerreiro, Léo Souza
 * @version 11/05/26
 * @inv simulador != null
 */
public class JanelaPrincipal extends JFrame {
    private final Timer timer;
    private final PainelMapa painel;

    /**
     * Construtor da classe JanelaPrincipal que inicializa a interface gráfica para o simulador marítimo.
     * Este construtor configura a janela principal, inclui um painel de mapa para exibição
     * das rotas, obstáculos, tempestades e portos, e define um temporizador para atualizar o andamento da simulação.
     *
     * @param simulador          O objeto Simulador responsável pela lógica da simulação marítima.
     * @param rotas              Lista de rotas marítimas utilizadas na simulação.
     * @param obstaculos         Lista de obstáculos representados por polígonos na área de simulação.
     * @param tempestades        Lista de tempestades ativas na área de simulação.
     * @param posicoesPortos     Mapeamento entre os nomes dos portos e suas respectivas localizações.
     * @param velocidadeCorrente Vetor representando a velocidade da corrente marítima no ambiente simulado.
     * @param recriarBarcos      Runnable que define a lógica para recriar os barcos usados na simulação.
     */
    public JanelaPrincipal(Simulador simulador,
                           List<Route> rotas,
                           List<Poligono> obstaculos,
                           List<Tempestade> tempestades,
                           Map<String, Ponto> posicoesPortos,
                           Vetor velocidadeCorrente,
                           Runnable recriarBarcos) {
        this.painel = new PainelMapa(rotas, tempestades, obstaculos, posicoesPortos, velocidadeCorrente);

        setTitle("Simulador marítimo");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(painel, BorderLayout.CENTER);

        // PAINEL INFERIOR (onde ficam todos os controlos)
        JPanel painelSul = new JPanel();

        AcaoReiniciarSimulacao reset = new AcaoReiniciarSimulacao(simulador, painel, recriarBarcos);
        JButton botaoReset = new JButton(reset);

        AcaoAlternarDirecao acaoAlternar = new AcaoAlternarDirecao(this.painel);
        JToggleButton btnAlternar = new JToggleButton(acaoAlternar);
        btnAlternar.setFocusable(false);

        painelSul.add(botaoReset);
        painelSul.add(Box.createHorizontalStrut(40)); // Cria um espaço em branco a separar os dois controlos
        painelSul.add(new JLabel("Orientação do Navio:"));
        painelSul.add(btnAlternar);

        add(painelSul, BorderLayout.SOUTH);

        this.timer = new Timer(16, e -> {
            simulador.atualizar(0.016 * 0.5);
            painel.atualizarSnapshot(simulador.gerarSnapshot());
            painel.repaint();
        });
    }

    /**
     * Inicia a janela principal da aplicação e dá início ao temporizador da simulação.
     * <p>
     * Este método torna a janela visível para o utilizador e inicia o temporizador,
     * que é responsável por executar atualizações periódicas na interface e no
     * estado da simulação. O temporizador ajuda a simular o comportamento em tempo
     * real, atualizando o mapa e os elementos relacionados.
     */
    public void iniciar() {
        setVisible(true);
        timer.start();
    }
}
