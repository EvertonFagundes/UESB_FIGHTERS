package entidades;

import enums.Estado;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import gerenciadores.InputManager;

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

    protected static final int GRAVIDADE = 1;
    protected static final int FORCA_PULO = -18;
    protected static final int DURACAO_ATAQUE = 15;
    protected static final int VELOCIDADE_ANIMACAO = 8;

    protected InputManager input;

    public Personagem(int x, int y, int largura, int altura, int vida, int velocidade, int forca) {
        this.x = x;
        this.y = y;

        this.largura = largura;
        this.altura = altura;

        this.vida = vida;
        this.velocidade = velocidade;
        this.forca = forca;

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

    // Inicia o pulo (só funciona se o personagem já estiver no chão)
    public void pular() {

        if (!pulando) {
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
                mudarEstado(Estado.PARADO);
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

        }else{

            mudarEstado(Estado.CHUTE);

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

        int insetX = (int) (largura * 0.2);
        int insetYTopo = (int) (altura * 0.2);

        int hitboxLargura = (int) (largura * 0.6);
        int hitboxAltura = (int) (altura * 0.75);

        return new Hitbox(
            x + insetX,
            y + insetYTopo,
            hitboxLargura,
            hitboxAltura
        );
    }

    // Conta quanto falta pro golpe atual terminar
    /*protected void atualizarAtaque() {

        if (atacando) {

            temporizadorAtaque--;

            if (temporizadorAtaque <= 0) {
                atacando = false;
            }
        }
    }*/
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