package telas;

import entidades.Biscoito;
import entidades.Jogador;
import entidades.LutadorUESB;
import gerenciadores.BancoLutadores;
import gerenciadores.GameLoop;
import gerenciadores.GerenciadorRounds;
import gerenciadores.GerenciadorSom;
import gerenciadores.GerenciadorTelas;
import gerenciadores.InputManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GamePanel extends JPanel {

    private static final int FPS = 60;

    private static final int LARGURA_BARRA = 400;
    private static final int ALTURA_BARRA = 28;

    private Image cenario;

    private Jogador jogador1;
    private Jogador jogador2;

    // teclas que estão sendo seguradas nesse momento
    private final Set<Integer> teclasPressionadas = new HashSet<>();

    private GameLoop loop;

    // cronômetro da luta
    private int tempoRestante = 99;
    private int contadorSegundo = 0;

    // fim de round
    private boolean jogoEncerrado = false;
    private boolean acabouPorTempo = false;
    private String mensagemFinal = "";
    private InputManager inputManager;

    private GerenciadorRounds gerenciadorRounds;

    // tempo que o K.O. fica na tela (2 segundos)
    private int tempoKO = 120;
    // indica que estamos esperando para iniciar o próximo round
    private boolean esperandoProximoRound = false;

    private enum EstadoLuta {
        CONTAGEM,
        LUTANDO,
        FINALIZADO
    }

    private EstadoLuta estadoLuta = EstadoLuta.CONTAGEM;
    private int contadorRound = 0;
    private int tempoContagem = 300; // 4 segundos em 60 FPS
    private String textoRound = "";
    private ArrayList<Biscoito> biscoitos = new ArrayList<>();

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

        LutadorUESB lutador1 = BancoLutadores.get(player1);
        LutadorUESB lutador2 = BancoLutadores.get(player2);

        biscoitos = new ArrayList<>();

        jogador1 = new Jogador(
            lutador1,
            100,
            280,
            true,
            inputManager,
            1,
            biscoitos
        );

        jogador2 = new Jogador(
            lutador2,
            880,
            280,
            false,
            inputManager,
            2,
            biscoitos
        );

        carregarCenario(arquivoCenario);
        gerenciadorRounds = new GerenciadorRounds();

        loop = new GameLoop(this);
        loop.start();
        textoRound = gerenciadorRounds.getTextoRound();

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
            loop.encerrar();
        }
    }

    public void atualizarJogo() {

        if (esperandoProximoRound) {

            tempoKO--;

            if (tempoKO <= 0) {

                esperandoProximoRound = false;

                gerenciadorRounds.proximoRound();

                resetarRound();

            }

            repaint();

            return;
        }

         if (estadoLuta == EstadoLuta.CONTAGEM) {
            atualizarContagem();

            return;
        }


        if (jogoEncerrado) return;

        limitarNaTela(jogador1);
        limitarNaTela(jogador2);

        int centro1 = jogador1.getHitbox().getCentroX();
        int centro2 = jogador2.getHitbox().getCentroX();

        if (centro1 < centro2) {
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

        for(Biscoito b : biscoitos){
            b.atualizar();
        }

        verificarBiscoitos();

        biscoitos.removeIf(b -> !b.isAtivo());


        atualizarTemporizador();
        verificarFimDeJogo();
    }

    private void atualizarContagem(){

        tempoContagem--;

        if(tempoContagem >= 240){
            if(tempoContagem == 240){
                if(gerenciadorRounds.getRoundAtual() == 1)
                    GerenciadorSom.tocarRound1();
                else if(gerenciadorRounds.getRoundAtual() == 2)
                    GerenciadorSom.tocarRound2();
                else
                    GerenciadorSom.tocarRoundFinal();
            }
            textoRound = gerenciadorRounds.getTextoRound();

        } 
        else if(tempoContagem > 180){

            textoRound = "3";

        }
        else if(tempoContagem > 120){

            textoRound = "2";

        }
        else if(tempoContagem > 60){

            textoRound = "1";

        }
        else if(tempoContagem > 0){
            if(tempoContagem == 60)
                GerenciadorSom.tocarLutem();
                if(gerenciadorRounds.getRoundAtual() == 1)
                    GerenciadorSom.iniciarMusicaLuta();

            textoRound = "LUTEM!";

        }
        else {

            estadoLuta = EstadoLuta.LUTANDO;

        }

    }

    // aplica dano quando um golpe em andamento encosta no adversário
    // (o dano já leva em conta a força do personagem + o golpe usado,
    // calculado lá no Personagem.calcularDano)
    private void verificarGolpe(Jogador atacante, Jogador alvo) {

        if (!atacante.isAtacando() || atacante.isGolpeAcertou()) {
            return;
        }

        if (atacante.getHitbox().colideCom(alvo.getHitbox())) {

            if (alvo.isBloqueando()) {

                // Recebe apenas parte do dano
                alvo.receberDano(atacante.getDanoGolpeAtual() / 4);

                atacante.adicionarEnergiaEspecial(3);
                alvo.adicionarEnergiaEspecial(2);

                // Som de bloqueio
                //GerenciadorSom.tocarBloqueio();

            } else {

                // Recebe dano normal
                alvo.receberDano(atacante.getDanoGolpeAtual());
                atacante.adicionarEnergiaEspecial(8);
                alvo.adicionarEnergiaEspecial(4);

                // Som do impacto
                GerenciadorSom.tocarSoco();
            }

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

        String vencedor = null;

        // =========================
        // MORTE DO JOGADOR
        // =========================
        if (!jogador1.estaVivo() || !jogador2.estaVivo()) {

            if (!jogador1.estaVivo() && !jogador2.estaVivo()) {

                mensagemFinal = "EMPATE!";
                jogoEncerrado = true;
                return;

            } else if (!jogador1.estaVivo()) {

                vencedor = jogador2.getNome();

            } else {

                vencedor = jogador1.getNome();

            }
        }

        // =========================
        // TEMPO ACABOU
        // =========================
        else if (tempoRestante <= 0) {

            acabouPorTempo = true;


            if (jogador1.getVida() > jogador2.getVida()) {

                vencedor = jogador1.getNome();

            } else if (jogador2.getVida() > jogador1.getVida()) {

                vencedor = jogador2.getNome();

            } else {

                esperandoProximoRound = true;
                tempoKO = 120;
                return;

            }

        }

        // =========================
        // FINALIZA ROUND
        // =========================
       if (vencedor != null) {

            // registra quem venceu o round
            if (vencedor.equals(jogador1.getNome())) {

                gerenciadorRounds.jogador1Venceu();
                GerenciadorSom.tocarJogador1Venceu();

            } else {

                gerenciadorRounds.jogador2Venceu();
                GerenciadorSom.tocarJogador2Venceu();

            }

            // verifica se alguém ganhou a luta
            if (gerenciadorRounds.lutaTerminou()) {

                jogoEncerrado = true;

                if (gerenciadorRounds.vencedorFinal() == 1) {

                    mensagemFinal =
                        jogador1.getNome() + " VENCEU A LUTA!";

                } else {

                    mensagemFinal =
                        jogador2.getNome() + " VENCEU A LUTA!";
                }

            } else {

                mensagemFinal =
                    vencedor + " VENCEU O ROUND!";

                esperandoProximoRound = true;
                tempoKO = 120;
            }
        }
    }

    private void resetarRound() {

        jogador1.setVida(100);
        jogador2.setVida(100);

        jogador1.setX(100);
        jogador2.setX(880);

        jogador1.setY(280);
        jogador2.setY(280);

        tempoRestante = 99;
        contadorSegundo = 0;

        jogoEncerrado = false;

        jogador1.resetarEstado();
        jogador2.resetarEstado();

        estadoLuta = EstadoLuta.CONTAGEM;
        tempoContagem = 300;
        textoRound = gerenciadorRounds.getTextoRound();

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

        for(Biscoito b : biscoitos){
            b.desenhar(g);
        }
        if(estadoLuta == EstadoLuta.CONTAGEM){
            desenharContagem(g);
        }

        if (jogoEncerrado) {
            desenharFimDeJogo(g);
        }
    }

    private void desenharContagem(Graphics g){

        g.setColor(Color.WHITE);
        g.setFont(new Font(
            "Arial",
            Font.BOLD,
            70
        ));


        int largura =
            g.getFontMetrics()
            .stringWidth(textoRound);


        g.drawString(
            textoRound,
            (getWidth()-largura)/2,
            250
        );

    }

    private void desenharHud(Graphics g) {

        desenharBarraDeVida(
            g,
            jogador1,
            40,
            30,
            jogador1.getNome(),
            false
        );

        desenharBarraEspecial(
            g,
            jogador1,
            40,
            65,
            false
        );

        desenharBarraDeVida(
            g,
            jogador2,
            getWidth() - 40 - LARGURA_BARRA,
            30,
            jogador2.getNome(),
            true
        );

        desenharBarraEspecial(
            g,
            jogador2,
            getWidth() - 40 - LARGURA_BARRA,
            65,
            true
        );

        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 20));

        String round = gerenciadorRounds.getTextoRound();

        int larguraRound = g.getFontMetrics().stringWidth(round);

        g.drawString(
            round,
            (getWidth() - larguraRound) / 2,
            25
        );

        desenharVitorias(g);

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

    private void desenharBarraEspecial(
        Graphics g,
        Jogador jogador,
        int x,
        int y,
        boolean cresceDaDireita) {

        int largura = LARGURA_BARRA;
        int altura = 12;

        // fundo
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, largura, altura);

        double percentual = jogador.getEnergiaEspecial() / 100.0;

        if(percentual < 0)
            percentual = 0;

        int larguraAtual = (int)(largura * percentual);
        if (jogador.isEspecialAtivo()) {

            if ((System.currentTimeMillis() / 150) % 2 == 0) {
                g.setColor(new Color(255,215,0)); // dourado
            } else {
                g.setColor(Color.YELLOW);
            }

        } else if (jogador.getEnergiaEspecial() >= 100) {

            if ((System.currentTimeMillis() / 200) % 2 == 0) {
                g.setColor(Color.YELLOW);
            } else {
                g.setColor(Color.ORANGE);
            }

        } else {

            g.setColor(Color.CYAN);

        }
        if(cresceDaDireita){

            g.fillRect(
                x + largura - larguraAtual,
                y,
                larguraAtual,
                altura
            );

        }else{

            g.fillRect(
                x,
                y,
                larguraAtual,
                altura
            );

        }

        g.setColor(Color.WHITE);
        g.drawRect(x, y, largura, altura);
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

    private void desenharVitorias(Graphics g) {

        int y = 85;

        int diametro = 20;
        int espacamento = 30;

        // =======================
        // Jogador 1
        // =======================
        for (int i = 0; i < 2; i++) {

            int x = 160 + i * espacamento;

            // Borda
            g.setColor(Color.WHITE);
            g.drawOval(x, y, diametro, diametro);

            // Preenchimento
            if (i < gerenciadorRounds.getVitoriasJogador1()) {
                g.setColor(Color.YELLOW);
            } else {
                g.setColor(Color.DARK_GRAY);
            }

            g.fillOval(x + 2, y + 2, diametro - 4, diametro - 4);
        }

        // =======================
        // Jogador 2
        // =======================
        for (int i = 0; i < 2; i++) {

            int x = getWidth() - 220 + i * espacamento;

            // Borda
            g.setColor(Color.WHITE);
            g.drawOval(x, y, diametro, diametro);

            // Preenchimento
            if (i < gerenciadorRounds.getVitoriasJogador2()) {
                g.setColor(Color.YELLOW);
            } else {
                g.setColor(Color.DARK_GRAY);
            }

            g.fillOval(x + 2, y + 2, diametro - 4, diametro - 4);
        }
    }

    private void verificarBiscoitos(){

        for(Biscoito b : biscoitos){

            if(!b.isAtivo())
                continue;


            if(b.getHitbox().intersects(jogador1.getHitbox().getArea())){

                jogador1.receberDano(10);

                b.desativar();

            }


            if(b.getHitbox().intersects(jogador2.getHitbox().getArea())){

                jogador2.receberDano(10);

                b.desativar();

            }
        }
    }
    
}
