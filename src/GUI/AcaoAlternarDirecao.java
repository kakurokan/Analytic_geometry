package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AcaoAlternarDirecao extends AbstractAction {
    private final PainelMapa painel;

    public AcaoAlternarDirecao(PainelMapa painel) {
        super("Aparente (Corrente)");
        this.painel = painel;

        putValue(Action.SHORT_DESCRIPTION, "Alternar entre direção real e direção da rota");
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JToggleButton botao = (JToggleButton) e.getSource();
        boolean selecionado = botao.isSelected();

        painel.setApontarParaRota(selecionado);

        putValue(Action.NAME, selecionado ? "Normal (Rota)" : "Aparente (Corrente)");

        painel.repaint();
    }
}
