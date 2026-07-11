package gerenciadores;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputManager implements KeyListener {

    // Jogador 1
    public boolean esquerdaP1, direitaP1, puloP1;
    public boolean socoP1, chuteP1;
    public boolean bloqueioP1;

    // Jogador 2
    public boolean esquerdaP2, direitaP2, puloP2;
    public boolean socoP2, chuteP2;
    public boolean bloqueioP2;

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {

            // ======================
            // PLAYER 1 (WASD + JKl)
            // ======================
            case KeyEvent.VK_A -> esquerdaP1 = true;
            case KeyEvent.VK_D -> direitaP1 = true;
            case KeyEvent.VK_W -> puloP1 = true;
            case KeyEvent.VK_J -> socoP1 = true;
            case KeyEvent.VK_K -> chuteP1 = true;
            case KeyEvent.VK_L -> bloqueioP1 = true;


            // ======================
            // PLAYER 2 (SETAS + 1/2)
            // ======================
            case KeyEvent.VK_LEFT -> esquerdaP2 = true;
            case KeyEvent.VK_RIGHT -> direitaP2 = true;
            case KeyEvent.VK_UP -> puloP2 = true;
            case KeyEvent.VK_NUMPAD1, KeyEvent.VK_1 -> socoP2 = true;
            case KeyEvent.VK_NUMPAD2, KeyEvent.VK_2 -> chuteP2 = true;
            case KeyEvent.VK_NUMPAD3, KeyEvent.VK_3 -> bloqueioP2 = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        switch (e.getKeyCode()) {

            // PLAYER 1
            case KeyEvent.VK_A -> esquerdaP1 = false;
            case KeyEvent.VK_D -> direitaP1 = false;
            case KeyEvent.VK_W -> puloP1 = false;
            case KeyEvent.VK_J -> socoP1 = false;
            case KeyEvent.VK_K -> chuteP1 = false;
            case KeyEvent.VK_L -> bloqueioP1 = false;

            // PLAYER 2
            case KeyEvent.VK_LEFT -> esquerdaP2 = false;
            case KeyEvent.VK_RIGHT -> direitaP2 = false;
            case KeyEvent.VK_UP -> puloP2 = false;
            case KeyEvent.VK_NUMPAD1, KeyEvent.VK_1 -> socoP2 = false;
            case KeyEvent.VK_NUMPAD2, KeyEvent.VK_2 -> chuteP2 = false;
            case KeyEvent.VK_NUMPAD3 -> bloqueioP2 = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}