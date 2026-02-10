import java.util.Scanner;

/**
 * @author Léo Souza
 * @version 10/02/26 10:03
 */
public class Cliente {

    /**
     * Executa o fluxo principal do programa. Este método realiza as seguintes operações:
     * <p>
     * 1. Lê as coordenadas de dois pontos a partir da entrada padrão, usando um objeto Scanner,
     * e cria instâncias da classe Ponto para os dois pontos lidos.
     * 2. Utiliza os dois pontos criados para instanciar um objeto da classe SegmentoReta, que
     * representa um segmento de linha no espaço cartesiano.
     * 3. Lê as componentes de um vetor a partir da entrada padrão e cria uma instância da classe Vetor.
     * 4. Verifica se o segmento de linha criado no passo 2 intersecta com o vetor criado no passo 3,
     * através do método `intersect` da classe SegmentoReta.
     * 5. Exibe o resultado da operação de interseção no console.
     * 6. Fecha o Scanner posteriormente para liberar recursos.
     */
    void main() {
        Scanner scan = new Scanner(System.in);

        double x = scan.nextDouble();
        double y = scan.nextDouble();
        Ponto a = new Ponto(x, y);

        x = scan.nextDouble();
        y = scan.nextDouble();
        Ponto b = new Ponto(x, y);

        SegmentoReta segmento = new SegmentoReta(a, b);

        x = scan.nextDouble();
        y = scan.nextDouble();

        Vetor vetor = new Vetor(x, y);

        System.out.println(segmento.intersect(vetor));

        scan.close();
    }
}
