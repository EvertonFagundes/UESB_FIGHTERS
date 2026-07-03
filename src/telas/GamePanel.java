package telas;

import entidades.Jogador;
import gerenciadores.GerenciadorTelas;
import gerenciadores.InputManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class GamePanel extends JPanel {

    private static final int FPS = 60;

    // mesma ordem de índices usada na SelecaoPanel
    private static final String[] NOMES_PERSONAGENS = {
        "EVERTON",
        "ERICK",
        "GIULIA"
    };

    private static final int LARGURA_BARRA = 400;
    private static final int ALTURA_BARRA = 28;

    private Image cenario;

    private Jogador jogador1;
    private Jogador jogador2;

    // teclas que estão sendo seguradas nesse momento
    private final Set<Integer> teclasPressionadas = new HashSet<>();

    private Timer loop;

    // guardados pra mostrar o nome certo na barra de vida
    // (os sprites por enquanto são sempre do Everton)
    private int indicePersonagem1;
    private int indicePersonagem2;

    // cronômetro da luta
    private int tempoRestante = 99;
    private int contadorSegundo = 0;

    // fim de round
    private boolean jogoEncerrado = false;
    private boolean acabouPorTempo = false;
    private String mensagemFinal = "";
    private InputManager inputManager;

    public GamePanel(
        GerenciadorTelas janela,
        int player1,
        int player2,
        String arquivoCenario
    ) {

        URL cenarioUrl = getClass().getResource("/assets/cenarios/" + arquivoCenario);

        if (cenarioUrl != null) {
            cenario = new ImageIcon(cenarioUrl).getImage();
        }

        inputManager = new InputManager();
        addKeyListener(inputManager);
        System.out.println("INPUT OK");

        setFocusable(true);
        requestFocusInWindow();

        jogador1 = new Jogador(player1, 100, 280, true, inputManager, 1);
        jogador2 = new Jogador(player2, 880, 280, false, inputManager, 2);

        carregarCenario(arquivoCenario);

        loop = new Timer(1000 / FPS, e -> {
            atualizarJogo();
            repaint();
        });
        loop.start();

        setFocusable(true);
        requestFocusInWindow();

        SwingUtilities.invokeLater(() -> {
            requestFocusInWindow();
        });
    }

    // para o loop quando essa tela sai de cena, evitando
    // que o Timer continue rodando em segundo plano
    @Override
    public void removeNotify() {
        super.removeNotify();

        if (loop != null) {
            loop.stop();
        }
    }

    private void atualizarJogo() {

        if (jogoEncerrado) return;

        limitarNaTela(jogador1);
        limitarNaTela(jogador2);

        if (jogador1.getX() < jogador2.getX()) {
            jogador1.setViradoDireita(true);
            jogador2.setViradoDireita(false);
        } else {
            jogador1.setViradoDireita(false);
            jogador2.setViradoDireita(true);
        }

        jogador1.atualizar();
        jogador2.atualizar();

        verificarGolpe(jogador1, jogador2);
        verificarGolpe(jogador2, jogador1);

        atualizarTemporizador();
        verificarFimDeJogo();
    }

    // aplica dano quando um golpe em andamento encosta no adversário
    // (o dano já leva em conta a força do personagem + o golpe usado,
    // calculado lá no Personagem.calcularDano)
    private void verificarGolpe(Jogador atacante, Jogador alvo) {

        if (!atacante.isAtacando() || atacante.isGolpeAcertou()) {
            return;
        }

        if (atacante.getHitbox().colideCom(alvo.getHitbox())) {
            alvo.receberDano(atacante.getDanoGolpeAtual());
            atacante.setGolpeAcertou(true);
        }
    }

    private void atualizarTemporizador() {

        contadorSegundo++;

        if (contadorSegundo >= FPS) {

            contadorSegundo = 0;

            if (tempoRestante > 0) {
                tempoRestante--;
            }
        }
    }

    private void verificarFimDeJogo() {

        if (!jogador1.estaVivo() || !jogador2.estaVivo()) {

            jogoEncerrado = true;
            acabouPorTempo = false;

            if (!jogador1.estaVivo() && !jogador2.estaVivo()) {
                mensagemFinal = "EMPATE!";
            } else if (!jogador1.estaVivo()) {
                mensagemFinal = nomePersonagem(indicePersonagem2) + " VENCEU!";
            } else {
                mensagemFinal = nomePersonagem(indicePersonagem1) + " VENCEU!";
            }

            return;
        }

        if (tempoRestante <= 0) {

            jogoEncerrado = true;
            acabouPorTempo = true;

            if (jogador1.getVida() > jogador2.getVida()) {
                mensagemFinal = nomePersonagem(indicePersonagem1) + " VENCEU!";
            } else if (jogador2.getVida() > jogador1.getVida()) {
                mensagemFinal = nomePersonagem(indicePersonagem2) + " VENCEU!";
            } else {
                mensagemFinal = "EMPATE!";
            }
        }
    }

    private String nomePersonagem(int indice) {

        if (indice < 0 || indice >= NOMES_PERSONAGENS.length) {
            return "JOGADOR";
        }

        return NOMES_PERSONAGENS[indice];
    }

    // não deixa o jogador sair da tela
    private void limitarNaTela(Jogador jogador) {

        int largura = getWidth() > 0 ? getWidth() : 1280;

        if (jogador.getX() < 0) {
            jogador.setX(0);
        }

        if (jogador.getX() > largura - jogador.getLargura()) {
            jogador.setX(largura - jogador.getLargura());
        }
    }

    private boolean estaPressionada(int tecla) {
        return teclasPressionadas.contains(tecla);
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        // 1) cenário
        if (cenario != null) {

            g.drawImage(
                cenario,
                0,
                0,
                getWidth(),
                getHeight(),
                null
            );
        }

        // 2) player 1
        jogador1.desenhar(g);

        // 3) player 2
        jogador2.desenhar(g);

        // 4) HUD (vida + tempo)
        desenharHud(g);

        if (jogoEncerrado) {
            desenharFimDeJogo(g);
        }
    }

    private void desenharHud(Graphics g) {

        desenharBarraDeVida(
            g,
            jogador1,
            40,
            30,
            nomePersonagem(indicePersonagem1),
            false
        );

        desenharBarraDeVida(
            g,
            jogador2,
            getWidth() - 40 - LARGURA_BARRA,
            30,
            nomePersonagem(indicePersonagem2),
            true
        );

        // temporizador central
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 36));

        String texto = String.valueOf(tempoRestante);
        int larguraTexto = g.getFontMetrics().stringWidth(texto);

        g.drawString(texto, (getWidth() - larguraTexto) / 2, 55);
    }

    private void desenharBarraDeVida(
        Graphics g,
        Jogador jogador,
        int x,
        int y,
        String nome,
        boolean cresceDaDireita
    ) {

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString(nome, x, y - 8);

        // fundo (vida já perdida)
        g.setColor(new Color(60, 0, 0));
        g.fillRect(x, y, LARGURA_BARRA, ALTURA_BARRA);

        double percentual = jogador.getVida() / 100.0;

        if (percentual < 0) {
            percentual = 0;
        }

        int larguraAtual = (int) (LARGURA_BARRA * percentual);

        Color corVida;

        if (percentual > 0.5) {
            corVida = Color.GREEN;
        } else if (percentual > 0.2) {
            corVida = Color.ORANGE;
        } else {
            corVida = Color.RED;
        }

        g.setColor(corVida);

        if (cresceDaDireita) {
            g.fillRect(x + LARGURA_BARRA - larguraAtual, y, larguraAtual, ALTURA_BARRA);
        } else {
            g.fillRect(x, y, larguraAtual, ALTURA_BARRA);
        }

        g.setColor(Color.WHITE);
        g.drawRect(x, y, LARGURA_BARRA, ALTURA_BARRA);
    }

    private void desenharFimDeJogo(Graphics g) {

        g.setColor(new Color(0, 0, 0, 170));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.WHITE);

        String titulo = acabouPorTempo ? "TEMPO ESGOTADO" : "K.O.";

        g.setFont(new Font("Arial", Font.BOLD, 64));
        int larguraTitulo = g.getFontMetrics().stringWidth(titulo);
        g.drawString(titulo, (getWidth() - larguraTitulo) / 2, 300);

        g.setFont(new Font("Arial", Font.BOLD, 32));
        int larguraMsg = g.getFontMetrics().stringWidth(mensagemFinal);
        g.drawString(mensagemFinal, (getWidth() - larguraMsg) / 2, 350);
    }

    private void carregarCenario(String arquivoCenario) {

        String path = "/assets/cenarios/" + arquivoCenario;

        URL url = getClass().getResource(path);

        if (url == null) {
            System.out.println("CENÁRIO NÃO ENCONTRADO: " + path);
            return;
        }

        cenario = new ImageIcon(url).getImage();
    }

    // ---------------- teclado ----------------
    /*
    @Override
    public void keyPressed(KeyEvent e) {

        teclasPressionadas.add(e.getKeyCode());

        if (jogoEncerrado) {
            return;
        }

        // golpes do jogador 1: J leve, K médio, L forte
        if (e.getKeyCode() == KeyEvent.VK_J) {
            jogador1.atacar(1);
        } else if (e.getKeyCode() == KeyEvent.VK_K) {
            jogador1.atacar(2);
        } else if (e.getKeyCode() == KeyEvent.VK_L) {
            jogador1.atacar(3);
        }

        // golpes do jogador 2: 1 leve, 2 médio, 3 forte
        // (numpad e a fileira normal de números, pra funcionar em qualquer teclado)
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD1 || e.getKeyCode() == KeyEvent.VK_1) {
            jogador2.atacar(1);
        } else if (e.getKeyCode() == KeyEvent.VK_NUMPAD2 || e.getKeyCode() == KeyEvent.VK_2) {
            jogador2.atacar(2);
        } else if (e.getKeyCode() == KeyEvent.VK_NUMPAD3 || e.getKeyCode() == KeyEvent.VK_3) {
            jogador2.atacar(3);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        teclasPressionadas.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    */
}
