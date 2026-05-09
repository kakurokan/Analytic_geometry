import Engine.*;
import GUI.DialogoCorrente;
import GUI.JanelaPrincipal;

import javax.swing.*;
import java.util.*;

public class Cliente {
    public static void main(String[] args) {

        // ========================================================
        // 1. PONTOS GEOMÉTRICOS (Layout Amplo e Extremo: 19x14)
        // ========================================================

        // 5 Portos principais (Cantos Absolutos e Centro)
        Ponto pLisboa = new Ponto(1, 1);    // Sudoeste
        Ponto pFaro = new Ponto(19, 1);   // Sudeste
        Ponto pVigo = new Ponto(1, 14);   // Noroeste
        Ponto pSagres = new Ponto(19, 14);  // Nordeste
        Ponto pLeixoes = new Ponto(10, 7);   // Centro exato do mapa

        // Nós de Navegação para criar curvas e segmentos
        Ponto nSul1 = new Ponto(7, 1);
        Ponto nSul2 = new Ponto(13, 1);
        Ponto nNorte1 = new Ponto(7, 14);
        Ponto nNorte2 = new Ponto(13, 14);

        Ponto nDiagEsqA = new Ponto(5, 4);
        Ponto nDiagEsqB = new Ponto(5, 10);
        Ponto nDiagDirA = new Ponto(15, 4);
        Ponto nDiagDirB = new Ponto(15, 10);

        Ponto nMeioEsq = new Ponto(2, 7);
        Ponto nMeioDir = new Ponto(18, 7);

        // ========================================================
        // 2. ROTAS (Extensas, de 3 a 4 segmentos, cruzando o centro)
        // ========================================================
        List<Route> rotas = Arrays.asList(
                // Rota 1: Borda Sul (Lisboa -> Faro)
                new Route(Arrays.asList(pLisboa, nSul1, nSul2, pFaro)),

                // Rota 2: Borda Norte (Vigo -> Sagres)
                new Route(Arrays.asList(pVigo, nNorte1, nNorte2, pSagres)),

                // Rota 3: Diagonal Ascendente (Lisboa -> Centro -> Sagres)
                new Route(Arrays.asList(pLisboa, nDiagEsqA, pLeixoes, nDiagDirB, pSagres)),

                // Rota 4: Diagonal Descendente (Vigo -> Centro -> Faro)
                new Route(Arrays.asList(pVigo, nDiagEsqB, pLeixoes, nDiagDirA, pFaro)),

                // Rota 5: Horizontal Sulada (Lisboa -> Contorno Centro -> Faro)
                new Route(Arrays.asList(pLisboa, nMeioEsq, pLeixoes, nMeioDir, pFaro)),

                // Rota 6: Horizontal Nortada (Vigo -> Contorno Centro -> Sagres)
                new Route(Arrays.asList(pVigo, nMeioEsq, pLeixoes, nMeioDir, pSagres))
        );

        // ========================================================
        // 3. MAPA DE PORTOS (Para as caixas da GUI)
        // ========================================================
        Map<String, Ponto> posicoesPortos = new HashMap<>();
        posicoesPortos.put("Porto de Lisboa", pLisboa);
        posicoesPortos.put("Porto de Faro", pFaro);
        posicoesPortos.put("Porto de Vigo", pVigo);
        posicoesPortos.put("Porto de Sagres", pSagres);
        posicoesPortos.put("Porto de Leixões", pLeixoes);

        // ========================================================
        // 4. OBSTÁCULOS ESTÁTICOS (Corrigidos para evitar Rotas)
        // ========================================================

        // Retângulo isolado no Norte (seguro entre o centro e a borda superior)
        Retangulo obsNorte = new Retangulo(new Ponto[]{
                new Ponto(9, 11), new Ponto(11, 11), new Ponto(11, 12.5), new Ponto(9, 12.5)
        });

        // Quadrado isolado no Sul (seguro entre o centro e a borda inferior)
        Quadrado obsSul = new Quadrado(new Ponto[]{
                new Ponto(9.5, 2.5), new Ponto(10.5, 2.5), new Ponto(10.5, 3.5), new Ponto(9.5, 3.5)
        });

        // Triângulo Oeste (Posicionado no vazio acíma da linha horizontal Y=7)
        Triangulo obsOeste = new Triangulo(new Ponto[]{
                new Ponto(3, 8), new Ponto(4.5, 8), new Ponto(3.75, 9.5)
        });

        // Triângulo Este (Simétrico ao do Oeste, livre da linha horizontal)
        Triangulo obsEste = new Triangulo(new Ponto[]{
                new Ponto(15.5, 8), new Ponto(17, 8), new Ponto(16.25, 9.5)
        });

        List<Poligono> obstaculosEstaticos = Arrays.asList(obsNorte, obsSul, obsOeste, obsEste);
        List<Obstaculo> todosObstaculos = new ArrayList<>(obstaculosEstaticos);

        // ========================================================
        // 5. INICIALIZAÇÃO DO MOTOR E SIMULADOR
        // ========================================================
        TorreDeControlo torre = new GestorMaritimo();

        Porto porto1 = new Porto("Porto de Lisboa", pLisboa, torre);
        Porto porto2 = new Porto("Porto de Faro", pFaro, torre);
        Porto porto3 = new Porto("Porto de Vigo", pVigo, torre);
        Porto porto4 = new Porto("Porto de Sagres", pSagres, torre);
        Porto porto5 = new Porto("Porto de Leixões", pLeixoes, torre);

        List<Porto> portos = Arrays.asList(porto1, porto2, porto3, porto4, porto5);

        // Pede a corrente ao utilizador (default: 1.0, 1.0)
        Vetor corrente = DialogoCorrente.pedirCorrente(new Vetor(1.0, 1.0));
        if (corrente == null) System.exit(0);

        Simulador simulador = new Simulador(corrente, rotas, portos, todosObstaculos, torre);

        // Criar 3 tempestades móveis (Obrigatório)
        List<Tempestade> tempestadesParaGUI = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            tempestadesParaGUI.add(simulador.criarTempestade());
        }

