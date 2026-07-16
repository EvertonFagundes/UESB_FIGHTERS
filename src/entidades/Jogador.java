package entidades;

import enums.Estado;
import gerenciadores.InputManager;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Jogador extends Personagem {
    
    private LutadorUESB dados;

    private int playerId; // 1 ou 2

    private boolean especialUsado = false;

    private ArrayList<Biscoito> biscoitos;

    public Jogador(LutadorUESB dados, int x, int y, boolean viradoDireita, InputManager input, int playerId, ArrayList<Biscoito> biscoitos
) {

        super(dados, x, y, dados.getLarguraSprite(), dados.getAlturaSprite());
        this.dados = dados;
        this.input = input;
        this.viradoDireita = viradoDireita;
        this.playerId = playerId;
        this.biscoitos = biscoitos;


        parado = new Image[4];
        soco = new Image[5];
        andar = new Image[8];
        chute = new Image[5]; // garante que não fica null
        pulo = new Image[5];
        dano = new Image[3];
        bloqueio = new Image[3];
        vitoria = new Image[5];
        derrota = new Image[3];

        // IDLE
        parado[0] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/idle_1.png");
        parado[1] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/idle_2.png");
        parado[2] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/idle_3.png");
        parado[3] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/idle_4.png");

        // SOCO
        soco[0] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/punch_1.png");
        soco[1] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/punch_2.png");
        soco[2] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/punch_3.png");
        soco[3] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/punch_4.png");
        soco[4] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/punch_5.png");

        // ANDAR
        andar[0] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/walk_1.png");
        andar[1] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/walk_2.png");
        andar[2] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/walk_3.png");
        andar[3] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/walk_4.png");
        andar[4] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/walk_5.png");
        andar[5] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/walk_6.png");
        andar[6] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/walk_7.png");
        andar[7] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/walk_8.png");

        //CHUTE
        chute[0] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/kick_1.png");
        chute[1] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/kick_2.png");
        chute[2] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/kick_3.png");
        chute[3] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/kick_4.png");
        chute[4] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/kick_5.png");

        //PULO
        pulo[0] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/jump_1.png");
        pulo[1] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/jump_2.png");
        pulo[2] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/jump_3.png");
        pulo[3] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/jump_4.png");
        pulo[4] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/jump_5.png");

        //DANO
        dano[0] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/hurt_1.png");
        dano[1] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/hurt_2.png");
        dano[2] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/hurt_3.png");

        //BLOQUEIO
        bloqueio[0] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/block_1.png");
        bloqueio[1] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/block_2.png");
        bloqueio[2] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/block_3.png");

        //VITORIA
        vitoria[0] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/victory_1.png");
        vitoria[1] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/victory_2.png");
        vitoria[2] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/victory_3.png");
        vitoria[3] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/victory_4.png");
        vitoria[4] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/victory_5.png");

        //DERROTA
        derrota[0] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/defeat_1.png");
        derrota[1] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/defeat_2.png");
        derrota[2] = carregarImagem("/assets/personagens/" + dados.getPastaSprites() + "/sprites/defeat_3.png");

    }

    private Image carregarImagem(String caminho) {

        URL url = getClass().getResource(caminho);

        if (url == null) {
            return null;
        }

        return new ImageIcon(url).getImage();
    }

    public String getNome() {
        return dados.getNome();
    }

    public int getIdLutador() {
        return dados.getId();
    }

    public LutadorUESB getDados() {
        return dados;
    }

    // =========================
    // UPDATE PRINCIPAL
    // =========================
    @Override
    public void atualizar() {

        boolean movendo = false;

        if(recebendoDano){

            atualizarFisica();
            atualizarDano();
            atualizarAnimacao();
            return;
        }

        // =========================
        // PLAYER 1
        // =========================
        if (playerId == 1) {

            if (input.esquerdaP1) {
                x -= velocidade;
                movendo = true;
            }

            if (input.direitaP1) {
                x += velocidade;
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

            if(input.bloqueioP1){
                bloquear();
            }else{
                pararBloqueio();
            }
            if(input.especialP1 && !especialUsado){

                usarEspecial();
                especialUsado = true;

            }

            if(!input.especialP1){

                especialUsado = false;

            }
        }

        // =========================
        // PLAYER 2
        // =========================
        else if (playerId == 2) {

            if (input.esquerdaP2) {
                x -= velocidade;
                movendo = true;
            }

            if (input.direitaP2) {
                x += velocidade;
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

            if(input.bloqueioP2){
                bloquear();
            }else{
                pararBloqueio();
            }
            if(input.especialP2 && !especialUsado){

                usarEspecial();
                especialUsado = true;

            }

            if(!input.especialP2){

                especialUsado = false;

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
        atualizarDano();
        atualizarEspecial();
        if(especialAtivo && tempoEspecial <= 0){
            desativarEspecial();
        }
    }

    private void desativarEspecial(){

        especialAtivo = false;

        if(getNome().equals("ERICK")){

            largura = dados.getLarguraSprite();
            altura = dados.getAlturaSprite();
            velocidade = dados.getVelocidade();
            forca = dados.getForca();
            larguraHitbox = dados.getLarguraHitbox();
            alturaHitbox = dados.getAlturaHitbox();
            ajusteY = dados.getAjusteY();

        }else if(getNome().equals("EVERTON")){
            forca = dados.getForca();
            velocidade = dados.getVelocidade();
        }
            
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

            case BLOQUEANDO:
                sprite = bloqueio[frameAtual];
                break;

            case DANO:
                sprite = dano[frameAtual];
                break;

            default:
                sprite = parado[0];
        }

        if (sprite == null)
            return;

        if (especialAtivo) {

            int alpha = (int)(70 + 50 *
                    Math.sin(System.currentTimeMillis() / 80.0));

            if(getNome().equals("EVERTON")){
                g.setColor(new Color(255,215,0,100));
            }else{
                g.setColor(new Color(255, 255, 255, alpha));
            }

            g.fillRoundRect(
                    x - 6,
                    y + ajusteY - 6,
                    largura + 12,
                    altura + 12,
                    18,
                    18
            );
        }

        if (viradoDireita) {

            g.drawImage(sprite, x, y + ajusteY, largura, altura, null);

        } else {

            g.drawImage(
                    sprite,
                    x + largura,
                    y + ajusteY,
                    -largura,
                    altura,
                    null
            );
        }
    }

    @Override
    public void usarEspecial(){

        if(getEnergiaEspecial() < 100)
            return;


        consumirEnergiaEspecial();


        if(getNome().equals("EVERTON")){

            ativarSuperForca();

        }else if(getNome().equals("ERICK")){

            ativarGigante();
        }else if(getNome().equals("GIULIA")){
            ativarChuvaBiscoitos();
        }

    }

    private void ativarSuperForca(){

        especialAtivo = true;

        tempoEspecial = DURACAO_ESPECIAL;

        forca += 10;

        velocidade -= 2;

    }

    private void ativarGigante(){

        especialAtivo = true;

        tempoEspecial = DURACAO_ESPECIAL;

        largura = (int)(largura * 1.8);
        altura = (int)(altura * 1.8);
        alturaHitbox = (int)(alturaHitbox * 1.8);
        larguraHitbox = (int)(larguraHitbox * 1.8);

        forca += 8;

        velocidade -= 8;

        ajusteY = -350;

    }

    private void ativarChuvaBiscoitos(){

        especialAtivo = true;
        tempoEspecial = DURACAO_ESPECIAL;
        int posicaoX;
        int posicaoY;

        if(viradoDireita){

            posicaoX = x + largura - 10;
            //posicaoY = y + alturaHitbox - 20;
            posicaoY = y + altura - 50;

        }else{

            posicaoX = x - 30;
            posicaoY = y + alturaHitbox - 20;

        }

        for(int i = 0; i < 6; i++){

            Biscoito b = new Biscoito(
                posicaoX,
                posicaoY - 120 + (i * 25),
                viradoDireita
            );

            biscoitos.add(b);
        }
    }

    // =========================
    // ATAQUE
    // =========================
    @Override
    public void atacar(int tipoGolpe) {
        if(bloqueando)
            return;
        iniciarAtaque(tipoGolpe);
    }

    // =========================
    // MOVIMENTO (usa estado!)
    // =========================
    @Override
    public void moverDireita() {
        if(bloqueando)
            return;

        super.moverDireita();
    }

    @Override
    public void moverEsquerda() {
        if(bloqueando)
            return;

        super.moverEsquerda();
    }
}