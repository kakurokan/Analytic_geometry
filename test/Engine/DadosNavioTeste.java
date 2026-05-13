package Engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DadosNavioTeste {
    private SnapshotSimulacao.DadosNavio dadosNavio;
    private Ponto posicao;
    private Vetor direcao,direcaoRota;
    private boolean emColisao;
    private double area = 1;
    @BeforeEach
    void setUp() {
        posicao= new Ponto(5,5);
        direcao = new Vetor(1,1);
        direcaoRota = new Vetor(2,2);
        emColisao=true;
        dadosNavio = new SnapshotSimulacao.DadosNavio(
                posicao,
                direcao,direcaoRota,emColisao,area
        );
    }

    @Test
    void testeGetPosicao(){
        assertEquals(posicao,dadosNavio.getPosicao());
    }

    @Test
    void testeGetDirecao(){
        assertEquals(direcao,dadosNavio.getDirecao());
    }

    @Test
    void testeGetDirecaoRota(){
        assertEquals(direcaoRota,dadosNavio.getDirecaoRota());
    }

    @Test
    void testeEmColisao(){
        assertTrue(emColisao);
    }

    @Test
    void testeGetRaioArea(){
        assertEquals(1,dadosNavio.getRaioArea());
    }

    @Test
    void testeEquals_True(){
        assertTrue(new SnapshotSimulacao.DadosNavio(posicao,direcao,direcaoRota,emColisao,area).equals(dadosNavio));
    }

    @Test
    void testeEquals_False(){
        assertFalse(new SnapshotSimulacao.DadosNavio(posicao,direcao,direcaoRota,false ,area).equals(dadosNavio));
    }


}
