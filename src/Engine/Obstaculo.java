package Engine;

import java.util.List;

/**
 * Representa a interface para objetos que podem funcionar como obstáculos num espaço cartesiano.
 * A interface define um contrato para verificar interseções entre o obstáculo e uma rota composta
 * por segmentos de reta.
 *
 * @author Acrismede Mendes, Alexandre Guerreiro, Léo Souza
 * @version 11/05/26
 */
public interface Obstaculo {

    /**
     * Verifica a interseção entre o obstáculo e uma rota.
     * @param rota A rota a ser verificada. Não pode ser nula.
     * @pre rota != null
     * @pos Retorna null se não houver interseções, ou uma lista com pelo menos um ponto e sem pontos repetidos.
     */
    List<Ponto> intersect(Route rota);
}