        // ========================================================
        // 6. ROTINA DE TRÁFEGO (Longas viagens, cruzamentos perigosos)
        // ========================================================
        Runnable criarBarcos = () -> {
            // Partidas Imediatas (Convergindo para o Centro)
            porto1.adicionarNavio(2.5, 0, porto4); // De Sudoeste a Nordeste
            porto3.adicionarNavio(2.2, 0, porto2); // De Noroeste a Sudeste
            porto5.adicionarNavio(3.0, 0, porto1); // Fuga do Centro

            // Vaga Curto Prazo
            porto2.adicionarNavio(2.0, 5, porto1);
            porto4.adicionarNavio(1.8, 8, porto3);
            porto1.adicionarNavio(3.5, 10, porto5); // Navio rápido
            porto3.adicionarNavio(2.8, 12, porto4);

            // Vaga Médio Prazo
            porto2.adicionarNavio(2.4, 20, porto5);
            porto4.adicionarNavio(1.5, 25, porto1); // Navio pesado/lento
            porto5.adicionarNavio(3.0, 30, porto2);
            porto1.adicionarNavio(2.0, 35, porto4);

            // Vaga Longo Prazo
            porto3.adicionarNavio(2.6, 45, porto2);
            porto2.adicionarNavio(1.8, 50, porto3);
            porto4.adicionarNavio(2.5, 60, porto5);
            porto5.adicionarNavio(2.1, 65, porto1);
        };

        criarBarcos.run();

        // ========================================================
        // 7. INICIAR INTERFACE GRÁFICA
        // ========================================================
        SwingUtilities.invokeLater(() -> {
            simulador.iniciar();
            JanelaPrincipal gui = new JanelaPrincipal(
                    simulador, rotas, obstaculosEstaticos, tempestadesParaGUI,
                    posicoesPortos, corrente, criarBarcos
            );
            gui.iniciar();
        });
    }
}