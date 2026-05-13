package Engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SnapshotSimulacaoTest {
    private  Map<String, List<SnapshotSimulacao.NavioEmEspera>> naviosEmEsperaPorPorto;
    private List<SnapshotSimulacao.DadosNavio> dadosNavios;
    private double tempoSimulacao;
    private SnapshotSimulacao snapshotSimulacao;
    @BeforeEach
    void setUp() {
        String nomePorto = "Albufeira cais";
        SnapshotSimulacao.NavioEmEspera navioEmEspera = new SnapshotSimulacao.NavioEmEspera(5,"Faro cais", 2);
        naviosEmEsperaPorPorto = new HashMap<>();
        naviosEmEsperaPorPorto.put(nomePorto,new ArrayList<>());
        naviosEmEsperaPorPorto.get(nomePorto).add(navioEmEspera);
        SnapshotSimulacao.DadosNavio dadosNavio= new SnapshotSimulacao.DadosNavio(new Ponto(2,2), new Vetor(2.3,4.5), new Vetor(4.5,6),false,1.0);
        dadosNavios = new ArrayList<>();
        dadosNavios.add(dadosNavio);
        tempoSimulacao = 3.5;
        snapshotSimulacao = new SnapshotSimulacao(naviosEmEsperaPorPorto,dadosNavios,tempoSimulacao);
    }
    @Test
    void getTempoSimulacao() {
        assertEquals(tempoSimulacao,snapshotSimulacao.getTempoSimulacao());
    }

    @Test
    void getNaviosEmEsperaPorPorto() {
        assertEquals(naviosEmEsperaPorPorto,snapshotSimulacao.getNaviosEmEsperaPorPorto());
    }

    @Test
    void getDadosNavios() {
        assertEquals(dadosNavios,snapshotSimulacao.getDadosNavios());
    }

}