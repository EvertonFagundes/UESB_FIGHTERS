package entidades;

import java.awt.Rectangle;

// Área de colisão de um personagem, um pouco menor que o
// retângulo inteiro do sprite (que tem bastante espaço vazio
// ao redor por causa do recorte da foto).
public class Hitbox {

    private Rectangle area;

    public Hitbox(int x, int y, int largura, int altura) {
        this.area = new Rectangle(x, y, largura, altura);
    }

    public boolean colideCom(Hitbox outra) {
        return this.area.intersects(outra.area);
    }

    public Rectangle getArea() {
        return area;
    }
}
