package entidades;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Jogador extends Personagem {

    public Jogador(int x, int y) {
		super(x, y, 120, 180, 100, 8);

		parado = new Image[2];
		andando = new Image[4];
		ataque = new Image[3];

		parado[0] = new ImageIcon("assets/parado1.png").getImage();
		parado[1] = new ImageIcon("assets/parado2.png").getImage();

		andando[0] = new ImageIcon("assets/andar1.png").getImage();
	}

    @Override
    public void atualizar() {

    }

    @Override
	public void desenhar(Graphics g) {

		if (atacando) {
			g.drawImage(ataque[frameAtual], x, y, largura, altura, null);

		} else {
			g.drawImage(parado[frameAtual], x, y, largura, altura, null);
		}
	}

    @Override
    public void atacar() {
        atacando = true;
    }

    public void moverDireita() {
        x += velocidade;
    }

    public void moverEsquerda() {
        x -= velocidade;
    }
}