package gerenciadores;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class InputManager implements KeyListener {

    private static final Set<Integer> teclas =
        new HashSet<>();

    @Override
    public void keyPressed(KeyEvent e) {

        teclas.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {

        teclas.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static boolean estaPressionada(
        int tecla
    ) {
        return teclas.contains(tecla);
    }
}