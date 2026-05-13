package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Ação responsável por alternar o modo de visualização da direção dos navios no mapa.
 * <p>
 * Permite alternar entre a exibição da direção aparente (afetada pela corrente)
 * e a direção normal (seguindo a rota planeada). Esta classe foi desenhada para ser
 * associada a um botão de alternância ({@code JToggleButton}) na interface gráfica.
 */
public class AcaoAlternarDirecao extends AbstractAction {
    private final PainelMapa painel;

    /**
     * Constrói uma nova ação de alternância de direção associada a um painel de mapa.
     *
     * @param painel O painel do mapa cuja visualização será alterada por esta ação.
     * @pre painel != null
     * @pos Associa a ação ao painel fornecido e define o título inicial como "Aparente (Corrente)".
     */
    public AcaoAlternarDirecao(PainelMapa painel) {
        super("Aparente (Corrente)");
        this.painel = painel;

        putValue(Action.SHORT_DESCRIPTION, "Alternar entre direção real e direção da rota");
    }

    /**
     * Executa a ação de alternância quando o componente associado é ativado.
     * <p>
     * Altera a configuração visual do painel para refletir o estado do botão,
     * atualiza o texto descritivo da própria ação e solicita o redesenho do ecrã.
     *
     * @param e O evento gerado pela interação do utilizador com o botão.
     * @pre e != null e e.getSource() é uma instância válida de JToggleButton.
     * @pos O estado de visualização (rota vs aparente) no painel é modificado, o nome da ação atualiza-se consoante esse estado, e o painel é redesenhado (repaint).
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JToggleButton botao = (JToggleButton) e.getSource();
        boolean selecionado = botao.isSelected();

        painel.setApontarParaRota(selecionado);

        putValue(Action.NAME, selecionado ? "Normal (Rota)" : "Aparente (Corrente)");

        painel.repaint();
    }
}
