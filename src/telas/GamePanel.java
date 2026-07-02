package telas;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import entidades.Jogador;
import gerenciadores.GerenciadorTelas;

public class GamePanel extends JPanel implements KeyListener {

    private static final int FPS = 60;

    private Image cenario;

    private Jogador jogador1;
    private Jogador jogador2;

    // teclas que estão sendo seguradas nesse momento
    private final Set<Integer> teclasPressionadas = new HashSet<>();

    private Timer loop;

    // guardados só pra quando os outros personagens tiverem sprites
    private int indicePersonagem1;
    private int indicePersonagem2;

    public GamePanel(
        GerenciadorTelas janela,
        int player1,
        int player2,
        String arquivoCenario
    ) {

        this.indicePersonagem1 = player1;
        this.indicePersonagem2 = player2;

        setFocusable(true);
        addKeyListener(this);

        URL cenarioUrl = getClass().getResource(
            "/assets/cenarios/" + arquivoCenario
        );

        if (cenarioUrl != null) {
            cenario = new ImageIcon(cenarioUrl).getImage();
        }

        jogador1 = new Jogador(150, 300, true);
        jogador2 = new Jogador(950, 300, false);

        loop = new Timer(1000 / FPS, e -> {
            atualizarJogo();
            repaint();
        });

        loop.start();
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

        // ---- movimento jogador 1 (WASD) ----
        if (estaPressionada(KeyEvent.VK_A)) {
            jogador1.moverEsquerda();
        }

        if (estaPressionada(KeyEvent.VK_D)) {
            jogador1.moverDireita();
        }

        if (estaPressionada(KeyEvent.VK_W)) {
            jogador1.pular();
        }

        // ---- movimento jogador 2 (setas) ----
        if (estaPressionada(KeyEvent.VK_LEFT)) {
            jogador2.moverEsquerda();
        }

        if (estaPressionada(KeyEvent.VK_RIGHT)) {
            jogador2.moverDireita();
        }

        if (estaPressionada(KeyEvent.VK_UP)) {
            jogador2.pular();
        }

        limitarNaTela(jogador1);
        limitarNaTela(jogador2);

        // cada jogador vira automaticamente pro lado do adversário
        if (jogador1.getX() < jogador2.getX()) {
            jogador1.setViradoDireita(true);
            jogador2.setViradoDireita(false);
        } else {
            jogador1.setViradoDireita(false);
            jogador2.setViradoDireita(true);
        }

        jogador1.atualizar();
        jogador2.atualizar();
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

        // HUD (vida, timer etc) fica pra próxima etapa
    }

    // ---------------- teclado ----------------

    @Override
    public void keyPressed(KeyEvent e) {

        teclasPressionadas.add(e.getKeyCode());

        // golpes do jogador 1: J, K, L
        if (e.getKeyCode() == KeyEvent.VK_J
            || e.getKeyCode() == KeyEvent.VK_K
            || e.getKeyCode() == KeyEvent.VK_L) {

            jogador1.atacar();
        }

        // golpes do jogador 2: 1, 2, 3 (numpad e teclado normal)
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD1
            || e.getKeyCode() == KeyEvent.VK_NUMPAD2
            || e.getKeyCode() == KeyEvent.VK_NUMPAD3
            || e.getKeyCode() == KeyEvent.VK_1
            || e.getKeyCode() == KeyEvent.VK_2
            || e.getKeyCode() == KeyEvent.VK_3) {

            jogador2.atacar();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        teclasPressionadas.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
