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
    List<Ponto> intersect(Route rota);
}
