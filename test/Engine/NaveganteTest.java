package Engine;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NaveganteTest {


    @Test
    void posicao_DistanciaAlcancaSegundoSegmento_RetornaPosicaoCorreta() {
        Route rota = new Route(List.of(
                new Ponto(5, 1),
                new Ponto(5, 5),
                new Ponto(7, 5)
        ));
        Navegante navegante = new Navegante(rota);
        Ponto esperado = new Ponto(5.5, 5);

        Ponto resultado = navegante.posicao(2, 2.25);

        assertEquals(esperado, resultado, "A posição calculada deveria estar no meio do segundo segmento.");
    }

    @Test
    void posicao_DistanciaAlcancaTerceiroSegmento_RetornaPosicaoCorreta() {
        Route rota = new Route(List.of(
                new Ponto(100, 100),
                new Ponto(75, 100),
                new Ponto(50, 50),
                new Ponto(25, 50)
        ));
        Navegante navegante = new Navegante(rota);
        Ponto esperado = new Ponto(62.48, 74.96);

        Ponto resultado = navegante.posicao(20, 2.65);

        assertEquals(esperado.getX(), resultado.getX(), 0.01, "A coordenada X da posição no terceiro segmento está incorreta.");
        assertEquals(esperado.getY(), resultado.getY(), 0.01, "A coordenada Y da posição no terceiro segmento está incorreta.");
    }

    @Test
    void velocidadePorSegmento_RotaSimplesComVento_CalculaVelocidadesAjustadas() {
        Route rota = new Route(List.of(
                new Ponto(5, 1),
                new Ponto(5, 5),
                new Ponto(7, 5)
        ));
        Navegante navegante = new Navegante(rota);
        List<Vetor> esperado = List.of(
                new Vetor(-1, 1),
                new Vetor(1, -1)
        );

        List<Vetor> resultado = navegante.velocidadePorSegmento(new Vetor(1, 1), 2);

        assertListasVetorIguais(esperado, resultado);
    }

    @Test
    void velocidadePorSegmento_RotaComplexaComVento_CalculaVelocidadesAjustadas() {
        Route rota = new Route(List.of(
                new Ponto(100, 100),
                new Ponto(75, 100),
                new Ponto(50, 50),
                new Ponto(25, 50)
        ));
        Navegante navegante = new Navegante(rota);
        List<Vetor> esperado = List.of(
                new Vetor(-21, -2),
                new Vetor(-9.94, -19.89), // Aproximado
                new Vetor(-21, -2)
        );

        List<Vetor> resultado = navegante.velocidadePorSegmento(new Vetor(1, 2), 20);

        assertListasVetorIguais(esperado, resultado);
    }

    @Test
    void tempoParaPercorrer_RotaDeUmSegmento_CalculaTempoCorreto() {
        Route rota = new Route(List.of(
                new Ponto(0, 0),
                new Ponto(10, 0)
        ));
        Navegante navegante = new Navegante(rota);

        assertEquals(5.0, navegante.tempoParaPercorrer(2.0), 0.01);
    }

    @Test
    void tempoParaPercorrer_RotaEmFormaDeL_CalculaTempoCorreto() {
        Route rota = new Route(List.of(
                new Ponto(0, 0),
                new Ponto(0, 4),
                new Ponto(3, 4)
        ));
        Navegante navegante = new Navegante(rota);

        assertEquals(3.5, navegante.tempoParaPercorrer(2.0), 0.01);
    }

    @Test
    void tempoParaPercorrer_RotaComCurva_CalculaTempoCorreto() {
        Route rota = new Route(List.of(
                new Ponto(5, 1),
                new Ponto(5, 5),
                new Ponto(7, 5)
        ));
        Navegante navegante = new Navegante(rota);

        assertEquals(3.0, navegante.tempoParaPercorrer(2.0), 0.01);
    }

    @Test
    void tempoParaPercorrer_RotaMultiplosSegmentos_CalculaTempoCorreto() {
        Route rota = new Route(List.of(
                new Ponto(100, 100),
                new Ponto(75, 100),
                new Ponto(50, 50),
                new Ponto(25, 50)
        ));
        Navegante navegante = new Navegante(rota);

        assertEquals(5.295, navegante.tempoParaPercorrer(20.0), 0.01);
    }

    @Test
    void mudarRota_NovaRotaFornecida_SubstituiSegmentosAntigos() {
        Route rotaInicial = new Route(List.of(
                new Ponto(0, 0), new Ponto(1, 1), new Ponto(2, 3)
        ));
        Navegante navegante = new Navegante(rotaInicial);
        Route rotaNova = new Route(List.of(
                new Ponto(0, 0), new Ponto(2, 4), new Ponto(4, 5)
        ));

        navegante.mudarRota(rotaNova);

        assertEquals(rotaNova.getSegmentos(), navegante.getSegmentos(), "Os segmentos do navegante devem refletir a nova rota.");
    }

    @Test
    void getSegmentos_NaveganteInicializado_RetornaSegmentosDaRota() {
        Route rotaInicial = new Route(List.of(
                new Ponto(0, 0), new Ponto(1, 1), new Ponto(2, 3)
        ));
        Navegante navegante = new Navegante(rotaInicial);

        assertEquals(rotaInicial.getSegmentos(), navegante.getSegmentos(), "Os segmentos retornados devem coincidir com a rota de inicialização.");
    }

    private void assertListasVetorIguais(List<Vetor> esperado, List<Vetor> resultado) {
        assertEquals(esperado.size(), resultado.size(), "As listas têm tamanhos diferentes");

        for (int i = 0; i < esperado.size(); i++) {
            assertEquals(esperado.get(i).getX(), resultado.get(i).getX(), 0.01, "Erro no X do vetor " + i);
            assertEquals(esperado.get(i).getY(), resultado.get(i).getY(), 0.01, "Erro no Y do vetor " + i);
        }
    }

    @Test
    void direcao() {
        Navegante navegante = new Navegante(new Route(List.of(
            new Ponto(0,0), new Ponto(1,1), new Ponto(3,2), new Ponto(3,5)
        )));
        double velocidade = 0.5;
        Vetor corrente = new Vetor(-1,1);
        Vetor esperado = new Vetor(1.44721359549,-0.7763932022);
        assertEquals(esperado,navegante.direcao(velocidade,3,corrente));
    }

    @Test
    void getSegmentoAtual() {
        Navegante navegante = new Navegante(new Route(List.of(
                new Ponto(0,0), new Ponto(1,1), new Ponto(3,2), new Ponto(3,5)
        )));
        double velocidade = 0.6;
        SegmentoReta esperado= new SegmentoReta(new Ponto(1,1),new Ponto(3,2));
        assertEquals(esperado, navegante.getSegmentoAtual(5,velocidade));
        }
}