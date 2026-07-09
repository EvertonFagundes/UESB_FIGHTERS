package entidades;

import enums.Estado;
import gerenciadores.InputManager;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public class Jogador extends Personagem {

    private int playerId; // 1 ou 2

    public Jogador(int id, int x, int y, boolean viradoDireita, InputManager input, int playerId) {

        super(x, y, 300, 400, 100, 6, 10);

        this.input = input;
        this.viradoDireita = viradoDireita;
        this.playerId = playerId;


        parado = new Image[4];
        soco = new Image[5];
        andar = new Image[8];
        chute = new Image[4]; // garante que não fica null
        pulo = new Image[5];

        String nomeJogador = "";

        if (id == 0) {
            nomeJogador = "everton";
        } else if (id == 1) {
            nomeJogador = "erick";
        } else if(id == 2){
            nomeJogador = "giulia";
        }

        // IDLE
        parado[0] = carregarImagem("/assets/personagens/" + nomeJogador + "/sprites/idle_1.png");
        parado[1] = carregarImagem("/assets/personagens/" + nomeJogador + "/sprites/idle_2.png");
        parado[2] = carregarImagem("/assets/personagens/" + nomeJogador + "/sprites/idle_3.png");
        parado[3] = carregarImagem("/assets/personagens/" + nomeJogador + "/sprites/idle_4.png");

        // SOCO
        soco[0] = carregarImagem("/assets/personagens/" + nomeJogador + "/sprites/punch_1.png");
        soco[1] = carregarImagem("/assets/personagens/" + nomeJogador + "/sprites/punch_2.png");
        soco[2] = carregarImagem("/assets/personagens/" + nomeJogador + "/sprites/punch_3.png");
        soco[3] = carregarImagem("/assets/personagens/" + nomeJogador + "/sprites/punch_4.png");
        soco[4] = carregarImagem("/assets/personagens/" + nomeJogador + "/sprites/punch_5.png");

        // ANDAR
        andar[0] = carregarImagem("/assets/personagens/" + nomeJogador + "/sprites/walk_1.png");
        andar[1] = carregarImagem("/assets/personagens/" + nomeJogador + "/sprites/walk_2.png");
        andar[2] = carregarImagem("/assets/personagens/" + nomeJogador + "/sprites/walk_3.png");
        andar[3] = carregarImagem("/assets/personagens/" + nomeJogador + "/sprites/walk_4.png");
        andar[4] = carregarImagem("/assets/personagens/" + nomeJogador + "/sprites/walk_5.png");
        andar[5] = carregarImagem("/assets/personagens/" + nomeJogador + "/sprites/walk_6.png");
        andar[6] = carregarImagem("/assets/personagens/" + nomeJogador + "/sprites/walk_7.png");
        andar[7] = carregarImagem("/assets/personagens/" + nomeJogador + "/sprites/walk_8.png");

        //PULO
        pulo[0] = carregarImagem("/assets/personagens/" + nomeJogador + "/sprites/jump_1.png");
        pulo[1] = carregarImagem("/assets/personagens/" + nomeJogador + "/sprites/jump_2.png");
        pulo[2] = carregarImagem("/assets/personagens/" + nomeJogador + "/sprites/jump_3.png");
        pulo[3] = carregarImagem("/assets/personagens/" + nomeJogador + "/sprites/jump_4.png");
        pulo[4] = carregarImagem("/assets/personagens/" + nomeJogador + "/sprites/jump_5.png");


    }

    private Image carregarImagem(String caminho) {

        URL url = getClass().getResource(caminho);

        if (url == null) {
            return null;
        }

        return new ImageIcon(url).getImage();
    }

    // =========================
    // UPDATE PRINCIPAL
    // =========================
    @Override
    public void atualizar() {

        boolean movendo = false;

        // =========================
        // PLAYER 1
        // =========================
        if (playerId == 1) {

            if (input.esquerdaP1) {
                x -= velocidade;
                viradoDireita = false;
                movendo = true;
            }

            if (input.direitaP1) {
                x += velocidade;
                viradoDireita = true;
                movendo = true;
            }

            if (input.puloP1) {
                pular();

            }

            if (input.socoP1 && !atacando) {
                atacar(1);
            }

            if (input.chuteP1 && !atacando) {
                atacar(2);
            }
        }

        // =========================
        // PLAYER 2
        // =========================
        else if (playerId == 2) {

            if (input.esquerdaP2) {
                x -= velocidade;
                viradoDireita = false;
                movendo = true;
            }

            if (input.direitaP2) {
                x += velocidade;
                viradoDireita = true;
                movendo = true;
            }

            if (input.puloP2) {
                pular();
            }

            if (input.socoP2 && !atacando) {
                atacar(1);
            }

            if (input.chuteP2 && !atacando) {
                atacar(2);
            }
        }

        // =========================
        // ESTADO
        // =========================
        if (estado != Estado.SOCO &&
            estado != Estado.CHUTE &&
            estado != Estado.PULANDO) {

            if (movendo) {
                mudarEstado(Estado.ANDANDO);
            } else {
                mudarEstado(Estado.PARADO);
            }
        }

        atualizarFisica();
        atualizarAtaque();
        atualizarAnimacao();
    }

    // =========================
    // DESENHO
    // =========================
    @Override
    public void desenhar(Graphics g) {

        Image sprite = null;

        switch (estado) {

            case PARADO:
                sprite = parado[frameAtual];
                break;

            case ANDANDO:
                sprite = andar[frameAtual];
                break;

            case SOCO:
                sprite = soco[frameAtual];
                break;

            case CHUTE:
                if (chute != null && chute.length > 0) {
                    sprite = chute[frameAtual];
                }
                break;

            case PULANDO:
                if (pulo != null && pulo.length > 0) {
                    sprite = pulo[frameAtual];
                }
                break;

            default:
                sprite = parado[0];
        }

        if (sprite == null)
            return;

        if (viradoDireita) {

            g.drawImage(sprite, x, y, largura, altura, null);

        } else {

            g.drawImage(
                    sprite,
                    x + largura,
                    y,
                    -largura,
                    altura,
                    null
            );
        }
    }

    // =========================
    // ATAQUE
    // =========================
    @Override
    public void atacar(int tipoGolpe) {
        iniciarAtaque(tipoGolpe);
    }

    // =========================
    // MOVIMENTO (usa estado!)
    // =========================
    @Override
    public void moverDireita() {

        super.moverDireita();
    }

    @Override
    public void moverEsquerda() {

        super.moverEsquerda();
    }
}