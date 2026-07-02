package entidades;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

public class Jogador extends Personagem {

    // por enquanto só existem sprites do Everton;
    // quando os outros personagens tiverem sprites próprios,
    // dá pra passar os caminhos certos pelo construtor.
    private static final String SPRITE_PARADO_1 =
        "/assets/personagens/everton/everton_idle.png";

    private static final String SPRITE_PARADO_2 =
        "/assets/personagens/everton/everton_idle2.png";

    private static final String SPRITE_SOCO =
        "/assets/personagens/everton/everton_punch.png";

    public Jogador(int x, int y, boolean viradoDireita) {

        super(x, y, 210, 280, 100, 6);

        this.viradoDireita = viradoDireita;

        parado = new Image[2];
        ataque = new Image[1];

        parado[0] = carregarImagem(SPRITE_PARADO_1);
        parado[1] = carregarImagem(SPRITE_PARADO_2);

        ataque[0] = carregarImagem(SPRITE_SOCO);
    }

    private Image carregarImagem(String caminho) {

        URL url = getClass().getResource(caminho);

        if (url == null) {
            return null;
        }

        return new ImageIcon(url).getImage();
    }

    @Override
    public void atualizar() {

        atualizarFisica();
        atualizarAtaque();
        atualizarAnimacao();
    }

    @Override
    public void desenhar(Graphics g) {

        Image sprite;

        if (atacando && ataque.length > 0) {
            sprite = ataque[0];
        } else {
            sprite = parado[frameAtual];
        }

        if (sprite == null) {
            return;
        }

        if (viradoDireita) {

            g.drawImage(
                sprite,
                x,
                y,
                largura,
                altura,
                null
            );

        } else {

            // espelha o sprite (ainda não existe uma versão
            // desenhada olhando pro outro lado)
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

    @Override
    public void atacar() {
        iniciarAtaque();
    }

    public void moverDireita() {
        x += velocidade;
    }

    public void moverEsquerda() {
        x -= velocidade;
    }
}
