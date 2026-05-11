package GUI;

import Engine.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representa um painel que exibe um mapa com rotas marítimas, pontos de interesse,
 * obstáculos, condições meteorológicas e outros elementos relacionados à navegação.
 * Essa classe estende a {@code JPanel} e é responsável por criar uma interface gráfica
 * para visualização de dados de simulação marítima.
 *
 * @author Acrismede Mendes, Alexandre Guerreiro, Léo Souza
 * @version 11/05/26
 * @inv rotas != null && obstaculos != null && posicoesPortos != null
 * @inv escala > 0
 */
public class PainelMapa extends JPanel {
    private final List<Route> rotas;
    private final List<Poligono> obstaculos;
    private final Map<String, Ponto> posicoesPortos;
    private final Map<Ponto, Character> nomePontos = new HashMap<>();
    private final int escala = 50;
    private final int offsetX = 200;
    private final int offsetY = 50;
    private final Color[] coresRotas = {Color.RED, Color.MAGENTA, Color.ORANGE, new Color(0, 150, 0), Color.PINK};
    private Vetor corrente;
    private List<Tempestade> tempestades;
    private SnapshotSimulacao snapshot;
    private boolean apontarParaRota;

    /**
     * Constrói um painel gráfico que exibe um mapa com rotas, tempestades, obstáculos estáticos,
     * posições de portos e informações sobre a corrente marítima.
     *
     * @param rotas               A lista de rotas a serem exibidas no painel.
     * @param tempestades         A lista de tempestades presentes no mapa.
     * @param obstaculosEstaticos A lista de polígonos que representam obstáculos estáticos.
     * @param posicoesPortos      Mapeamento dos nomes dos portos para as suas coordenadas geográficas.
     * @param corrente            O vetor que representa a corrente marítima no mapa.
     */
    public PainelMapa(List<Route> rotas, List<Tempestade> tempestades, List<Poligono> obstaculosEstaticos, Map<String, Ponto> posicoesPortos, Vetor corrente) {
        this.rotas = rotas;
        this.tempestades = tempestades;
        this.obstaculos = obstaculosEstaticos;
        this.posicoesPortos = posicoesPortos;
        this.corrente = corrente;
        this.apontarParaRota = false;

        setBackground(Color.white);
        atribuirLetrasAosPontos();
    }

    /**
     * Recupera o vetor que representa a corrente marítima no mapa.
     *
     * @return o vetor que representa a corrente marítima.
     */
    public Vetor getCorrente() {
        return corrente;
    }

    /**
     * Define o vetor que representa a corrente marítima no mapa.
     *
     * @param corrente o vetor que representa a corrente marítima. Não pode ser nulo.
     */
    public void setCorrente(Vetor corrente) {
        this.corrente = corrente;
    }

    /**
     * Converte uma coordenada real X para a coordenada correspondente no espaço gráfico do painel.
     *
     * @param x a coordenada real no espaço horizontal.
     * @return a coordenada transformada no espaço de píxeis do painel.
     */
    private int telaX(double x) {
        return offsetX + (int) (x * escala);
    }

    /**
     * Converte uma coordenada real Y para a coordenada correspondente no espaço gráfico do painel.
     *
     * @param y a coordenada real no espaço vertical.
     * @return a coordenada transformada no espaço de píxeis do painel.
     */
    private int telaY(double y) {
        return getHeight() - offsetY - (int) (y * escala);
    }

    /**
     * Atribui letras únicas (A, B, C...) aos pontos presentes nas rotas e obstáculos do mapa.
     */
    private void atribuirLetrasAosPontos() {
        char letraAtual = 'A';

        for (Route r : rotas) {
            for (SegmentoReta seg : r.getSegmentos()) {
                if (!nomePontos.containsKey(seg.getA()))
                    nomePontos.put(seg.getA(), letraAtual++);
                if (!nomePontos.containsKey(seg.getB()))
                    nomePontos.put(seg.getB(), letraAtual++);
            }
        }

        for (Poligono poligono : obstaculos) {
            for (Ponto p : poligono.getVertices()) {
                if (!nomePontos.containsKey(p))
                    nomePontos.put(p, letraAtual++);
            }
        }
    }

