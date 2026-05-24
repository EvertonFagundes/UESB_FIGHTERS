package entidades;

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

    protected boolean atacando;
    protected boolean pulando;
	protected boolean viradoDireita;
	
    // animações
    protected Image[] parado;
    protected Image[] andando;
    protected Image[] ataque;
    protected Image[] pulo;
    protected Image[] dano;

    // frame atual
    protected int frameAtual;

    public Personagem(int x, int y, int largura, int altura, int vida, int velocidade) {

        this.x = x;
        this.y = y;

        this.largura = largura;
        this.altura = altura;

        this.vida = vida;
        this.velocidade = velocidade;

		this.frameAtual = 0;

        this.atacando = false;
        this.pulando = false;

		this.viradoDireita = true;
    }

    public abstract void atualizar();

    public abstract void desenhar(Graphics g);

    public abstract void atacar();

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

	public Image[] getParado() {
    	return parado;
	}

	public void setParado(Image[] parado) {
		this.parado = parado;
	}

	public Image[] getAndando() {
		return andando;
	}

	public void setAndando(Image[] andando) {
		this.andando = andando;
	}

	public Image[] getAtaque() {
		return ataque;
	}

	public void setAtaque(Image[] ataque) {
		this.ataque = ataque;
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