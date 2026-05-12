package Engine;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SegmentoRetaTest {

    @Test
    void construtor_PontosIguais_LancaIllegalArgumentException() {
        Ponto p1 = new Ponto(1.5, 2.0);
        Ponto p2 = new Ponto(1.5, 2.0);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new SegmentoReta(p1, p2);
        });
        assertEquals("Engine.SegmentoReta:iv", exception.getMessage());
    }

    @Test
    void getA_SegmentoValido_RetornaPontoInicial() {
        Ponto a = new Ponto(1, 2);
        Ponto b = new Ponto(3, 4);
        SegmentoReta sr = new SegmentoReta(a, b);

        assertEquals(a, sr.getA(), "O ponto A (origem) retornado não corresponde ao esperado.");
    }

    @Test
    void getB_SegmentoValido_RetornaPontoFinal() {
        Ponto a = new Ponto(1, 2);
        Ponto b = new Ponto(3, 4);
        SegmentoReta sr = new SegmentoReta(a, b);

        assertEquals(b, sr.getB(), "O ponto B (destino) retornado não corresponde ao esperado.");
    }

    @Test
    void toString_SegmentoValido_RetornaStringFormatadaOrdenada() {
        SegmentoReta sr1 = new SegmentoReta(new Ponto(2, 2), new Vetor(-1, 0));
        SegmentoReta sr2 = new SegmentoReta(new Ponto(3, 3), new Vetor(2, 4.5));

        assertEquals("sr((1.00,2.00); (2.00,2.00))", sr1.toString());
        assertEquals("sr((3.00,3.00); (5.00,7.50))", sr2.toString());
    }

    @Test
    void equals_SegmentosComMesmosPontosMesmoInvertidos_RetornaTrue() {
        SegmentoReta seg1 = new SegmentoReta(new Ponto(0, 0), new Ponto(10, 10));
        SegmentoReta seg2 = new SegmentoReta(new Ponto(10, 10), new Ponto(0, 0));

        assertEquals(seg1, seg2, "Segmentos com os mesmos pontos (mesmo em ordem inversa) devem ser considerados iguais.");
    }

    @Test
    void equals_SegmentosDiferentes_RetornaFalse() {
        SegmentoReta seg1 = new SegmentoReta(new Ponto(0, 0), new Ponto(10, 10));
        SegmentoReta seg2 = new SegmentoReta(new Ponto(0, 0), new Ponto(10, 11));

        assertNotEquals(seg1, seg2, "Segmentos com coordenadas diferentes não podem ser iguais.");
    }

    @Test
    void comprimento_CalculaDistanciaEntrePontos_RetornaValorCorreto() {
        SegmentoReta sr = new SegmentoReta(new Ponto(0, 0), new Ponto(3, 4));

        double resultado = sr.comprimento();

        assertEquals(5.0, resultado, Ponto.eps, "O comprimento do segmento deve ser calculado usando o Teorema de Pitágoras.");
    }

    @Test
    void comprimentoDiferente_SegmentosComMesmoComprimento_RetornaFalse() {
        SegmentoReta sr1 = new SegmentoReta(new Ponto(0, 0), new Ponto(3, 4));
        SegmentoReta sr2 = new SegmentoReta(new Ponto(1, 1), new Ponto(1, 6));

        assertFalse(sr1.comprimentoDiferente(sr2), "O método deveria retornar false porque os comprimentos são iguais.");
    }

    @Test
    void comprimentoDiferente_SegmentosComComprimentoDistinto_RetornaTrue() {
        SegmentoReta sr1 = new SegmentoReta(new Ponto(0, 0), new Ponto(3, 4));
        SegmentoReta sr3 = new SegmentoReta(new Ponto(0, 0), new Ponto(10, 0));

        assertTrue(sr1.comprimentoDiferente(sr3), "O método deveria retornar true porque os comprimentos diferem.");
    }

    @Test
    void intersectSegmento_CruzamentoSimples_RetornaPontoIntersecao() {
        SegmentoReta sr1 = new SegmentoReta(new Ponto(0, 0), new Ponto(4, 4));
        SegmentoReta sr2 = new SegmentoReta(new Ponto(0, 4), new Ponto(4, 0));

        Ponto p = sr1.intersect(sr2);

        assertNotNull(p);
        assertEquals(2.0, p.getX(), Ponto.eps);
        assertEquals(2.0, p.getY(), Ponto.eps);

        SegmentoReta sr3 = new SegmentoReta(new Ponto(2,5), new Ponto(6,4));
        SegmentoReta sr4 = new SegmentoReta(new Ponto(4,3), new Ponto(5,8));
        p = sr3.intersect(sr4);
        assertEquals(4.285714285714286, p.getX());
        assertEquals(4.428571428571429,p.getY());
    }

    @Test
    void intersectSegmento_TocamNasExtremidades_RetornaPontoIntersecao() {
        SegmentoReta sr1 = new SegmentoReta(new Ponto(0, 0), new Ponto(2, 0));
        SegmentoReta sr2 = new SegmentoReta(new Ponto(2, 0), new Ponto(2, 2));

        Ponto p = sr1.intersect(sr2);

        assertNotNull(p);
        assertEquals(2.0, p.getX(), Ponto.eps);
        assertEquals(0.0, p.getY(), Ponto.eps);
    }

    @Test
    void intersectSegmento_SegmentosParalelos_RetornaNull() {
        SegmentoReta sr1 = new SegmentoReta(new Ponto(0, 0), new Ponto(4, 0));
        SegmentoReta sr2 = new SegmentoReta(new Ponto(0, 2), new Ponto(4, 2));

        assertNull(sr1.intersect(sr2), "Segmentos paralelos não devem gerar interseção.");
    }

    @Test
    void intersectSegmento_ColinearesComSobreposicao_RetornaPonto() {
        SegmentoReta sr1 = new SegmentoReta(new Ponto(0, 0), new Ponto(4, 0));
        SegmentoReta sr2 = new SegmentoReta(new Ponto(2, 0), new Ponto(6, 0));

        Ponto p = sr1.intersect(sr2);

        assertNotNull(p);
        assertEquals(2.0, p.getX(), Ponto.eps, "Deveria retornar uma das extremidades da área de sobreposição.");
        assertEquals(0.0, p.getY(), Ponto.eps);
    }

    @Test
    void intersectSegmento_ColinearesSemSobreposicao_RetornaNull() {
        SegmentoReta sr1 = new SegmentoReta(new Ponto(0, 0), new Ponto(2, 0));
        SegmentoReta sr2 = new SegmentoReta(new Ponto(4, 0), new Ponto(6, 0));

        assertNull(sr1.intersect(sr2), "Segmentos na mesma linha, mas afastados, não devem intersetar.");
    }

    @Test
    void intersectVetor_CruzaVetor_RetornaPontoDeIntersecao() {
        Vetor v = new Vetor(3.22, 3.72);
        SegmentoReta sr = new SegmentoReta(new Ponto(3.4, -0.24), new Ponto(-2.78, 3.84));

        Ponto result = sr.intersect(v);

        assertNotNull(result);
        assertEquals(1.1, result.getX(), 0.01);
        assertEquals(1.28, result.getY(), 0.01);
    }

    @Test
    void intersectVetor_NaoChegaAoSegmento_RetornaNull() {
        Vetor v = new Vetor(1, 4);
        SegmentoReta sr = new SegmentoReta(new Ponto(4, 4), new Ponto(6, 6));

        assertNull(sr.intersect(v));
    }

    @Test
    void intersectVetor_ParaleloAoSegmento_RetornaNull() {
        Vetor v = new Vetor(4, 0);
        SegmentoReta sr = new SegmentoReta(new Ponto(0, 2), new Ponto(4, 2));

        assertNull(sr.intersect(v));
    }

    @Test
    void intersectVetor_ColinearEComSobreposicao_RetornaExtremidade() {
        Vetor v = new Vetor(4, 4);
        SegmentoReta sr = new SegmentoReta(new Ponto(2, 2), new Ponto(6, 6));

        Ponto result = sr.intersect(v);

        assertNotNull(result);
        assertTrue(result.equals(new Ponto(4, 4)) || result.equals(new Ponto(2, 2)), "Deveria retornar a extremidade do vetor ou do segmento na sobreposição.");
    }

    @Test
    void intersectCirculo_SegmentoAtravessaCirculo_RetornaDoisPontos() {
        SegmentoReta sr = new SegmentoReta(new Ponto(0, 2), new Ponto(10, 2));
        Circulo c = new Circulo(new Ponto(5, 2), 2);
        List<Ponto> esperado = List.of(
                new Ponto(3, 2),
                new Ponto(7, 2)
        );

        assertEquals(esperado, sr.intersect(c));
    }

    @Test
    void intersectCirculo_LongeDoCirculo_RetornaNull() {
        SegmentoReta sr = new SegmentoReta(new Ponto(0, 0), new Ponto(2, 2));
        Circulo c = new Circulo(new Ponto(10, 10), 1);

        assertNull(sr.intersect(c));
    }

    @Test
    void intersectCirculo_ApenasDirecaoCruzaMasSegmentoCurto_RetornaNull() {
        SegmentoReta sr = new SegmentoReta(new Ponto(0, 2), new Ponto(2, 2));
        Circulo c = new Circulo(new Ponto(5, 2), 2);

        assertNull(sr.intersect(c));
    }

    @Test
    void intersectCirculo_TangenteAoCirculo_RetornaListaComUmPonto() {
        SegmentoReta seg = new SegmentoReta(new Ponto(3, 2), new Ponto(3, 5));
        Circulo circulo = new Circulo(new Ponto(4, 3), 1);

        List<Ponto> interseca = seg.intersect(circulo);

        assertNotNull(interseca);
        assertFalse(interseca.isEmpty(), "Deveria haver exatamente 1 ponto de interseção tangente.");
        assertEquals(1, interseca.size());
    }

    @Test
    void intersectRota_CruzaComUmSegmentoDaRota_RetornaPontoDeIntersecao() {
        List<Ponto> pontos = List.of(
                new Ponto(0, 1),
                new Ponto(1, 1),
                new Ponto(4, 4),
                new Ponto(4, 3)
        );
        Route rota = new Route(pontos);
        SegmentoReta seg = new SegmentoReta(new Ponto(2, 1), new Ponto(2, 4));
        List<Ponto> esperado = List.of(new Ponto(2, 2));

        assertEquals(esperado, seg.intersect(rota));
    }

    @Test
    void intersectRota_NaoCruzaNenhumSegmentoDaRota_RetornaNull() {
        List<Ponto> pontos = List.of(
                new Ponto(4, 4),
                new Ponto(6, 6),
                new Ponto(7, 7)
        );
        Route rota = new Route(pontos);
        SegmentoReta seg = new SegmentoReta(new Ponto(2, 2), new Ponto(4, 2));

        assertNull(seg.intersect(rota));
    }

    @Test
    void ponto_Pertence_Segmento(){
        SegmentoReta seg = new SegmentoReta(new Ponto(0,0), new Ponto(5,5));
        Ponto pertence = new Ponto(2.5,2.5);
        assertTrue(seg.noSegmento(pertence));
       }

    @Test
    void ponto_Nao_Pertence(){
        SegmentoReta seg = new SegmentoReta(new Ponto(0,0), new Ponto(5,5));
        Ponto pertence = new Ponto(2.6,2.6);
        assertTrue(seg.noSegmento(pertence));

    }

    @Test
    void ponto_Pertence_tolerancia(){
        SegmentoReta seg = new SegmentoReta(new Ponto(0,0), new Ponto(5,5));
        Ponto pertence = new Ponto(2.5+Ponto.eps,2.5+Ponto.eps);
        assertTrue(seg.noSegmento(pertence));
    }

}