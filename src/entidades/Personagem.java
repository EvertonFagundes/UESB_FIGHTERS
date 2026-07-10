package entidades;

import enums.Estado;
import gerenciadores.GerenciadorSom;
import gerenciadores.InputManager;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public abstract class Personagem {
    
    protected int x;
    protected int y;

    protected int largura;
    protected int altura;

    protected int vida;

    protected int velocidade;

    protected Estado estado = Estado.PARADO;
    protected boolean pulando;
    protected boolean viradoDireita;
    protected boolean atacando;
	
    // animações
    protected Image[] parado;
    protected Image[] andar;
    protected Image[] soco;
    protected Image[] chute;
    protected Image[] pulo;
    protected Image[] dano;

    // frame atual
    protected int frameAtual;

    // física do pulo
    protected int velocidadeY;
    protected int chaoY;

    // controle do ataque
    protected int temporizadorAtaque;

    // controle da animação parado/idle
    protected int contadorAnimacao;

    // poder de ataque do personagem
    protected int forca;

    // dano do golpe que está em andamento
    protected int danoGolpeAtual;

    // impede que o mesmo golpe acerte várias vezes seguidas
    protected boolean golpeAcertou;

    protected int larguraHitbox;

    protected int alturaHitbox;

    protected int ajusteY;

    protected static final int GRAVIDADE = 1;
    protected static final int FORCA_PULO = -18;
    protected static final int DURACAO_ATAQUE = 15;
    protected static final int VELOCIDADE_ANIMACAO = 8;

    protected InputManager input;

    public Personagem(LutadorUESB dados, int x, int y, int largura, int altura) {
        this.x = x;
        this.y = y;

        this.largura = largura;
        this.altura = altura;

        this.vida = dados.getVida();
        this.velocidade = dados.getVelocidade();
        this.forca = dados.getForca();
        this.larguraHitbox = dados.getLarguraHitbox();
        this.alturaHitbox = dados.getAlturaHitbox();
        this.ajusteY = dados.getAjusteY();

		this.frameAtual = 0;

        this.atacando = false;
        this.pulando = false;

		this.viradoDireita = true;

        // assume que a posição inicial em Y é o chão do personagem
        this.chaoY = y;

        this.velocidadeY = 0;
        this.temporizadorAtaque = 0;
        this.contadorAnimacao = 0;
        this.danoGolpeAtual = 0;
        this.golpeAcertou = true;
    }

    public void pular() {

        if (!pulando && !atacando) {

            pulando = true;
            velocidadeY = FORCA_PULO;

            mudarEstado(Estado.PULANDO);
        }
    }

    // Aplica gravidade enquanto o personagem estiver no ar
    protected void atualizarFisica() {

        if (pulando) {

            y += velocidadeY;
            velocidadeY += GRAVIDADE;
        
            if (y >= chaoY) {
                y = chaoY;
                velocidadeY = 0;
                pulando = false;
                if(!atacando){
                    mudarEstado(Estado.PARADO);
                }
            }
        }
    }

    // Marca o início do ataque; o subtipo chama isso dentro de atacar(tipoGolpe).
    // tipoGolpe: 1 = golpe leve, 2 = médio, 3 = forte
    protected void iniciarAtaque(int tipoGolpe) {

        if(atacando)
            return;

        atacando = true;

        if(tipoGolpe == 1){

            mudarEstado(Estado.SOCO);
            GerenciadorSom.tocarWhoosh();

        }else{

            mudarEstado(Estado.CHUTE);
            GerenciadorSom.tocarWhoosh();

        }

        danoGolpeAtual = calcularDano(tipoGolpe);

        golpeAcertou = false;

    }

    // Dano = força do personagem + bônus do golpe usado.
    // Pode ser sobrescrito por um personagem com golpes especiais.
    protected int calcularDano(int tipoGolpe) {

        int bonusGolpe;

        switch (tipoGolpe) {
            case 1:
                bonusGolpe = 2;
                break;
            case 2:
                bonusGolpe = 5;
                break;
            case 3:
                bonusGolpe = 9;
                break;
            default:
                bonusGolpe = 2;
        }

        return forca + bonusGolpe;
    }

    // Área de colisão real, um pouco menor que o sprite inteiro
    // (a foto recortada tem bastante espaço vazio ao redor)
    public Hitbox getHitbox() {

        return new Hitbox(
            x + (largura - larguraHitbox) / 2,
            y + (altura - alturaHitbox),
            larguraHitbox,
            alturaHitbox
        );
    }

    protected void atualizarAtaque() {

        // A animação controla o ataque.
        // Quando ela termina, o estado volta para PARADO.

    }

    protected void atualizarAnimacao() {

        contadorAnimacao++;

        if (contadorAnimacao < VELOCIDADE_ANIMACAO)
            return;

            contadorAnimacao = 0;

            switch (estado) {

                case PARADO:

                    frameAtual++;

                    if(frameAtual >= parado.length)
                        frameAtual = 0;

                    break;

                case ANDANDO:

                    frameAtual++;

                    if(frameAtual >= andar.length)
                        frameAtual = 0;

                    break;

                case SOCO:

                    frameAtual++;

                    if(frameAtual >= soco.length){

                        frameAtual = 0;
                        atacando = false;
                        mudarEstado(Estado.PARADO);

                    }

                    break;

                case CHUTE:

                    frameAtual++;

                    if(frameAtual >= chute.length){

                        frameAtual = 0;
                        atacando = false;
                        mudarEstado(Estado.PARADO);

                    }

                    break;

                case PULANDO:

                    frameAtual++;

                    if (frameAtual >= pulo.length) {
                        frameAtual = pulo.length - 1;
                    }

                    break;
            }

    }

    public void moverDireita() {

        if(atacando)
            return;

        x += velocidade;

        mudarEstado(Estado.ANDANDO);

    }

    public void moverEsquerda() {

        if(atacando)
            return;

        x -= velocidade;

        mudarEstado(Estado.ANDANDO);

    }

    public void parar(){

        if(atacando)
            return;

        mudarEstado(Estado.PARADO);

    }

    protected void mudarEstado(Estado novoEstado) {

        if (estado == novoEstado)
            return;

        estado = novoEstado;

        frameAtual = 0;
        contadorAnimacao = 0;
    }

    public void resetarEstado(){

        estado = Estado.PARADO;

        frameAtual = 0;
        contadorAnimacao = 0;

        atacando = false;
        pulando = false;

        velocidadeY = 0;

        temporizadorAtaque = 0;
        danoGolpeAtual = 0;
        golpeAcertou = false;
    }

    public abstract void atualizar();

    public abstract void desenhar(Graphics g);

    public abstract void atacar(int tipoGolpe);

    // Hitbox do personagem
    public Rectangle getBounds() {
        return new Rectangle(x, y, largura, altura);
    }

    // Tomar dano
    public void receberDano(int dano) {
        vida -= dano;

        if (vida < 0) {
            vida = 0;
        }
    }

    // Verifica se morreu
    public boolean estaVivo() {
        return vida > 0;
    }

    // GETTERS E SETTERS

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getLargura() {
        return largura;
    }

    public int getAltura() {
        return altura;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(int velocidade) {
        this.velocidade = velocidade;
    }

    public boolean isAtacando() {
        return atacando;
    }

    public void setAtacando(boolean atacando) {
        this.atacando = atacando;
    }

    public boolean isPulando() {
        return pulando;
    }

    public void setPulando(boolean pulando) {
        this.pulando = pulando;
    }

    public int getForca() {
        return forca;
    }

    public int getDanoGolpeAtual() {
        return danoGolpeAtual;
    }

    public boolean isGolpeAcertou() {
        return golpeAcertou;
    }

    public void setGolpeAcertou(boolean golpeAcertou) {
        this.golpeAcertou = golpeAcertou;
    }

	public Image[] getParado() {
    	return parado;
	}

	public void setParado(Image[] parado) {
		this.parado = parado;
	}

	public Image[] getAndar() {
		return andar;
	}

	public void setAndar(Image[] andando) {
		this.andar = andar;
	}

	public Image[] getSoco() {
		return soco;
	}

    public Image[] getChute() {
		return chute;
	}

	public void setSoco(Image[] soco) {
		this.soco = soco;
	}

    public void setChute(Image[] chute) {
		this.chute = chute;
	}

	public Image[] getPulo() {
		return pulo;
	}

	public void setPulo(Image[] pulo) {
		this.pulo = pulo;
	}

	public Image[] getDano() {
		return dano;
	}

	public void setDano(Image[] dano) {
		this.dano = dano;
	}

	public boolean isViradoDireita() {
		return viradoDireita;
	}

	public void setViradoDireita(boolean viradoDireita){
		this.viradoDireita = viradoDireita;
	}

}