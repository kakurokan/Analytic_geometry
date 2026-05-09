import Engine.*;
import GUI.DialogoCorrente;
import GUI.JanelaPrincipal;

import javax.swing.*;
import java.util.*;

public class Cliente {
    static void main() {

        // ========================================================
        // 1. PONTOS GEOMÉTRICOS (Mapa Expandido e Amplo)
        // Coordenadas espalhadas de 1 a 15 (X) e 1 a 13 (Y)
        // ========================================================

        // Portos nas extremidades e centro
        Ponto pA = new Ponto(1, 1);   // Lisboa (Sudoeste)
        Ponto pB = new Ponto(15, 1);  // Faro (Sudeste)
        Ponto pC = new Ponto(1, 13);  // Vigo (Noroeste)
        Ponto pD = new Ponto(15, 13); // Sagres (Nordeste)
        Ponto pE = new Ponto(8, 7);   // Leixões (Centro exato do mapa)

        // Nós intermédios para afastar as rotas e garantir os 3 segmentos
        Ponto pN1 = new Ponto(4, 1);
        Ponto pN2 = new Ponto(12, 1);
        Ponto pN3 = new Ponto(1, 7);
        Ponto pN4 = new Ponto(15, 7);
        Ponto pN5 = new Ponto(4, 13);
        Ponto pN6 = new Ponto(12, 13);
        Ponto pM1 = new Ponto(8, 1);
        Ponto pM2 = new Ponto(8, 13);

        // ========================================================
        // 2. ROTAS (Amplas, com cruzamentos no centro e nas bordas)
        // ========================================================
        List<Route> rotas = Arrays.asList(
                // Borda Sul
                new Route(Arrays.asList(pA, pN1, pM1, pN2, pB)),     // Rota 1: 4 segmentos
                // Borda Esquerda -> Centro -> Topo Esquerda
                new Route(Arrays.asList(pA, pN3, pE, pC)),           // Rota 2: 3 segmentos
                // Borda Direita -> Centro -> Fundo Direita
                new Route(Arrays.asList(pD, pN4, pE, pB)),           // Rota 3: 3 segmentos
                // Diagonal Descendente
                new Route(Arrays.asList(pD, pM2, pE, pA)),           // Rota 4: 3 segmentos (cruza no centro)
                // Diagonal Central
                new Route(Arrays.asList(pC, pN5, pE, pB)),           // Rota 5: 3 segmentos (cruza no centro)
                // Borda Norte
                new Route(Arrays.asList(pC, pN5, pM2, pN6, pD)),     // Rota 6: 4 segmentos
                // Eixo Vertical
                new Route(Arrays.asList(pE, pM1, pN2, pB))           // Rota 7: 3 segmentos
        );

        // ========================================================
        // 3. POSIÇÕES DOS PORTOS
        // ========================================================
        Map<String, Ponto> posicoesPortos = new HashMap<>();
        posicoesPortos.put("Porto de Lisboa", pA);
        posicoesPortos.put("Porto de Faro", pB);
        posicoesPortos.put("Porto de Vigo", pC);
        posicoesPortos.put("Porto de Sagres", pD);
        posicoesPortos.put("Porto de Leixões", pE);

        // ========================================================
        // 4. OBSTÁCULOS ESTÁTICOS (Alojados nos "bolsos" vazios do mapa)
        // ========================================================

        // Quadrante Inferior Esquerdo
        Triangulo obs1 = new Triangulo(new Ponto[]{
                new Ponto(3, 3), new Ponto(5, 3), new Ponto(4, 5)
        });

        // Quadrante Inferior Direito (Quadrado 2x2)
        Quadrado obs2 = new Quadrado(new Ponto[]{
                new Ponto(10, 3), new Ponto(12, 3), new Ponto(12, 5), new Ponto(10, 5)
        });

        // Quadrante Superior Direito (Retângulo 3x2)
        Retangulo obs3 = new Retangulo(new Ponto[]{
                new Ponto(10, 9), new Ponto(13, 9), new Ponto(13, 11), new Ponto(10, 11)
        });

        // Quadrante Superior Esquerdo
        Triangulo obs4 = new Triangulo(new Ponto[]{
                new Ponto(3, 9), new Ponto(5, 9), new Ponto(4, 11)
        });

        List<Poligono> obstaculosEstaticos = Arrays.asList(obs1, obs2, obs3, obs4);
        List<Obstaculo> todosObstaculos = new ArrayList<>(obstaculosEstaticos);

        // ========================================================
        // 5. INICIALIZAR GESTOR MARÍTIMO E PORTOS
        // ========================================================
        TorreDeControlo torre = new GestorMaritimo();

        Porto porto1 = new Porto("Porto de Lisboa", pA, torre);
        Porto porto2 = new Porto("Porto de Faro", pB, torre);
        Porto porto3 = new Porto("Porto de Vigo", pC, torre);
        Porto porto4 = new Porto("Porto de Sagres", pD, torre);
        Porto porto5 = new Porto("Porto de Leixões", pE, torre);

        List<Porto> portos = Arrays.asList(porto1, porto2, porto3, porto4, porto5);

        // ========================================================
        // 6. SOLICITAR CORRENTE E INSTANCIAR O SIMULADOR
        // ========================================================
        Vetor corrente = DialogoCorrente.pedirCorrente(new Vetor(1.0, 2.0));
        if (corrente == null) {
            System.exit(0);
        }

        Simulador simulador = new Simulador(
                corrente,
                rotas,
                portos,
                todosObstaculos,
                torre
        );

        // ========================================================
        // 7. CRIAR TEMPESTADES MÓVEIS
        // ========================================================
        List<Tempestade> tempestadesParaGUI = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            tempestadesParaGUI.add(simulador.criarTempestade());
        }

        // ========================================================
        // 8. CONFIGURAR A ROTINA DE TRÁFEGO
        // ========================================================
        Runnable criarBarcos = () -> {
            // Partidas Imediatas (Tempo 0)
            porto1.adicionarNavio(1.5, 0, porto2);
            porto3.adicionarNavio(2.0, 0, porto4);
            porto5.adicionarNavio(3.0, 0, porto1);

            // Partidas Curto Prazo
            porto2.adicionarNavio(2.5, 2, porto1);
            porto4.adicionarNavio(2.0, 5, porto2);
            porto1.adicionarNavio(2.5, 8, porto3);

            // Partidas Médio Prazo
            porto3.adicionarNavio(1.0, 12, porto2);
            porto5.adicionarNavio(2.5, 15, porto4);
        };

        criarBarcos.run();

        // ========================================================
        // 9. INICIAR A INTERFACE GRÁFICA
        // ========================================================
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