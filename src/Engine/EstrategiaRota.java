package Engine;

/**
 * Interface que define a estratégia de cálculo de rotas entre dois pontos em um contexto navegável.
 * Implementações dessa interface devem fornecer a lógica necessária para encontrar um caminho
 * viável entre os pontos de origem e destino, levando em consideração as restrições impostas
 * por elementos como obstáculos ou tráfego de navios.
 *
 * @author Acrismede Mendes, Alexandre Guerreiro, Léo Souza
 * @version 11/05/26
 */
public interface EstrategiaRota {
    /**
     * Calcula a rota entre dois pontos de origem e destino. A implementação deste método deve levar em conta
     * restrições como obstáculos, áreas interditadas ou travessias seguras, dependendo
     * das informações fornecidas pelos objetos na lista de navios.
     *
     * @param origem  O ponto de origem a partir do qual a rota será calculada.
     *                Não pode ser nulo.
     * @param destino O ponto de destino para onde a rota será traçada.
     *                Não pode ser nulo.
     * @return Um objeto {@code Route} que representa a rota calculada entre os
     * pontos de origem e destino. O resultado será sempre uma rota válida
     * ou nula caso não seja possível calcular uma rota viável.
     * @throws IllegalArgumentException Se algum dos parâmetros obrigatórios for nulo ou
     *                                  se as condições prévias para o cálculo da rota
     *                                  não forem satisfeitas.
     * @see <a href="https://refactoring.guru/pt-br/design-patterns/strategy">Padrão de Projeto: Strategy (Refactoring Guru)</a>
     */
    Route caminhos(Ponto origem, Ponto destino);
}
