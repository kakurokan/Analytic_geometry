import java.util.Scanner;

/**
 * @author Léo Souza
 * @version 24/02/26
 * <p>
 * Classe principal para o gerenciamento de cálculos e interações relacionados ao sistema de navegação.
 * Esta classe realiza a leitura de dados de entrada, configura os objetos necessários e calcula:
 * <p>
 * - O tempo necessário para alcançar um ponto de destino considerando a velocidade e o vento.
 * - A velocidade vetorial requerida para completar o trajeto no tempo calculado.
 * <p>
 * A classe se apoia nas classes Ponto, Vetor e AutoPilot para realizar os cálculos e operações necessárias.
 */
public class Cliente {

    public static void main() {
        Scanner sc = new Scanner(System.in);

        //Get start and finish points
        Ponto start = new Ponto(sc.nextDouble(), sc.nextDouble());
        Ponto finish = new Ponto(sc.nextDouble(), sc.nextDouble());

        //Get wind speed and direction
        Vetor w = new Vetor(sc.nextDouble(), sc.nextDouble());

        //Get linear speed
        double s = sc.nextDouble();
        sc.close();

        //Setup auto pilot and compute:
        // i) desired time to reach the finish point
        // ii) vectorial speed required
        AutoPilot ap = new AutoPilot(start, finish);
        double t = ap.time(w, s);
        IO.println(String.format("%.2f", t));
        IO.println(ap.speed(w, t));
    }
}