    /**
     * Define a lista de tempestades dinâmicas que serão exibidas no mapa.
     *
     * @param t A lista de tempestades a ser atribuída.
     */
    public void setTempestades(List<Tempestade> t) {
        this.tempestades = t;
    }

    /**
     * Define se o desenho deve usar a direção da rota ou a direção
     * real do navio
     *
     * @param apontarParaRota um valor booleano que indica se é para
     *                        usar a direção da rota
     */
    public void setApontarParaRota(boolean apontarParaRota) {
        this.apontarParaRota = apontarParaRota;
    }

    /**
     * Atualiza o instantâneo (snapshot) de dados da simulação para renderização dos elementos móveis.
     *
     * @param snapshotSimulacao O novo snapshot contendo as posições atuais de navios e portos.
     */
    public void atualizarSnapshot(SnapshotSimulacao snapshotSimulacao) {
        this.snapshot = snapshotSimulacao;
    }

    /**
     * Método principal de desenho do componente que orquestra a renderização de todos os elementos.
     *
     * @param g O objeto {@code Graphics} para desenho.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        desenharEixos(graphics2D);
        desenharObstaculos(graphics2D);
        desenharTempestades(graphics2D);
        desenharRotasEPontos(graphics2D);
        desenharCaixaCorrente(graphics2D);

        if (snapshot != null) {
            desenharNavios(graphics2D);
            desenharCaixasFlutuantesPorto(graphics2D);
            desenharRelogio(graphics2D);
        }
    }

    /**
     * Desenha os eixos cartesianos, as marcas de escala e as etiquetas de identificação X e Y.
     *
     * @param g O contexto gráfico {@code Graphics2D}.
     */
    private void desenharEixos(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        int origemX = telaX(0);
        int origemY = telaY(0);

        g.drawLine(origemX, origemY, origemX, 20);
        g.drawString("Y", origemX - 15, 20);

        g.drawLine(origemX, origemY, getWidth() - 20, origemY);
        g.drawString("X", getWidth() - 20, origemY + 15);

        g.setStroke(new BasicStroke(1));
        g.setFont(new Font("Arial", Font.PLAIN, 10));

        int maxX = (getWidth() - offsetX) / escala;
        int maxY = (getHeight() - offsetY) / escala;

        for (int i = 1; i <= maxX; i++) {
            int posX = telaX(i);
            g.drawLine(posX, origemY - 4, posX, origemY + 4);
            g.drawString(String.valueOf(i), posX - 3, origemY + 18);
        }

        for (int i = 1; i <= maxY; i++) {
            int posY = telaY(i);
            g.drawLine(origemX - 4, posY, origemX + 4, posY);
            g.drawString(String.valueOf(i), origemX - 18, posY + 4);
        }

        g.drawString("0", origemX - 12, origemY + 14);
    }

    /**
     * Renderiza as tempestades como círculos semi-transparentes sobre o mapa.
     *
     * @param g O contexto gráfico {@code Graphics2D}.
     */
    private void desenharTempestades(Graphics2D g) {
        g.setColor(new Color(44, 136, 227, 80));
        for (Tempestade tempestade : tempestades) {
            Circulo c = tempestade.getArea();
            int r = (int) (c.getRaio() * escala);
            int cx = telaX(c.getCentro().getX());
            int cy = telaY(c.getCentro().getY());
            g.fillOval(cx - r, cy - r, r * 2, r * 2);
        }
    }

