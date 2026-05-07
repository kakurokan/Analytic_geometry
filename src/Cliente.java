import Engine.*;
import GUI.DialogoCorrente;
import GUI.JanelaPrincipal;

import javax.swing.*;
import java.util.*;

public class Cliente {
    public static void main(String[] args) {

        // 1. PONTOS GEOMÉTRICOS (Mapa alargado)
        Ponto pA = new Ponto(2, 2);   // Lisboa
        Ponto pB = new Ponto(12, 4);  // Faro
        Ponto pC = new Ponto(8, 10);  // Vigo
        Ponto pD = new Ponto(16, 12); // Sagres

        // NOVOS PONTOS
        Ponto pE = new Ponto(4, 13);  // Leixões (Norte)
        Ponto pF = new Ponto(2, 7);   // Sines (Costa Centro)
        Ponto pG = new Ponto(18, 5);  // Tavira (Sul)

        // 2. ROTAS (Uma teia complexa de interligações)
        List<Route> rotas = Arrays.asList(
                // Rotas originais
                new Route(Arrays.asList(pA, pB)),
                new Route(Arrays.asList(pB, pC)),
                new Route(Arrays.asList(pC, pA)),
                new Route(Arrays.asList(pB, pD)),
                // Novas Rotas
                new Route(Arrays.asList(pC, pE)), // Vigo -> Leixões
                new Route(Arrays.asList(pE, pF)), // Leixões -> Sines
                new Route(Arrays.asList(pF, pA)), // Sines -> Lisboa
                new Route(Arrays.asList(pF, pB)), // Sines -> Faro
                new Route(Arrays.asList(pB, pG)), // Faro -> Tavira
                new Route(Arrays.asList(pG, pD))  // Tavira -> Sagres
        );

        // 3. POSIÇÕES DOS PORTOS (Para as caixas flutuantes da GUI)
        Map<String, Ponto> posicoesPortos = new HashMap<>();
        posicoesPortos.put("Porto de Lisboa", pA);
        posicoesPortos.put("Porto de Faro", pB);
        posicoesPortos.put("Porto de Vigo", pC);
        posicoesPortos.put("Porto de Sagres", pD);
        posicoesPortos.put("Porto de Leixões", pE);
        posicoesPortos.put("Porto de Sines", pF);
        posicoesPortos.put("Porto de Tavira", pG);

        // 4. OBSTÁCULOS ESTÁTICOS
        Triangulo ilhaBege = new Triangulo(new Ponto[]{
                new Ponto(5, 5), new Ponto(7, 8), new Ponto(9, 5)
        });

        // Pode adicionar mais obstáculos estáticos aqui no futuro!
        List<Poligono> obstaculosEstaticos = List.of(ilhaBege);

        // 5. INICIALIZAR O MOTOR DE JOGO COM OS OBSTÁCULOS BASE
        List<Obstaculo> todosObstaculos = new ArrayList<>(obstaculosEstaticos);
        TorreDeControlo torre = new GestorMaritimo();

        // 6. CRIAR OS PORTOS
        Porto porto1 = new Porto("Porto de Lisboa", pA, torre);
        Porto porto2 = new Porto("Porto de Faro", pB, torre);
        Porto porto3 = new Porto("Porto de Vigo", pC, torre);
        Porto porto4 = new Porto("Porto de Sagres", pD, torre);
        Porto porto5 = new Porto("Porto de Leixões", pE, torre);
        Porto porto6 = new Porto("Porto de Sines", pF, torre);
        Porto porto7 = new Porto("Porto de Tavira", pG, torre);

        List<Porto> portos = Arrays.asList(porto1, porto2, porto3, porto4, porto5, porto6, porto7);

        // Corrente marítima a afetar o cenário
        Vetor corrente = DialogoCorrente.pedirCorrente(new Vetor(1.0, 2.0));

        Simulador simulador = new Simulador(
                corrente,
                rotas,
                portos,
                todosObstaculos,
                torre
        );

        // 7. CRIAR TEMPESTADES ALEATÓRIAS
        List<Tempestade> tempestadesParaGUI = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            tempestadesParaGUI.add(simulador.criarTempestade());
        }

        // 8. CRIAR TRÁFEGO (Adicionar Navios)
        Runnable criarBarcos = () -> {
            // (Velocidade, Tempo de Partida, Destino)

            // Arranques Imediatos (Tempo 0)
            porto1.adicionarNavio(1.5, 0, porto2); // Lento
            porto3.adicionarNavio(2.0, 0, porto4); // Normal
            porto5.adicionarNavio(3.0, 0, porto1); // Muito Rápido!

            // Arranques Curto Prazo (Tempo 2 a 5)
            porto2.adicionarNavio(2.5, 2, porto1);
            porto6.adicionarNavio(1.2, 3, porto2); // Cargueiro muito lento
            porto7.adicionarNavio(2.2, 4, porto5);
            porto1.adicionarNavio(2.5, 5, porto3);

            // Arranques a Médio Prazo (Tempo 8 a 15)
            porto2.adicionarNavio(2.8, 8, porto3);
            porto4.adicionarNavio(2.0, 10, porto6);
            porto3.adicionarNavio(1.0, 12, porto6); // Cargueiro pesado
            porto1.adicionarNavio(1.8, 15, porto7);

            // Arranque Tardio (Tempo 20)
            porto5.adicionarNavio(2.5, 20, porto4);
        };

        criarBarcos.run();

        // 9. ARRANCAR A INTERFACE GRÁFICA
        SwingUtilities.invokeLater(() -> {
            simulador.iniciar();
            JanelaPrincipal gui = new JanelaPrincipal(
                    simulador,
                    rotas,
                    obstaculosEstaticos,
                    tempestadesParaGUI,
                    posicoesPortos,
                    corrente,
                    criarBarcos
            );
            gui.iniciar();
        });
    }
}