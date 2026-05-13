package GUI;

import Engine.Vetor;

import javax.swing.*;

/**
 * Classe responsável por apresentar um diálogo ao utilizador para inserção
 * de componentes de um vetor de corrente. O diálogo permite ao utilizador
 * inserir e confirmar os valores para os componentes X e Y de uma corrente.
 *
 * @author Acrismede Mendes, Alexandre Guerreiro, Léo Souza
 * @version 11/05/26
 */
public class DialogoCorrente {

    /**
     * Abre uma janela de diálogo a pedir os componentes X e Y da corrente.
     *
     * @param valorAtual O vetor com a corrente atual (para pré-preencher os campos).
     * @return O novo Vetor introduzido pelo utilizador, ou null se o utilizador cancelar.
     * @pre valorAtual != null
     */
    public static Vetor pedirCorrente(Vetor valorAtual) {
        JTextField campoX = new JTextField(String.valueOf(valorAtual.getX()), 5);
        JTextField campoY = new JTextField(String.valueOf(valorAtual.getY()), 5);

        JPanel painelInput = new JPanel();
        painelInput.add(new JLabel("Corrente X:"));
        painelInput.add(campoX);
        painelInput.add(Box.createHorizontalStrut(15));
        painelInput.add(new JLabel("Corrente Y:"));
        painelInput.add(campoY);

        // Usar um ciclo infinito para obrigar o utilizador a inserir números válidos se clicar em OK
        while (true) {
            int resultado = JOptionPane.showConfirmDialog(null, painelInput,
                    "Configuração da Corrente", JOptionPane.OK_CANCEL_OPTION);

            // Se o utilizador clicar em Cancelar ou fechar a janela, devolvemos null
            if (resultado != JOptionPane.OK_OPTION) {
                return null;
            }

            try {
                double x = Double.parseDouble(campoX.getText().replace(",", "."));
                double y = Double.parseDouble(campoY.getText().replace(",", "."));
                return new Vetor(x, y); // Se der sucesso, devolve o vetor e sai do ciclo

            } catch (NumberFormatException e) {
                // Se der erro, avisa e o ciclo repete, mostrando a janela outra vez
                JOptionPane.showMessageDialog(null,
                        "Por favor, insira valores numéricos válidos (ex: 1.5).",
                        "Erro de Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}