    /**
     * Renderiza as rotas coloridas e os pontos identificados por letras no mapa.
     *
     * @param g O contexto gráfico {@code Graphics2D}.
     */
    private void desenharRotasEPontos(Graphics2D g) {
        int corIndex = 0;

        g.setStroke(new BasicStroke(2));
        for (Route rota : rotas) {
            g.setColor(coresRotas[corIndex % coresRotas.length]);
            for (SegmentoReta seg : rota.getSegmentos()) {
                g.drawLine(telaX(seg.getA().getX()), telaY(seg.getA().getY()),
                        telaX(seg.getB().getX()), telaY(seg.getB().getY()));
            }
            corIndex++;
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        for (Map.Entry<Ponto, Character> entry : nomePontos.entrySet()) {
            Ponto p = entry.getKey();
            int x = telaX(p.getX());
            int y = telaY(p.getY());

            g.fillOval(x - 3, y - 3, 6, 6);
            g.drawString(String.valueOf(entry.getValue()), x + 5, y - 5);
        }
    }

    /**
     * Desenha os navios em movimento, incluindo a sua orientação e o alerta visual de colisão.
     *
     * @param g O contexto gráfico {@code Graphics2D}.
     */
    private void desenharNavios(Graphics2D g) {
        for (SnapshotSimulacao.DadosNavio navio : snapshot.getDadosNavios()) {
            Ponto posAtual = navio.getPosicao();
            Vetor direcao = navio.getDirecao();

            int x = telaX(posAtual.getX());
            int y = telaY(posAtual.getY());

            if (navio.isEmColisao()) {
                int raioNoEcra = (int) (navio.getRaioArea() * escala);
                g.setColor(new Color(255, 0, 0, 60));
                g.fillOval(x - raioNoEcra, y - raioNoEcra, raioNoEcra * 2, raioNoEcra * 2);
                g.setColor(Color.RED);
                Stroke linhaOriginal = g.getStroke();
                g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4}, 0));
                g.drawOval(x - raioNoEcra, y - raioNoEcra, raioNoEcra * 2, raioNoEcra * 2);
                g.setStroke(linhaOriginal);
            }

            // Escolhe qual direção usar baseado na preferência
            Vetor direcaoCalculada = (apontarParaRota && navio.getDirecaoRota() != null)
                    ? navio.getDirecaoRota()
                    : navio.getDirecao();

            double angulo = 0;
            if (direcaoCalculada != null) {
                angulo = Math.atan2(-direcaoCalculada.getY(), direcaoCalculada.getX());
            }

            AffineTransform transformacaoOriginal = g.getTransform();
            g.translate(x, y);
            g.rotate(angulo);

            int[] cascoX = {-10, 6, 12, 6, -10};
            int[] cascoY = {-5, -5, 0, 5, 5};
            g.setColor(Color.DARK_GRAY);
            g.fillPolygon(cascoX, cascoY, 5);
            g.setColor(Color.WHITE);
            g.fillRect(-8, -3, 6, 6);
            g.setColor(Color.BLACK);
            g.drawPolygon(cascoX, cascoY, 5);

