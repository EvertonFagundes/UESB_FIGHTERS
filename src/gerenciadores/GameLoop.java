package gerenciadores;

import telas.GamePanel;

public class GameLoop extends Thread {

    private boolean running = true;
    private GamePanel game;

    public GameLoop(GamePanel game) {
        this.game = game;
    }

    @Override
    public void run() {

        while (running) {

            game.atualizarJogo();

            game.repaint();

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void encerrar() {
        running = false;
    }
}