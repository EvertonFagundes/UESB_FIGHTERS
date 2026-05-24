package telas;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import gerenciadores.GerenciadorTelas;

public class GamePanel extends JPanel {

    public GamePanel(GerenciadorTelas janela) {

        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        setBackground(Color.BLACK);

		g.setColor(Color.DARK_GRAY);

		g.fillRect(0, 580, 1280, 140);

		g.setColor(Color.BLUE);
		g.fillRect(150, 380, 120, 200);

		g.setColor(Color.RED);
		g.fillRect(1000, 380, 120, 200);

		g.setColor(Color.GREEN);

		g.fillRect(50, 40, 400, 30);

		g.fillRect(830, 40, 400, 30);
    }
}