            g.setTransform(transformacaoOriginal);
        }
    }

    /**
     * Desenha caixas de informação flutuantes próximas aos portos com a lista de navios em espera.
     *
     * @param g O contexto gráfico {@code Graphics2D}.
     */
    private void desenharCaixasFlutuantesPorto(Graphics2D g) {
        g.setFont(new Font("Arial", Font.PLAIN, 11));

        for (Map.Entry<String, Ponto> entry : posicoesPortos.entrySet()) {
            String nomePorto = entry.getKey();
            Ponto posicao = entry.getValue();
            int cx = telaX(posicao.getX());
            int cy = telaY(posicao.getY());

            List<SnapshotSimulacao.NavioEmEspera> emEspera = snapshot.getNaviosEmEsperaPorPorto().getOrDefault(nomePorto, new ArrayList<>());
            List<String> linhasTexto = new ArrayList<>();
            linhasTexto.add("[" + nomePorto + " (" + nomePontos.get(posicao) + ")]");
            if (emEspera.isEmpty()) {
                linhasTexto.add("Nenhum navio em espera");
            } else {
                for (SnapshotSimulacao.NavioEmEspera info : emEspera) {
                    linhasTexto.add(info.toString());
                }
            }

            int larguraCaixa = 180;
            int alturaCaixa = (linhasTexto.size() * 15) + 10;
            int boxX = cx + 30;
            int boxY = cy + 30;

            if (boxX + larguraCaixa > getWidth() - 10) boxX = cx - larguraCaixa - 30;
            if (boxY + alturaCaixa > getHeight() - 10) boxY = cy - alturaCaixa - 30;

            g.setColor(Color.GRAY);
            Stroke linhaOriginal = g.getStroke();
            g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4}, 0));
            int pontoAncoragemX = (boxX > cx) ? boxX : boxX + larguraCaixa;
            int pontoAncoragemY = (boxY > cy) ? boxY : boxY + alturaCaixa;
            g.drawLine(cx, cy, pontoAncoragemX, pontoAncoragemY);
            g.setStroke(linhaOriginal);

            g.setColor(new Color(255, 255, 255, 200));
            g.fillRect(boxX, boxY, larguraCaixa, alturaCaixa);
            g.setColor(Color.BLACK);
            g.drawRect(boxX, boxY, larguraCaixa, alturaCaixa);

            int textY = boxY + 15;
            for (String linha : linhasTexto) {
                g.drawString(linha, boxX + 5, textY);
                textY += 15;
            }
        }
    }

    /**
     * Exibe o tempo corrente de simulação no topo do painel.
     *
     * @param g O contexto gráfico {@code Graphics2D}.
     */
    private void desenharRelogio(Graphics2D g) {
        String textoTempo = String.format("Tempo: %.1f", snapshot.getTempoSimulacao());
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.setColor(Color.BLACK);
        FontMetrics metricas = g.getFontMetrics();
        int larguraTexto = metricas.stringWidth(textoTempo);
        int textoX = telaX(0) - larguraTexto - 45;
        int textoY = telaY(0) - 400;
        g.drawString(textoTempo, textoX, textoY);
    }

    /**
     * Renderiza os obstáculos estáticos como polígonos beges com bordas pretas.
     *
     * @param g O contexto gráfico {@code Graphics2D}.
     */
    private void desenharObstaculos(Graphics2D g) {
        Color bege = new Color(245, 245, 220);
        for (Poligono poligono : obstaculos) {
            Ponto[] vertices = poligono.getVertices();
            int nVertices = vertices.length;
            int[] xVertices = new int[nVertices];
            int[] yVertices = new int[nVertices];

            for (int i = 0; i < nVertices; i++) {
                xVertices[i] = telaX(vertices[i].getX());
                yVertices[i] = telaY(vertices[i].getY());
            }

            Polygon shapePoligono = new Polygon(xVertices, yVertices, nVertices);
            g.setColor(bege);
            g.fillPolygon(shapePoligono);
            g.setColor(Color.BLACK);
            g.drawPolygon(shapePoligono);
        }
    }

    /**
     * Desenha a caixa informativa com os componentes da corrente marítima atual.
     *
     * @param g O contexto gráfico {@code Graphics2D}.
     */
    private void desenharCaixaCorrente(Graphics2D g) {
        if (corrente == null) return;

        int largura = 130;
        int altura = 110;
        int corte = 20;
        int boxX = telaX(0) - largura - 30;
        int boxY = telaY(0) - altura - 200;

        int[] xPoints = {boxX, boxX + largura - corte, boxX + largura, boxX + largura, boxX};
        int[] yPoints = {boxY, boxY, boxY + corte, boxY + altura, boxY + altura};

        int offsetSombra = 5;
        int[] sombraX = {boxX + offsetSombra, boxX + largura - corte + offsetSombra, boxX + largura + offsetSombra, boxX + largura + offsetSombra, boxX + offsetSombra};
        int[] sombraY = {boxY + offsetSombra, boxY + offsetSombra, boxY + corte + offsetSombra, boxY + altura + offsetSombra, boxY + altura + offsetSombra};

        g.setColor(new Color(130, 145, 165));
        g.fillPolygon(sombraX, sombraY, 5);

        g.setColor(new Color(230, 245, 225));
        g.fillPolygon(xPoints, yPoints, 5);

        g.setColor(Color.RED);
        g.setFont(new Font("Times New Roman", Font.BOLD, 18));
        g.drawString("Velocidade", boxX + 15, boxY + 25);
        g.drawString("da corrente", boxX + 15, boxY + 45);

        g.setFont(new Font("Times New Roman", Font.BOLD, 20));
        g.drawString("X = " + String.format("%.0f", corrente.getX()), boxX + 15, boxY + 80);
        g.drawString("Y = " + String.format("%.0f", corrente.getY()), boxX + 15, boxY + 100);
    }
}