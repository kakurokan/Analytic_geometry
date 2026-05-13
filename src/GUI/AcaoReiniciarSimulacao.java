package GUI;

import Engine.Simulador;
import Engine.Tempestade;
import Engine.Vetor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Representa uma ação para reiniciar a simulação dentro da aplicação.
 * Esta ação reinicia o estado da simulação, atualiza os componentes relevantes
 * e aciona a rotina de tráfego para refletir as mudanças.
 * <p>
 * Esta classe estende {@code AbstractAction}, permitindo que seja usada
 * como uma ação num componente GUI.
 *
 * @author Acrismede Mendes, Alexandre Guerreiro, Léo Souza
 * @version 11/05/26
 * @inv simulador != null, painel != null e rotinaDeTrafego != null
 */
public class AcaoReiniciarSimulacao extends AbstractAction {
    private final Simulador simulador;
    private final PainelMapa painel;
    private final Runnable rotinaDeTrafego;

    /**
     * Construtor da classe {@code AcaoReiniciarSimulacao}.
     * Inicializa a ação "Reiniciar" para reiniciar o estado da simulação, atualizar
     * os componentes relevantes e acionar uma rotina específica relacionada ao tráfego.
     *
     * @param simulador       o objeto {@code Simulador} responsável pela lógica principal da simulação.
     * @param painel          o objeto {@code PainelMapa} responsável por exibir o estado atual da simulação.
     * @param rotinaDeTrafego uma rotina {@code Runnable} que é executada para gerir o fluxo de tráfego
     *                        após a reinicialização da simulação.
     * @pre simulador != null, painel != null e rotinaDeTrafego != null
     * @pos Associa a ação aos elementos do sistema e define o título do botão como "Reiniciar".
     */
    public AcaoReiniciarSimulacao(Simulador simulador, PainelMapa painel, Runnable rotinaDeTrafego) {
        super("Reiniciar");

        this.simulador = simulador;
        this.painel = painel;
        this.rotinaDeTrafego = rotinaDeTrafego;
    }

    /**
     * Trata a ação de reiniciar a simulação quando acionada.
     * Atualiza o estado da simulação, atualiza os componentes de visualização,
     * e executa a rotina de tráfego associada para refletir as mudanças.
     *
     * @param e o {@code ActionEvent} que acionou esta ação, tipicamente
     *          gerado por um componente GUI.
     * @pos Pede ao utilizador uma nova corrente marítima.
     * Se confirmada, atualiza o simulador e o painel, recria as tempestades,
     * relança a rotina de tráfego e redesenha o mapa atualizado.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Vetor corrente = DialogoCorrente.pedirCorrente(painel.getCorrente());

        if (corrente != null) {

            simulador.setCorrente(corrente);
            painel.setCorrente(corrente);

            List<Tempestade> novasTempestades = simulador.reiniciarSimulacao();
            painel.setTempestades(novasTempestades);
            rotinaDeTrafego.run();
            painel.atualizarSnapshot(simulador.gerarSnapshot());
            painel.repaint();
        }
    }
}
